package backend.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class GlobalLogger implements ApplicationContextAware {

    private Integer id;

    private Integer userId;

    private String endpoint;

    private String operation;

    private LocalDateTime created;

    private static ApplicationContext context;
    public GlobalLogger getThis() {
        return this;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static class Builder {
        private Integer id;
        private Integer userId;
        private String endpoint;
        private String operation;
        private LocalDateTime created;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }

        public Builder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public GlobalLogger build() {
            GlobalLogger globalLogger = context.getBean(GlobalLogger.class);
            globalLogger.setId(id);
            globalLogger.setUserId(userId);
            globalLogger.setEndpoint(endpoint);
            globalLogger.setOperation(operation);
            globalLogger.setCreated(created);
            return globalLogger;
        }
    }



}
