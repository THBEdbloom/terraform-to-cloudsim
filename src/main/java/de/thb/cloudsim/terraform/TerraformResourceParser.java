package de.thb.cloudsim.terraform;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser für Terraform Ressourcen (resource Blöcke).
 *
 * Beispiel:
 * resource "stackit_server" "app" {
 *   name   = "bottletube-app"
 *   flavor = "g1.2"
 * }
 *
 * Ziel:
 * → Extrahiert:
 *    - type  (z. B. stackit_server)
 *    - name  (z. B. app)
 *    - Attribute (key=value)
 *
 * WICHTIG:
 * → Kein vollständiger Terraform Parser!
 * → Unterstützt nur einfache Attribute (keine verschachtelten Blöcke)
 */
public class TerraformResourceParser {

    /**
     * Erkennt den Start eines resource-Blocks.
     *
     * Beispiel:
     * resource "stackit_server" "app" {
     */
    private static final Pattern RESOURCE_HEADER_PATTERN =
            Pattern.compile("resource\\s+\"([^\"]+)\"\\s+\"([^\"]+)\"\\s*\\{");

    /**
     * Erkennt einfache Attribute:
     * Beispiel: flavor = "g1.2"
     */
    private static final Pattern ATTRIBUTE_PATTERN =
            Pattern.compile("^\\s*([a-zA-Z0-9_]+)\\s*=\\s*(.+)$");

    /**
     * Hauptmethode:
     * → Parst alle Dateien
     */
    public static List<TerraformResource> parseResources(List<String> fileContents) {
        List<TerraformResource> resources = new ArrayList<>();

        for (String content : fileContents) {
            resources.addAll(parseResourcesFromSingleFile(content));
        }

        return resources;
    }

    /**
     * Parst eine einzelne Datei.
     */
    private static List<TerraformResource> parseResourcesFromSingleFile(String content) {
        List<TerraformResource> resources = new ArrayList<>();
        String[] lines = content.split("\\R");

        TerraformResource currentResource = null;
        boolean insideResource = false; // sind wir im resource-Block?
        int braceDepth = 0;             // Tracking von { }

        for (String line : lines) {
            String trimmed = line.trim();

            // Start eines resource-Blocks erkennen
            if (!insideResource) {
                Matcher headerMatcher = RESOURCE_HEADER_PATTERN.matcher(trimmed);
                if (headerMatcher.find()) {
                    String type = headerMatcher.group(1);
                    String name = headerMatcher.group(2);

                    currentResource = new TerraformResource(type, name);
                    insideResource = true;
                    braceDepth = 1;
                }
                continue;
            }

            // Klammern zählen (für Block-Ende)
            braceDepth += countChar(line, '{');
            braceDepth -= countChar(line, '}');

            // Wenn Block endet → Resource speichern
            if (braceDepth == 0) {
                resources.add(currentResource);
                currentResource = null;
                insideResource = false;
                continue;
            }

            // Leere Zeilen und Kommentare ignorieren
            if (trimmed.isEmpty() || trimmed.startsWith("#") || trimmed.startsWith("//")) {
                continue;
            }

            // Attribute parsen
            Matcher attributeMatcher = ATTRIBUTE_PATTERN.matcher(trimmed);
            if (attributeMatcher.find()) {
                String key = attributeMatcher.group(1);
                String value = attributeMatcher.group(2).trim();

                // Nur einfache Werte berücksichtigen (keine nested blocks)
                if (isSimpleValue(value)) {
                    value = stripOptionalQuotes(value);
                    currentResource.addAttribute(key, value);
                }
            }
        }

        return resources;
    }

    /**
     * Prüft, ob ein Wert ein einfacher Wert ist.
     *
     * Ignoriert:
     * → verschachtelte Blöcke
     * → komplexe Strukturen
     */
    private static boolean isSimpleValue(String value) {
        String trimmed = value.trim();

        // Blockanfänge ausschließen
        if (trimmed.equals("{")) return false;
        if (trimmed.endsWith("{")) return false;

        return true;
    }

    /**
     * Entfernt optionale Anführungszeichen.
     */
    private static String stripOptionalQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Zählt Zeichen (für Klammer-Tracking).
     */
    private static int countChar(String text, char c) {
        int count = 0;
        for (char current : text.toCharArray()) {
            if (current == c) {
                count++;
            }
        }
        return count;
    }
}