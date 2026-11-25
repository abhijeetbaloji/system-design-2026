# Design Patterns - Strategy Pattern

Total Questions in this category: **8**

---

### Q059: We have a payment system and based on users choice need to choose different

2. We have a payment system and based on users choice need to choose different 
kind of payments like Credit Card/UPI etc. What is the pattern which will be used for 
this?

---

### Q060: Interview Question

```text
Coding Question#2: 
Suppose you have an existing class named PaymentProcessor responsible for handling 
payment transactions. At present, the logic for calculating transaction fees is hardcoded 
within the PaymentProcessor class. Now, you are required to support multiple fee 
calculation strategies, such as: - A percentage-based fee - A fixed (flat) fee 
The solution should ensure that: - PaymentProcessor remains unchanged when introducing new fee calculation methods. - The fee calculation strategy can be selected dynamically at runtime. - How would you modify the design if you also needed to support fee calculation rules that 
depend on external configuration or user preferences, without changing the 
PaymentProcessor class?
```

---

### Q061: You are given an existing class called PaymentProcessor that processes

```text
Question 2. You are given an existing class called PaymentProcessor that processes 
payments. 
Currently, the fee calculation logic is embedded directly inside the class. You are now 
asked to support two different fee calculation behaviors:
```

---

### Q062: Percentage-based fee - Flat fee

- Percentage-based fee - Flat fee 
The system should be designed so that: - The PaymentProcessor does not need to change when new fee calculation rules are 
added. - The fee calculation behavior can be chosen at runtime.

---

### Q063: Guess the output

```text
4.Guess the output 
static int test() { 
    int x = 10; 
    try { 
        return x; 
    } finally { 
        x = 20; 
    } 
} 
 
Diff between Predicate and function 
Stream API - name of some intermediatory operations 
Filters and Map - when do you use 
What type of functional interface fits for filter and Map 
Multithreading - what is the diff between concurrency and parallelism 
Consider CPU course - how will concurrency and parallelism work 
Parallel Streams use concurrency or parallelism? 
What isolates concurrency from parallelism 
What is singleton? Why do we need singleton 
What is Serialization 
Strategy Design pattern 
You are given an existing class called Payment Processor that processes payments. 
Currently, the fee calculation logic is embedded directly inside the class. You are now 
asked to support two different fee calculation behaviors: - Percentage-based fee - Flat fee 
Design and implement an immutable object
```

---

### Q064: Lets say I have some event came in, that event needs a credit card pay. How w...

7. Lets say I have some event came in, that event needs a credit card pay. How would i 
implement a resolver so that it picks the credit card one instead of upi payment one?

---

### Q065: You have strategy and different type of payment implementing strategy. How wo...

8. You have strategy and different type of payment implementing strategy. How would you 
know which one to pick, and you need some sort of identifier? 
so then you would need to pick first one or pick the nect one, then need to implement some 
sort of method in interface, so it knows which one to pick?

---

### Q066: Lets say you have all the strategies, what data structure can you use, if my ...

9. Lets say you have all the strategies, what data structure can you use, if my payment type 
is upi then use this or if my payment is credit card use this strategy. 
what would i use in order to store the data?

---
