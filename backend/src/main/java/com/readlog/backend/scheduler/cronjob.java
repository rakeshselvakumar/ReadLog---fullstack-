package com.readlog.backend.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class cronjob {

    @Scheduled(cron = "0 */30 * * * ?")
    public void keepAlive() {
        System.out.println("Aiven keep alive ping");
    }
}