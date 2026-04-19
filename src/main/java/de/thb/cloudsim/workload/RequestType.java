package de.thb.cloudsim.workload;

/**
 * Enum zur Definition der verschiedenen Request-Typen
 * innerhalb des simulierten Workloads.
 *
 * Zweck:
 * → Klassifizierung von Nutzeranfragen
 * → Grundlage für unterschiedliche Lastprofile in der Simulation
 *
 * Jeder Request-Typ repräsentiert einen typischen Anwendungsfall
 * der Beispielanwendung (z. B. BottleTube).
 */
public enum RequestType {

    /**
     * Statische Inhalte (z. B. CSS, JS, Bilder).
     *
     * Eigenschaften:
     * - geringer Rechenaufwand
     * - kein oder minimaler Datenbankzugriff
     * - häufige Nutzung
     */
    STATIC_ASSET,

    /**
     * Anzeige einer Galerie (z. B. Übersicht mehrerer Bilder).
     *
     * Eigenschaften:
     * - mittlerer Rechenaufwand
     * - Datenbankzugriffe (z. B. Metadaten)
     * - Storage-Zugriffe (Thumbnails)
     */
    GALLERY_VIEW,

    /**
     * Detailansicht eines einzelnen Bildes.
     *
     * Eigenschaften:
     * - höherer Rechenaufwand
     * - Datenbank + Storage Zugriff
     */
    IMAGE_DETAIL,

    /**
     * Upload eines Bildes durch den Nutzer.
     *
     * Eigenschaften:
     * - hoher Rechenaufwand
     * - intensiver Storage-Zugriff
     * - ggf. Datenbankoperationen (Metadaten speichern)
     */
    IMAGE_UPLOAD
}