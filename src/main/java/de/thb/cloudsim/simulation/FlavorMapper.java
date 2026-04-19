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
            case "g1.2" -> 2;   // kleiner Instanztyp → 2 Kerne
            case "g1.4" -> 4;   // größerer Instanztyp → 4 Kerne
            default -> 2;       // Fallback für unbekannte Flavors
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
            case "g1.2" -> 4096;   // 4 GB RAM
            case "g1.4" -> 8192;   // 8 GB RAM
            default -> 2048;       // Fallback (2 GB)
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
            case "g1.2" -> 1000;
            case "g1.4" -> 2000;
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
            case "g1.2" -> 40_000;   // 40 GB
            case "g1.4" -> 80_000;   // 80 GB
            default -> 20_000;       // 20 GB
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
            case "g1.2" -> 2000;
            case "g1.4" -> 2500;
            default -> 1500;
        };
    }
}