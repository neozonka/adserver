package com.ads.adserver.impl;


import com.ads.adserver.dao.ClickDao;
import com.ads.adserver.entities.Click;
import com.ads.adserver.mapper.ClicksRowMapper;
import com.ads.adserver.mapper.LastIdMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements ClickDao to perform CRUD operations on clicks table.
 */
@Repository
public class ClickDaoImpl implements ClickDao {

    AtomicInteger lastId = new AtomicInteger();
    AtomicInteger lastIdFinal = new AtomicInteger();


    public ClickDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        System.out.println("Last Click ID" + template.query("select * from common", new LastIdMapper()).get(0).getLastclickid());
        this.lastId.set(template.query("select * from common", new LastIdMapper()).get(0).getLastclickid());
        this.lastIdFinal.set(this.lastId.get());
    }

    NamedParameterJdbcTemplate template;

    /**
     * Method to find all the clicks from the table.
     *
     * @return List<Click> List of Click Objects
     */
    @Override
    public List<Click> findAll() {
        return template.query("select * from clicks", new ClicksRowMapper());
    }

    /**
     * Returns a Click Entity for a given clickId.
     *
     * @param clickId
     * @return
     */
    @Override
    public Click getClick(int clickId) {
        System.out.println("Fetching Clickid " + clickId);

        SqlParameterSource params = new MapSqlParameterSource().addValue("click", clickId);
        return template.queryForObject("select * from clicks where clickid=:clickId", params, new ClicksRowMapper());
    }

    /**
     * Thread Safe method to create a click record in DB.
     *
     * @param click
     */
    @Override
    public synchronized void createClick(Click click) {
        final String sql =
                "insert into clicks(" +
                        "clickid," +
                        "adcat," +
                        "adid," +
                        "dateclicked," +
                        "timeclicked," +
                        "impressionid," +
                        "rlogid)" +
                        "values(" +
                        ":clickId," +
                        ":adCat," +
                        ":adId," +
                        ":dateClicked," +
                        ":timeClicked," +
                        ":impressionId," +
                        ":rLogId)";
        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("clickId", lastId.incrementAndGet())
                .addValue("adCat", click.getAdCat())
                .addValue("adId", click.getAdId())
                .addValue("dateClicked", LocalDate.now())
                .addValue("timeClicked", LocalTime.now())
                .addValue("impressionId", click.getImpressionId())
                .addValue("rLogId", click.getrLogId());

        System.out.println("Writing to Clicks DB");
        ((MapSqlParameterSource) param).getValues().forEach((k, v) -> {
            System.out.println("Key: " + k + " Value: " + v);
        });

        template.update(sql, param, holder);
        updateLastId();
    }

    /**
     * Updates the record form a given Click Record.
     *
     * @param click
     */
    @Override
    public synchronized void updateClick(Click click) {
        final String sql = "update clicks set clickid=:clickId, adcat=:adCat,  adid=:adId, dateclicked=:dateClicked, timeclicked=:timeClicked, impressionid=:impressionId, rlogid=:rLogId where clickid=:clickId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("clickId", click.getClickId())
                .addValue("adCat", click.getAdCat())
                .addValue("adId", click.getAdId())
                .addValue("dateClicked", LocalDate.now())
                .addValue("timeClicked", LocalTime.now())
                .addValue("impressionId", click.getImpressionId())
                .addValue("rLogId", click.getrLogId());
        template.update(sql, param, holder);
    }

    @Override
    public synchronized void executeUpdateClick(Click click) {
        final String sql = "update clicks set clickid=:clickId, adcat=:adCat,  adid=:adId, dateclicked=:dateClicked, timeclicked=:timeClicked, impressionid=:impressionId, rlogid=:rLogId where clickid=:clickId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clickId", click.getClickId());
        map.put("adCat", click.getAdCat());
        map.put("adId", click.getAdId());
        map.put("dateClicked", LocalDate.now());
        map.put("timeClicked", LocalTime.now());
        map.put("impressionId", click.getImpressionId());
        map.put("rLogId", click.getrLogId());
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    /**
     * Deletes the click for a given clickid.
     *
     * @param click
     */
    @Override
    public synchronized void deleteClick(Click click) {
        final String sql = "delete from clicks where clickid=:clickId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("clickId", click.getClickId());
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    /**
     * Method to update the last assigned id to click record.
     */
    public synchronized void updateLastId() {
        final String sql = "update common set lastclickid=:lastClickId where lastclickid=:clickIdFinal";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lastClickId", lastId)
                .addValue("clickIdFinal", lastIdFinal);
        template.update(sql, param, holder);
        lastIdFinal.set(lastId.get());

    }


    /**
     * Returns a list of click objects that occured between now and given days back.
     *
     * @param days
     * @return
     */
    @Override
    public List<Click> getAllClicksWithinInterval(int days) {
        String sql = "select * from clicks  where dateclicked between NOW() - interval '" + days + " DAY' AND NOW()";
        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("days", days);
        System.out.println("Retrieving all impressions for sql " + sql);
        return template.query(sql, map, new ClicksRowMapper());
    }
}
