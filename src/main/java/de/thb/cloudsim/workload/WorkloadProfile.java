package de.thb.cloudsim.workload;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert ein komplettes Workload-Profil.
 *
 * Beispiel:
 * → "Light", "Medium", "Heavy"
 *
 * Zweck:
 * → Bündelt mehrere RequestProfile (verschiedene Request-Typen)
 * → Beschreibt die gesamte Last, die auf die Infrastruktur wirkt
 *
 * Struktur:
 * → Ein Workload besteht aus mehreren Request-Typen:
 *    - STATIC_ASSET
 *    - GALLERY_VIEW
 *    - IMAGE_DETAIL
 *    - IMAGE_UPLOAD
 */
public class WorkloadProfile {

    // Name des Workloads (z. B. "Light", "Medium", "Heavy")
    private final String name;

    /**
     * Liste aller RequestProfile innerhalb dieses Workloads.
     *
     * Jedes RequestProfile beschreibt:
     * → einen Request-Typ
     * → Anzahl
     * → Rechenkosten
     */
    private final List<RequestProfile> requestProfiles = new ArrayList<>();

    /**
     * Konstruktor setzt den Namen des Workloads.
     */
    public WorkloadProfile(String name) {
        this.name = name;
    }

    // Getter für den Namen
    public String getName() {
        return name;
    }

    // Zugriff auf alle enthaltenen Request-Profile
    public List<RequestProfile> getRequestProfiles() {
        return requestProfiles;
    }

    /**
     * Fügt ein RequestProfile hinzu.
     *
     * Wird von der WorkloadFactory verwendet.
     */
    public void addRequestProfile(RequestProfile profile) {
        requestProfiles.add(profile);
    }

    /**
     * Berechnet die Gesamtanzahl aller Requests im Workload.
     *
     * Beispiel:
     * → 3 STATIC + 5 GALLERY + 2 UPLOAD = 10
     */
    public int getTotalRequestCount() {
        int sum = 0;

        for (RequestProfile profile : requestProfiles) {
            sum += profile.getCount();
        }

        return sum;
    }
}