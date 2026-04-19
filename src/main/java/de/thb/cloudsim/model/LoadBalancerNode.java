package de.thb.cloudsim.model;

/**
 * Repräsentiert einen Load Balancer aus Terraform
 * (z. B. stackit_loadbalancer) im internen Infrastrukturmodell.
 *
 * Zweck:
 * → Abbildung von Traffic-Verteilungskomponenten
 * → Teil der Gesamtarchitektur (z. B. für Web-Apps wie BottleTube)
 *
 * Wichtig:
 * Der Load Balancer wird aktuell NICHT direkt in CloudSim modelliert,
 * sondern indirekt über das Scheduling-Verhalten des Brokers abstrahiert.
 */
public class LoadBalancerNode {

    // Name des Load Balancers (zur Identifikation)
    private final String name;

    // Typ des Load Balancers (z. B. application, network)
    private final String type;

    /**
     * Konstruktor zur Initialisierung des Load Balancers.
     */
    public LoadBalancerNode(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gibt den Namen des Load Balancers zurück.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Typ des Load Balancers zurück.
     */
    public String getType() {
        return type;
    }

    /**
     * Debug-Ausgabe zur Darstellung im Infrastrukturmodell.
     */
    @Override
    public String toString() {
        return "LoadBalancerNode{name='" + name + "', type='" + type + "'}";
    }
}