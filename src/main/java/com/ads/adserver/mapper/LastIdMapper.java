package com.ads.adserver.mapper;

import com.ads.adserver.entities.LastId;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps output of the table to the object.
 */
public class LastIdMapper implements RowMapper<LastId> {
    @Override
    public LastId mapRow(ResultSet rs, int arg1) throws SQLException {
        LastId id = new LastId();
        id.setLastadid(rs.getInt("lastAdId"));
        id.setLastclickid(rs.getInt("lastClickId"));
        id.setLastimpressionid(rs.getInt("lastImpressionId"));
        return id;
    }
}