package com.ads.adserver.mapper;

import com.ads.adserver.entities.Click;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps output of the table to the object.
 */
public class ClicksRowMapper implements RowMapper<Click> {
    @Override
    public Click mapRow(ResultSet rs, int arg1) throws SQLException {
        Click click = new Click();
        click.setClickId(rs.getInt("clickId"));
        click.setAdId(rs.getInt("adId"));
        click.setAdCat(rs.getInt("adCat"));
        click.setDateClicked(rs.getDate("dateClicked").toLocalDate());
        click.setTimeClicked(rs.getTime("timeClicked").toLocalTime());
        click.setImpressionId(rs.getInt("impressionId"));
        click.setrLogId(rs.getInt("rLogId"));
        return click;
    }
}