package dse.beachcombservice;

import dse.beachcombservice.mongodb.VehicleRepository;
import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;

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
}
