package dse.datafeeder.rabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.datafeeder.dto.VehicleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RabbitMq {

    private final Logger logger = LoggerFactory.getLogger(RabbitMq.class);

    private final String exchange = "position";

    private final RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMq() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(new CachingConnectionFactory());
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(VehicleData vehicleData) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(vehicleData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.debug("Sending vehicle data: {}", jsonString);
        rabbitTemplate.convertAndSend(exchange, "", jsonString);
    }

}
