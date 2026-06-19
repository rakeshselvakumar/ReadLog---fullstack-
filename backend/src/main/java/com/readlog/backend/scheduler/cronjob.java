package com.readlog.backend.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class cronjob {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 */30 * * * ?")
    public void keepAlive() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        System.out.println("Aiven ping successful");
    }
}