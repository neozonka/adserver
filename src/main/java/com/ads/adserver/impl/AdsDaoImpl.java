package com.ads.adserver.impl;


import com.ads.adserver.dao.AdsDao;
import com.ads.adserver.entities.AdEntity;
import com.ads.adserver.mapper.AdsRowMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Implementation of Ads Databse operations.
 */
@Repository
public class AdsDaoImpl implements AdsDao {

    volatile AtomicInteger lastId = new AtomicInteger(0);
    volatile AtomicInteger lastIdFinal = new AtomicInteger();


    public AdsDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
        System.out.println("Last Ad ID" + template.query("select * from common", new LastIdMapper()).get(0).getLastadid());
        this.lastId.set(template.query("select * from common", new LastIdMapper()).get(0).getLastadid());
        this.lastIdFinal.set(this.lastId.get());

    }

    NamedParameterJdbcTemplate template;

    @Override
    public List<AdEntity> findAll() {
        return template.query("select * from ads", new AdsRowMapper());
    }

    /**
     * Returns the adId entity for a given adid.
     *
     * @param adId
     * @return AdEntity
     */
    @Override
    public AdEntity getAd(int adId) {
        System.out.println("Fetching Adid " + adId);

        SqlParameterSource params = new MapSqlParameterSource().addValue("adId", adId);
        return template.queryForObject("select * from ads where adid=:adId", params, new AdsRowMapper());
    }

    /**
     * Thread Safe Write Operation to DB to create an Ad.
     *
     * @param ad
     */
    @Override
    public synchronized void createAd(AdEntity ad) {
        System.out.println("Request received as ");
        ad.printData();
        final String sql =
                "insert into ads(" +
                        "adid, " +
                        "custname , " +
                        "custemail," +
                        "catid," +
                        "stime) " +
                        "values(" +
                        ":adId," +
                        ":custName," +
                        ":custEmail," +
                        ":catId," +
                        ":sTime) ";
        KeyHolder holder = new GeneratedKeyHolder();

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("adId", lastId.incrementAndGet())
                .addValue("custName", ad.getCustName())
                .addValue("custEmail", ad.getEmail())
                .addValue("catId", ad.getCatId())
                .addValue("sTime", ad.getsTime());

        System.out.println("Writing to Ads DB");
        ((MapSqlParameterSource) param).getValues().forEach((k, v) -> {
            System.out.println("Key: " + k + " Value: " + v);
        });


        template.update(sql, param, holder);
        updateLastId();

    }

    /**
     * Thread Safe write operation to update Ad data.
     *
     * @param ad
     */
    @Override
    public synchronized void updateAd(AdEntity ad) {
        final String sql = "update ads set adid=:adId, custname=:custName,  custemail=:custEmail, catid=:custId, stime=:sTime where adid=:adId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("adId", ad.getId())
                .addValue("custName", ad.getCustName())
                .addValue("custEmail", ad.getEmail())
                .addValue("catId", ad.getCatId())
                .addValue("sTime", ad.getsTime());
        template.update(sql, param, holder);
    }

    @Override
    public void executeUpdateAd(AdEntity ad) {
        final String sql = "update ads set adid=:adId, custname=:custName,  custemail=:custEmail, catid=:custId, stime=:sTime where adid=:adId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("adId", ad.getId());
        map.put("custName", ad.getCustName());
        map.put("custEmail", ad.getEmail());
        map.put("catId", ad.getCatId());
        map.put("sTime", ad.getsTime());
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }

    /**
     * Thread Safe Write method to update the last assigned id for ad.
     */
    public synchronized void updateLastId() {
        final String sql = "update common set lastadid=:lastAdId where lastadid=:adIdFinal";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lastAdId", lastId)
                .addValue("adIdFinal", lastIdFinal);
        template.update(sql, param, holder);
        lastIdFinal.set(lastId.get());

    }

    /**
     * Thread Safe operation to delete an ad.
     * @param adId
     */

    @Override
    public synchronized void deleteAd(int adId) {
        final String sql = "delete from ads where adid=:adId";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("employeeId", adId);
        template.execute(sql, map, new PreparedStatementCallback<Object>() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.executeUpdate();
            }
        });
    }
}