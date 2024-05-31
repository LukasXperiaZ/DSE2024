package dse.beachcombservice;

import dse.beachcombservice.exceptions.VehicleNotFoundException;
import dse.beachcombservice.mongodb.VehicleRepository;
import dse.beachcombservice.mongodb.models.IVehicleModel;
import dse.beachcombservice.mongodb.models.LeadingVehicleModel;
import dse.beachcombservice.mongodb.models.VehicleLocation;
import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Component;

import javax.ejb.Singleton;
import java.util.*;

@Singleton
@Component
public class BeachcombService {

    private final ModelMapper modelMapper = new ModelMapper();

    Logger logger = LoggerFactory.getLogger(BeachcombService.class);

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(VehicleDTO vehicleDTO) {
        logger.trace("Inserting vehicle into MongoDB!");
        LeadingVehicleModel vehicleModel = modelMapper.map(vehicleDTO, LeadingVehicleModel.class);
        vehicleModel.setLocation(List.of(vehicleDTO.getCoordinates().getLongitude(), vehicleDTO.getCoordinates().getLatitude()));
        vehicleRepository.insert(vehicleModel);
    }

    public Map<String, List<String>> getFollowMeCandidates() {
        logger.trace("Retrieving FollowMeCandidates from MongoDB!");
        var allVehicles = vehicleRepository.findNewestVehiclesGroupedByVin();
        Map<String, List<String>> followMeCandidates = new HashMap<>();
        for (IVehicleModel vehicle : allVehicles) {
            Point location = new Point(vehicle.getLocation().get(0), vehicle.getLocation().get(1));
            var byLocationNear = this.findVehiclesNearPoint(vehicle.getVin(), location.getX(), location.getY(), 0.2);
            if (!byLocationNear.isEmpty())
                followMeCandidates.put(vehicle.getVin(), byLocationNear.stream().map(IVehicleModel::getVin).toList());
        }
        return followMeCandidates;
    }

    public VehicleLocationDTO getVehicleLocationByVin(String vin) {
        logger.trace("Retrieving vehicle location by vin!");
        var vehicleLocation = vehicleRepository.findFirstByVinOrderByTimestampDesc(vin);
        if (vehicleLocation == null) throw new VehicleNotFoundException(vin);
        VehicleLocationDTO vehicleLocationDTO = new VehicleLocationDTO();
        vehicleLocationDTO.setVin(vehicleLocation.getVin());
        vehicleLocationDTO.setCoordinates(new Coordinates(vehicleLocation.getLocation().get(0), vehicleLocation.getLocation().get(1)));
        vehicleLocationDTO.setSpeed(vehicleLocation.getSpeed());
        vehicleLocationDTO.setTargetSpeed(vehicleLocation.getTargetSpeed());
        vehicleLocationDTO.setLane(vehicleLocation.getLane());
        vehicleLocationDTO.setTargetLane(vehicleLocation.getTargetLane());
        return vehicleLocationDTO;
    }

    public List<VehicleLocation> findVehiclesNearPoint(String vin, double longitude, double latitude, double maxDistance) {

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();

        // Calculate one second before the current time
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, -5);
        Date oneSecondBefore = calendar.getTime();

        // Calculate one second after the current time
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, 5);
        Date oneSecondAfter = calendar.getTime();

        // Step 1: Aggregate the most recent locations
        TypedAggregation<VehicleLocation> aggregation = Aggregation.newAggregation(
                VehicleLocation.class,
                Aggregation.match(Criteria.where("vin").ne(vin).and("timestamp").gte(oneSecondBefore).lte(oneSecondAfter)),
                Aggregation.sort(Sort.by(Sort.Order.desc("timestamp"))),
                Aggregation.group("vin")
                        .first("location").as("location")
                        .first("timestamp").as("timestamp"),
                Aggregation.project("location", "timestamp").and("vin").previousOperation()
        );

        var recentLocations = mongoTemplate.aggregate(aggregation, VehicleLocation.class, Document.class);
        if (recentLocations.getMappedResults().isEmpty()) return new ArrayList<>();
        // Step 2: Create a temporary collection and insert the results
        String collectionName = "recentVehicleLocations_" + UUID.randomUUID();
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.indexOps(collectionName)
                .ensureIndex(new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE));
        mongoTemplate.getCollection(collectionName).insertMany(recentLocations.getMappedResults());

        // Step 3: Perform the geoNear query on the recent locations
        NearQuery nearQuery = NearQuery.near(longitude, latitude, Metrics.KILOMETERS)
                .maxDistance(maxDistance);

        var geoNearResults = mongoTemplate.geoNear(nearQuery, VehicleLocation.class, collectionName);

        mongoTemplate.dropCollection(collectionName);

        return geoNearResults.getContent().stream().map(GeoResult::getContent).toList();
    }
}
