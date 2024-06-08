package dse.datafeeder.rabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.datafeeder.dto.VehicleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMq {
    // Responsible for sending position data.

    private final Logger logger = LoggerFactory.getLogger(RabbitMq.class);

    private final String exchange = "position";

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMq() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("34.141.72.250");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("secret");
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
    }

    public void send(VehicleData vehicleData) {
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(vehicleData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.trace("Sending vehicle data: {}", jsonString);

        try{
            rabbitTemplate.convertAndSend(exchange, "", jsonString);
        } catch (Exception e) {
            logger.error("Error sending vehicle data: {}", e.getMessage());
        }
    }

}
