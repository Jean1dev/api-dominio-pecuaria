package com.binno.dominio.schedule;

import com.binno.dominio.shared.ApplicationJobs;
import com.binno.dominio.shared.IApplicationJobExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OncePerDayJobsExecutor {

    private final ApplicationJobs jobs;

    @Scheduled(cron = "00 03 14 * * *")
    public void onExecute() {
        jobs.getJobs().forEach(IApplicationJobExecutor::run);
    }
}
