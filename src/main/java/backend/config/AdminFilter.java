package backend.config;

import backend.model.entity.Teacher;
import backend.model.entity.User;

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

@Component
public class AdminFilter extends OncePerRequestFilter {
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {

        Teacher teacher = User.getAuth().getTeacher();

        if (teacher == null || teacher.getIdentity() != 2) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("code", HttpStatus.FORBIDDEN.value());

            String message = teacher == null ?
                    "Student users cannot access this endpoint":
                    String.format("Teacher [identity=%d] is not allowed", teacher.getIdentity());
            responseMap.put("message", message);
            responseMap.put("time", System.currentTimeMillis() / 1000);
            responseMap.put("data", null);

            String jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
            response.getWriter().write(jsonResponse);

            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.matches("^/admin.*$");
    }
}
