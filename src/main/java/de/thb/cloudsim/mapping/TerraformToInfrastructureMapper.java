package de.thb.cloudsim.mapping;

import de.thb.cloudsim.model.ComputeNode;
import de.thb.cloudsim.model.DatabaseNode;
import de.thb.cloudsim.model.InfrastructureModel;
import de.thb.cloudsim.model.LoadBalancerNode;
import de.thb.cloudsim.model.StorageNode;
import de.thb.cloudsim.terraform.TerraformResource;

import java.util.List;
import java.util.Map;

/**
 * Diese Klasse ist der zentrale Schritt der Transformation:
 *
 * Terraform → Infrastrukturmodell
 *
 * Zweck:
 * → Wandelt generische TerraformResource Objekte in konkrete Infrastruktur-Objekte um:
 *    - ComputeNode
 *    - DatabaseNode
 *    - StorageNode
 *    - LoadBalancerNode
 *
 * → Baut daraus das InfrastructureModel für die Simulation
 */
public class TerraformToInfrastructureMapper {

    /**
     * Hauptmethode:
     * → Nimmt alle geparsten Terraform Ressourcen
     * → Wandelt sie in das interne Infrastrukturmodell um
     */
    public static InfrastructureModel map(List<TerraformResource> resources) {
        InfrastructureModel model = new InfrastructureModel();

        for (TerraformResource resource : resources) {
            String type = resource.getType();                 // z. B. stackit_server
            Map<String, String> attrs = resource.getAttributes();

            /**
             * Mapping je nach Ressourcentyp
             *
             * WICHTIG:
             * → Hier passiert die eigentliche "Semantik-Übersetzung"
             * → Terraform → Simulation
             */
            switch (type) {

                /**
                 * Compute → wird zu VM in CloudSim
                 */
                case "stackit_server" -> {
                    String name = getEffectiveName(resource);
                    String flavor = attrs.getOrDefault("flavor", "unknown");
                    String region = attrs.getOrDefault("region", "unknown");

                    model.getComputeNodes().add(new ComputeNode(name, flavor, region));
                    model.getImportReport().incrementSupportedResources();
                }

                /**
                 * PostgreSQL → wird zu DatabaseNode
                 *
                 * In der Simulation:
                 * → erzeugt KEINE eigene Ressource
                 * → sondern beeinflusst Request-Laufzeiten (Penalty)
                 */
                case "stackit_postgresqlflex_instance" -> {
                    String name = getEffectiveName(resource);
                    String flavor = attrs.getOrDefault("flavor", "unknown");
                    String instanceType = attrs.getOrDefault("instance_type", "unknown");
                    int diskSize = parseIntOrDefault(attrs.get("disk_size"), 0);

                    model.getDatabaseNodes().add(new DatabaseNode(name, flavor, instanceType, diskSize));
                    model.getImportReport().incrementSupportedResources();
                }

                /**
                 * Object Storage → StorageNode
                 *
                 * In Simulation:
                 * → beeinflusst ebenfalls nur Laufzeiten (Penalty)
                 */
                case "stackit_objectstorage_bucket" -> {
                    String name = getEffectiveName(resource);

                    model.getStorageNodes().add(new StorageNode(name));
                    model.getImportReport().incrementSupportedResources();
                }

                /**
                 * Load Balancer → abstrakt modelliert
                 *
                 * In Simulation:
                 * → keine echte Routing-Logik
                 * → wird indirekt über Broker-Verteilung simuliert
                 */
                case "stackit_loadbalancer" -> {
                    String name = getEffectiveName(resource);
                    String lbType = attrs.getOrDefault("type", "unknown");

                    model.getLoadBalancers().add(new LoadBalancerNode(name, lbType));
                    model.getImportReport().incrementSupportedResources();
                }

                /**
                 * Alle nicht unterstützten Ressourcen:
                 * → werden ignoriert
                 * → aber im Report dokumentiert
                 */
                default -> {
                    model.getImportReport().incrementIgnoredResources();

                    model.getImportReport().addWarning(
                            "Resource type '" + type + "' with name '" + resource.getName() + "' is currently not supported and was ignored."
                    );
                }
            }
        }

        // Validierung der erzeugten Infrastruktur
        validateModel(model);

        return model;
    }

    /**
     * Bestimmt den effektiven Namen einer Ressource.
     *
     * Priorität:
     * 1. Attribut "name"
     * 2. Terraform interner Name
     */
    private static String getEffectiveName(TerraformResource resource) {
        String attributeName = resource.getAttributes().get("name");

        if (attributeName != null && !attributeName.isBlank()) {
            return attributeName;
        }

        return resource.getName();
    }

    /**
     * Validiert das Infrastrukturmodell.
     *
     * Ziel:
     * → Frühzeitig Probleme erkennen
     * → Warnungen erzeugen
     */
    private static void validateModel(InfrastructureModel model) {

        if (model.getComputeNodes().isEmpty()) {
            model.getImportReport().addWarning(
                    "No compute nodes were detected. Simulation may not run meaningfully."
            );
        }

        if (model.getLoadBalancers().isEmpty()) {
            model.getImportReport().addWarning(
                    "No load balancer was detected."
            );
        }

        if (model.getDatabaseNodes().isEmpty()) {
            model.getImportReport().addWarning(
                    "No database node was detected."
            );
        }

        if (model.getStorageNodes().isEmpty()) {
            model.getImportReport().addWarning(
                    "No storage node was detected."
            );
        }
    }

    /**
     * Hilfsmethode zum sicheren Parsen von Integer-Werten.
     *
     * → Verhindert Abstürze bei fehlerhaften Terraform-Werten
     */
    private static int parseIntOrDefault(String value, int defaultValue) {
        if (value == null) return defaultValue;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}