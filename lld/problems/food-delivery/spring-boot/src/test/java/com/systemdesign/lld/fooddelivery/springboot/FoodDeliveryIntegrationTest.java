package com.systemdesign.lld.fooddelivery.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systemdesign.lld.fooddelivery.springboot.domain.FoodCategory;
import com.systemdesign.lld.fooddelivery.springboot.dto.*;
import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FoodDeliveryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Complete End-to-End REST API Workflow: Catalog -> Cart -> Checkout -> Fulfillment -> Dispatch -> Delivery")
    void testCompleteE2ERestFlow() throws Exception {
        // 1. Create Restaurant
        CreateRestaurantRequest restReq = new CreateRestaurantRequest("REST-100", "Dominos Pizza", 12.97, 77.59);
        mockMvc.perform(post("/api/v1/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("REST-100"))
                .andExpect(jsonPath("$.name").value("Dominos Pizza"));

        // 2. Add Menu Item
        AddMenuItemRequest itemReq = new AddMenuItemRequest("ITEM-101", "Farmhouse Pizza", "Veggie delight", 350.0, FoodCategory.VEG, true);
        mockMvc.perform(post("/api/v1/restaurants/REST-100/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("ITEM-101"))
                .andExpect(jsonPath("$.price").value(350.0));

        // 3. Register Delivery Partner
        CreatePartnerRequest partnerReq = new CreatePartnerRequest("DRV-100", "Bob Courier", 12.971, 77.591, 4.8);
        mockMvc.perform(post("/api/v1/delivery/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("DRV-100"));

        // 4. Add to Cart
        AddToCartRequest cartReq = new AddToCartRequest("REST-100", "ITEM-101", 2);
        mockMvc.perform(post("/api/v1/carts/CUST-100/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurantId").value("REST-100"))
                .andExpect(jsonPath("$.subtotal").value(700.0));

        // 5. Checkout
        CheckoutRequest checkoutReq = new CheckoutRequest("CUST-100", PaymentMethod.UPI, "cust@upi", "FLAT50");
        String checkoutRes = mockMvc.perform(post("/api/v1/orders/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(checkoutReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.bill.subtotal").value(700.0))
                .andExpect(jsonPath("$.bill.discount").value(50.0))
                .andReturn().getResponse().getContentAsString();

        OrderResponse orderResponse = objectMapper.readValue(checkoutRes, OrderResponse.class);
        String orderId = orderResponse.orderId();

        // 6. Kitchen updates: CONFIRMED -> PREPARING -> READY_FOR_PICKUP
        mockMvc.perform(post("/api/v1/orders/" + orderId + "/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        mockMvc.perform(post("/api/v1/orders/" + orderId + "/preparing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PREPARING"));

        mockMvc.perform(post("/api/v1/orders/" + orderId + "/ready-for-pickup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY_FOR_PICKUP"));

        // 7. Dispatch Partner
        mockMvc.perform(post("/api/v1/delivery/dispatch/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("DRV-100"))
                .andExpect(jsonPath("$.status").value("BUSY"));

        // 8. Pickup and Deliver
        mockMvc.perform(post("/api/v1/delivery/partners/DRV-100/orders/" + orderId + "/pickup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OUT_FOR_DELIVERY"));

        mockMvc.perform(post("/api/v1/delivery/partners/DRV-100/orders/" + orderId + "/deliver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when adding items from a different restaurant to cart")
    void testCartRestaurantMismatchApiError() throws Exception {
        // Create 2 restaurants
        CreateRestaurantRequest r1 = new CreateRestaurantRequest("REST-A", "Biryani Central", 12.91, 77.51);
        CreateRestaurantRequest r2 = new CreateRestaurantRequest("REST-B", "Kebab Corner", 12.92, 77.52);
        mockMvc.perform(post("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(r1)));
        mockMvc.perform(post("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(r2)));

        mockMvc.perform(post("/api/v1/restaurants/REST-A/menu-items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddMenuItemRequest("ITEM-A", "Biryani", "Hyderabadi", 220.0, FoodCategory.NON_VEG, true))));
        mockMvc.perform(post("/api/v1/restaurants/REST-B/menu-items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddMenuItemRequest("ITEM-B", "Kebab", "Seekh", 180.0, FoodCategory.NON_VEG, true))));

        // Add item from REST-A
        mockMvc.perform(post("/api/v1/carts/CUST-MULTI/items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddToCartRequest("REST-A", "ITEM-A", 1))))
                .andExpect(status().isOk());

        // Attempt to add item from REST-B into same cart -> expect 400 BAD_REQUEST
        mockMvc.perform(post("/api/v1/carts/CUST-MULTI/items").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddToCartRequest("REST-B", "ITEM-B", 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when attempting invalid order lifecycle transition")
    void testInvalidStateTransitionApiError() throws Exception {
        CreateRestaurantRequest r1 = new CreateRestaurantRequest("REST-C", "Taco House", 12.93, 77.53);
        mockMvc.perform(post("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(r1)));
        mockMvc.perform(post("/api/v1/restaurants/REST-C/menu-items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddMenuItemRequest("ITEM-C", "Taco", "Crispy", 100.0, FoodCategory.VEG, true))));

        mockMvc.perform(post("/api/v1/carts/CUST-CANCEL/items").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddToCartRequest("REST-C", "ITEM-C", 1))));

        String checkoutRes = mockMvc.perform(post("/api/v1/orders/checkout").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CheckoutRequest("CUST-CANCEL", PaymentMethod.CASH_ON_DELIVERY, null, null))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OrderResponse order = objectMapper.readValue(checkoutRes, OrderResponse.class);

        // Move order to PREPARING
        mockMvc.perform(post("/api/v1/orders/" + order.orderId() + "/confirm")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/orders/" + order.orderId() + "/preparing")).andExpect(status().isOk());

        // Cancelling during PREPARING is illegal -> expect 400 BAD_REQUEST
        mockMvc.perform(post("/api/v1/orders/" + order.orderId() + "/cancel"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }
}
