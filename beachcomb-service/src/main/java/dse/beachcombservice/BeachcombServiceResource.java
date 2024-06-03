package dse.beachcombservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("beachcomb")
public class BeachcombServiceResource {

    Logger logger = LoggerFactory.getLogger(BeachcombServiceResource.class);
    @Autowired
    private BeachcombService beachcombService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Inserts a new vehicle into the database
    @PostMapping("/insert")
    public String insert(@Valid @RequestBody VehicleDTO vehicleModel) {
        logger.info("Inserting vehicle: " + vehicleModel);
        beachcombService.insert(vehicleModel);
        return "Inserted!";
    }

    // Returns vehicle-vins and a list with potential FollowMe-Candidates
    @GetMapping("/follow_me")
    public Map<String, List<String>> followMe() {
        logger.info("Returning FollowMe-Candidates!");
        return beachcombService.getFollowMeCandidates();
    }

    // Returns vehicle data for the given vin
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vehicles/{vin}")
    public VehicleLocationDTO getVehicleByVin(@PathVariable String vin) {
        logger.info("Returning vehicle with vin: " + vin);
        return beachcombService.getVehicleLocationByVin(vin);
    }

    // Health check for GKE
    @GetMapping("/health")
    @ResponseStatus(value = HttpStatus.OK)
    public void healthCheck() {
    }
}
