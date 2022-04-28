package com.binno.dominio.shared;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationJobs {

    protected List<IApplicationJobExecutor> jobs = new ArrayList<>();

    public void registerJob(IApplicationJobExecutor job) {
        jobs.add(job);
    }

    public List<IApplicationJobExecutor> getJobs() {
        return jobs;
    }
}
