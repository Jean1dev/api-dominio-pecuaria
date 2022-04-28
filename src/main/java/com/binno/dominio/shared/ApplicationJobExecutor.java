package com.binno.dominio.shared;

public abstract class ApplicationJobExecutor implements IApplicationJobExecutor {

    private final ApplicationJobs applicationJobs;

    public ApplicationJobExecutor(ApplicationJobs applicationJobs) {
        this.applicationJobs = applicationJobs;
        applicationJobs.registerJob(this);
    }
}
