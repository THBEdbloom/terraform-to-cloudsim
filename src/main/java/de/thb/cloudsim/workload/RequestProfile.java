package de.thb.cloudsim.workload;

public class RequestProfile {
    private final RequestType requestType;
    private final int count;
    private final long baseLength;
    private final long storagePenalty;
    private final int pes;
    private final long cloudletSize;

    private final int dbQueryCount;
    private final long dbQueryLength;

    public RequestProfile(
            RequestType requestType,
            int count,
            long baseLength,
            long storagePenalty,
            int pes,
            long cloudletSize,
            int dbQueryCount,
            long dbQueryLength
    ) {
        this.requestType = requestType;
        this.count = count;
        this.baseLength = baseLength;
        this.storagePenalty = storagePenalty;
        this.pes = pes;
        this.cloudletSize = cloudletSize;
        this.dbQueryCount = dbQueryCount;
        this.dbQueryLength = dbQueryLength;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public int getCount() {
        return count;
    }

    public long getBaseLength() {
        return baseLength;
    }

    public long getStoragePenalty() {
        return storagePenalty;
    }

    public int getPes() {
        return pes;
    }

    public long getCloudletSize() {
        return cloudletSize;
    }

    public int getDbQueryCount() {
        return dbQueryCount;
    }

    public long getDbQueryLength() {
        return dbQueryLength;
    }
}