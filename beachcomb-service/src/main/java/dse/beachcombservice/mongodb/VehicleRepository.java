package dse.beachcombservice.mongodb;

import dse.beachcombservice.mongodb.models.IVehicleModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends MongoRepository<IVehicleModel, String> {
}
