package dse.beachcombservice.mongodb;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
@DependsOn("mongoTemplate")
public class MongoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Create Indexes for the MongoDB
    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps("vehicles").ensureIndex(
                new GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE)
        );
        mongoTemplate.indexOps("vehicles").ensureIndex(
                new Index().on("vin", Sort.Direction.ASC)
        );
        mongoTemplate.indexOps("vehicles").ensureIndex(
                new Index().on("timestamp", Sort.Direction.ASC)
        );
    }
}
