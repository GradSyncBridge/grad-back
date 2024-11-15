package backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // A thread-safe map to store the number of requests per client IP
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();

    // Define maximum allowed requests per minute
    private final int MAX_REQUESTS_PER_MINUTE;

    public RateLimitingFilter(int maxRequestsPerMinute) {
        MAX_REQUESTS_PER_MINUTE = maxRequestsPerMinute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Get the client's IP address
        String clientIp = request.getRemoteAddr();

        // Initialize the count if the IP is new, otherwise get the current count
        requestCounts.putIfAbsent(clientIp, 0);
        int requestCount = requestCounts.get(clientIp);

        // If the count exceeds the limit, return a 429 Too Many Requests response
        if (requestCount >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
            responseMap.put("message", "Too many requests - please try again later.");
            responseMap.put("time", System.currentTimeMillis() / 1000);
            responseMap.put("data", null);

            String jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
            response.getWriter().write(jsonResponse);
            return;
        }

        // Otherwise, increment the request count and proceed with the request
        requestCounts.put(clientIp, requestCount + 1);
        filterChain.doFilter(request, response);
    }
}
