package com.rabit.consumer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class RabitConfig {
    @Value("${rabitmq.exchange.test}")
    private String testExchange;

    @Value("${rabitmq.queue.test}")
    private String testQueue;

    @Value("${rabitmq.route-key.test}")
    private String testRouteKey;

    private final ConnectionFactory connectionFactory;

    @Bean
    public TopicExchange testExchange() {
        return new TopicExchange(testExchange);
    }

    @Bean
    public Queue testQueue() {
        return new Queue(testQueue);
    }

    @Bean
    public Binding testBinding() {
        return BindingBuilder
                .bind(testQueue())
                .to(testExchange())
                .with(testRouteKey);
    }

    @Bean
    public AmqpTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        rabbitTemplate.addAfterReceivePostProcessors(
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message) throws AmqpException {
                        message.getMessageProperties().getHeaders().put("__TypeId__", "com.rabit.consumer.data.ProductDTO");
                        return message;
                    }
                }
        );
        rabbitTemplate.setDefaultReceiveQueue(testQueue);
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(10);
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    @Bean
    public MessageConverter jacksonConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
