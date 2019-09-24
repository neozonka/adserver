package com.ads.adserver.service;

import com.ads.adserver.entities.Click;

import java.util.List;

/**
 * Interface for the Click Service to expose CRUD operations to user.
 */
public interface ClickService {
    List<Click> findAll();

    void createClick(Click click);

    Click getClick(int click);

    void updateClick(Click click);

    void executeUpdateClick(Click click);

    void deleteClick(Click click);

    List<Click> getAllClicksWithinInterval(int days);

}
