package com.ads.adserver.impl;

import com.ads.adserver.dao.AdsDao;
import com.ads.adserver.entities.AdEntity;
import com.ads.adserver.service.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service Class to expose DB operations to user as a service.
 */
@Component
public class AdServiceImpl implements AdService {

    @Resource
    AdsDao adsDao;

    @Override
    public List<AdEntity> findAll() {
        return adsDao.findAll();
    }

    @Override
    public void createAd(AdEntity ad) {
        adsDao.createAd(ad);
    }

    @Override
    public void updateAd(AdEntity ad) {
        adsDao.updateAd(ad);
    }

    @Override
    public AdEntity getAd(int id) {
        return adsDao.getAd(id);
    }

    @Override
    public void executeUpdateAd(AdEntity ad) {
        adsDao.executeUpdateAd(ad);
    }

    @Override
    public void deleteAd(int adId) {
        adsDao.deleteAd(adId);
    }
}
