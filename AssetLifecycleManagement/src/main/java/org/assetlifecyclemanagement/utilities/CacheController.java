package org.assetlifecyclemanagement.utilities;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assetlifecyclemanagement.config.MultiLevelCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@Tag(name = "Cache", description = "Check cache stats & clearing cache.")
public class CacheController {

    private final MultiLevelCacheManager multiLevelCacheManager;

    @Operation(summary = "Get stats of cache", description = "Retrieves detailed information of cache getting used in the application.")
    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('CACHE_READ')")
    public ResponseEntity<String> getCacheStats() {
        Cache cache = multiLevelCacheManager.getCache("employees");

        if (cache instanceof ConcurrentMapCache concurrentMapCache) {
            Map<Object, Object> nativeCache = concurrentMapCache.getNativeCache();
            StringBuilder sb = new StringBuilder("Cache contents:\n");
            nativeCache.forEach((key, value) -> sb.append(key).append(" = ").append(value).append("\n"));
            return ResponseEntity.ok(sb.toString());
        }

        return ResponseEntity.ok("No cache stats available or unsupported cache type");
    }

    @Operation(summary = "Clear Cache", description = "Clears the cache stored for the the application.")
    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('CACHE_DELETE')")
    public ResponseEntity<String> clearCache() {
        // Clear all caches
        multiLevelCacheManager.getCacheNames().forEach(name -> {
            Cache cache = multiLevelCacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
                log.info("Cleared cache: {}", name);
            }
        });
        return ResponseEntity.ok("All caches cleared successfully");
    }

}
