package com.ads.adserver.dao;

import com.ads.adserver.entities.Impression;

import java.util.List;

/**
 * DAO to handle Impression Data from impressions table.
 * @param <T>
 */
public interface ImpressionDao<T> {

    List<Impression> findAll();

    void createImpression(Impression imp);

    Impression getImpression(int imp);

    void updateImpression(Impression imp);

    void executeUpdateImpression(Impression imp);

    void deleteImpression(Impression imp);

    List<Impression> getAllImpressionsWithinInterval(int days);

}
