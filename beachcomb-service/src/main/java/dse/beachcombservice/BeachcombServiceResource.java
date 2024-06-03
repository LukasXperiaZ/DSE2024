package dse.beachcombservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.beachcombservice.mongodb.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private VehicleRepository vehicleRepository;

    @PostMapping("/insert")
    public String insert(@Valid @RequestBody VehicleDTO vehicleModel) {
        logger.info("Inserting vehicle: " + vehicleModel);
        beachcombService.insert(vehicleModel);
        return "Inserted!";
    }

    @GetMapping("/follow_me")
    public Map<String, List<String>> followMe() {
        logger.info("Returning FollowMe-Candidates!");
        return beachcombService.getFollowMeCandidates();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vehicles/{vin}")
    public VehicleLocationDTO getVehicleByVin(@PathVariable String vin) {
        logger.info("Returning vehicle with vin: " + vin);
        return beachcombService.getVehicleLocationByVin(vin);
    }

    @GetMapping("/health")
    @ResponseStatus(value = HttpStatus.OK)
    public void healthCheck() {
    }
}
