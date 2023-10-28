package com.example.demo;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("topic");
    }

    @Bean
    public AnonymousQueue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1(TopicExchange topic, AnonymousQueue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.hello");
    }

    @Bean
    public Binding binding2(TopicExchange topic, AnonymousQueue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("say.#");
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("no-exchange-queue")
                .deadLetterExchange("dlx")
                .deadLetterRoutingKey("dlq")
                .build();
    }

    @Bean
    public DirectExchange dlx() {
        return new DirectExchange("dlx");
    }

    @Bean
    public Queue dlq() {
        return QueueBuilder.durable("dlq").build();
    }

    @Bean
    public Binding dlb() {
        return BindingBuilder.bind(dlq()).to(dlx()).with("dlq");
    }
}
