package dse.beachcombservice.message;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("Q");
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange("position");
    }

    @Bean
    public Binding binding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public PositionReceiver positionReceiver() {
        return new PositionReceiver();
    }
}
