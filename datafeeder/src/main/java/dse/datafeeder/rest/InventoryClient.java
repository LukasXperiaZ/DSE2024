package dse.datafeeder.rest;

import dse.datafeeder.dto.RegisterCar;
import dse.datafeeder.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class InventoryClient {

    private final Logger logger = LoggerFactory.getLogger(InventoryClient.class);

    private final String baseUrl = "https://localhost:8001";

    private final RestClient client = RestClient.builder()
            .baseUrl(baseUrl)
            .build();

    public void registerCar(RegisterCar car) {
        logger.info("Register car: {}", car);
        String result = client.post()
                .uri("/register")
                .body(car)
                .accept(MediaType.APPLICATION_JSON)
                .exchange((request, response) -> {
                   if (response.getStatusCode().is4xxClientError()) {
                       throw new NotFoundException("Path "+ baseUrl + "'/register' not found!");
                   } else {
                       return response.toString();
                   }
                });

        logger.info(result);
    }
}
