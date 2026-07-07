package de.thb.cloudsim.simulation;

import de.thb.cloudsim.model.ComputeNode;
import de.thb.cloudsim.model.DatabaseNode;
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

public class InfrastructureToCloudSim {

    public static SimulationSummary runSimulation(InfrastructureModel model, WorkloadProfile workload) {
        CloudSimPlus simulation = new CloudSimPlus();

        createDatacenter(simulation);
        DatacenterBrokerSimple broker = new DatacenterBrokerSimple(simulation);

        List<Vm> appVms = createAppVms(model);
        List<Vm> dbVms = createDatabaseVms(model);

        List<Vm> allVms = new ArrayList<>();
        allVms.addAll(appVms);
        allVms.addAll(dbVms);

        List<RequestExecution> requests = createRequestExecutions(model, workload, appVms, dbVms);

        List<Cloudlet> allCloudlets = new ArrayList<>();
        for (RequestExecution request : requests) {
            allCloudlets.add(request.appCloudlet());
            allCloudlets.addAll(request.dbCloudlets());
        }

        broker.submitVmList(allVms);
        broker.submitCloudletList(allCloudlets);

        simulation.start();

        double totalFinishTime = 0.0;
        double maxFinishTime = 0.0;

        for (RequestExecution request : requests) {
            double requestFinishTime = request.appCloudlet().getFinishTime();

            for (Cloudlet dbCloudlet : request.dbCloudlets()) {
                requestFinishTime = Math.max(requestFinishTime, dbCloudlet.getFinishTime());
            }

            totalFinishTime += requestFinishTime;

            if (requestFinishTime > maxFinishTime) {
                maxFinishTime = requestFinishTime;
            }
        }

        double averageFinishTime = requests.isEmpty()
                ? 0.0
                : totalFinishTime / requests.size();

        return new SimulationSummary(
                appVms.size(),
                requests.size(),
                averageFinishTime,
                maxFinishTime
        );
    }

    private static Datacenter createDatacenter(CloudSimPlus simulation) {
        List<Pe> peList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            peList.add(new PeSimple(3000));
        }

        Host host = new HostSimple(16384, 10000, 1_000_000, peList);
        host.setVmScheduler(new VmSchedulerTimeShared());

        return new DatacenterSimple(
                simulation,
                List.of(host),
                new VmAllocationPolicySimple()
        );
    }

    private static List<Vm> createAppVms(InfrastructureModel model) {
        List<Vm> vmList = new ArrayList<>();
        int vmId = 0;

        for (ComputeNode node : model.getComputeNodes()) {
            String flavor = node.getFlavor();

            Vm vm = new VmSimple(
                    vmId++,
                    FlavorMapper.getMipsPerPe(flavor),
                    FlavorMapper.getPes(flavor)
            );

            vm.setRam(FlavorMapper.getRamMb(flavor))
                    .setBw(FlavorMapper.getBw(flavor))
                    .setSize(FlavorMapper.getSizeMb(flavor));

            vm.setCloudletScheduler(new CloudletSchedulerTimeShared());
            vmList.add(vm);
        }

        return vmList;
    }

    private static List<Vm> createDatabaseVms(InfrastructureModel model) {
        List<Vm> dbVms = new ArrayList<>();
        int vmId = 1000;

        for (DatabaseNode ignored : model.getDatabaseNodes()) {
            Vm dbVm = new VmSimple(vmId++, 1200, 1);

            dbVm.setRam(2048)
                    .setBw(500)
                    .setSize(20_000);

            dbVm.setCloudletScheduler(new CloudletSchedulerTimeShared());
            dbVms.add(dbVm);
        }

        return dbVms;
    }

    private static List<RequestExecution> createRequestExecutions(
            InfrastructureModel model,
            WorkloadProfile workload,
            List<Vm> appVms,
            List<Vm> dbVms
    ) {
        List<RequestExecution> requests = new ArrayList<>();

        int cloudletId = 0;
        int requestIndex = 0;

        for (RequestProfile requestProfile : workload.getRequestProfiles()) {
            for (int i = 0; i < requestProfile.getCount(); i++) {
                Vm appVm = appVms.get(requestIndex % appVms.size());

                long appLength = requestProfile.getBaseLength();

                // Netzwerkzustand berücksichtigen
                switch (SimulationConfig.NETWORK_STATE) {

                    case DEGRADED -> appLength += 3000;

                    case OUTAGE -> {
                        // Requests mit DB oder Storage können nicht verarbeitet werden
                        if (requestProfile.getDbQueryCount() > 0 || !model.getStorageNodes().isEmpty()) {
                            continue;
                        }
                    }

                    case NORMAL -> {
                        // keine Änderung
                    }
                }

                if (!model.getStorageNodes().isEmpty()) {
                    appLength += requestProfile.getStoragePenalty();
                }

                Cloudlet appCloudlet = new CloudletSimple(
                        cloudletId++,
                        appLength,
                        requestProfile.getPes()
                );

                appCloudlet.setSizes(requestProfile.getCloudletSize());
                appCloudlet.setVm(appVm);

                List<Cloudlet> dbCloudlets = new ArrayList<>();

                if (!dbVms.isEmpty()) {
                    for (int q = 0; q < requestProfile.getDbQueryCount(); q++) {
                        Vm dbVm = dbVms.get(q % dbVms.size());

                        Cloudlet dbCloudlet = new CloudletSimple(
                                cloudletId++,
                                requestProfile.getDbQueryLength(),
                                1
                        );

                        dbCloudlet.setSizes(512);
                        dbCloudlet.setVm(dbVm);

                        dbCloudlets.add(dbCloudlet);
                    }
                }

                requests.add(new RequestExecution(appCloudlet, dbCloudlets));
                requestIndex++;
            }
        }

        return requests;
    }

    private record RequestExecution(
            Cloudlet appCloudlet,
            List<Cloudlet> dbCloudlets
    ) {
    }
}