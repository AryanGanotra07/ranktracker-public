package com.codingee.ranked.ranktracker.config;

import com.codingee.ranked.ranktracker.service.task.receivers.NotificationsReceiver;
import com.codingee.ranked.ranktracker.service.task.receivers.TrackJobCompletedReceiver;
import com.codingee.ranked.ranktracker.service.task.receivers.TrackRequestReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    public static final String TRACK_REQUEST_QUEUE = "track-request";
    public static final String TRACK_JOB_COMPLETED_QUEUE = "completed-track-job-queue";

    public static final String NOTIFICATIONS_QUEUE = "notifications-queue";


    @Bean
    @Qualifier(TRACK_REQUEST_QUEUE)
    public Queue trackRequestQueue() {
        return new Queue(TRACK_REQUEST_QUEUE, true);
    }

    @Bean
    @Qualifier(TRACK_JOB_COMPLETED_QUEUE)
    public Queue processTrackJobCompletedQueue() {return new Queue(TRACK_JOB_COMPLETED_QUEUE, true);}

    @Bean
    @Qualifier(NOTIFICATIONS_QUEUE)
    public Queue notificationsQueue() {return new Queue(NOTIFICATIONS_QUEUE, true);}

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    @Qualifier(TRACK_REQUEST_QUEUE)
    public MessageListenerAdapter trackRequestListenerAdapter(TrackRequestReceiver receiver, Jackson2JsonMessageConverter messageConverter) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        listenerAdapter.setMessageConverter(messageConverter);
        return listenerAdapter;
    }

    @Bean
    @Qualifier(TRACK_JOB_COMPLETED_QUEUE)
    public MessageListenerAdapter trackJobCompletedListenerAdapter(TrackJobCompletedReceiver receiver, Jackson2JsonMessageConverter messageConverter) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        listenerAdapter.setMessageConverter(messageConverter);
        return listenerAdapter;
    }

    @Bean
    @Qualifier(NOTIFICATIONS_QUEUE)
    public MessageListenerAdapter notificationsListenerAdapter(NotificationsReceiver receiver, Jackson2JsonMessageConverter messageConverter) {
        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveMessage");
        listenerAdapter.setMessageConverter(messageConverter);
        return listenerAdapter;
    }

    @Bean
    public SimpleMessageListenerContainer trackRequestMessageListenerContainer(ConnectionFactory connectionFactory, @Qualifier(TRACK_REQUEST_QUEUE)Queue trackRequestQueue,  @Qualifier(TRACK_REQUEST_QUEUE) MessageListenerAdapter trackRequestListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(trackRequestQueue.getName());
        container.setMessageListener(trackRequestListenerAdapter);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer trackJobCompletedMessageListenerContainer(ConnectionFactory connectionFactory, @Qualifier(TRACK_JOB_COMPLETED_QUEUE)  Queue trackJobCompletedQueue, @Qualifier(TRACK_JOB_COMPLETED_QUEUE) MessageListenerAdapter trackJobCompletedListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(trackJobCompletedQueue.getName());
        container.setMessageListener(trackJobCompletedListenerAdapter);
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer notificationsListenerContainer(ConnectionFactory connectionFactory, @Qualifier(NOTIFICATIONS_QUEUE)  Queue notificationsQueue, @Qualifier(NOTIFICATIONS_QUEUE) MessageListenerAdapter notificationsListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(notificationsQueue.getName());
        container.setMessageListener(notificationsListenerAdapter);
        return container;
    }

}
