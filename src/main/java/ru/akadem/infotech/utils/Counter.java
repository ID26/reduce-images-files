package ru.akadem.infotech.utils;

import java.math.BigDecimal;
import java.util.Map;

public class Counter {
    private String name;
    private Long totalFiles;
    private Long totalErrors;
    private Long quantityOfProcessedFiles;
    private Long quantityOfUnmodifiedFiles;
    private BigDecimal totalSizeBeforeModifier;
    private BigDecimal totalSizeAfterModifier;

    public Counter(String name,
                   Long totalFiles,
                   Long totalErrors,
                   Long quantityOfProcessedFiles,
                   Long quantityOfUnmodifiedFiles,
                   BigDecimal totalSizeBeforeModifier,
                   BigDecimal totalSizeAfterModifier) {
        this.name = name;
        this.totalFiles = totalFiles;
        this.quantityOfProcessedFiles = quantityOfProcessedFiles;
        this.quantityOfUnmodifiedFiles = quantityOfUnmodifiedFiles;
        this.totalSizeBeforeModifier = totalSizeBeforeModifier;
        this.totalSizeAfterModifier = totalSizeAfterModifier;
        this.totalErrors = totalErrors;
    }

    public void incrementTotalFiles() {
        totalFiles++;
    }

    public void incrementTotalErrors() {
        totalErrors++;
    }

    public void incrementOfProcessedFiles() {
        quantityOfProcessedFiles++;
    }

    public void incrementOfUnmodifiedFiles() {
        quantityOfUnmodifiedFiles++;
    }

    public void addFileSizeBeforeKb(Double size) {
        totalSizeBeforeModifier = totalSizeBeforeModifier.add(BigDecimal.valueOf(size));
    }

    public void addFileSizeAfterKb(Double size) {
        totalSizeAfterModifier = totalSizeAfterModifier.add(BigDecimal.valueOf(size));
    }

    public static Counter getTotalCounter(Map<String, Counter> counters) {
        Counter total = new Counter("total",
                0L,
                0L,
                0L,
                0L,
                BigDecimal.ZERO,
                BigDecimal.ZERO);
        for (Counter during : counters.values()) {
            total.setTotalSizeBeforeModifier(total.getTotalSizeBeforeModifier().add(during.getTotalSizeBeforeModifier()));
            total.setTotalSizeAfterModifier(total.getTotalSizeAfterModifier().add(during.getTotalSizeAfterModifier()));
            total.setTotalFiles(total.getTotalFiles() + during.getTotalFiles());
            total.setTotalErrors(total.getTotalErrors() + during.getTotalErrors());
            total.setQuantityOfProcessedFiles(total.getQuantityOfProcessedFiles() + during.getQuantityOfProcessedFiles());
            total.setQuantityOfUnmodifiedFiles(total.getQuantityOfUnmodifiedFiles() + during.getQuantityOfUnmodifiedFiles());
        }
        return total;
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

    public Long getTotalErrors() {
        return totalErrors;
    }

    public void setTotalFiles(Long totalFiles) {
        this.totalFiles = totalFiles;
    }

    public void setTotalErrors(Long totalErrors) {
        this.totalErrors = totalErrors;
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
        return "Counter{" + "name='" + name + '\'' + ", totalFiles=" + totalFiles
                + ", totalErrors=" + totalErrors
                + ", quantityOfProcessedFiles=" + quantityOfProcessedFiles
                + ", quantityOfUnmodifiedFiles=" + quantityOfUnmodifiedFiles + '}';
    }
}
