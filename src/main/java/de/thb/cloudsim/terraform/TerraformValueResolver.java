package de.thb.cloudsim.terraform;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Diese Klasse löst Terraform-Werte auf.
 *
 * Ziel:
 * → Wandelt Referenzen wie:
 *    - var.name
 *    - local.app_name
 *    - ${var.name}-app-1
 * in konkrete Werte um
 *
 * Beispiel:
 * → var.name = "bottletube"
 * → local.app_name = var.name
 *
 * → ${local.app_name}-app-1 → bottletube-app-1
 *
 * WICHTIG:
 * → Unterstützt nur einfache Referenzen
 * → Kein vollständiger Terraform Expression Parser
 */
public class TerraformValueResolver {

    /**
     * Erkennt Terraform Interpolation:
     * Beispiel: ${...}
     */
    private static final Pattern INTERPOLATION_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Hauptmethode:
     * → Löst einen einzelnen Wert vollständig auf
     */
    public static String resolveValue(String rawValue, TerraformContext context) {
        if (rawValue == null) {
            return null;
        }

        String resolved = rawValue;

        // Schritt 1: direkte Referenzen (var.x / local.x)
        resolved = resolveDirectReference(resolved, context);

        // Schritt 2: Interpolationen ${...}
        resolved = resolveInterpolations(resolved, context);

        // Schritt 3: nochmal direkte Referenzen (für verschachtelte Fälle)
        resolved = resolveDirectReference(resolved, context);

        return resolved;
    }

    /**
     * Löst direkte Referenzen:
     * → var.name
     * → local.app_name
     */
    private static String resolveDirectReference(String value, TerraformContext context) {

        // Variable
        if (value.startsWith("var.")) {
            String varName = value.substring("var.".length());
            return context.getVariables().getOrDefault(varName, value);
        }

        // Local
        if (value.startsWith("local.")) {
            String localName = value.substring("local.".length());
            String localValue = context.getLocals().getOrDefault(localName, value);

            // Rekursive Auflösung (z. B. local → var)
            if (!localValue.equals(value)) {
                return resolveDirectReference(localValue, context);
            }

            return localValue;
        }

        return value;
    }

    /**
     * Löst Terraform Interpolationen:
     *
     * Beispiel:
     * → "${var.name}-app"
     * → "bottletube-app"
     */
    private static String resolveInterpolations(String value, TerraformContext context) {
        Matcher matcher = INTERPOLATION_PATTERN.matcher(value);

        // StringBuffer nötig für appendReplacement
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String expression = matcher.group(1).trim();

            // Ausdruck auflösen (z. B. var.name)
            String replacement = resolveDirectReference(expression, context);

            // Sicher ersetzen (escaping beachten)
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Löst alle Attribute einer TerraformResource auf.
     *
     * Beispiel:
     * → name = "${local.app_name}-app-1"
     * → wird ersetzt durch → bottletube-app-1
     */
    public static void resolveResourceAttributes(TerraformResource resource, TerraformContext context) {

        for (Map.Entry<String, String> entry : resource.getAttributes().entrySet()) {

            String resolvedValue = resolveValue(entry.getValue(), context);

            // Wert im Resource-Objekt überschreiben
            entry.setValue(resolvedValue);
        }
    }
}