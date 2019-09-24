package com.ads.adserver.dao;

import com.ads.adserver.entities.AdEntity;
import java.util.List;

/**
 * DAO Object to handle ADEntity Data
 * @param <T>
 */

public interface AdsDao<T> {

    List<AdEntity> findAll();

    void createAd(AdEntity ad);

    AdEntity getAd(int ad);

    void updateAd(AdEntity ad);

    void executeUpdateAd(AdEntity ad);

    void deleteAd(int adId);
}