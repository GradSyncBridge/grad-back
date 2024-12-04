package backend.rabbitMQ;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("mq")
public class RabbitMQConfig {

    private static final String QUEUE_NAME = "message-queue";

    private static final String EXCHANGE_NAME = "topic-exchange";

    /**
     * 配置RabbitMQ的队列
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true, false, false);
    }

    /**
     * 配置RabbitMQ的交换机
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("message.*");
    }
}
