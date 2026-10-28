package com.systemdesign.lld.patterns.state.example01.good;

/**
 * Runner demonstrating good VendingMachine state pattern.
 */
public class VendingDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine(2);
        System.out.println("Initial State: " + vm.getCurrentState().getStateName());

        // 1. Successful purchase 1
        System.out.println("\n--- Purchase 1 ---");
        vm.insertCoin();
        System.out.println("State after coin: " + vm.getCurrentState().getStateName());
        vm.turnCrank();
        System.out.println("State after crank: " + vm.getCurrentState().getStateName());
        System.out.println("Items left: " + vm.getCount());

        // 2. Successful purchase 2 (drains stock)
        System.out.println("\n--- Purchase 2 ---");
        vm.insertCoin();
        vm.turnCrank();
        System.out.println("Final State: " + vm.getCurrentState().getStateName());
        System.out.println("Items left: " + vm.getCount());

        // 3. Purchase when sold out
        System.out.println("\n--- Attempting purchase while sold out ---");
        vm.insertCoin();
        vm.turnCrank();

        // 4. Maintenance refill
        System.out.println("\n--- Technician Refills Machine ---");
        vm.refill(5);
        System.out.println("State after refill: " + vm.getCurrentState().getStateName());
        vm.insertCoin();
        System.out.println("State after coin: " + vm.getCurrentState().getStateName());
    }
}
