package de.thb.cloudsim.reporting;

import de.thb.cloudsim.model.InfrastructureModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Diese Klasse übernimmt die Ausgabe und den Export der Simulationsergebnisse.
 *
 * Zweck:
 * → Darstellung der Ergebnisse im Terminal (Vergleichstabelle)
 * → Persistenz der Ergebnisse als CSV-Dateien (für Analyse / Auswertung)
 *
 * Wichtig:
 * Diese Klasse ist rein für Output zuständig (keine Logik der Simulation selbst).
 */
public class ResultExporter {

    /**
     * Gibt eine formatierte Vergleichstabelle aller Szenarien in der Konsole aus.
     *
     * Inhalt:
     * - Szenarioname
     * - Anzahl VMs
     * - Anzahl Requests
     * - Durchschnittliche Laufzeit
     * - Maximale Laufzeit
     */
    public static void printComparisonTable(ScenarioResult[] results) {

        System.out.println("\n--- ARCHITECTURE COMPARISON TABLE ---\n");

        // Tabellenkopf
        System.out.printf(
                "%-18s %-10s %-8s %-18s %-18s%n",
                "Scenario", "VMs", "Reqs", "Avg Finish Time", "Max Finish Time"
        );

        System.out.println("--------------------------------------------------------------------------");

        // Tabellenzeilen
        for (ScenarioResult result : results) {
            System.out.printf(
                    Locale.US, // sorgt für Punkt statt Komma bei Dezimalzahlen
                    "%-18s %-10d %-8d %-18.3f %-18.3f%n",
                    result.getScenarioName(),
                    result.getVmCount(),
                    result.getFinishedCloudlets(),
                    result.getAverageFinishTime(),
                    result.getMaxFinishTime()
            );
        }
    }

    /**
     * Exportiert die Simulationsergebnisse in eine CSV-Datei.
     *
     * Diese Datei kann z. B. in Excel oder Python weiterverarbeitet werden.
     */
    public static void exportToCsv(ScenarioResult[] results, String filePath) throws IOException {

        StringBuilder csv = new StringBuilder();

        // Header-Zeile
        csv.append("scenario,architecture,workload,vm_count,finished_cloudlets,static_asset_count,gallery_view_count,image_detail_count,image_upload_count,average_finish_time,max_finish_time\n");

        // Datenzeilen
        for (ScenarioResult result : results) {
            csv.append(result.getScenarioName()).append(",")
                    .append(result.getArchitectureLabel()).append(",")
                    .append(result.getWorkloadLabel()).append(",")
                    .append(result.getVmCount()).append(",")
                    .append(result.getFinishedCloudlets()).append(",")
                    .append(result.getStaticAssetCount()).append(",")
                    .append(result.getGalleryViewCount()).append(",")
                    .append(result.getImageDetailCount()).append(",")
                    .append(result.getImageUploadCount()).append(",")
                    .append(format(result.getAverageFinishTime())).append(",")
                    .append(format(result.getMaxFinishTime())).append("\n");
        }

        // Datei schreiben
        Files.writeString(Path.of(filePath), csv.toString());
    }

    /**
     * Formatiert Double-Werte für CSV (3 Nachkommastellen, US-Format).
     */
    private static String format(double value) {
        return String.format(Locale.US, "%.3f", value);
    }

    /**
     * Exportiert den Capability Report und Import Report als CSV.
     *
     * Inhalt:
     * - Anzahl unterstützter / ignorierter Ressourcen
     * - unterstützte Features
     * - ignorierte Features
     * - simulierte Konzepte
     * - Einschränkungen
     * - Warnungen beim Import
     */
    public static void exportCapabilityReport(
            CapabilityReport capabilityReport,
            InfrastructureModel infrastructureModel,
            String filePath
    ) throws IOException {

        StringBuilder csv = new StringBuilder();

        // Zweispaltiges Format: section + item
        csv.append("section,item\n");

        // Anzahl unterstützter Ressourcen
        csv.append("supported_resources_count,")
                .append(infrastructureModel.getImportReport().getSupportedResources())
                .append("\n");

        // Anzahl ignorierter Ressourcen
        csv.append("ignored_resources_count,")
                .append(infrastructureModel.getImportReport().getIgnoredResources())
                .append("\n");

        // Unterstützte Features
        for (String item : capabilityReport.getSupportedTerraformResources()) {
            csv.append("supported_terraform_feature,").append(escape(item)).append("\n");
        }

        // Ignorierte Features
        for (String item : capabilityReport.getIgnoredTerraformResources()) {
            csv.append("ignored_or_partial_feature,").append(escape(item)).append("\n");
        }

        // Simulierte Konzepte
        for (String item : capabilityReport.getSimulatedConcepts()) {
            csv.append("simulated_concept,").append(escape(item)).append("\n");
        }

        // Einschränkungen
        for (String item : capabilityReport.getLimitations()) {
            csv.append("limitation,").append(escape(item)).append("\n");
        }

        // Warnungen aus dem Importprozess
        for (String warning : infrastructureModel.getImportReport().getWarnings()) {
            csv.append("import_warning,").append(escape(warning)).append("\n");
        }

        // Datei schreiben
        Files.writeString(Path.of(filePath), csv.toString());
    }

    /**
     * Escaping für CSV:
     * - ersetzt " durch ""
     * - setzt den gesamten Wert in Anführungszeichen
     */
    private static String escape(String value) {
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}