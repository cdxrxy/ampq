package com.example.demo;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DemoApplication {
    public final RabbitTemplate rabbitTemplate;
    public final TopicExchange topic;

    public DemoApplication(RabbitTemplate rabbitTemplate, TopicExchange topic) {
        this.rabbitTemplate = rabbitTemplate;
        this.topic = topic;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//	@Scheduled(fixedRate = 1000)
//	@Async
//	public void sender1() {
//		rabbitTemplate.convertAndSend(topic.getName(), "say.hello", "Hello There!");
//        System.out.println("Message sent");
//	}

    @Scheduled(fixedRate = 1000)
    @Async
    public void sender2() {
        rabbitTemplate.convertAndSend("no-exchange-queue", "Hello!");
    }

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    @Async
    public void listener1(String message) throws InterruptedException {
        System.out.println("Listener1 got message: " + message);
        Thread.sleep(3000);
        System.out.println("Listener1 finished");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    @Async
    public void listener2(String message) throws InterruptedException {
        System.out.println("Listener2 got message: " + message);
        Thread.sleep(3000);
        System.out.println("Listener2 finished");
    }

    @RabbitListener(queues = "#{queue.name}")
    public void listener3(String message) {
        if (1 == 1) {
            throw new RuntimeException("No way");
        }

        System.out.println("Listener3 got message with default exchange: " + message);
    }

    @RabbitListener(queues = "#{dlq.name}")
    public void dll(String message) {
        System.out.println("dll got lost message: " + message);
    }
}
