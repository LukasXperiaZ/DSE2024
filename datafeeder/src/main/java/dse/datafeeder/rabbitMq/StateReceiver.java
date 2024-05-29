package dse.datafeeder.rabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.datafeeder.dto.FVState;
import dse.datafeeder.dto.LVState;
import dse.datafeeder.rest.StateAPI;
import dse.datafeeder.simulation.AutonomousVehicleSimulation;
import dse.datafeeder.simulation.NonAutonomousVehicleSimulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StateReceiver {
    private final Logger logger = LoggerFactory.getLogger(StateAPI.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AutonomousVehicleSimulation autonomousVehicleSimulation;
    private boolean leadingMode = false;

    @Autowired
    private NonAutonomousVehicleSimulation nonAutonomousVehicleSimulation;
    private boolean followMode = false;

    @RabbitListener(queues = "#{autonomousVehicleQueue.name}")
    public void makeLeadingVehicle(String lvStateJson) {
        logger.debug("Autonomous: Received LVState: {}", lvStateJson);
        LVState lvState;
        try {
            lvState = objectMapper.readValue(lvStateJson, LVState.class);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing LVState: {}", e.getMessage());
            return;
        }
        if (lvState.getIsLeadingVehicle()) {
            if (!leadingMode) {
                // Vehicle has to change from non-leading into leading mode.
                leadingMode = true;
                autonomousVehicleSimulation.makeLeadingVehicle();
                logger.info("Vehicle entered leading vehicle mode!");
            } else {
                // Vehicle stays in leading mode.
                autonomousVehicleSimulation.stayLeadingVehicle();
                logger.trace("Vehicle stays in leading vehicle mode.");
            }

        } else {
            if (leadingMode) {
                // Vehicle has to change from leading into non-leading mode.
                leadingMode = false;
                autonomousVehicleSimulation.disableLeadingVehicle();
                logger.info("Vehicle exited leading vehicle mode!");
            } else {
                // Vehicle stays in non-leading mode.
                autonomousVehicleSimulation.stayNonLeadingVehicle();
                logger.trace("Vehicle stays in non-leading vehicle mode.");
            }
        }
    }

    @RabbitListener(queues = "#{nonAutonomousVehicleQueue.name}")
    public void makeFollowingVehicle(String fvStateJson) {
        logger.debug("Non-Autonomous: Received FVState: {}", fvStateJson);
        FVState fvState;
        try {
            fvState = objectMapper.readValue(fvStateJson, FVState.class);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing FVState: {}", e.getMessage());
            return;
        }
        if (fvState.getUsesFM()) {
            if (!followMode) {
                // The vehicle has to change from non-follow to follow mode.
                followMode = true;
                nonAutonomousVehicleSimulation.enableFM(fvState);
                logger.info("Vehicle entered the follow mode!");
            } else {
                // The vehicle stays in follow mode.
                if (fvState.getTargetSpeed() == 141.0) {
                    logger.warn("Vehicle received target speed {}", fvState.getTargetSpeed());
                }
                nonAutonomousVehicleSimulation.stayInFM(fvState);
                logger.trace("Vehicle stays in follow mode.");
            }

        } else {
            if (followMode) {
                // The vehicle has to change from follow to non-follow mode.
                followMode = false;
                nonAutonomousVehicleSimulation.disableFM();
                logger.info("Vehicle exited the follow mode!");
            } else {
                // The vehicle stays in non-follow mode.
                nonAutonomousVehicleSimulation.stayInNonFM();
                logger.trace("Vehicle stays in non-follow mode.");
            }
        }
    }
}
