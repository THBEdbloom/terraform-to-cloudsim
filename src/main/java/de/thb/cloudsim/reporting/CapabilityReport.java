package de.thb.cloudsim.reporting;

import java.util.List;

/**
 * Der CapabilityReport beschreibt die Fähigkeiten und Grenzen
 * des Prototyps bezüglich Terraform-Verarbeitung und Simulation.
 *
 * Diese Klasse enthält KEINE Logik, sondern nur strukturierte Daten.
 * Sie dient zur Dokumentation, Ausgabe und zum CSV-Export.
 */
public class CapabilityReport {

    /**
     * Liste der unterstützten Terraform-Ressourcen und Features.
     * Beispiel:
     * - stackit_server
     * - stackit_loadbalancer
     * - variable defaults
     */
    private final List<String> supportedTerraformResources;

    /**
     * Liste der ignorierten oder nur teilweise unterstützten Features.
     * Beispiel:
     * - modules
     * - for_each
     * - dynamic blocks
     */
    private final List<String> ignoredTerraformResources;

    /**
     * Beschreibt, wie Terraform-Konzepte in Simulation übersetzt werden.
     * Beispiel:
     * - Compute nodes → CloudSim VMs
     * - DB → Request Penalty
     */
    private final List<String> simulatedConcepts;

    /**
     * Grundsätzliche Limitationen des Prototyps.
     * Beispiel:
     * - Kein vollständiger Terraform-Interpreter
     * - Keine Netzwerkmodellierung
     */
    private final List<String> limitations;

    /**
     * Konstruktor setzt alle vier Bereiche.
     * Die Klasse ist danach unveränderlich (immutable).
     */
    public CapabilityReport(
            List<String> supportedTerraformResources,
            List<String> ignoredTerraformResources,
            List<String> simulatedConcepts,
            List<String> limitations
    ) {
        this.supportedTerraformResources = supportedTerraformResources;
        this.ignoredTerraformResources = ignoredTerraformResources;
        this.simulatedConcepts = simulatedConcepts;
        this.limitations = limitations;
    }

    /**
     * Gibt die unterstützten Terraform-Features zurück.
     */
    public List<String> getSupportedTerraformResources() {
        return supportedTerraformResources;
    }

    /**
     * Gibt die ignorierten oder nur teilweise unterstützten Features zurück.
     */
    public List<String> getIgnoredTerraformResources() {
        return ignoredTerraformResources;
    }

    /**
     * Gibt die simulierten Konzepte zurück.
     */
    public List<String> getSimulatedConcepts() {
        return simulatedConcepts;
    }

    /**
     * Gibt die bekannten Limitationen zurück.
     */
    public List<String> getLimitations() {
        return limitations;
    }
}