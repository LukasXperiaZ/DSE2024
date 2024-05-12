package dse.beachcombservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class BeachcombServiceResource {

    @Autowired
    private BeachcombService beachcombService;

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

}
