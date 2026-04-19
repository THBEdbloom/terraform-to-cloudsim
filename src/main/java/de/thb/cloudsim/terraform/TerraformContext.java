package de.thb.cloudsim.terraform;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Diese Klasse repräsentiert den "Terraform Context".
 *
 * Das entspricht in Terraform:
 * → variable {...}
 * → locals {...}
 *
 * Zweck:
 * → Zentrale Sammlung aller Variablen und lokalen Werte
 * → Wird später beim Auflösen von Attributen verwendet (z. B. var.name, local.app_name)
 *
 * Wichtig:
 * → Ohne diesen Context könnten Ressourcenwerte nicht korrekt berechnet werden
 */
public class TerraformContext {

    /**
     * Speichert alle Terraform-Variablen.
     *
     * Beispiel:
     * variable "name" {
     *   default = "bottletube"
     * }
     *
     * → "name" -> "bottletube"
     */
    private final Map<String, String> variables = new LinkedHashMap<>();

    /**
     * Speichert alle Terraform-Locals.
     *
     * Beispiel:
     * locals {
     *   app_name = var.name
     * }
     *
     * → "app_name" -> "bottletube" (nach Auflösung)
     */
    private final Map<String, String> locals = new LinkedHashMap<>();

    /**
     * Gibt alle Variablen zurück.
     */
    public Map<String, String> getVariables() {
        return variables;
    }

    /**
     * Gibt alle Locals zurück.
     */
    public Map<String, String> getLocals() {
        return locals;
    }

    /**
     * Fügt eine Variable hinzu.
     *
     * Wird beim Parsen der Terraform-Dateien aufgerufen.
     */
    public void addVariable(String name, String value) {
        variables.put(name, value);
    }

    /**
     * Fügt einen Local-Wert hinzu.
     *
     * Wird beim Parsen von "locals" Blöcken verwendet.
     */
    public void addLocal(String name, String value) {
        locals.put(name, value);
    }
}