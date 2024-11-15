package backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableAsync
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${rate.max}")
    private int maxRequestsPerMinute;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private AdminFilter adminFilter;

    @Autowired
    @Lazy
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    @Lazy
    private CorsConfigurationSourceImpl corsConfigurationSource;

    @Bean
    public RateLimitingFilter rateLimitingFilter() {
        return new RateLimitingFilter(maxRequestsPerMinute);
    }

    /**
     * 密码加密
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 安全过滤器链
     *
     * @param http HttpSecurity
     * @return 安全过滤器链
     * @throws Exception 异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                        .requestMatchers("/resources/**", "/unauthorized/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // dev mode, easy for debug by view
                // httpBasic(Customizer.withDefaults()) // prod mode, disable view for security
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(adminFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitingFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authEntryPoint)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .build();

    }

    /**
     * 认证提供者
     *
     * @param passwordEncoder 密码加密
     * @return DaoAuthenticationProvider 认证提供者
     */
    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    /**
     * 认证管理器
     *
     * @return 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return null;
    }


    /**
     * 静态资源处理
     *
     * @param registry 资源处理注册
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resources/media/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/src/main/resources/media/");
    }

}
