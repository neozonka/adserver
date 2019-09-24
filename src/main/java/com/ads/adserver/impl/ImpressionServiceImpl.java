package com.ads.adserver.impl;

import com.ads.adserver.dao.ImpressionDao;
import com.ads.adserver.entities.Impression;
import com.ads.adserver.service.ImpressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service class to expose CRUD operations on impressions table.
 */
@Component
public class ImpressionServiceImpl implements ImpressionService {

    private static Logger log = LoggerFactory.getLogger(ImpressionDaoImpl.class);

    @Resource
    ImpressionDao impressionDao;

    @Override
    public List<Impression> findAll() {
        return impressionDao.findAll();
    }

    @Override
    public void createImpression(Impression ad) {
        impressionDao.createImpression(ad);
    }

    @Override
    public void updateImpression(Impression ad) {
        impressionDao.updateImpression(ad);
    }

    @Override
    public Impression getImpression(int id) {
        return impressionDao.getImpression(id);
    }

    @Override
    public void executeUpdateImpression(Impression ad) {
        impressionDao.executeUpdateImpression(ad);
    }

    @Override
    public void deleteImpression(Impression ad) {
        impressionDao.deleteImpression(ad);
    }

    @Override
    public List<Impression> getAllImpressionWithinInterval(int days) {
        return impressionDao.getAllImpressionsWithinInterval(days);
    }


}
