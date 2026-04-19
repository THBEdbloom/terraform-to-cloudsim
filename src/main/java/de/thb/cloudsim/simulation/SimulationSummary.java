package de.thb.cloudsim.simulation;

/**
 * Diese Klasse fasst die wichtigsten Ergebnisse einer einzelnen Simulation zusammen.
 *
 * Zweck:
 * → Rückgabeobjekt der CloudSim-Ausführung
 * → Enthält aggregierte Metriken zur Performance-Bewertung
 * → Wird später in ScenarioResult weiterverarbeitet und exportiert
 */
public class SimulationSummary {

    // Anzahl der verwendeten VMs in der Simulation
    private final int vmCount;

    // Anzahl der insgesamt ausgeführten Cloudlets (Requests)
    private final int cloudletCount;

    // Durchschnittliche Bearbeitungszeit aller Cloudlets
    private final double averageFinishTime;

    // Maximale Bearbeitungszeit (Worst Case)
    private final double maxFinishTime;

    /**
     * Konstruktor setzt alle relevanten Ergebniswerte.
     */
    public SimulationSummary(int vmCount, int cloudletCount, double averageFinishTime, double maxFinishTime) {
        this.vmCount = vmCount;
        this.cloudletCount = cloudletCount;
        this.averageFinishTime = averageFinishTime;
        this.maxFinishTime = maxFinishTime;
    }

    // Getter für VM-Anzahl
    public int getVmCount() {
        return vmCount;
    }

    // Getter für Anzahl der Cloudlets
    public int getCloudletCount() {
        return cloudletCount;
    }

    // Getter für durchschnittliche Laufzeit
    public double getAverageFinishTime() {
        return averageFinishTime;
    }

    // Getter für maximale Laufzeit
    public double getMaxFinishTime() {
        return maxFinishTime;
    }

    /**
     * String-Repräsentation für Debugging und Logging.
     */
    @Override
    public String toString() {
        return "SimulationSummary{" +
                "vmCount=" + vmCount +
                ", cloudletCount=" + cloudletCount +
                ", averageFinishTime=" + averageFinishTime +
                ", maxFinishTime=" + maxFinishTime +
                '}';
    }
}