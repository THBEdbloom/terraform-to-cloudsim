package de.thb.cloudsim.workload;

/**
 * Beschreibt ein einzelnes Request-Muster innerhalb eines Workloads.
 *
 * Zweck:
 * → Abstraktion realer Nutzeranfragen (z. B. Seitenaufrufe, Uploads)
 * → Grundlage zur Generierung von Cloudlets für die Simulation
 *
 * Ein RequestProfile definiert:
 * - wie viele Requests auftreten (count)
 * - wie rechenintensiv sie sind (baseLength)
 * - wie stark externe Services (DB, Storage) Einfluss haben
 */
public class RequestProfile {

    // Typ der Anfrage (z. B. STATIC_ASSET, IMAGE_UPLOAD)
    private final RequestType requestType;

    // Anzahl dieser Requests im Workload
    private final int count;

    // Basis-Rechenaufwand (ohne externe Abhängigkeiten)
    // Einheit: CloudSim-Length (Instruction Count)
    private final long baseLength;

    // Zusätzlicher Aufwand, wenn eine Datenbank vorhanden ist
    private final long dbPenalty;

    // Zusätzlicher Aufwand, wenn Storage verwendet wird
    private final long storagePenalty;

    // Anzahl benötigter CPU-Kerne für diesen Request
    private final int pes;

    // Größe der Daten (I/O-Größe des Cloudlets)
    private final long cloudletSize;

    /**
     * Konstruktor zur vollständigen Definition eines Request-Profils.
     */
    public RequestProfile(
            RequestType requestType,
            int count,
            long baseLength,
            long dbPenalty,
            long storagePenalty,
            int pes,
            long cloudletSize
    ) {
        this.requestType = requestType;
        this.count = count;
        this.baseLength = baseLength;
        this.dbPenalty = dbPenalty;
        this.storagePenalty = storagePenalty;
        this.pes = pes;
        this.cloudletSize = cloudletSize;
    }

    /**
     * Gibt den Request-Typ zurück.
     */
    public RequestType getRequestType() {
        return requestType;
    }

    /**
     * Gibt die Anzahl dieser Requests zurück.
     */
    public int getCount() {
        return count;
    }

    /**
     * Basis-Rechenaufwand ohne externe Abhängigkeiten.
     */
    public long getBaseLength() {
        return baseLength;
    }

    /**
     * Zusätzlicher Rechenaufwand durch Datenbankzugriffe.
     *
     * Wird nur berücksichtigt, wenn im Infrastrukturmodell
     * eine Datenbank vorhanden ist.
     */
    public long getDbPenalty() {
        return dbPenalty;
    }

    /**
     * Zusätzlicher Rechenaufwand durch Storage-Zugriffe.
     *
     * Wird nur berücksichtigt, wenn Storage vorhanden ist.
     */
    public long getStoragePenalty() {
        return storagePenalty;
    }

    /**
     * Anzahl der benötigten CPU-Kerne für diesen Request.
     */
    public int getPes() {
        return pes;
    }

    /**
     * Größe der Daten (z. B. für IO-Operationen).
     */
    public long getCloudletSize() {
        return cloudletSize;
    }
}