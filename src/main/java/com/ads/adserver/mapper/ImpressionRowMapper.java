package com.ads.adserver.mapper;

import com.ads.adserver.entities.Impression;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps output of the table to the object.
 */
public class ImpressionRowMapper implements RowMapper<Impression> {
    @Override
    public Impression mapRow(ResultSet rs, int arg1) throws SQLException {
        Impression impression = new Impression();
        impression.setImpressionId(rs.getInt("impressionId"));
        impression.setAdId(rs.getInt("adId"));
        impression.setAdCat(rs.getInt("adCat"));
        impression.setDateImpressed(rs.getDate("dateImpressed").toLocalDate());
        impression.setTimeImpressed(rs.getTime("timeImpressed").toLocalTime());
        impression.setrLogId(rs.getInt("rLogId"));
        return impression;
    }
}