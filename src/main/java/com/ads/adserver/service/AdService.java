package com.ads.adserver.service;

import com.ads.adserver.entities.AdEntity;

import java.util.List;

/**
 * Interface for the Ad Service to expose CRUD operations to user.
 */
public interface AdService {

    List<AdEntity> findAll();

    void createAd(AdEntity ad);

    AdEntity getAd(int ad);

    void updateAd(AdEntity ad);

    void executeUpdateAd(AdEntity ad);

    void deleteAd(int adId);

}
