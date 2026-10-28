package com.systemdesign.lld.patterns.state.example01.bad;

/**
 * Runner demonstrating bad design execution.
 */
public class VendingDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine(2);
        System.out.println("Initial State: " + vm.getState());

        vm.insertCoin();
        vm.turnCrank();
        System.out.println("Remaining items: " + vm.getCount());

        vm.insertCoin();
        vm.turnCrank();
        System.out.println("Final State: " + vm.getState());

        // Try inserting coin when sold out
        vm.insertCoin();
    }
}
