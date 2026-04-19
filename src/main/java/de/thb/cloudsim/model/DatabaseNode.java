package de.thb.cloudsim.model;

/**
 * Repräsentiert eine Datenbank-Instanz aus Terraform
 * (z. B. stackit_postgresqlflex_instance) im internen Infrastrukturmodell.
 *
 * Diese Klasse ist eine abstrahierte Darstellung einer Datenbankressource
 * und dient als Grundlage für die Simulation.
 *
 * Wichtig:
 * Die Datenbank wird in der Simulation nicht als eigenständige CloudSim-Entität
 * modelliert, sondern indirekt über zusätzliche Verarbeitungsaufwände
 * (z. B. Latenz/CPU-Penalty bei Requests).
 */
public class DatabaseNode {

    // Name der Datenbankinstanz (zur Identifikation)
    private final String name;

    // Flavor beschreibt die Größe/Leistung der Datenbankinstanz
    // (z. B. small, medium, etc.)
    private final String flavor;

    // Instanztyp (z. B. single, HA, cluster)
    // Gibt an, wie die Datenbank betrieben wird
    private final String instanceType;

    // Größe des persistenten Speichers (in GB)
    private final int diskSize;

    /**
     * Konstruktor zur Initialisierung aller Eigenschaften der Datenbankinstanz.
     */
    public DatabaseNode(String name, String flavor, String instanceType, int diskSize) {
        this.name = name;
        this.flavor = flavor;
        this.instanceType = instanceType;
        this.diskSize = diskSize;
    }

    /**
     * Gibt den Namen der Datenbank zurück.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Flavor (Leistungsprofil) der Datenbank zurück.
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * Gibt den Instanztyp zurück (z. B. single oder cluster).
     */
    public String getInstanceType() {
        return instanceType;
    }

    /**
     * Gibt die Größe des Speichers in GB zurück.
     */
    public int getDiskSize() {
        return diskSize;
    }

    /**
     * Debug-Ausgabe zur Darstellung im Infrastrukturmodell.
     */
    @Override
    public String toString() {
        return "DatabaseNode{name='" + name + "', flavor='" + flavor + "', instanceType='" + instanceType + "', diskSize=" + diskSize + "}";
    }
}