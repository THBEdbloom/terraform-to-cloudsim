package de.thb.cloudsim.reporting;

/**
 * Diese Klasse repräsentiert das Ergebnis eines einzelnen Simulationsszenarios.
 *
 * Ein Szenario ist z. B.:
 * → "1 VM - Light"
 * → "2 VM - Heavy"
 *
 * Zweck:
 * → Speicherung aller relevanten Kennzahlen für Vergleich und Export
 * → Wird später in Tabellen und CSV-Dateien verwendet
 */
public class ScenarioResult {

    // Name des Szenarios (z. B. "1 VM - Light")
    private final String scenarioName;

    // Architekturbezeichnung (z. B. "1 VM" oder "2 VM")
    private final String architectureLabel;

    // Workload-Typ (z. B. "Light", "Medium", "Heavy")
    private final String workloadLabel;

    // Anzahl der verwendeten VMs in diesem Szenario
    private final int vmCount;

    // Anzahl der erfolgreich abgearbeiteten Cloudlets (Requests)
    private final int finishedCloudlets;

    // Durchschnittliche Bearbeitungszeit aller Cloudlets
    private final double averageFinishTime;

    // Maximale Bearbeitungszeit (Worst Case / langsamster Request)
    private final double maxFinishTime;

    // Detailverteilung der Request-Typen (für Analyse)
    private final int staticAssetCount;
    private final int galleryViewCount;
    private final int imageDetailCount;
    private final int imageUploadCount;

    /**
     * Konstruktor setzt alle Metriken eines Szenarios.
     */
    public ScenarioResult(
            String scenarioName,
            String architectureLabel,
            String workloadLabel,
            int vmCount,
            int finishedCloudlets,
            double averageFinishTime,
            double maxFinishTime,
            int staticAssetCount,
            int galleryViewCount,
            int imageDetailCount,
            int imageUploadCount
    ) {
        this.scenarioName = scenarioName;
        this.architectureLabel = architectureLabel;
        this.workloadLabel = workloadLabel;
        this.vmCount = vmCount;
        this.finishedCloudlets = finishedCloudlets;
        this.averageFinishTime = averageFinishTime;
        this.maxFinishTime = maxFinishTime;
        this.staticAssetCount = staticAssetCount;
        this.galleryViewCount = galleryViewCount;
        this.imageDetailCount = imageDetailCount;
        this.imageUploadCount = imageUploadCount;
    }

    // Getter für Szenarioname
    public String getScenarioName() {
        return scenarioName;
    }

    // Getter für Architektur (VM-Anzahl)
    public String getArchitectureLabel() {
        return architectureLabel;
    }

    // Getter für Workload-Typ
    public String getWorkloadLabel() {
        return workloadLabel;
    }

    // Getter für VM-Anzahl
    public int getVmCount() {
        return vmCount;
    }

    // Getter für Anzahl verarbeiteter Requests
    public int getFinishedCloudlets() {
        return finishedCloudlets;
    }

    // Getter für durchschnittliche Laufzeit
    public double getAverageFinishTime() {
        return averageFinishTime;
    }

    // Getter für maximale Laufzeit
    public double getMaxFinishTime() {
        return maxFinishTime;
    }

    // Getter für Anzahl STATIC_ASSET Requests
    public int getStaticAssetCount() {
        return staticAssetCount;
    }

    // Getter für Anzahl GALLERY_VIEW Requests
    public int getGalleryViewCount() {
        return galleryViewCount;
    }

    // Getter für Anzahl IMAGE_DETAIL Requests
    public int getImageDetailCount() {
        return imageDetailCount;
    }

    // Getter für Anzahl IMAGE_UPLOAD Requests
    public int getImageUploadCount() {
        return imageUploadCount;
    }
}