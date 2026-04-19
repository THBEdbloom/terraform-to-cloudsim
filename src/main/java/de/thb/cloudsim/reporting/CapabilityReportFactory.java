package de.thb.cloudsim.reporting;

import java.util.List;

/**
 * Factory-Klasse zur Erstellung eines Standard-CapabilityReports.
 *
 * Diese Klasse definiert den gesamten Funktionsumfang
 * und die Grenzen des Prototyps.
 *
 * Wichtig: Die Inhalte dieser Klasse sind konzeptionell relevant
 * für die wissenschaftliche Einordnung der Arbeit.
 */
public class CapabilityReportFactory {

    /**
     * Erstellt den Standard-Report mit allen unterstützten,
     * ignorierten und simulierten Features.
     */
    public static CapabilityReport createDefaultReport() {
        return new CapabilityReport(

                // Unterstützte Terraform-Ressourcen und Features
                List.of(
                        "stackit_server",                      // Compute → VM
                        "stackit_postgresqlflex_instance",     // DB → vorhanden
                        "stackit_objectstorage_bucket",        // Storage → vorhanden
                        "stackit_loadbalancer",                // LB → abstrahiert
                        "variable defaults",                   // Variablenwerte
                        "locals",                              // lokale Variablen
                        "simple var/local interpolation"       // einfache Auflösung
                ),

                // Nicht unterstützte oder ignorierte Features
                List.of(
                        "stackit_key_pair",
                        "stackit_postgresqlflex_database",
                        "stackit_postgresqlflex_user",
                        "stackit_objectstorage_instance",
                        "stackit_objectstorage_object",
                        "nested blocks with full semantics",
                        "count",
                        "for_each",
                        "modules",
                        "dynamic blocks"
                ),

                // Abbildung von Terraform auf Simulation
                List.of(
                        "Compute nodes as CloudSim VMs",
                        "Database presence as additional request penalty",
                        "Object storage presence as additional request penalty",
                        "Horizontal scaling through VM count",
                        "Mixed BottleTube request types"
                ),

                // Grundsätzliche Einschränkungen
                List.of(
                        "No full Terraform evaluation engine",
                        "No provider-complete STACKIT support",
                        "No exact database service emulation",
                        "No exact object storage/CDN/network latency model",
                        "Load balancer is abstracted via broker scheduling",
                        "Unsupported resources are ignored, not transformed"
                )
        );
    }
}