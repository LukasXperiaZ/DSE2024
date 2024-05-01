package dse.datafeeder;

import dse.datafeeder.dto.Coordinates;
import dse.datafeeder.dto.VehicleData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
class DatafeederApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testVehicleDataCreation() {
        try {
            VehicleData vehicleData = new VehicleData("SB164ABN1PE082986", new Coordinates(1, 2), 100.0, 1, new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            assert false;
        }
        assert true;
    }

}
