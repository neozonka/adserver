package com.ads.adserver.impl;


import com.ads.adserver.dao.ImpressionDao;
import com.ads.adserver.entities.Impression;
import com.ads.adserver.mapper.ImpressionRowMapper;
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
 * Implements Impressions Dao to perform CRUD operations on impressions table.
 */
@Repository
public class ImpressionDaoImpl implements ImpressionDao {

    AtomicInteger lastImpressionId = new AtomicInteger();
    AtomicInteger impIdFinal = new AtomicInteger();


    public ImpressionDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        System.out.println("Last Impression ID" + template.query("select * from common", new LastIdMapper()).get(0).getLastimpressionid());
        this.lastImpressionId.set(template.query("select * from common", new LastIdMapper()).get(0).getLastimpressionid());
        this.impIdFinal.set(this.lastImpressionId.get());
    }

    NamedParameterJdbcTemplate template;

    /**
     * Returns a list of all the Impressions from the table.
     *
     * @return
     */
    @Override
    public List<Impression> findAll() {
        return template.query("select * from impressions", new ImpressionRowMapper());
    }

    /**
     * Returns an Impression record for a given impressionId.
     *
     * @param impressionId
     * @return
     */
    @Override
    public Impression getImpression(int impressionId) {
        System.out.println("Fetching Impressionid " + impressionId);

        SqlParameterSource params = new MapSqlParameterSource().addValue("impression", impressionId);
        return template.queryForObject("select * from impressions where impressionid=:impressionId", params, new ImpressionRowMapper());
    }

    /**
     * Thread safe method to create an Impression record in the table.
     *
     * @param impression
     */
    @Override
    public synchronized void createImpression(Impression impression) {
        final String sql =
                "insert into impressions(" +
                        "impressionid," +
                        "adcat," +
                        "adid," +
                        "dateimpressed," +
                        "timeimpressed," +
                        "rlogid)" +
                        "values(" +
                        ":impressionId," +
                        ":adCat," +
                        ":adId," +
                        ":dateImpressed," +
                        ":timeImpressed," +
                        ":rLogId)";
        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("impressionId", lastImpressionId.incrementAndGet())
                .addValue("adCat", impression.getAdCat())
                .addValue("adId", impression.getAdId())
                .addValue("dateImpressed", LocalDate.now())
                .addValue("timeImpressed", LocalTime.now())
                .addValue("rLogId", impression.getrLogId());

        System.out.println("Writing to Impression DB");
        ((MapSqlParameterSource) param).getValues().forEach((k, v) -> {
            System.out.println("Key: " + k + " Value: " + v);
        });

        template.update(sql, param, holder);
        updateLastId();

    }

    /**
     * Thread Safe method to update an impression record.
     *
     * @param impression
     */
    @Override
    public synchronized void updateImpression(Impression impression) {
        final String sql = "update impressions set impressionid=:impressionId, adcat=:adCat,  adid=:adId, dateimpressed=:dateImpressed, timeimpressed=:timeImpressed, impressionid=:impressionId, rlogid=:rLogId where impressionid=:impressionId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("impressionId", impression.getImpressionId())
                .addValue("adCat", impression.getAdCat())
                .addValue("adId", impression.getAdId())
                .addValue("dateImpressed", LocalDate.now())
                .addValue("timeImpressed", LocalTime.now())
                .addValue("rLogId", impression.getrLogId());
        template.update(sql, param, holder);
    }

    /**
     * @param impression
     */
    @Override
    public synchronized void executeUpdateImpression(Impression impression) {
        final String sql = "update impressions set impressionid=:impressionId, adcat=:adCat,  adid=:adId, dateimpressed=:dateImpressed, timeimpressed=:timeImpressed impressionid=:impressionId, rlogid=:rLogId where impressionid=:impressionId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("impressionId", impression.getImpressionId());
        map.put("adCat", impression.getAdCat());
        map.put("adId", impression.getAdId());
        map.put("dateImpressed", LocalDate.now());
        map.put("timeImpressed", LocalTime.now());
        map.put("rLogId", impression.getrLogId());
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    /**
     * Thread safe method to delete an impression record.
     *
     * @param impression
     */
    @Override
    public synchronized void deleteImpression(Impression impression) {
        final String sql = "delete from impressions where impressionid=:impressionId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("impressionId", impression.getImpressionId());
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });

    }

    /**
     * Thread safe write method to update the lastimpressionid.
     */
    public synchronized void updateLastId() {
        final String sql = "update common set lastimpressionid=:lastImpressionId where lastImpressionId=:impIdFinal";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lastImpressionId", lastImpressionId)
                .addValue("impIdFinal", impIdFinal);
        template.update(sql, param, holder);
        impIdFinal.set(lastImpressionId.get());

    }

    /**
     * Method to find the latest available version from table.
     *
     * @return
     */
    public int getLastIdFromDB() {
        return template.query("select * from common", new LastIdMapper()).get(0).getLastimpressionid();
    }

    /**
     * Returns the list of impressions impressed between now and given days back.
     *
     * @param days
     * @return
     */
    @Override
    public List<Impression> getAllImpressionsWithinInterval(int days) {
        String sql = "select * from impressions  where dateimpressed between NOW() - interval '" + days + " DAY' AND NOW()";
        Map<String, Object> map = new HashMap<String, Object>();
        //map.put("days", days);
        System.out.println("Retrieving all impressions for sql " + sql);
        return template.query(sql, map, new ImpressionRowMapper());

    }
}
