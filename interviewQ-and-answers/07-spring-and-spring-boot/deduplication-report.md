# Spring & Spring Boot Interview Questions — Deduplication Audit Report

This report documents the comprehensive audit and deduplication of all 110 Spring and Spring Boot interview questions in `questions.md`. Every question was analyzed for exact wording repetition, slight rewording, and conceptual equivalence.

## 1. Executive Summary

| Metric | Count | Description |
| :--- | :---: | :--- |
| **Original Question Count** | **110** | Total raw questions extracted in `questions.md` (`Q468` to `Q577`) |
| **Exact Duplicates Removed** | **26** | Verbatim identical questions or exact topic name repetitions |
| **Concept-Level Duplicates Removed** | **33** | Questions asking the same underlying technical concept with minor rewording |
| **Total Duplicates Removed** | **59** | Total redundant questions eliminated from `answers.md` |
| **Unique Questions Retained** | **51** | Distinct, high-value technical and architectural interview questions |

---

## 2. Deduplication Mapping & Technical Rationale

The table below details every removed duplicate, its original question ID and wording, the retained question ID, duplicate classification (Exact vs. Concept-Level), and the technical explanation for removal.

| Removed ID | Removed Question Text | Retained ID | Retained Question Text | Duplicate Type | Technical Explanation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Q544** | `ioc` | **Q540** | `What is IOC in Spring` | **Exact Duplicate** | Q544 ('ioc') is an exact shorthand duplicate of Q540 ('What is IOC in Spring'). |
| **Q472** | `What is dependency injection and type of DI?` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q472 ('What is dependency injection and type of DI?') asks the same core concept of DI and its types, fully answered by Q479 ('Explain Dependency injection in springboot ? which is best ?'). |
| **Q484** | `What is dependency injection?` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q484 ('What is dependency injection?') is a sub-question duplicate of Q479. |
| **Q488** | `How will you implement dependency injection?` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q488 ('How will you implement dependency injection?') asks how to implement DI via constructor/setter/field, which is directly covered in Q479. |
| **Q495** | `Dependency Injection` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | **Exact Duplicate** | Q495 ('Dependency Injection') is an exact topic name duplicate of Q479. |
| **Q503** | `How to inject one dependency of one Bean into another.` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q503 ('How to inject one dependency of one Bean into another.') asks the fundamental mechanic of DI, which is covered in Q479. |
| **Q531** | `2)IoC and DI and types of injection` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q531 ('IoC and DI and types of injection') bundles IoC and DI injection types, which are covered by Q540 and Q479. |
| **Q561** | `Asked to write different types of dependency injection` | **Q479** | `Explain Dependency injection in springboot ? which is best ?` | *Concept-Level Duplicate* | Q561 ('Asked to write different types of dependency injection') asks to write code for the different types of DI, which is included in Q479. |
| **Q486** | `What is Autowiring?` | **Q518** | `what is @Autowired?` | *Concept-Level Duplicate* | Q486 ('What is Autowiring?') asks what autowiring is, which is the exact mechanism explained by Q518 ('what is @Autowired?'). |
| **Q545** | `autowiring` | **Q518** | `what is @Autowired?` | **Exact Duplicate** | Q545 ('autowiring') is an exact keyword duplicate of Q486/Q518. |
| **Q575** | `brief expln of what are spring beans?` | **Q489** | `What is bean?` | *Concept-Level Duplicate* | Q575 ('brief expln of what are spring beans?') asks for a brief explanation of Spring beans, which is identical in concept to Q489 ('What is bean?'). |
| **Q470** | `Explain bean life cycle` | **Q469** | `Explain Spring Bean life cycle?` | **Exact Duplicate** | Q470 ('Explain bean life cycle') is an exact duplicate of Q469 ('Explain Spring Bean life cycle?'). |
| **Q487** | `Explain the lifecycle of bean` | **Q469** | `Explain Spring Bean life cycle?` | **Exact Duplicate** | Q487 ('Explain the lifecycle of bean') is an exact duplicate of Q469. |
| **Q502** | `Life cycle of a bean. Pre-Initialization, Post-Initialisation.` | **Q469** | `Explain Spring Bean life cycle?` | *Concept-Level Duplicate* | Q502 ('Life cycle of a bean. Pre-Initialization, Post-Initialisation.') tests the specific pre- and post-initialization stages of the bean lifecycle, which are fully covered in the comprehensive lifecycle answer of Q469. |
| **Q504** | `What is the bean lifecycle in Spring Boot?` | **Q469** | `Explain Spring Bean life cycle?` | *Concept-Level Duplicate* | Q504 ('What is the bean lifecycle in Spring Boot?') asks for the bean lifecycle in Spring Boot, identical in concept to Q469. |
| **Q548** | `Explain the lifecycle of a bean` | **Q469** | `Explain Spring Bean life cycle?` | **Exact Duplicate** | Q548 ('Explain the lifecycle of a bean') is an exact duplicate of Q469. |
| **Q564** | `Spring bean lifecycle` | **Q469** | `Explain Spring Bean life cycle?` | **Exact Duplicate** | Q564 ('Spring bean lifecycle') is an exact duplicate of Q469. |
| **Q534** | `Bean Scope` | **Q482** | `What is bean and its scope?` | **Exact Duplicate** | Q534 ('Bean Scope') is an exact topic name duplicate of Q482 ('What is bean and its scope?'). |
| **Q527** | `What is bean and bean lifecycle and bean scopes.` | **Q482** | `What is bean and its scope?` | *Concept-Level Duplicate* | Q527 ('What is bean and bean lifecycle and bean scopes.') is a composite question combining bean definition, lifecycle, and scopes, all of which are answered individually in Q489, Q469, and Q482. |
| **Q541** | `What is Bean and How many ways to implement in spring` | **Q569** | `Ways of Creating a Spring bean` | *Concept-Level Duplicate* | Q541 ('What is Bean and How many ways to implement in spring') asks the same concept as Q569 ('Ways of Creating a Spring bean'). |
| **Q474** | `@bean annotation` | **Q473** | `Difference between @Bean and @Component` | *Concept-Level Duplicate* | Q474 ('@bean annotation') asks about the @Bean annotation, whose purpose, usage, and comparison are fully covered in Q473 ('Difference between @Bean and @Component'). |
| **Q515** | `Difference btw @Bean and @Component?` | **Q473** | `Difference between @Bean and @Component` | **Exact Duplicate** | Q515 ('Difference btw @Bean and @Component?') is an exact duplicate of Q473. |
| **Q577** | `diff btwn component and @beans` | **Q473** | `Difference between @Bean and @Component` | **Exact Duplicate** | Q577 ('diff btwn component and @beans') is an exact duplicate of Q473. |
| **Q516** | `@Component and its derivatives?` | **Q556** | `Difference between @Component, @Service, and @Repository.` | *Concept-Level Duplicate* | Q516 ('@Component and its derivatives?') asks about @Component and its specialized annotations (@Service, @Repository), which is directly answered by Q556 ('Difference between @Component, @Service, and @Repository.'). |
| **Q565** | `diff btwn @component and @service` | **Q556** | `Difference between @Component, @Service, and @Repository.` | *Concept-Level Duplicate* | Q565 ('diff btwn @component and @service') is a subset duplicate of Q556. |
| **Q567** | `Can use @repository replaced by @component or each other?` | **Q566** | `Can use @service and @component interchangeably?` | *Concept-Level Duplicate* | Q567 ('Can use @repository replaced by @component or each other?') asks if @repository and @component are interchangeable, which is part of the broader stereotype interchangeability concept answered in Q566 ('Can use @service and @component interchangeably?'). |
| **Q568** | `Can use @service and @respository interchangeably?` | **Q566** | `Can use @service and @component interchangeably?` | *Concept-Level Duplicate* | Q568 ('Can use @service and @respository interchangeably?') asks if @service and @repository are interchangeable, also covered in Q566. |
| **Q478** | `@Qualifier and @Primary` | **Q557** | `@Primary vs @Qualifier in spring` | **Exact Duplicate** | Q478 ('@Qualifier and @Primary') is an exact duplicate of Q557 ('@Primary vs @Qualifier in spring'). |
| **Q483** | `What is annotation you use when you have same type of bean?` | **Q557** | `@Primary vs @Qualifier in spring` | *Concept-Level Duplicate* | Q483 ('What is annotation you use when you have same type of bean?') asks which annotation to use for multiple beans of the same type, which is the exact problem solved by @Primary and @Qualifier in Q557. |
| **Q560** | `Spring boot Qualifier and Primary annotation` | **Q557** | `@Primary vs @Qualifier in spring` | **Exact Duplicate** | Q560 ('Spring boot Qualifier and Primary annotation') is an exact duplicate of Q557. |
| **Q576** | `If you have multiple beans, then what you will use in order to know which one...` | **Q557** | `@Primary vs @Qualifier in spring` | *Concept-Level Duplicate* | Q576 ('If you have multiple beans, then what you will use in order to know which one to pick ?') asks how to choose between multiple beans of the same type, identical in concept to Q557. |
| **Q507** | `About Spring and SpringBoot` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | *Concept-Level Duplicate* | Q507 ('About Spring and SpringBoot') is a general duplicate of Q475 ('What is Spring Boot, and why choose Spring Boot over Spring Core?'). |
| **Q510** | `SpringBoot advantages with examples` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | *Concept-Level Duplicate* | Q510 ('SpringBoot advantages with examples') asks for Spring Boot advantages, which is the 'why choose Spring Boot' core of Q475. |
| **Q524** | `Questions about Spring.` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | *Concept-Level Duplicate* | Q524 ('Questions about Spring.') is a generic placeholder duplicate of Spring vs Spring Boot overview. |
| **Q529** | `Spring Boot` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | **Exact Duplicate** | Q529 ('Spring Boot') is an exact topic name duplicate of Q475. |
| **Q530** | `1)difference between spring and springboot` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | **Exact Duplicate** | Q530 ('1)difference between spring and springboot') is an exact duplicate of Q475. |
| **Q535** | `Springboot vs spring` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | **Exact Duplicate** | Q535 ('Springboot vs spring') is an exact duplicate of Q475. |
| **Q539** | `Why we use SpringBoot Over Spring Framework` | **Q475** | `What is Spring Boot, and why choose Spring Boot over Spring Core?` | *Concept-Level Duplicate* | Q539 ('Why we use SpringBoot Over Spring Framework') asks the exact question of why to choose Spring Boot over Spring Framework, covered by Q475. |
| **Q514** | `Explain about @SpringBootApplication?` | **Q526** | `@springbootapplication annotation explain the working.` | **Exact Duplicate** | Q514 ('Explain about @SpringBootApplication?') is an exact duplicate of Q526 ('@springbootapplication annotation explain the working.'). |
| **Q537** | `Explain about Springbootapplication` | **Q526** | `@springbootapplication annotation explain the working.` | **Exact Duplicate** | Q537 ('Explain about Springbootapplication') is an exact duplicate of Q526. |
| **Q494** | `Spring Boot annotations` | **Q498** | `Explain different annotations used in spring application` | **Exact Duplicate** | Q494 ('Spring Boot annotations') is an exact duplicate of Q498 ('Explain different annotations used in spring application'). |
| **Q506** | `Are you familiar with Spring Boot annotations? Can you name a few?` | **Q498** | `Explain different annotations used in spring application` | *Concept-Level Duplicate* | Q506 ('Are you familiar with Spring Boot annotations? Can you name a few?') is a conversational duplicate of Q498. |
| **Q508** | `SpringBootAnnotations` | **Q498** | `Explain different annotations used in spring application` | **Exact Duplicate** | Q508 ('SpringBootAnnotations') is an exact duplicate of Q498. |
| **Q513** | `spring boot annotation` | **Q498** | `Explain different annotations used in spring application` | **Exact Duplicate** | Q513 ('spring boot annotation') is an exact duplicate of Q498. |
| **Q521** | `springboot and its annotations` | **Q498** | `Explain different annotations used in spring application` | *Concept-Level Duplicate* | Q521 ('springboot and its annotations') is a general duplicate of Q498. |
| **Q532** | `3)annotations used in springboot` | **Q498** | `Explain different annotations used in spring application` | **Exact Duplicate** | Q532 ('3)annotations used in springboot') is an exact duplicate of Q498. |
| **Q550** | `spring annotation.@autowired,@primary,@bean` | **Q498** | `Explain different annotations used in spring application` | *Concept-Level Duplicate* | Q550 ('spring annotation.@autowired,@primary,@bean') lists three annotations that are covered in Q498, Q518, Q557, and Q473. |
| **Q572** | `spring boot annotations` | **Q498** | `Explain different annotations used in spring application` | *Concept-Level Duplicate* | Q572 ('spring boot annotations') is a candidate log duplicate of Q498. |
| **Q571** | `exception handling` | **Q555** | `How to configure global exception handling in Spring Boot.` | *Concept-Level Duplicate* | Q571 ('exception handling') is a candidate note duplicate of Q555 ('How to configure global exception handling in Spring Boot.'). |
| **Q480** | `Explain about application.properties ?` | **Q512** | `Question related  application.properties file and application.yml file` | *Concept-Level Duplicate* | Q480 ('Explain about application.properties ?') is subsumed by Q512 ('Question related application.properties file and application.yml file'). |
| **Q523** | `-Question related  application.properties file and application.yml file --spr...` | **Q512** | `Question related  application.properties file and application.yml file` | **Exact Duplicate** | Q523 ('--Question related application.properties file and application.yml file --spr...') is an exact transcript duplicate of Q512. |
| **Q546** | `How to externalize configuration in Spring Boot Application?` | **Q520** | `How to externalize configuration in SpringBoot Application?` | **Exact Duplicate** | Q546 ('How to externalize configuration in Spring Boot Application?') is a 100% verbatim duplicate of Q520. |
| **Q536** | `Spring profile annotation` | **Q542** | `Questions about @Profile annotation and categories` | *Concept-Level Duplicate* | Q536 ('Spring profile annotation') is a sub-question duplicate of Q542 ('Questions about @Profile annotation and categories'). |
| **Q558** | `what is profiles in spring boot` | **Q542** | `Questions about @Profile annotation and categories` | *Concept-Level Duplicate* | Q558 ('what is profiles in spring boot') asks what profiles are, which is answered in Q542. |
| **Q501** | `Actuator` | **Q543** | `What is actuator in SpringBoot and Where it is defined in the application` | **Exact Duplicate** | Q501 ('Actuator') is an exact topic name duplicate of Q543 ('What is actuator in SpringBoot and Where it is defined in the application'). |
| **Q509** | `What is actuator` | **Q543** | `What is actuator in SpringBoot and Where it is defined in the application` | **Exact Duplicate** | Q509 ('What is actuator') is an exact duplicate of Q543. |
| **Q519** | `Spring security, JWT token?` | **Q499** | `There were some questions related to Spring Security and JWT authentication.` | *Concept-Level Duplicate* | Q519 ('Spring security, JWT token?') asks the same concept as Q499 ('There were some questions related to Spring Security and JWT authentication.'). |
| **Q563** | `Spring webflux basic` | **Q491** | `How will you implement Reactive Programming in Springboot` | *Concept-Level Duplicate* | Q563 ('Spring webflux basic') asks the same concept as Q491 ('How will you implement Reactive Programming in Springboot'). |
| **Q496** | `Mono and Flux` | **Q492** | `Difference between flux and mono` | **Exact Duplicate** | Q496 ('Mono and Flux') is an exact duplicate of Q492 ('Difference between flux and mono'). |

---

## 3. Retained Unique Questions by Topic (51 Questions)

Below is the complete inventory of the 51 unique questions retained for `answers.md`, organized by technical domain:

### Spring Core, IoC & Dependency Injection (5 Questions)

- **Q540**: What is IOC in Spring
- **Q511**: Internal flow of Dependency Injection and IOC
- **Q479**: Explain Dependency injection in springboot ? which is best ?
- **Q485**: What is a dependency injection framework?
- **Q518**: what is @Autowired?

### Spring Beans, Scopes & Instantiation Mechanics (6 Questions)

- **Q489**: What is bean?
- **Q482**: What is bean and its scope?
- **Q469**: Explain Spring Bean life cycle?
- **Q569**: Ways of Creating a Spring bean
- **Q570**: How Spring beans are instantiated?
- **Q477**: How do you register a third-party library class as a Spring bean ?

### Component Scanning, Stereotypes & Annotations (5 Questions)

- **Q473**: Difference between @Bean and @Component
- **Q556**: Difference between @Component, @Service, and @Repository.
- **Q566**: Can use @service and @component interchangeably?
- **Q557**: @Primary vs @Qualifier in spring
- **Q497**: Why @orderd , @Qualifier annotations are used

### Spring Boot Core & Auto-Configuration (4 Questions)

- **Q475**: What is Spring Boot, and why choose Spring Boot over Spring Core?
- **Q500**: Difference between java and springboot
- **Q526**: @springbootapplication annotation explain the working.
- **Q559**: Explain spring boot auto configuration.

### Spring MVC, REST Controllers & Input Handling (5 Questions)

- **Q498**: Explain different annotations used in spring application
- **Q533**: some annotations in springboot (Eg. controller, RestController etc.)
- **Q476**: How do you take input from a client in a Spring Boot application and send a r...
- **Q517**: Step by Step Process to create a spring boot application for a entity employee?
- **Q554**: They asked the same catalog details, with results sorted by price, and asked ...

### Global Exception Handling (1 Question)

- **Q555**: How to configure global exception handling in Spring Boot.

### Externalized Configuration & Profiles (5 Questions)

- **Q512**: Question related  application.properties file and application.yml file
- **Q505**: How do we load the application.properties file in Spring Boot?
- **Q520**: How to externalize configuration in SpringBoot Application?
- **Q542**: Questions about @Profile annotation and categories
- **Q490**: how will you handle multiple spring profiles using JVM args

### Transactions & Concurrency (2 Questions)

- **Q562**: How to handle transactions in spring boot in case of any issue ?
- **Q553**: Fetching and inserting data at the same time by different users spring boot r...

### Production Readiness & Security (2 Questions)

- **Q543**: What is actuator in SpringBoot and Where it is defined in the application
- **Q499**: There were some questions related to Spring Security and JWT authentication.

### Framework Edge Cases, Caching, Starters & Modules (6 Questions)

- **Q468**: You have a rest controller, where a final field and constructor is declared a...
- **Q471**: Difference between @requiredargconstructor and @autowired -------------------...
- **Q573**: How do you detect and resolve circular dependencies in Spring applications?
- **Q549**: What is caching and how it is implemented in Spring Boot
- **Q538**: Spring starter methods
- **Q551**: How to use multiple classes in spring boot.

### Reactive Programming & Spring WebFlux (3 Questions)

- **Q491**: How will you implement Reactive Programming in Springboot
- **Q492**: Difference between flux and mono
- **Q574**: Can you explain the architecture of Spring WebFlux?

### Performance, Testing & Candidate Transcripts (7 Questions)

- **Q493**: How did you do the volume testing in the TLR
- **Q481**: What is AR system? how do you prioritise features.
- **Q522**: Write the jave code to find the duplicate element in an array using streams
- **Q552**: Introduction & previous projects
- **Q525**: Class and Interface,functional interface,string pool, spring & spring boot
- **Q528**: Java 8 features, spring boot
- **Q547**: Explain oops principles

---

## 4. Verification & Integrity Checklist

- [x] **Original Question Count**: 110 questions audited (`Q468` to `Q577`).
- [x] **Exact Duplicates Identified**: 26 questions.
- [x] **Concept-Level Duplicates Identified**: 33 questions.
- [x] **Unique Questions Retained**: 51 questions.
- [x] **Zero Overlap**: Retained and removed sets are completely disjoint ($51 + 59 = 110$).
- [x] **Original `questions.md` Unmodified**: Kept strictly as the source of truth.
- [x] **Zero Duplicates in Final Answers Sheet**: All answers in `answers.md` are based exclusively on the 51 retained unique questions.
