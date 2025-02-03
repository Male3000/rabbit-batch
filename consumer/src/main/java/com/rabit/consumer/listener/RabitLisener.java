package com.rabit.consumer.listener;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabit.consumer.data.ProductDTO;
import com.rabit.consumer.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabitLisener {
    private final JobLauncher jobLauncher;
    private final Job mainJob;

//    @RabbitListener(id = "testListener2", queues = "${rabitmq.queue.test}")
//    void testListener_2(final ProductDTO body, final Channel channel) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, IOException {
//        channel.abort();
//        System.out.println("message arrive"+ body);
//        jobLauncher.run(mainJob, new JobParametersBuilder()
//                .addLong("timestamp", System.currentTimeMillis())
//                .toJobParameters());
//        System.out.println("job ended");
//    }

    @RabbitListener(id = "testListener2", queues = "${rabitmq.queue.test}")
    void testListener_2(ProductDTO productDTO, final Message message, final Channel channel) throws Exception {
//        channel.abort();
        jobLauncher.run(mainJob, new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters());
    }
}
