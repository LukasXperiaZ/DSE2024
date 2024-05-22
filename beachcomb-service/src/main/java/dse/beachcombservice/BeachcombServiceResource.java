package dse.beachcombservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dse.beachcombservice.mongodb.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/vehicles/{vin}")
    public VehicleLocationDTO getVehicleByVin(@PathVariable String vin) {
        logger.info("Returning vehicle with vin: " + vin);
        return beachcombService.getVehicleLocationByVin(vin);
    }

    @GetMapping("/test2")
    public void test2() {
        var allVehicles = beachcombService.findVehiclesNearPoint("123", 1.0, 2.0, 0.2);
        System.out.println(allVehicles);
    }


    @GetMapping("/test")
    public void test() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVin("123");
        vehicleDTO.setCoordinates(new Coordinates(1.0, 2.0));
        vehicleDTO.setSpeed(10.0f);
        vehicleDTO.setLane(1);
        vehicleDTO.setTimestamp(new Date());
        vehicleDTO.setTargetLane(2);
        vehicleDTO.setTargetSpeed(20.0d);
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(vehicleDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        rabbitTemplate.convertAndSend("position", "", jsonString);
    }

}
