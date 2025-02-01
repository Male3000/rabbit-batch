package com.rabit.producer.controllers;

import com.rabit.producer.data.ProductData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {
    @Value("${rabitmq.exchange.test}")
    private String testExchange;

    @Value("${rabitmq.route-key.test}")
    private String testRouteKey;

    private final AmqpTemplate amqpTemplate;

    @PostMapping("produce")
    public ResponseEntity<?> send(
            @RequestBody ProductData body,
            @RequestParam(name = "dupe", defaultValue = "1", required = false) Integer dupeTime
    ) {
        var count = 0;
        while (count < dupeTime) {
            amqpTemplate.convertAndSend(testExchange, testRouteKey, body);
            count++;
        }
        return ResponseEntity.ok("Send to exchange %s".formatted(testExchange));
    }
}
