package dse.datafeeder.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("state")
public class StateAPI {

    @GetMapping("/get-project-name")
    public String getProjectName() {
        return "Datafeeder";
    }
}
