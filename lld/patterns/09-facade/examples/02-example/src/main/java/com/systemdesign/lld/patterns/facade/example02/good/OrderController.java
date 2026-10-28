package com.systemdesign.lld.patterns.facade.example02.good;

/**
 * Client Class:
 * Thin presentation / API controller that delegates entire checkout lifecycle to OrderCheckoutFacade.
 */
public class OrderController {

    private final OrderCheckoutFacade checkoutFacade;

    public OrderController(OrderCheckoutFacade checkoutFacade) {
        this.checkoutFacade = checkoutFacade;
    }

    public CheckoutResult handleWebCheckout(CheckoutRequest request) {
        return checkoutFacade.checkout(request);
    }
}
