package de.thb.cloudsim.simulation;

import de.thb.cloudsim.model.ComputeNode;
import de.thb.cloudsim.model.InfrastructureModel;
import de.thb.cloudsim.workload.RequestProfile;
import de.thb.cloudsim.workload.WorkloadProfile;
import org.cloudsimplus.allocationpolicies.VmAllocationPolicySimple;
import org.cloudsimplus.brokers.DatacenterBrokerSimple;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.cloudlets.CloudletSimple;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.Datacenter;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.Host;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.Pe;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.schedulers.cloudlet.CloudletSchedulerTimeShared;
import org.cloudsimplus.schedulers.vm.VmSchedulerTimeShared;
import org.cloudsimplus.vms.Vm;
import org.cloudsimplus.vms.VmSimple;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist der Kern der Transformation:
 *
 * → Sie überführt das abstrakte Infrastrukturmodell in eine konkrete CloudSim-Simulation.
 *
 * Pipeline:
 * InfrastructureModel → VMs + Cloudlets → CloudSim → Ergebnisse
 *
 * Wichtig:
 * Hier findet die eigentliche "Terraform → Simulation"-Abbildung statt.
 */
public class InfrastructureToCloudSim {

    /**
     * Führt eine komplette Simulation aus.
     *
     * Schritte:
     * 1. CloudSim initialisieren
     * 2. Datacenter erstellen
     * 3. Broker erstellen
     * 4. VMs aus Infrastruktur erzeugen
     * 5. Cloudlets aus Workload erzeugen
     * 6. Simulation starten
     * 7. Ergebnisse auswerten
     */
    public static SimulationSummary runSimulation(InfrastructureModel model, WorkloadProfile workload) {

        // Simulationsumgebung initialisieren
        CloudSimPlus simulation = new CloudSimPlus();

        // Rechenzentrum erstellen (physische Infrastruktur)
        Datacenter datacenter = createDatacenter(simulation);

        // Broker = Vermittler zwischen VMs und Cloudlets
        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);

        // VMs aus Terraform-Infrastruktur erzeugen
        List<Vm> vmList = createVmsFromInfrastructure(model);

        // Workload in Cloudlets übersetzen
        List<Cloudlet> cloudletList = createCloudlets(model, workload);

        // VMs und Aufgaben beim Broker registrieren
        broker.submitVmList(vmList);
        broker.submitCloudletList(cloudletList);

        // Simulation starten
        simulation.start();

        // Ergebnisliste der abgeschlossenen Cloudlets
        List<Cloudlet> finishedCloudlets = broker.getCloudletFinishedList();

        // Optional: detaillierte Ausgabe
        if (SimulationConfig.PRINT_CLOUDLET_DETAILS) {
            System.out.println("\n--- CLOUDSIM RESULTS ---\n");
            for (Cloudlet cloudlet : finishedCloudlets) {
                System.out.println("Cloudlet " + cloudlet.getId()
                        + " finished on VM " + cloudlet.getVm().getId()
                        + " | status=" + cloudlet.getStatus()
                        + " | finish=" + cloudlet.getFinishTime());
            }
        }

        // Kennzahlen berechnen
        double totalFinishTime = 0.0;
        double maxFinishTime = 0.0;

        for (Cloudlet cloudlet : finishedCloudlets) {
            double finishTime = cloudlet.getFinishTime();
            totalFinishTime += finishTime;

            // Maximum bestimmen (schlechtester Fall)
            if (finishTime > maxFinishTime) {
                maxFinishTime = finishTime;
            }
        }

        // Durchschnitt berechnen
        double averageFinishTime = finishedCloudlets.isEmpty()
                ? 0.0
                : totalFinishTime / finishedCloudlets.size();

        // Zusammenfassung zurückgeben
        return new SimulationSummary(
                vmList.size(),
                finishedCloudlets.size(),
                averageFinishTime,
                maxFinishTime
        );
    }

    /**
     * Erstellt ein einfaches Datacenter (physische Hosts).
     *
     * Aktuell:
     * - 1 Host
     * - 8 CPU-Kerne
     * - fixe Ressourcen
     *
     * Einschränkung:
     * → nicht aus Terraform abgeleitet (statisch)
     */
    private static Datacenter createDatacenter(CloudSimPlus simulation) {

        List<Host> hostList = new ArrayList<>();

        // CPU-Kerne erstellen
        List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            peList.add(new PeSimple(3000)); // 3000 MIPS pro Kern
        }

        // Host mit RAM, Bandbreite und Storage
        Host host = new HostSimple(16384, 10000, 1_000_000, peList);

        // Scheduler bestimmt, wie VMs CPU teilen
        host.setVmScheduler(new VmSchedulerTimeShared());

        hostList.add(host);

        // Datacenter erstellen
        return new DatacenterSimple(simulation, hostList, new VmAllocationPolicySimple());
    }

    /**
     * Wandelt ComputeNodes in CloudSim-VMs um.
     *
     * → zentrale Abbildung: Terraform → Simulation
     */
    private static List<Vm> createVmsFromInfrastructure(InfrastructureModel model) {

        List<Vm> vmList = new ArrayList<>();
        int vmId = 0;

        for (ComputeNode node : model.getComputeNodes()) {

            String flavor = node.getFlavor();

            // Hardwareparameter aus Flavor ableiten
            int pes = FlavorMapper.getPes(flavor);        // CPU-Kerne
            long ram = FlavorMapper.getRamMb(flavor);     // RAM
            long bw = FlavorMapper.getBw(flavor);         // Netzwerk
            long size = FlavorMapper.getSizeMb(flavor);   // Disk
            long mips = FlavorMapper.getMipsPerPe(flavor);// CPU-Leistung

            // VM erzeugen
            Vm vm = new VmSimple(vmId++, mips, pes);

            vm.setRam(ram)
                    .setBw(bw)
                    .setSize(size);

            // Scheduler bestimmt, wie Tasks auf VM laufen
            vm.setCloudletScheduler(new CloudletSchedulerTimeShared());

            vmList.add(vm);
        }

        return vmList;
    }

    /**
     * Wandelt Workload in Cloudlets (Tasks) um.
     *
     * Wichtig:
     * → Hier wird Infrastrukturverhalten simuliert
     *   (DB / Storage beeinflussen Laufzeit)
     */
    private static List<Cloudlet> createCloudlets(InfrastructureModel model, WorkloadProfile workload) {

        List<Cloudlet> cloudletList = new ArrayList<>();
        int cloudletId = 0;

        for (RequestProfile requestProfile : workload.getRequestProfiles()) {

            // Basis-Rechenaufwand
            long length = requestProfile.getBaseLength();

            // Datenbank vorhanden → zusätzlicher Aufwand
            if (!model.getDatabaseNodes().isEmpty()) {
                length += requestProfile.getDbPenalty();
            }

            // Storage vorhanden → zusätzlicher Aufwand
            if (!model.getStorageNodes().isEmpty()) {
                length += requestProfile.getStoragePenalty();
            }

            // Für jede Anfrage ein Cloudlet erzeugen
            for (int i = 0; i < requestProfile.getCount(); i++) {

                Cloudlet cloudlet = new CloudletSimple(
                        cloudletId++,
                        length,
                        requestProfile.getPes()
                );

                // Größe der Daten (I/O)
                cloudlet.setSizes(requestProfile.getCloudletSize());

                cloudletList.add(cloudlet);
            }
        }

        return cloudletList;
    }
}