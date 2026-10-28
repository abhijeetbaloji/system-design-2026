# Caching Service Proxy Example

## 1. Scenario Overview

A financial trading platform (such as Bloomberg Terminal, Robinhood, or Interactive Brokers) provides real-time stock price quotes to thousands of active traders.
- Querying a live exchange API (e.g., NASDAQ or NYSE direct market feeds) costs money per API call, incurs 150ms network round-trip latency, and has strict API rate limits (e.g., maximum 60 requests/minute).
- Thousands of users simultaneously request the quote for popular tickers like `AAPL`, `NVDA`, or `MSFT`.
- Real-time stock prices for retail display do not need microsecond updates; a 5-second Time-To-Live (TTL) cache is completely acceptable and drastically reduces network load.

A **Caching Proxy** (`CachingStockPriceProxy`) wraps the `RealStockPriceService`:
- On receiving `getStockQuote("AAPL")`, it checks an internal cache map.
- If the ticker exists in cache and has not expired (within the 5-second TTL), it immediately returns the cached quote (latency ~0ms).
- If it is a cache miss or the cached entry has expired, it delegates to `RealStockPriceService`, stores the fresh quote in the cache with the current timestamp, and returns it.

---

## 2. Bad Design vs Good Design

### Bad Design (No Caching / Redundant Expensive Remote Calls)
- `BadStockPriceService` issues a full network API request every single time `getStockQuote()` is called.
- 1,000 queries for `NVDA` generate 1,000 duplicate network round-trips, exhausting API rate limits and causing high latencies.
- There is no central mechanism to control caching or cache eviction.

### Good Design (Caching Proxy Pattern)
- `StockPriceService` provides the uniform contract (`getStockQuote()`, `isMarketOpen()`).
- `RealStockPriceService` manages communication with the financial exchange feed.
- `CachingStockPriceProxy` transparently handles the cache lookup, TTL expiration, cache invalidation (`evict`), and metrics (`cacheHits`, `cacheMisses`).
- The client `StockTraderDashboard` is completely unaware of the caching layer and simply calls `priceService.getStockQuote()`.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.proxy.example03
├── bad
│   ├── BadStockPriceService.java
│   └── BadStockTraderDemo.java
└── good
    ├── CachingStockPriceProxy.java
    ├── RealStockPriceService.java
    ├── StockPriceService.java
    ├── StockQuote.java
    ├── StockTraderDashboard.java
    └── StockTraderDemo.java
```

---

## 4. Execution Flow & SOLID Principles

- **Cache Hit / Miss Workflow**:
  - Request 1 (`AAPL`): Cache miss -> delegates to `RealStockPriceService` -> caches result -> returns quote.
  - Request 2 (`AAPL`, within 5s): Cache hit -> returns cached quote immediately without network call.
  - Request 3 (`AAPL`, after 6s): Expired -> delegates to `RealStockPriceService` -> updates cache -> returns fresh quote.
- **Single Responsibility Principle (SRP)**: `RealStockPriceService` is responsible for live exchange API communication; `CachingStockPriceProxy` is responsible for storage, TTL expiration, and cache eviction.
- **Open/Closed Principle (OCP)**: Swapping the in-memory cache for a distributed Redis cache proxy requires no changes to `RealStockPriceService` or `StockTraderDashboard`.
