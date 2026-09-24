package com.systemdesign.lld.fooddelivery.springboot.config;

import com.systemdesign.lld.fooddelivery.springboot.observer.*;
import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentProcessorFactory;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DeliveryFeeStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DeliveryPartnerMatchingStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DistanceBasedDeliveryFeeStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.NearestPartnerMatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FoodDeliveryConfig {

    @Bean
    public DeliveryFeeStrategy deliveryFeeStrategy() {
        return new DistanceBasedDeliveryFeeStrategy(20.0, 5.0, 1000.0);
    }

    @Bean
    public DeliveryPartnerMatchingStrategy deliveryPartnerMatchingStrategy() {
        return new NearestPartnerMatchingStrategy();
    }

    @Bean
    public PaymentProcessorFactory paymentProcessorFactory() {
        return new PaymentProcessorFactory();
    }

    @Bean
    public CustomerNotifier customerNotifier() {
        return new CustomerNotifier();
    }

    @Bean
    public RestaurantNotifier restaurantNotifier() {
        return new RestaurantNotifier();
    }

    @Bean
    public DeliveryPartnerNotifier deliveryPartnerNotifier() {
        return new DeliveryPartnerNotifier();
    }

    @Bean
    public OrderAuditLogger orderAuditLogger() {
        return new OrderAuditLogger();
    }

    @Bean
    public OrderEventPublisher orderEventPublisher(CustomerNotifier customerNotifier,
                                                   RestaurantNotifier restaurantNotifier,
                                                   DeliveryPartnerNotifier deliveryPartnerNotifier,
                                                   OrderAuditLogger orderAuditLogger) {
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.registerObserver(customerNotifier);
        publisher.registerObserver(restaurantNotifier);
        publisher.registerObserver(deliveryPartnerNotifier);
        publisher.registerObserver(orderAuditLogger);
        return publisher;
    }
}
