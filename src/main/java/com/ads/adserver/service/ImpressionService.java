package com.ads.adserver.service;

import com.ads.adserver.entities.Impression;

import java.util.List;

/**
 * Interface for the Impression Service to expose CRUD operations to user.
 */
public interface ImpressionService {
    List<Impression> findAll();

    void createImpression(Impression impression);

    Impression getImpression(int impression);

    void updateImpression(Impression impression);

    void executeUpdateImpression(Impression impression);

    void deleteImpression(Impression impression);

    List<Impression> getAllImpressionWithinInterval(int days);

}