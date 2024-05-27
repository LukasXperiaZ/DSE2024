package dse.datafeeder.rest;

import dse.datafeeder.dto.FVState;
import dse.datafeeder.dto.LVState;
import dse.datafeeder.simulation.AutonomousVehicleSimulation;
import dse.datafeeder.simulation.NonAutonomousVehicleSimulation;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
 * This class is used for receiving requests regarding the state of the vehicles.
 * Currently only used in dev and not production.
 */
@RestController
@RequestMapping("state")
public class StateAPI {

    private final Logger logger = LoggerFactory.getLogger(StateAPI.class);

    private AutonomousVehicleSimulation autonomousVehicleSimulation;
    private boolean leadingMode = false;

    private NonAutonomousVehicleSimulation nonAutonomousVehicleSimulation;
    private boolean followMode = false;

    // Initialize the simulations at the start of the application.
    @PostConstruct
    public void init() {
        autonomousVehicleSimulation = new AutonomousVehicleSimulation();
        nonAutonomousVehicleSimulation = new NonAutonomousVehicleSimulation();
    }

    // This is just to test if the api works.
    @GetMapping("/get-project-name")
    public String getProjectName() {
        return "Datafeeder";
    }

    @GetMapping("/registerVehicles")
    public String registerVehicles() {
        autonomousVehicleSimulation.registerVehicle();
        nonAutonomousVehicleSimulation.registerVehicle();
        return "Successful!";
    }

    @GetMapping("/startSimulations")
    public String startSimulations() {
        autonomousVehicleSimulation.startSimulation();
        nonAutonomousVehicleSimulation.startSimulation();
        return "Successful!";
    }

    // Send a put request to set the state of the autonomous vehicle.
    // If isLeadingVehicle is set to true, this will result in the leading vehicle sending also the target Speed
    // and the target Lane.
    @PutMapping("autonomousVehicle")
    public String makeLeadingVehicle(@RequestBody LVState lvState) {
        logger.info("Received LVState: {}", lvState);
        if (lvState.getIsLeadingVehicle()) {
            if (!leadingMode) {
                // Vehicle has to change from non-leading into leading mode.
                leadingMode = true;
                autonomousVehicleSimulation.makeLeadingVehicle();
                return "Vehicle entered leading vehicle mode!";
            } else {
                // Vehicle stays in leading mode.
                autonomousVehicleSimulation.stayLeadingVehicle();
                return "Vehicle stays in leading vehicle mode.";
            }

        } else {
            if (leadingMode) {
                // Vehicle has to change from leading into non-leading mode.
                leadingMode = false;
                autonomousVehicleSimulation.disableLeadingVehicle();
                return "Vehicle exited leading vehicle mode!";
            } else {
                // Vehicle stays in non-leading mode.
                autonomousVehicleSimulation.stayNonLeadingVehicle();
                return "Vehicle stays in non-leading vehicle mode.";
            }
        }
    }

    @PutMapping("nonAutonomousVehicle")
    public String makeFollowingVehicle(@RequestBody FVState fvState) {
        logger.info("Received FVState: {}", fvState);
        if (fvState.getUsesFM()) {
            if (!followMode) {
                // The vehicle has to change from non-follow to follow mode.
                followMode = true;
                nonAutonomousVehicleSimulation.enableFM(fvState);
                return "Vehicle entered the follow mode!";
            } else {
                // The vehicle stays in follow mode.
                nonAutonomousVehicleSimulation.stayInFM(fvState);
                return "Vehicle stays in follow mode.";
            }

        } else {
            if (followMode) {
                // The vehicle has to change from follow to non-follow mode.
                followMode = false;
                nonAutonomousVehicleSimulation.disableFM();
                return "Vehicle exited the follow mode!";
            } else {
                // The vehicle stays in non-follow mode.
                nonAutonomousVehicleSimulation.stayInNonFM();
                return "Vehicle stays in non-follow mode.";
            }
        }

    }
}
