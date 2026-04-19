package de.thb.cloudsim.terraform;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser für Terraform Context (Variables + Locals).
 *
 * Zweck:
 * → Extrahiert aus .tf Dateien:
 *    - variable Blöcke
 *    - locals Blöcke
 * → Befüllt ein TerraformContext Objekt
 *
 * WICHTIG:
 * → Das ist KEIN vollständiger Terraform Parser!
 * → Arbeitet regex-basiert → bewusst simpel gehalten
 */
public class TerraformContextParser {

    /**
     * Erkennt den Start eines variable-Blocks:
     * Beispiel: variable "name" {
     */
    private static final Pattern VARIABLE_BLOCK_START =
            Pattern.compile("^\\s*variable\\s+\"([^\"]+)\"\\s*\\{\\s*$");

    /**
     * Erkennt default-Werte innerhalb eines variable-Blocks:
     * Beispiel: default = "bottletube"
     */
    private static final Pattern DEFAULT_PATTERN =
            Pattern.compile("^\\s*default\\s*=\\s*(.+)\\s*$");

    /**
     * Erkennt den Start eines locals-Blocks:
     * Beispiel: locals {
     */
    private static final Pattern LOCALS_BLOCK_START =
            Pattern.compile("^\\s*locals\\s*\\{\\s*$");

    /**
     * Erkennt Zuweisungen in locals:
     * Beispiel: app_name = var.name
     */
    private static final Pattern LOCAL_ASSIGNMENT_PATTERN =
            Pattern.compile("^\\s*([a-zA-Z0-9_]+)\\s*=\\s*(.+)\\s*$");

    /**
     * Hauptmethode:
     * → Durchläuft alle Terraform-Dateien
     * → Extrahiert Variablen und Locals
     */
    public static TerraformContext parseContext(List<String> fileContents) {
        TerraformContext context = new TerraformContext();

        for (String content : fileContents) {
            parseVariables(content, context);
            parseLocals(content, context);
        }

        return context;
    }

    /**
     * Parst alle variable-Blöcke in einer Datei.
     *
     * Ablauf:
     * → Suche nach "variable" Start
     * → Tracke geschweifte Klammern (braceDepth)
     * → Extrahiere default-Wert
     */
    private static void parseVariables(String content, TerraformContext context) {
        String[] lines = content.split("\\R");

        boolean insideVariable = false;     // sind wir innerhalb eines variable-Blocks?
        String currentVariableName = null;  // Name der aktuellen Variable
        int braceDepth = 0;                 // Tracking für verschachtelte Klammern

        for (String line : lines) {
            String trimmed = line.trim();

            // Start eines variable-Blocks erkennen
            if (!insideVariable) {
                Matcher startMatcher = VARIABLE_BLOCK_START.matcher(trimmed);
                if (startMatcher.find()) {
                    insideVariable = true;
                    currentVariableName = startMatcher.group(1);
                    braceDepth = 1;
                }
                continue;
            }

            // Klammern zählen → wichtig für Block-Ende
            braceDepth += countChar(line, '{');
            braceDepth -= countChar(line, '}');

            // default-Wert extrahieren
            Matcher defaultMatcher = DEFAULT_PATTERN.matcher(trimmed);
            if (defaultMatcher.find() && currentVariableName != null) {
                String value = stripOptionalQuotes(defaultMatcher.group(1).trim());
                context.addVariable(currentVariableName, value);
            }

            // Ende des variable-Blocks erreicht
            if (braceDepth == 0) {
                insideVariable = false;
                currentVariableName = null;
            }
        }
    }

    /**
     * Parst locals-Blöcke.
     *
     * Ablauf:
     * → Suche nach "locals {"
     * → Extrahiere key = value Paare
     */
    private static void parseLocals(String content, TerraformContext context) {
        String[] lines = content.split("\\R");

        boolean insideLocals = false; // sind wir im locals-Block?
        int braceDepth = 0;

        for (String line : lines) {
            String trimmed = line.trim();

            // Start eines locals-Blocks
            if (!insideLocals) {
                Matcher startMatcher = LOCALS_BLOCK_START.matcher(trimmed);
                if (startMatcher.find()) {
                    insideLocals = true;
                    braceDepth = 1;
                }
                continue;
            }

            // Klammern zählen
            braceDepth += countChar(line, '{');
            braceDepth -= countChar(line, '}');

            // Zuweisung erkennen (key = value)
            Matcher assignmentMatcher = LOCAL_ASSIGNMENT_PATTERN.matcher(trimmed);
            if (assignmentMatcher.find() && !trimmed.startsWith("}")) {
                String key = assignmentMatcher.group(1);
                String value = stripOptionalQuotes(assignmentMatcher.group(2).trim());
                context.addLocal(key, value);
            }

            // Ende des locals-Blocks
            if (braceDepth == 0) {
                insideLocals = false;
            }
        }
    }

    /**
     * Entfernt optionale Anführungszeichen.
     *
     * Beispiel:
     * → "bottletube" → bottletube
     */
    private static String stripOptionalQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Zählt wie oft ein bestimmtes Zeichen in einem String vorkommt.
     *
     * Wird verwendet für:
     * → Tracking von { und } (braceDepth)
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