package dse.datafeeder.rabbitMq;

import dse.datafeeder.dto.VehicleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Service;

@Service
public class RabbitMq {

    private final Logger logger = LoggerFactory.getLogger(RabbitMq.class);

    private final String exchange = "position";

    private final AmqpTemplate rabbitTemplate;

    public RabbitMq() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(new CachingConnectionFactory());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(VehicleData vehicleData) {
        logger.debug("Sending vehicle data: {}", vehicleData);
        rabbitTemplate.convertAndSend(exchange, vehicleData);
    }

}
