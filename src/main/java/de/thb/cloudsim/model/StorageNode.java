package de.thb.cloudsim.model;

/**
 * Repräsentiert einen Object Storage / Bucket aus der Infrastruktur.
 *
 * Beispiel (Terraform):
 * → stackit_objectstorage_bucket
 *
 * Zweck:
 * → Abbildung von Storage-Komponenten im Infrastrukturmodell
 * → Wird in der Simulation aktuell NICHT direkt als Ressource modelliert,
 *   sondern indirekt über Performance-Penalties berücksichtigt
 *   (z. B. längere Bearbeitungszeit bei Zugriff auf Dateien)
 */
public class StorageNode {

    // Name des Storage-Buckets (z. B. "bottletube-media")
    private final String name;

    /**
     * Konstruktor setzt den Namen des Storage-Nodes.
     */
    public StorageNode(String name) {
        this.name = name;
    }

    // Getter für den Namen
    public String getName() {
        return name;
    }

    /**
     * String-Repräsentation für Debug-Ausgaben.
     */
    @Override
    public String toString() {
        return "StorageNode{name='" + name + "'}";
    }
}