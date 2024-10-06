package r16a.Athena.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to track and handle multiple requests from the same IP
 *
 * @author Ramphy Aquino Nova
 */
@Slf4j
@Service
@AllArgsConstructor
public class IpRequestTrackingService {
    private final ConcurrentHashMap<String, IpInfo> ipInfoByRoute = new ConcurrentHashMap<>();

    private static final long BLOCK_TIME_SECONDS = 60;
    private static final int MAX_ROUTE_REQUESTS = 10;
    private static final long REQUEST_LIMIT_PERIOD = 30;

    @Getter
    private static class IpInfo {
        private int requestCount = 0;
        private boolean blocked = false;
        private LocalDateTime blockedUntil;
        private LocalDateTime firstRequestTime = LocalDateTime.now();

        public void incrementRequestCount() {
            this.requestCount++;
        }

        public void blockRoute() {
            this.blocked = true;
            this.blockedUntil = LocalDateTime.now().plusSeconds(BLOCK_TIME_SECONDS);
        }

        public void resetRequestCount() {
            this.requestCount = 0;
            this.firstRequestTime = LocalDateTime.now();
        }
    }

    public boolean isBlocked(String ip, String route) {
        String key = ip + ":" + route;
        IpInfo ipInfo = ipInfoByRoute.get(key);

        if (ipInfo == null) {
            return false;
        }

        // Check if IP is still blocked
        if (ipInfo.isBlocked() && ipInfo.getBlockedUntil().isAfter(LocalDateTime.now())) {
            return true;
        }

        // If block time is over, reset and remove from the map
        if (ipInfo.isBlocked() && ipInfo.getBlockedUntil().isBefore(LocalDateTime.now())) {
            ipInfoByRoute.remove(key);
        }

        return false;
    }

    public void registerRequest(String ip, String route) {
        String key = ip + ":" + route;
        IpInfo routeInfo = ipInfoByRoute.getOrDefault(key, new IpInfo());

        // Reset counts if 30 seconds have passed since the first request
        if (routeInfo.getFirstRequestTime().plusSeconds(REQUEST_LIMIT_PERIOD).isBefore(LocalDateTime.now())) {
            routeInfo.resetRequestCount();
        }

        // Increment request count for this IP and route
        routeInfo.incrementRequestCount();

        // Block if the request limit for this route is exceeded
        if (routeInfo.getRequestCount() > MAX_ROUTE_REQUESTS) {
            routeInfo.blockRoute();
        }

        ipInfoByRoute.put(key, routeInfo);
    }
}
