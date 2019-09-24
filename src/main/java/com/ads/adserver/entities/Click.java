package com.ads.adserver.entities;


import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity To handle Click data
 */
public class Click {

    private int clickId;
    // Impression that was clicked
    private int impressionId;
    // Ad that was Clicked. Can also be retrieved from Impression but creating here to reduce table reads if only click related info is needed.
    private int adId;

    private int adCat;
    private LocalDate dateClicked;
    private LocalTime timeClicked;
    // This is an id which is used to pull data from the server request logs to get more data
    private long rLogId;

    public Click(int clickId, int impressionId, int adId, int adCat, LocalDate dateClicked, LocalTime timeClicked, long rLogId) {
        this.clickId = clickId;
        this.impressionId = impressionId;
        this.adId = adId;
        this.adCat = adCat;
        this.dateClicked = dateClicked;
        this.timeClicked = timeClicked;
        this.rLogId = rLogId;
    }


    // Default Constructor
    public Click() {
    }

    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
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

    public LocalDate getDateClicked() {
        return dateClicked;
    }

    public void setDateClicked(LocalDate dateClicked) {
        this.dateClicked = dateClicked;
    }

    public LocalTime getTimeClicked() {
        return timeClicked;
    }

    public void setTimeClicked(LocalTime timeClicked) {
        this.timeClicked = timeClicked;
    }

    public long getrLogId() {
        return rLogId;
    }

    public void setrLogId(long rLogId) {
        this.rLogId = rLogId;
    }
}
