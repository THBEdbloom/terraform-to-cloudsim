package de.thb.cloudsim.simulation;

/**
 * Diese Klasse übernimmt das Mapping von Terraform-"Flavors"
 * (z. B. g1.2, g1.4) auf konkrete Hardware-Parameter,
 * die in CloudSim verwendet werden.
 *
 * Hintergrund:
 * Terraform beschreibt Instanztypen abstrakt (Flavor),
 * während CloudSim konkrete Werte wie CPU, RAM, Bandbreite benötigt.
 *
 * → Diese Klasse ist damit ein zentraler Bestandteil der
 *   Transformation von Infrastruktur → Simulation.
 */
public class FlavorMapper {

    /**
     * Anzahl der Processing Elements (PEs) = CPU-Kerne.
     *
     * Wird in CloudSim verwendet, um Parallelität zu simulieren.
     */
    public static int getPes(String flavor) {
        return switch (flavor) {
            case "t3.micro" -> 2;
            case "t3.small" -> 2;
            case "t3.medium" -> 2;
            case "t3.large" -> 2;
            default -> 2;
        };
    }

    /**
     * Arbeitsspeicher (RAM) in Megabyte.
     *
     * Wichtig für:
     * - Ressourcenlimitierung
     * - realistischere Simulation
     */
    public static long getRamMb(String flavor) {
        return switch (flavor) {
            case "t3.micro" -> 1024;
            case "t3.small" -> 2048;
            case "t3.medium" -> 4096;
            case "t3.large" -> 8192;
            default -> 2048;
        };
    }

    /**
     * Netzwerkbandbreite (Bandwidth).
     *
     * Einheit in CloudSim typischerweise: Mbit/s oder abstrakte Einheit.
     *
     * Einfluss auf:
     * - Datenübertragung
     * - IO-intensive Workloads
     */
    public static long getBw(String flavor) {
        return switch (flavor) {
            case "t3.micro" -> 500;
            case "t3.small" -> 750;
            case "t3.medium" -> 1000;
            case "t3.large" -> 1500;
            default -> 1000;
        };
    }

    /**
     * Größe des VM-Speichers (Disk) in MB.
     *
     * Repräsentiert:
     * - virtuellen Speicherplatz
     * - Image-Größe / Root-Disk
     */
    public static long getSizeMb(String flavor) {
        return switch (flavor) {
            case "t3.micro" -> 20_000;
            case "t3.small" -> 30_000;
            case "t3.medium" -> 40_000;
            case "t3.large" -> 80_000;
            default -> 20_000;
        };
    }

    /**
     * Rechenleistung pro CPU-Kern (MIPS = Million Instructions Per Second).
     *
     * Das ist der wichtigste Wert für die Simulation:
     * → bestimmt direkt die Ausführungszeit der Cloudlets
     *
     * Höherer Wert = schnellere Verarbeitung
     */
    public static long getMipsPerPe(String flavor) {
        return switch (flavor) {
            case "t3.micro" -> 1000;
            case "t3.small" -> 1500;
            case "t3.medium" -> 2000;
            case "t3.large" -> 2500;
            default -> 1500;
        };
    }
}