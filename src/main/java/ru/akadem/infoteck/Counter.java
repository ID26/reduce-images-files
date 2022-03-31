package ru.akadem.infoteck;

import java.math.BigDecimal;

public class Counter {
    private String name;
    private Long totalFiles;
    private Long quantityOfProcessedFiles;
    private Long quantityOfUnmodifiedFiles;
    private Long quantityOfUnmodifiedFilesToSmall;
    private Long quantityIncreasedFiles;
    private BigDecimal totalSizeBeforeModifier;
    private BigDecimal totalSizeAfterModifier;

    public Counter(String name, Long totalFiles, Long quantityOfProcessedFiles, Long quantityOfUnmodifiedFiles, Long quantityIncreasedFiles, BigDecimal totalSizeBeforeModifier, BigDecimal totalSizeAfterModifier) {
        this.name = name;
        this.totalFiles = totalFiles;
        this.quantityOfProcessedFiles = quantityOfProcessedFiles;
        this.quantityOfUnmodifiedFiles = quantityOfUnmodifiedFiles;
        this.quantityIncreasedFiles = quantityIncreasedFiles;
        this.totalSizeBeforeModifier = totalSizeBeforeModifier;
        this.totalSizeAfterModifier = totalSizeAfterModifier;
    }

    public void incrementTotalFiles() {
        totalFiles++;
    }

    public void incrementOfProcessedFiles() {
        quantityOfProcessedFiles++;
    }

    public void incrementOfUnmodifiedFiles() {
        quantityOfUnmodifiedFiles++;
    }

    public void incrementIncreasedFile() {
        quantityIncreasedFiles++;
    }

    public void addFileSizeBeforeKb(Double size) {
        totalSizeBeforeModifier = totalSizeBeforeModifier.add(BigDecimal.valueOf(size));
    }

    public void addFileSizeAfterKb(Double size) {
        totalSizeAfterModifier = totalSizeAfterModifier.add(BigDecimal.valueOf(size));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public Long getQuantityOfProcessedFiles() {
        return quantityOfProcessedFiles;
    }

    public void setQuantityOfProcessedFiles(Long quantityOfProcessedFiles) {
        this.quantityOfProcessedFiles = quantityOfProcessedFiles;
    }

    public Long getQuantityOfUnmodifiedFiles() {
        return quantityOfUnmodifiedFiles;
    }

    public void setQuantityOfUnmodifiedFiles(Long quantityOfUnmodifiedFiles) {
        this.quantityOfUnmodifiedFiles = quantityOfUnmodifiedFiles;
    }

    public Long getQuantityIncreasedFiles() {
        return quantityIncreasedFiles;
    }

    public void setQuantityIncreasedFiles(Long quantityIncreasedFiles) {
        this.quantityIncreasedFiles = quantityIncreasedFiles;
    }

    public BigDecimal getTotalSizeBeforeModifier() {
        return totalSizeBeforeModifier;
    }

    public void setTotalSizeBeforeModifier(BigDecimal totalSizeBeforeModifier) {
        this.totalSizeBeforeModifier = totalSizeBeforeModifier;
    }

    public BigDecimal getTotalSizeAfterModifier() {
        return totalSizeAfterModifier;
    }

    public void setTotalSizeAfterModifier(BigDecimal totalSizeAfterModifier) {
        this.totalSizeAfterModifier = totalSizeAfterModifier;
    }

    @Override
    public String toString() {
        return "Counter{" + "name='" + name + '\'' + ", totalFiles=" + totalFiles + ", quantityOfProcessedFiles=" + quantityOfProcessedFiles + ", quantityOfUnmodifiedFiles=" + quantityOfUnmodifiedFiles + '}';
    }
}
