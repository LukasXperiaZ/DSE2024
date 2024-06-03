package dse.beachcombservice.mongodb;

import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import dse.beachcombservice.mongodb.models.VehicleControlLocation;
import dse.beachcombservice.mongodb.models.VehicleLocation;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<IVehicleModel, String> {

    // Find the newest entry for all vehicles
    @Aggregation(pipeline = {
            "{ '$match': { 'vin': { '$ne': null } } }",
            "{ '$sort': { 'timestamp': -1 } }",
            "{ '$group': { '_id': '$vin', 'location': { '$first': '$location' }, 'timestamp': { '$first': '$timestamp' }, 'vin': { '$first': '$vin' } } }",
            "{ '$project': { '_id': 0, 'vin': 1, 'location': 1, 'timestamp': 1 } }"
    })
    List<VehicleLocation> findNewestVehiclesGroupedByVin();

    // Find the newest entry for a specific vehicle
    VehicleControlLocation findFirstByVinOrderByTimestampDesc(String vin);
}
