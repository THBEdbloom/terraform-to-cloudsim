package de.thb.cloudsim.app;

import de.thb.cloudsim.mapping.TerraformToInfrastructureMapper;
import de.thb.cloudsim.model.InfrastructureModel;
import de.thb.cloudsim.reporting.CapabilityReport;
import de.thb.cloudsim.reporting.CapabilityReportFactory;
import de.thb.cloudsim.reporting.ResultExporter;
import de.thb.cloudsim.reporting.ScenarioResult;
import de.thb.cloudsim.scenario.InfrastructureCloner;
import de.thb.cloudsim.simulation.InfrastructureToCloudSim;
import de.thb.cloudsim.simulation.SimulationSummary;
import de.thb.cloudsim.terraform.*;
import de.thb.cloudsim.workload.RequestProfile;
import de.thb.cloudsim.workload.RequestType;
import de.thb.cloudsim.workload.WorkloadFactory;
import de.thb.cloudsim.workload.WorkloadProfile;

import java.util.List;

/**
 * Zentrale Startklasse des Prototyps.
 *
 * Diese Klasse steuert den kompletten Ablauf:
 * 1. Terraform-Dateien laden
 * 2. Terraform-Kontext (variables, locals) lesen
 * 3. Terraform-Ressourcen parsen
 * 4. Werte/Referenzen auflösen
 * 5. internes Infrastrukturmodell erzeugen
 * 6. Reports ausgeben
 * 7. Workloads definieren
 * 8. Simulationen für mehrere Szenarien ausführen
 * 9. Ergebnisse als Tabelle und CSV exportieren
 */
public class App {

    public static void main(String[] args) throws Exception {

        // Pfad zum Terraform-Testprojekt.
        // Hier liegen die .tf-Dateien, die eingelesen werden sollen.
        String terraformPath = "terraform-testdata/bottletube-mvp";

        // 1. Alle Terraform-Dateien aus dem Zielordner laden.
        // Ergebnis: Liste mit Datei-Inhalten als Strings.
        List<String> files = TerraformLoader.loadAllTerraformFiles(terraformPath);

        // 2. Terraform-Kontext parsen.
        // Hier werden variable-defaults und locals gelesen.
        // Beispiel:
        // var.region -> eu01
        // local.app_name -> var.name
        TerraformContext context = TerraformContextParser.parseContext(files);

        // Ausgabe des eingelesenen Kontexts zur Kontrolle.
        System.out.println("\n--- TERRAFORM CONTEXT ---\n");

        System.out.println("Variables:");
        for (var entry : context.getVariables().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        System.out.println("\nLocals:");
        for (var entry : context.getLocals().entrySet()) {
            System.out.println("  " + entry.getKey() + " = " + entry.getValue());
        }

        // 3. Terraform-Ressourcen parsen.
        // Hier werden resource-Blöcke aus den Terraform-Dateien gelesen.
        // Ergebnis: Liste technischer TerraformResource-Objekte.
        List<TerraformResource> resources = TerraformResourceParser.parseResources(files);

        // 4. Werte und Referenzen in den Ressourcen auflösen.
        // Beispiele:
        // region = var.region -> region = eu01
        // name = "${local.app_name}-app-1" -> bottletube-app-1
        for (TerraformResource resource : resources) {
            TerraformValueResolver.resolveResourceAttributes(resource, context);
        }

        // 5. Terraform-Ressourcen in ein internes Infrastrukturmodell überführen.
        // Dieser Schritt ist die eigentliche Brücke:
        // Terraform -> simulationsrelevante Modellobjekte.
        InfrastructureModel infrastructureModel = TerraformToInfrastructureMapper.map(resources);

        // Infrastrukturmodell ausgeben, damit sichtbar ist,
        // was tatsächlich erkannt und übernommen wurde.
        System.out.println("\n--- INFRASTRUCTURE MODEL ---\n");

        System.out.println("Compute Nodes:");
        for (de.thb.cloudsim.model.ComputeNode node : infrastructureModel.getComputeNodes()) {
            System.out.println("  " + node);
        }

        System.out.println("\nDatabase Nodes:");
        for (de.thb.cloudsim.model.DatabaseNode node : infrastructureModel.getDatabaseNodes()) {
            System.out.println("  " + node);
        }

        System.out.println("\nStorage Nodes:");
        for (de.thb.cloudsim.model.StorageNode node : infrastructureModel.getStorageNodes()) {
            System.out.println("  " + node);
        }

        System.out.println("\nLoad Balancers:");
        for (de.thb.cloudsim.model.LoadBalancerNode node : infrastructureModel.getLoadBalancers()) {
            System.out.println("  " + node);
        }

        // 6. Import Report ausgeben.
        // Dieser Report beschreibt:
        // - wie viele Ressourcen unterstützt wurden
        // - wie viele ignoriert wurden
        // - welche Warnungen beim Import entstanden sind
        System.out.println("\n--- IMPORT REPORT ---\n");
        System.out.println("Supported resources: " + infrastructureModel.getImportReport().getSupportedResources());
        System.out.println("Ignored resources: " + infrastructureModel.getImportReport().getIgnoredResources());

        if (infrastructureModel.getImportReport().getWarnings().isEmpty()) {
            System.out.println("Warnings: none");
        } else {
            System.out.println("Warnings:");
            for (String warning : infrastructureModel.getImportReport().getWarnings()) {
                System.out.println("  - " + warning);
            }
        }

        // Capability Report erstellen.
        // Dieser Report ist eher methodisch:
        // Er beschreibt, was der Prototyp grundsätzlich unterstützt
        // und welche Grenzen/Limitierungen bekannt sind.
        CapabilityReport capabilityReport = CapabilityReportFactory.createDefaultReport();

        System.out.println("\n--- CAPABILITY REPORT ---\n");

        System.out.println("Supported Terraform resources/features:");
        for (String item : capabilityReport.getSupportedTerraformResources()) {
            System.out.println("  - " + item);
        }

        System.out.println("\nIgnored or not fully supported:");
        for (String item : capabilityReport.getIgnoredTerraformResources()) {
            System.out.println("  - " + item);
        }

        System.out.println("\nSimulated concepts:");
        for (String item : capabilityReport.getSimulatedConcepts()) {
            System.out.println("  - " + item);
        }

        System.out.println("\nKnown limitations:");
        for (String item : capabilityReport.getLimitations()) {
            System.out.println("  - " + item);
        }

        // Drei unterschiedliche Lastprofile erzeugen.
        // Diese modellieren unterschiedliche Laststufen:
        // Light, Medium, Heavy.
        WorkloadProfile light = WorkloadFactory.createLightProfile();
        WorkloadProfile medium = WorkloadFactory.createMediumProfile();
        WorkloadProfile heavy = WorkloadFactory.createHeavyProfile();

        // Zusätzlich aus dem aktuellen Infrastrukturmodell
        // eine 1-VM-Variante ableiten.
        // So kann man 1 VM vs. 2 VM direkt vergleichen.
        InfrastructureModel singleVmModel = InfrastructureCloner.cloneWithSingleComputeNode(infrastructureModel);

        // Array für alle sechs Vergleichsszenarien.
        ScenarioResult[] results = new ScenarioResult[6];

        // Simulationen für alle Architektur-/Last-Kombinationen ausführen.
        results[0] = runScenario("1 VM - Light", "1 VM", "Light", singleVmModel, light);
        results[1] = runScenario("2 VM - Light", "2 VM", "Light", infrastructureModel, light);
        results[2] = runScenario("1 VM - Medium", "1 VM", "Medium", singleVmModel, medium);
        results[3] = runScenario("2 VM - Medium", "2 VM", "Medium", infrastructureModel, medium);
        results[4] = runScenario("1 VM - Heavy", "1 VM", "Heavy", singleVmModel, heavy);
        results[5] = runScenario("2 VM - Heavy", "2 VM", "Heavy", infrastructureModel, heavy);

        // Kompakte Tabelle der Simulationsergebnisse in der Konsole ausgeben.
        ResultExporter.printComparisonTable(results);

        // Hauptergebnisse als CSV exportieren.
        String resultsCsvPath = "results/simulation-results.csv";
        ResultExporter.exportToCsv(results, resultsCsvPath);
        System.out.println("\nCSV export written to: " + resultsCsvPath);

        // Capability-/Limitations-Report ebenfalls als CSV exportieren.
        String capabilityCsvPath = "results/capability-report.csv";
        ResultExporter.exportCapabilityReport(capabilityReport, infrastructureModel, capabilityCsvPath);
        System.out.println("Capability report export written to: " + capabilityCsvPath);
    }

    /**
     * Führt genau ein Simulationsszenario aus.
     *
     * @param scenarioName       Vollständiger Szenarioname, z. B. "1 VM - Light"
     * @param architectureLabel  Architekturbezeichnung, z. B. "1 VM"
     * @param workloadLabel      Lastprofilbezeichnung, z. B. "Light"
     * @param infrastructureModel Das zu simulierende Infrastrukturmodell
     * @param workload           Das Lastprofil für dieses Szenario
     * @return Ein ScenarioResult mit allen wichtigen Kennzahlen
     */
    private static ScenarioResult runScenario(
            String scenarioName,
            String architectureLabel,
            String workloadLabel,
            InfrastructureModel infrastructureModel,
            WorkloadProfile workload
    ) {
        // Workload in der Konsole beschreiben.
        System.out.println("\n--- WORKLOAD PROFILE: " + scenarioName + " ---\n");
        System.out.println("Total requests: " + workload.getTotalRequestCount());

        for (RequestProfile profile : workload.getRequestProfiles()) {
            System.out.println("Type: " + profile.getRequestType()
                    + " | count=" + profile.getCount()
                    + " | baseLength=" + profile.getBaseLength()
                    + " | dbPenalty=" + profile.getDbPenalty()
                    + " | storagePenalty=" + profile.getStoragePenalty());
        }

        // Eigentliche CloudSim-Simulation starten.
        SimulationSummary summary = InfrastructureToCloudSim.runSimulation(infrastructureModel, workload);

        // Ergebnisobjekt bauen.
        // Neben den Standardkennzahlen werden auch die Counts
        // einzelner Request-Typen gespeichert.
        return new ScenarioResult(
                scenarioName,
                architectureLabel,
                workloadLabel,
                summary.getVmCount(),
                summary.getCloudletCount(),
                summary.getAverageFinishTime(),
                summary.getMaxFinishTime(),
                countRequests(workload, RequestType.STATIC_ASSET),
                countRequests(workload, RequestType.GALLERY_VIEW),
                countRequests(workload, RequestType.IMAGE_DETAIL),
                countRequests(workload, RequestType.IMAGE_UPLOAD)
        );
    }

    /**
     * Zählt, wie oft ein bestimmter Request-Typ
     * innerhalb eines Workload-Profils vorkommt.
     *
     * Beispiel:
     * countRequests(workload, RequestType.GALLERY_VIEW)
     */
    private static int countRequests(WorkloadProfile workload, RequestType type) {
        int count = 0;
        for (RequestProfile profile : workload.getRequestProfiles()) {
            if (profile.getRequestType() == type) {
                count += profile.getCount();
            }
        }
        return count;
    }
}
/**
 * Die App.java nimmt Terraform als Eingabe,
 * erzeugt daraus ein simulationsrelevantes Infrastrukturmodell,
 * führt mehrere CloudSim-Szenarien aus und exportiert die Ergebnisse sowie die Grenzen des Ansatzes.
*/
