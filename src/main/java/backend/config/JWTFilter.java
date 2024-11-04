package backend.config;

import backend.model.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.validation.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Value("${jwt.token-name}")
    private String tokenName;

    /**
     * 过滤器，用于验证token
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤链
     * @throws ServletException 异常
     * @throws IOException      异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(tokenName);
        String token = null;
        Integer uid = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // username = jwtService.extractUserName(token);
                uid = jwtService.extractUid(token);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                responseMap.put("message", e.getMessage());
                responseMap.put("time", System.currentTimeMillis());
                responseMap.put("data", null);

                String jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
                response.getWriter().write(jsonResponse);

                return;
            }
        }

        if (uid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // UserDetails userDetails =
                // context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);
                User user = context.getBean(UserDetailsServiceImpl.class).loadUserById(uid);
                // if (jwtService.validateToken(token, userDetails)) {
                if (jwtService.validateTokenById(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
                            ((UserDetails) user).getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                responseMap.put("message", e.getMessage());
                responseMap.put("time", System.currentTimeMillis());
                responseMap.put("data", null);

                String jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
                response.getWriter().write(jsonResponse);

                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
