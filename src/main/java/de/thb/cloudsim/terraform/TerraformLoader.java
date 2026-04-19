package de.thb.cloudsim.terraform;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist für das Laden aller Terraform-Dateien (.tf) aus einem Verzeichnis zuständig.
 *
 * Zweck:
 * → Liest rekursiv alle Terraform-Dateien ein
 * → Gibt deren Inhalte als Liste von Strings zurück
 *
 * Wichtig:
 * → Dies ist der erste Schritt der Pipeline:
 *   Terraform → Parsing → Modell → Simulation
 */
public class TerraformLoader {

    /**
     * Lädt alle .tf Dateien aus einem Ordner (inkl. Unterordner).
     *
     * @param folderPath Pfad zum Terraform-Projekt
     * @return Liste mit den Inhalten aller .tf Dateien
     */
    public static List<String> loadAllTerraformFiles(String folderPath) throws IOException {
        List<String> fileContents = new ArrayList<>();

        // Durchläuft rekursiv alle Dateien im Ordner
        Files.walk(Paths.get(folderPath))
                // Filtert nur Terraform-Dateien
                .filter(path -> path.toString().endsWith(".tf"))
                .forEach(path -> {
                    try {
                        // Liest den kompletten Dateiinhalt als String
                        String content = Files.readString(path);

                        // Fügt den Inhalt zur Liste hinzu
                        fileContents.add(content);

                        // Debug-Ausgabe: zeigt welche Datei geladen wurde
                        System.out.println("Loaded: " + path);
                    } catch (IOException e) {
                        // Fehler beim Lesen der Datei
                        e.printStackTrace();
                    }
                });

        return fileContents;
    }
}