package dev.wuason.mechanics.library.repositories;

public class ChecksumResult {
    private final Checksum.ChecksumType type;
    private String originalChecksum = "";
    private String calculatedChecksum = "";
    private boolean resolved = false;

    public ChecksumResult(Checksum.ChecksumType type) {
        this.type = type;
    }

    public Checksum.ChecksumType getType() {
        return type;
    }

    public String getOriginalChecksum() {
        return originalChecksum;
    }

    public void setOriginalChecksum(String originalChecksum) {
        this.originalChecksum = originalChecksum;
    }

    public String getCalculatedChecksum() {
        return calculatedChecksum;
    }

    public boolean isResolved() {
        return resolved;
    }

    public boolean isChecksumValid() {
        return isResolved() && originalChecksum.equals(calculatedChecksum);
    }

    public void setCalculatedChecksum(String calculatedChecksum) {
        this.calculatedChecksum = calculatedChecksum;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

}
