package dse.beachcombservice.mongodb.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "vehicles")
//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = VehicleModel.class, name = "VehicleModel"),
//        @JsonSubTypes.Type(value = LeadingVehicleModel.class, name = "LeadingVehicleModel"),
//})
public interface IVehicleModel {
    String getVin();

    List<Double> getLocation();
}
