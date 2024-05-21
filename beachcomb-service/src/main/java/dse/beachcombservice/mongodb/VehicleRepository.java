package dse.beachcombservice.mongodb;

import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import dse.beachcombservice.mongodb.models.VehicleLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<IVehicleModel, String> {

    @Query("{'vin': {$ne: ?0}, 'location': {$near: {$geometry: {type: 'Point', coordinates: [?1, ?2]}, $maxDistance: ?3}}}")
    List<LeadingVehicleModel> findByLocationNear(String vin, double longitude, double latitude, double maxDistance);

    List<VehicleLocation> findFirstByVinIsNotNullOrderByTimestampDesc();

    List<VehicleLocation> findFirstByVinOrderByTimestampDesc(String vin);
}
