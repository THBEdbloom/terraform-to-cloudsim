package de.thb.cloudsim.workload;

/**
 * Factory-Klasse zur Erstellung vordefinierter Workload-Profile.
 *
 * Zweck:
 * → Definiert typische Lastszenarien für die Simulation
 * → Kapselt die Erstellung von Request-Profilen
 *
 * Workloads:
 * → Light   = geringe Last
 * → Medium  = mittlere Last
 * → Heavy   = hohe Last
 *
 * Diese Profile simulieren typische Nutzungsmuster einer Anwendung
 * wie z. B. BottleTube (Media Plattform).
 */
public class WorkloadFactory {

    /**
     * Light Workload:
     * → Wenige Requests
     * → Geringe Systemlast
     */
    public static WorkloadProfile createLightProfile() {
        WorkloadProfile profile = new WorkloadProfile("Light");

        // Einfacher statischer Content (z. B. CSS, Bilder)
        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET,
                1,          // Anzahl Requests
                3_000,      // Basis-Rechenaufwand
                0,          // keine DB-Nutzung
                500,        // geringer Storage-Zugriff
                1,          // benötigte CPU-Kerne
                512         // Datenmenge
        ));

        // Galerie anzeigen (DB + Storage)
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW,
                2,
                8_000,
                2_000,      // DB-Zugriff
                1_000,      // Storage-Zugriff
                1,
                1024
        ));

        // Bild-Upload (teuer!)
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD,
                1,
                10_000,
                4_000,      // viel DB
                3_000,      // viel Storage
                1,
                1024
        ));

        return profile;
    }

    /**
     * Medium Workload:
     * → Mehr Requests
     * → Mischung aus Lesen und Schreiben
     */
    public static WorkloadProfile createMediumProfile() {
        WorkloadProfile profile = new WorkloadProfile("Medium");

        // Mehr statische Requests
        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET,
                3,
                3_000,
                0,
                500,
                1,
                512
        ));

        // Mehr Galerie-Views
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW,
                5,
                8_000,
                2_000,
                1_000,
                1,
                1024
        ));

        // Detailansicht eines Bildes
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_DETAIL,
                2,
                10_000,
                3_000,
                1_500,
                1,
                1024
        ));

        // Mehr Uploads
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD,
                2,
                12_000,
                5_000,
                4_000,
                1,
                1024
        ));

        return profile;
    }

    /**
     * Heavy Workload:
     * → Hohe Last
     * → Viele gleichzeitige Requests
     *
     * Ziel:
     * → Stress-Test der Infrastruktur
     */
    public static WorkloadProfile createHeavyProfile() {
        WorkloadProfile profile = new WorkloadProfile("Heavy");

        // Viele statische Requests
        profile.addRequestProfile(new RequestProfile(
                RequestType.STATIC_ASSET,
                6,
                3_000,
                0,
                500,
                1,
                512
        ));

        // Sehr viele Galerie-Aufrufe
        profile.addRequestProfile(new RequestProfile(
                RequestType.GALLERY_VIEW,
                8,
                8_000,
                2_000,
                1_000,
                1,
                1024
        ));

        // Mehr Detailansichten
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_DETAIL,
                4,
                10_000,
                3_000,
                1_500,
                1,
                1024
        ));

        // Viele Uploads → sehr teuer
        profile.addRequestProfile(new RequestProfile(
                RequestType.IMAGE_UPLOAD,
                6,
                12_000,
                5_000,
                4_000,
                1,
                1024
        ));

        return profile;
    }
}