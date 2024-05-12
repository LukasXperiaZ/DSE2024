package dse.beachcombservice;

import dse.beachcombservice.mongodb.VehicleRepository;
import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Component
public class BeachcombService {

    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private VehicleRepository vehicleRepository;

    public void insert(VehicleDTO vehicleDTO) {
        IVehicleModel vehicleModel = modelMapper.map(vehicleDTO, LeadingVehicleModel.class);
        vehicleRepository.insert(vehicleModel);
    }

    public Map<String, List<String>> getFollowMeCandidates() {
        //TODO: Add correct implementation
        var allVehicles = vehicleRepository.findAll();
        Map<String, List<String>> followMeCandidates = new HashMap<>();
        for (IVehicleModel vehicle : allVehicles) {
            LeadingVehicleModel vehicleModel = (LeadingVehicleModel) vehicle;
            followMeCandidates.put(vehicleModel.getVin(), List.of("1", "2", "3"));
        }
        return followMeCandidates;
    }
}
