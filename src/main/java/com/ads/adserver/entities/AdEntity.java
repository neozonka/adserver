package com.ads.adserver.entities;

/**
 * Entity to handle Ads Data
 */
public class AdEntity {

    // Note: I didn't create Cust object for cust details to avoid not required work.
    private int id; // Auto Generated Id which will be done in DaoImpl based on what's there in the DB
    private String custName; // The Customer Name

    private long sTime; // Serving time
    private int catId; // Cat ID that the ad belong to.
    private String email; // CustId.

    public AdEntity(int id, String custName, long sTime, int catId, String email) {
        this.id = id;
        this.custName = custName;
        this.sTime = sTime;
        this.catId = catId;
        this.email = email;
    }

    public AdEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public long getsTime() {
        return sTime;
    }

    public void setsTime(long sTime) {
        this.sTime = sTime;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void printData() {
        System.out.println("Id is " + id);
        System.out.println("Custname is " + custName);
        System.out.println("Id is " + sTime);
        System.out.println("Id is " + catId);
    }
}
