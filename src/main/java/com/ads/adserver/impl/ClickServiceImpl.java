package com.ads.adserver.impl;

import com.ads.adserver.dao.ClickDao;
import com.ads.adserver.entities.Click;
import com.ads.adserver.service.ClickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service class to expose CRUD operations for clicks table.
 */
@Component
public class ClickServiceImpl implements ClickService {
    private static Logger log = LoggerFactory.getLogger(ClickServiceImpl.class);


    @Resource
    ClickDao clickDao;

    @Override
    //@Async("asyncExecutor")
    public List<Click> findAll() {

        log.info("Click Service FindAll starts");
        return clickDao.findAll();
    }

    @Override
    public void createClick(Click click) {
        clickDao.createClick(click);
    }

    @Override
    public void updateClick(Click click) {
        clickDao.updateClick(click);
    }

    @Override
    //@Async("asyncExecutor")
    public Click getClick(int id) {

        log.info("Click Service getClick starts");
        return clickDao.getClick(id);
    }

    @Override
    public void executeUpdateClick(Click click) {
        clickDao.executeUpdateClick(click);
    }

    @Override
    public void deleteClick(Click click) {
        clickDao.deleteClick(click);
    }

    @Override
    //@Async("asyncExecutor")
    public List<Click> getAllClicksWithinInterval(int days) {

        log.info("Click Service getAllClicksInterval starts");
        return clickDao.getAllClicksWithinInterval(days);
    }
}
