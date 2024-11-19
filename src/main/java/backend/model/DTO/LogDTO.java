package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class LogDTO implements ApplicationContextAware {

    private Integer id;

    private Integer userId;

    private String endpoint;

    private String operation;

    private LocalDateTime created;

    private static ApplicationContext context;
    public LogDTO getThis() {
        LogDTO logDTOAux = context.getBean(LogDTO.class);
        logDTOAux.setId(this.getId());
        logDTOAux.setUserId(this.getUserId());
        logDTOAux.setEndpoint(this.getEndpoint());
        logDTOAux.setOperation(this.getOperation());
        logDTOAux.setCreated(this.getCreated());
        return logDTOAux;
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

        public LogDTO build() {
            LogDTO logDTO = new LogDTO();
            logDTO.setId(id);
            logDTO.setUserId(userId);
            logDTO.setEndpoint(endpoint);
            logDTO.setOperation(operation);
            logDTO.setCreated(created);
            return logDTO;
        }
    }



}
