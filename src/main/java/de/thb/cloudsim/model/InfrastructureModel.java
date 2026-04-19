package de.thb.cloudsim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Zentrales Domänenmodell für die gesamte Infrastruktur.
 *
 * Diese Klasse bündelt alle aus Terraform extrahierten Ressourcen
 * in einer vereinheitlichten, technologieunabhängigen Struktur.
 *
 * Rolle im System:
 * Terraform → Parser → Mapper → InfrastructureModel → Simulation
 *
 * → Sie ist die zentrale Schnittstelle zwischen Parsing und CloudSim.
 */
public class InfrastructureModel {

    // Liste aller Compute-Knoten (VMs / Server)
    private final List<ComputeNode> computeNodes = new ArrayList<>();

    // Liste aller Datenbankinstanzen
    private final List<DatabaseNode> databaseNodes = new ArrayList<>();

    // Liste aller Storage-Komponenten (z. B. Object Storage)
    private final List<StorageNode> storageNodes = new ArrayList<>();

    // Liste aller Load Balancer
    private final List<LoadBalancerNode> loadBalancers = new ArrayList<>();

    /**
     * Report über den Importprozess.
     *
     * Enthält:
     * - Anzahl unterstützter Ressourcen
     * - Anzahl ignorierter Ressourcen
     * - Warnungen
     *
     * Wird während des Mappings befüllt.
     */
    private final ImportReport importReport = new ImportReport();

    /**
     * Gibt alle Compute Nodes zurück.
     */
    public List<ComputeNode> getComputeNodes() {
        return computeNodes;
    }

    /**
     * Gibt alle Datenbank-Knoten zurück.
     */
    public List<DatabaseNode> getDatabaseNodes() {
        return databaseNodes;
    }

    /**
     * Gibt alle Storage-Knoten zurück.
     */
    public List<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    /**
     * Gibt alle Load Balancer zurück.
     */
    public List<LoadBalancerNode> getLoadBalancers() {
        return loadBalancers;
    }

    /**
     * Gibt den ImportReport zurück.
     *
     * Wichtig für:
     * - Debugging
     * - Transparenz
     * - wissenschaftliche Auswertung
     */
    public ImportReport getImportReport() {
        return importReport;
    }
}