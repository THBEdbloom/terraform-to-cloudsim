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
                List.of(
                        "aws_instance",
                        "aws_db_instance",
                        "aws_s3_bucket",
                        "aws_lb",
                        "variable defaults",
                        "locals",
                        "simple var/local interpolation"
                ),
                List.of(
                        "aws_security_group",
                        "aws_vpc",
                        "aws_subnet",
                        "aws_internet_gateway",
                        "aws_route_table",
                        "aws_key_pair",
                        "tags block parsing",
                        "nested blocks with full semantics",
                        "count",
                        "for_each",
                        "modules",
                        "dynamic blocks"
                ),
                List.of(
                        "EC2 instances as CloudSim VMs",
                        "RDS presence as additional request penalty",
                        "S3 presence as additional request penalty",
                        "Horizontal scaling through VM count",
                        "Mixed BottleTube request types"
                ),
                List.of(
                        "No full Terraform evaluation engine",
                        "No complete AWS provider support",
                        "No exact RDS service emulation",
                        "No exact S3 or network latency model",
                        "Load balancer is abstracted via broker scheduling",
                        "Unsupported resources are ignored, not transformed"
                )
        );
    }
}