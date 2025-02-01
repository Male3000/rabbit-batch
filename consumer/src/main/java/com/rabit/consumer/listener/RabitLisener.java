package com.rabit.consumer.listener;

import com.rabit.consumer.data.ProductDTO;
import com.rabit.consumer.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class RabitLisener {
//    @RabbitListener(id = "testListener", queues = "${rabitmq.queue.test}")
//    void testListener(final ProductDTO body) {
//        log.info("new message arrive");
//        System.out.println(body);
//    }
//
//    @RabbitListener(id = "testListener2", queues = "${rabitmq.queue.test}")
//    void testListener_2(final ProductDTO body) {
//        log.info("new message arrive lisenter2");
//        System.out.println(body);
//    }
}
