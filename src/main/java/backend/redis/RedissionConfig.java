package backend.redis;

import jakarta.annotation.PreDestroy;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissionConfig {

    @Value("${spring.data.redis.host}")
    private String REDIS_HOSTNAME;

    @Value("${spring.data.redis.port}")
    private int REDIS_PORT;

    @Value("${spring.data.redis.password}")
    private String REDIS_PASSWORD;

    @Value("${spring.data.redis.username}")
    private String REDIS_USERNAME;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + REDIS_HOSTNAME + ":" + REDIS_PORT)
                .setPassword(REDIS_PASSWORD)
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10)
                .setConnectTimeout(10000)
                .setKeepAlive(true);

        return Redisson.create(config);
    }
}
