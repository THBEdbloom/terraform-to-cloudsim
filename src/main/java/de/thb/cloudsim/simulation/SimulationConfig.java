package de.thb.cloudsim.simulation;

/**
 * Zentrale Konfigurationsklasse für die Simulation.
 *
 * Zweck:
 * → Bündelt globale Flags und Einstellungen an einer Stelle
 * → Ermöglicht schnelles Aktivieren/Deaktivieren von Features,
 *   ohne den Code an mehreren Stellen ändern zu müssen
 */
public class SimulationConfig {

    /**
     * Steuert, ob detaillierte Cloudlet-Ergebnisse ausgegeben werden.
     *
     * true  → Für jedes Cloudlet wird ausgegeben:
     *         - ID
     *         - zugewiesene VM
     *         - Status
     *         - Finish Time
     *
     * false → Nur aggregierte Ergebnisse (Average / Max) werden verwendet
     *
     * Verwendung:
     * → Besonders hilfreich beim Debugging oder zur Analyse einzelner Requests
     * → Sollte für größere Simulationen deaktiviert bleiben (Performance + Übersicht)
     */
    public static final boolean PRINT_CLOUDLET_DETAILS = false;
}