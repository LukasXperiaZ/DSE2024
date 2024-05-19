package dse.beachcombservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class BeachcombServiceResource {

    @Autowired
    private BeachcombService beachcombService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/insert")
    public String insert(@Valid @RequestBody VehicleDTO vehicleModel) {
        System.out.println("Inserting");
        beachcombService.insert(vehicleModel);
        return "Inserted!";
    }

    @GetMapping("/follow_me")
    public Map<String, List<String>> followMe() {
        System.out.println("Follow me");
        return beachcombService.getFollowMeCandidates();
    }

    @GetMapping("/test")
    public void test() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVin("123");
        vehicleDTO.setLocation(Arrays.asList(1.0, 2.0));
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
