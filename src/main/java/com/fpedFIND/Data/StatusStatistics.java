package com.fpedFIND.Data;

public class StatusStatistics {
    private long newCount;
    private long pendingCount;
    private long ongoingCount; // Added ongoing count
    private long completedCount;
    private long archivedCount; // Added archived count

    public long getNewCount()	 {
        return newCount;
    }

    public void setNewCount(long newCount) {
        this.newCount = newCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }

    public long getOngoingCount() {
        return ongoingCount;
    }

    public void setOngoingCount(long ongoingCount) {
        this.ongoingCount = ongoingCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getArchivedCount() {
        return archivedCount;
    }

    public void setArchivedCount(long archivedCount) {
        this.archivedCount = archivedCount;
    }

    public StatusStatistics() {
        super();
    }

    public StatusStatistics(long newCount, long pendingCount, long ongoingCount, long completedCount, long archivedCount) {
        this.newCount = newCount;
        this.pendingCount = pendingCount;
        this.ongoingCount = ongoingCount;
        this.completedCount = completedCount;
        this.archivedCount = archivedCount;
    }

    @Override
    public String toString() {
        return "StatusStatistics [newCount=" + newCount + ", pendingCount=" + pendingCount + ", ongoingCount="
                + ongoingCount + ", completedCount=" + completedCount + ", archivedCount=" + archivedCount + "]";
    }
}
