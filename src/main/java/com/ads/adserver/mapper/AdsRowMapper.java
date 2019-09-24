package com.ads.adserver.mapper;

import java.sql.ResultSet;
        import java.sql.SQLException;

import com.ads.adserver.entities.AdEntity;
import org.springframework.jdbc.core.RowMapper;

/**
 * Maps the output of the SQL query to an object.
 */
public class AdsRowMapper implements RowMapper<AdEntity> {
    @Override
    public AdEntity mapRow(ResultSet rs, int arg1) throws SQLException {
        AdEntity ad = new AdEntity();
        ad.setId(rs.getInt("adId"));
        ad.setCustName(rs.getString("custName"));
        ad.setEmail(rs.getString("custEmail"));
        ad.setsTime(rs.getLong("sTime"));
        ad.setCatId(rs.getInt("catId"));
        return ad;
    }
}