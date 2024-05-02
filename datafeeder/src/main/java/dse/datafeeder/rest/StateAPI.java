package dse.datafeeder.rest;

import dse.datafeeder.simulation.AutonomousVehicleSimulation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
 * This class is used for receiving requests regarding the state of the vehicles.
 */
@RestController
@RequestMapping("state")
public class StateAPI {

    private final Logger logger = LoggerFactory.getLogger(StateAPI.class);

    private AutonomousVehicleSimulation autonomousVehicleSimulation;

    // Initialize the simulations at the start of the application.
    @PostConstruct
    public void init() {
        logger.info("Initializing the state API");
        autonomousVehicleSimulation = new AutonomousVehicleSimulation();
        autonomousVehicleSimulation.startSimulation();
    }

    // This is just to test if the api works.
    @GetMapping("/get-project-name")
    public String getProjectName() {
        return "Datafeeder";
    }

    // Send a patch request (no body needed) to make the autonomous vehicle a leading vehicle.
    // This will result in the leading vehicle sending also the target Speed and the target Lane.
    @PatchMapping("/makeLeadingVehicle")
    public String makeLeadingVehicle() {
        autonomousVehicleSimulation.makeLeadingVehicle();
        return "Vehicle is now a leading vehicle!";
    }
}
