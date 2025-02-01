package com.rabit.consumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequiredArgsConstructor
@EnableAsync
public class JobController {
    private final JdbcTemplate jdbcTemplate;
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final Job mainJob;
    private final ApplicationContext context;

    static int time = 0;
    static List<Map<String, JobParameter<String>>> cache = new ArrayList<>();

    @GetMapping(value = "/batch/{name}")
    private ResponseEntity<?> defaultGet(
            @PathVariable(name = "name") String name
    ) throws Exception {
        runJobAsync();
        return ResponseEntity.ok(  "Job has launch");
    }
    @Async
    public void runJobAsync() throws Exception {
        jobLauncher.run(mainJob, new JobParameters());
    }

//    @GetMapping(value = "/repo/{name}")
//    private ResponseEntity<?> defaultGetRepo(
//            @PathVariable(name = "name") String name
//    ) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
//        final var map = new ArrayList<Object>();
//        for (var params : cache) {
//            map.add(jobRepository.getJobInstance(
//                    Objects.nonNull(name) ? name : "mainJob",
//                    new JobParameters(new HashMap<>(params))));
//        }
//        return ResponseEntity.ok(map);
//    }
}