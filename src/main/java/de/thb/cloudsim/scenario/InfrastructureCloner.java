package de.thb.cloudsim.scenario;

import de.thb.cloudsim.model.InfrastructureModel;

/**
 * Hilfsklasse zum Erzeugen von Varianten eines Infrastrukturmodells.
 *
 * Zweck:
 * → Vergleich verschiedener Architekturen (z. B. 1 VM vs. mehrere VMs)
 * → Grundlage für Skalierungsanalysen in der Simulation
 *
 * Wichtig:
 * Diese Klasse verändert NICHT das Originalmodell,
 * sondern erzeugt eine angepasste Kopie.
 */
public class InfrastructureCloner {

    /**
     * Erstellt eine Variante des Infrastrukturmodells mit genau einem Compute Node.
     *
     * Verwendung:
     * → Simulation eines "Single-VM"-Szenarios
     * → Vergleich gegen horizontale Skalierung (mehrere VMs)
     *
     * Vorgehen:
     * - übernimmt genau einen ComputeNode (falls vorhanden)
     * - alle anderen Ressourcen (DB, Storage, LB) bleiben unverändert
     *
     * Einschränkung:
     * - Es wird nur der erste ComputeNode verwendet (keine Auswahlstrategie)
     * - Es handelt sich um eine flache Kopie (keine Deep Copy)
     */
    public static InfrastructureModel cloneWithSingleComputeNode(InfrastructureModel original) {

        // Neues, leeres Infrastrukturmodell
        InfrastructureModel clone = new InfrastructureModel();

        // Falls Compute Nodes vorhanden sind:
        // → genau einen (den ersten) übernehmen
        if (!original.getComputeNodes().isEmpty()) {
            clone.getComputeNodes().add(original.getComputeNodes().get(0));
        }

        // Alle anderen Ressourcen unverändert übernehmen

        // Datenbanken
        clone.getDatabaseNodes().addAll(original.getDatabaseNodes());

        // Storage (z. B. Object Storage)
        clone.getStorageNodes().addAll(original.getStorageNodes());

        // Load Balancer
        clone.getLoadBalancers().addAll(original.getLoadBalancers());

        // Ergebnis zurückgeben
        return clone;
    }
}