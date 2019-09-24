package com.ads.adserver.entities;


import org.jvnet.hk2.annotations.Optional;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Class to store Impression related data objects.
 */
public class Impression {


    private int impressionId;
    // Ad that was served
    private int adId;

    private int adCat;
    // DateTime API Date can be used but keeping it to Java's primary type
    @Optional
    private LocalDate dateImpressed;
    @Optional
    private LocalTime timeImpressed;
    // This is an id which is used to pull data from the server request logs to get more data
    private long rLogId;

    public Impression(int impressionId, int adId, int adCat, LocalDate dateImpressed, LocalTime timeImpressed, long rLogId) {
        this.impressionId = impressionId;
        this.adId = adId;
        this.adCat = adCat;
        this.dateImpressed = dateImpressed;
        this.timeImpressed = timeImpressed;
        this.rLogId = rLogId;
    }


    // Default Constructor
    public Impression() {
    }

    public int getImpressionId() {
        return impressionId;
    }

    public void setImpressionId(int impressionId) {
        this.impressionId = impressionId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getAdCat() {
        return adCat;
    }

    public void setAdCat(int adCat) {
        this.adCat = adCat;
    }

    public LocalDate getDateImpressed() {
        return dateImpressed;
    }

    public void setDateImpressed(LocalDate dateImpressed) {
        this.dateImpressed = dateImpressed;
    }

    public LocalTime getTimeImpressed() {
        return timeImpressed;
    }

    public void setTimeImpressed(LocalTime timeImpressed) {
        this.timeImpressed = timeImpressed;
    }

    public long getrLogId() {
        return rLogId;
    }

    public void setrLogId(long rLogId) {
        this.rLogId = rLogId;
    }
}
