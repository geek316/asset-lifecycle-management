package org.assetlifecyclemanagement.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

/**
 * Custom CacheManager that coordinates L1 (Caffeine) and L2 (Redis) caches.
 * This acts as a facade over both cache managers.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MultiLevelCacheManager implements CacheManager {

    private final CacheManager caffeineCacheManager;  // L1
    private final CacheManager redisCacheManager;     // L2

    @Override
    public Cache getCache(String name) {
        // Return a MultiLevelCache that wraps both L1 and L2
        Cache l1Cache = caffeineCacheManager.getCache(name);
        Cache l2Cache = redisCacheManager.getCache(name);

        if (isNull(l1Cache) && isNull(l2Cache)) {
            return null;
        }

        return new MultiLevelCache(name, l1Cache, l2Cache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(
                new ConcurrentHashMap<String, Boolean>() {{
                    caffeineCacheManager.getCacheNames().forEach(name -> put(name, true));
                    redisCacheManager.getCacheNames().forEach(name -> put(name, true));
                }}.keySet()
        );
    }

    /**
     * Multi-level cache that checks L1 first, then L2.
     * Writes go to both caches.
     * Evictions go to both caches.
     */
    @Slf4j
    public static class MultiLevelCache implements Cache {

        private final String name;
        private final Cache l1Cache;  // Caffeine
        private final Cache l2Cache;  // Redis

        public MultiLevelCache(String name, Cache l1Cache, Cache l2Cache) {
            this.name = name;
            this.l1Cache = l1Cache;
            this.l2Cache = l2Cache;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getNativeCache() {
            return this;
        }

        @Override
        public ValueWrapper get(Object key) {
            // 1. Try L1 (Caffeine) first
            if (l1Cache != null) {
                ValueWrapper value = l1Cache.get(key);
                if (value != null) {
                    log.debug("✅ L1 HIT - key: {}", key);
                    return value;
                }
            }

            // 2. Try L2 (Redis) if L1 miss
            if (l2Cache != null) {
                ValueWrapper value = l2Cache.get(key);
                if (value != null) {
                    log.debug("✅ L2 HIT - key: {}", key);
                    // Populate L1 with L2 value
                    if (l1Cache != null) {
                        l1Cache.put(key, value.get());
                        log.debug("📦 Populated L1 from L2 for key: {}", key);
                    }
                    return value;
                }
            }

            log.debug("❌ CACHE MISS - key: {}", key);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Object key, Class<T> type) {
            // 1. Try L1 (Caffeine) first
            if (l1Cache != null) {
                T value = l1Cache.get(key, type);
                if (value != null) {
                    log.debug("✅ L1 HIT - key: {}", key);
                    return value;
                }
            }

            // 2. Try L2 (Redis) if L1 miss
            if (l2Cache != null) {
                T value = l2Cache.get(key, type);
                if (value != null) {
                    log.debug("✅ L2 HIT - key: {}", key);
                    // Populate L1 with L2 value
                    if (l1Cache != null) {
                        l1Cache.put(key, value);
                        log.debug("📦 Populated L1 from L2 for key: {}", key);
                    }
                    return value;
                }
            }

            log.debug("❌ CACHE MISS - key: {}", key);
            return null;
        }

        @Override
        public @Nullable <T> T get(Object key, Callable<T> valueLoader) {
            return null;
        }

        @Override
        public @Nullable CompletableFuture<?> retrieve(Object key) {
            return Cache.super.retrieve(key);
        }

        @Override
        public <T> CompletableFuture<T> retrieve(Object key, Supplier<CompletableFuture<T>> valueLoader) {
            return Cache.super.retrieve(key, valueLoader);
        }

        @Override
        public void put(Object key, Object value) {
            // Write to both caches
            if (l1Cache != null) {
                l1Cache.put(key, value);
                log.debug("📦 L1 PUT - key: {}", key);
            }
            if (l2Cache != null) {
                l2Cache.put(key, value);
                log.debug("📦 L2 PUT - key: {}", key);
            }
        }

        @Override
        public void evict(Object key) {
            // Evict from both caches
            if (l1Cache != null) {
                l1Cache.evict(key);
                log.debug("🗑️ L1 EVICT - key: {}", key);
            }
            if (l2Cache != null) {
                l2Cache.evict(key);
                log.debug("🗑️ L2 EVICT - key: {}", key);
            }
        }

        @Override
        public boolean evictIfPresent(Object key) {
            return Cache.super.evictIfPresent(key);
        }

        @Override
        public void clear() {
            // Clear both caches
            if (l1Cache != null) {
                l1Cache.clear();
                log.debug("🗑️ L1 CLEAR");
            }
            if (l2Cache != null) {
                l2Cache.clear();
                log.debug("🗑️ L2 CLEAR");
            }
        }

        @Override
        public boolean invalidate() {
            return Cache.super.invalidate();
        }

        @Override
        public ValueWrapper putIfAbsent(Object key, Object value) {
            // Try to put in L1 first
            if (l1Cache != null) {
                ValueWrapper existing = l1Cache.putIfAbsent(key, value);
                if (existing == null && l2Cache != null) {
                    l2Cache.putIfAbsent(key, value);
                }
                return existing;
            }
            if (l2Cache != null) {
                return l2Cache.putIfAbsent(key, value);
            }
            return null;
        }
    }
}