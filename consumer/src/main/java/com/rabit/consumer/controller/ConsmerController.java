package com.rabit.consumer.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConsmerController {
    private final RabbitListenerEndpointRegistry registry;

    @GetMapping("rabit/{state}")
    ResponseEntity<?> startStop(
            @PathVariable(name = "state") String name
    ) {
        final var listener = registry.getListenerContainer("testListener");
        if ("stop".equalsIgnoreCase(name)) {
            listener.stop();
        } else if ("start".equalsIgnoreCase(name)) {
            listener.start();
        } else {
            throw new IllegalArgumentException("not provided state either stop or start");
        }
        return ResponseEntity.ok("listener %s".formatted(name));
    }
}

