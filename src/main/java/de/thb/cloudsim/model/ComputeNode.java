package de.thb.cloudsim.model;

/**
 * Repräsentiert einen Compute-Knoten aus Terraform
 * (z. B. stackit_server) im internen Infrastrukturmodell.
 *
 * Diese Klasse dient als Abstraktionsebene zwischen
 * Terraform und CloudSim.
 */
public class ComputeNode {

    // Name des Servers (eindeutige Identifikation)
    private final String name;

    // Flavor beschreibt die Größe/Leistung der VM (z. B. g1.2)
    private final String flavor;

    // Region (aktuell nur informativ, nicht in Simulation genutzt)
    private final String region;

    public ComputeNode(String name, String flavor, String region) {
        this.name = name;
        this.flavor = flavor;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getRegion() {
        return region;
    }

    /**
     * Debug-Ausgabe zur Anzeige im Infrastrukturmodell.
     */
    @Override
    public String toString() {
        return "ComputeNode{name='" + name + "', flavor='" + flavor + "', region='" + region + "'}";
    }
}