package dse.beachcombservice.mongodb.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehicles")
//@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = VehicleModel.class, name = "VehicleModel"),
//        @JsonSubTypes.Type(value = LeadingVehicleModel.class, name = "LeadingVehicleModel"),
//})
public interface IVehicleModel {
}
