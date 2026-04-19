package de.thb.cloudsim.terraform;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Repräsentiert eine einzelne Terraform-Ressource.
 *
 * Beispiel aus Terraform:
 * resource "stackit_server" "app" {
 *   name   = "bottletube-app"
 *   flavor = "g1.2"
 * }
 *
 * → type = "stackit_server"
 * → name = "app"
 * → attributes = { name=bottletube-app, flavor=g1.2 }
 *
 * Zweck:
 * → Zwischenschritt zwischen Parsing und Infrastrukturmodell
 * → Wird später in ComputeNode, DatabaseNode usw. gemappt
 */
public class TerraformResource {

    // Terraform Ressourcentyp (z. B. stackit_server)
    private final String type;

    // Terraform Ressourcenname (z. B. "app")
    private final String name;

    /**
     * Attribute der Ressource als Key-Value Map.
     *
     * Beispiel:
     * → "flavor" → "g1.2"
     * → "region" → "eu01"
     *
     * LinkedHashMap:
     * → erhält Einfügereihenfolge (hilfreich für Debugging / Output)
     */
    private final Map<String, String> attributes = new LinkedHashMap<>();

    /**
     * Konstruktor setzt Typ und Name.
     */
    public TerraformResource(String type, String name) {
        this.type = type;
        this.name = name;
    }

    // Getter für Ressourcentyp
    public String getType() {
        return type;
    }

    // Getter für Ressourcenname
    public String getName() {
        return name;
    }

    // Zugriff auf alle Attribute
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Fügt ein Attribut hinzu (key = value).
     *
     * Wird vom ResourceParser verwendet.
     */
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    /**
     * String-Repräsentation für Debug-Ausgaben.
     */
    @Override
    public String toString() {
        return "TerraformResource{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}