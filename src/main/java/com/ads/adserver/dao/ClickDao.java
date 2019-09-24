package com.ads.adserver.dao;

import com.ads.adserver.entities.Click;

import java.util.List;

/**
 * DAO to handle data from clicks table.
 * @param <T>
 */
public interface ClickDao<T> {

    List<Click> findAll();

    void createClick(Click click);

    Click getClick(int click);

    void updateClick(Click click);

    void executeUpdateClick(Click click);

    void deleteClick(Click click);

    List<Click> getAllClicksWithinInterval(int days);

}