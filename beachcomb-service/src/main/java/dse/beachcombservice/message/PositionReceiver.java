package dse.beachcombservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.beachcombservice.BeachcombService;
import dse.beachcombservice.VehicleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@RabbitListener(queues = "Q")
public class PositionReceiver {

    Logger logger = LoggerFactory.getLogger(PositionReceiver.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private BeachcombService beachcombService;

    // Receives a message from RabbitMQ and inserts the vehicle into the database
    @RabbitHandler
    public void receiveMessage(String message) {
        try {
            logger.info("RabbitMQ message received:{}", message);
            var vehicle = objectMapper.readValue(message, VehicleDTO.class);
            beachcombService.insert(vehicle);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
