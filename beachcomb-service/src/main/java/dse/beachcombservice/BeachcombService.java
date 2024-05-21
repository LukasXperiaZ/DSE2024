package dse.beachcombservice;

import dse.beachcombservice.mongodb.VehicleRepository;
import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import dse.beachcombservice.mongodb.models.VehicleLocation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
@Component
public class BeachcombService {

    private final ModelMapper modelMapper = new ModelMapper();

    Logger logger = LoggerFactory.getLogger(BeachcombService.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    public void insert(VehicleDTO vehicleDTO) {
        logger.trace("Inserting vehicle into MongoDB!");
        LeadingVehicleModel vehicleModel = modelMapper.map(vehicleDTO, LeadingVehicleModel.class);
        vehicleRepository.insert(vehicleModel);
    }

    public Map<String, List<String>> getFollowMeCandidates() {
        logger.trace("Retrieving FollowMeCandidates from MongoDB!");
        //TODO: Maybe change this if we consider storing all data and not only the newest location of a vehicle
        var allVehicles = vehicleRepository.findFirstByVinIsNotNullOrderByTimestampDesc();
        Map<String, List<String>> followMeCandidates = new HashMap<>();
        for (IVehicleModel vehicle : allVehicles) {
            Point location = new Point(vehicle.getLocation().get(0), vehicle.getLocation().get(1));
            List<LeadingVehicleModel> byLocationNear = vehicleRepository.findByLocationNear(vehicle.getVin(), location.getX(), location.getY(), 0.2);
            followMeCandidates.put(vehicle.getVin(), byLocationNear.stream().map(IVehicleModel::getVin).toList());
        }
        return followMeCandidates;
    }

    public VehicleLocation getVehicleLocationByVin(String vin) {
        logger.trace("Retrieving vehicle location by vin!");
        var vehicleLocation = vehicleRepository.findFirstByVinOrderByTimestampDesc(vin);
        return vehicleLocation.get(0);
        if (vehicleLocation == null) throw new VehicleNotFoundException(vin);
    }
}
