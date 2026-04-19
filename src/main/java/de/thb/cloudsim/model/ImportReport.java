package de.thb.cloudsim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse sammelt Informationen darüber,
 * wie gut Terraform-Ressourcen in das interne Modell übernommen wurden.
 *
 * Zweck:
 * → Transparenz beim Importprozess
 * → Nachvollziehbarkeit für Nutzer und wissenschaftliche Auswertung
 *
 * Der Report wird während des Parsings und Mappings schrittweise befüllt.
 */
public class ImportReport {

    // Anzahl erfolgreich unterstützter/übernommener Ressourcen
    private int supportedResources = 0;

    // Anzahl ignorierter (nicht unterstützter) Ressourcen
    private int ignoredResources = 0;

    // Liste von Warnungen für nicht unterstützte oder problematische Ressourcen
    private final List<String> warnings = new ArrayList<>();

    /**
     * Gibt die Anzahl unterstützter Ressourcen zurück.
     */
    public int getSupportedResources() {
        return supportedResources;
    }

    /**
     * Erhöht den Zähler für unterstützte Ressourcen.
     *
     * Wird z. B. aufgerufen, wenn eine Terraform-Ressource
     * erfolgreich in das Infrastrukturmodell überführt wurde.
     */
    public void incrementSupportedResources() {
        this.supportedResources++;
    }

    /**
     * Gibt die Anzahl ignorierter Ressourcen zurück.
     */
    public int getIgnoredResources() {
        return ignoredResources;
    }

    /**
     * Erhöht den Zähler für ignorierte Ressourcen.
     *
     * Wird verwendet, wenn eine Ressource nicht unterstützt wird
     * und daher nicht ins Modell übernommen werden kann.
     */
    public void incrementIgnoredResources() {
        this.ignoredResources++;
    }

    /**
     * Gibt alle gesammelten Warnungen zurück.
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Fügt eine neue Warnung hinzu.
     *
     * Typische Fälle:
     * - nicht unterstützter Ressourcentyp
     * - unvollständig interpretierte Konfiguration
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }
}