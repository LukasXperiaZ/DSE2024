package dse.datafeeder.rabbitMq;

import dse.datafeeder.simulation.AutonomousVehicleSimulation;
import dse.datafeeder.simulation.NonAutonomousVehicleSimulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Configuration for the RabbitMQ connection.

    public static final String EXCHANGE_NAME = "control";
    private final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue autonomousVehicleQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue nonAutonomousVehicleQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingAutonomous(TopicExchange topicExchange, Queue autonomousVehicleQueue, AutonomousVehicleSimulation autonomousVehicleSimulation) {
        logger.debug("Binding autonomous vehicle queue to topic exchange.");
        return BindingBuilder.bind(autonomousVehicleQueue).to(topicExchange).with(autonomousVehicleSimulation.getVin());
    }

    @Bean
    public Binding bindingNonAutonomous(TopicExchange topicExchange, Queue nonAutonomousVehicleQueue, NonAutonomousVehicleSimulation nonAutonomousVehicleSimulation) {
        logger.debug("Binding non-autonomous vehicle queue to topic exchange.");
        return BindingBuilder.bind(nonAutonomousVehicleQueue).to(topicExchange).with(nonAutonomousVehicleSimulation.getVin());
    }

    @Bean
    public AutonomousVehicleSimulation autonomousVehicleSimulation() {
        logger.info("Initializing the state API. Autonomous vehicle simulation.");
        var autonomousVehicleSimulation = new AutonomousVehicleSimulation();
        autonomousVehicleSimulation.registerVehicle();
        autonomousVehicleSimulation.startSimulation();
        return autonomousVehicleSimulation;
    }

    @Bean
    public NonAutonomousVehicleSimulation nonAutonomousVehicleSimulation() {
        logger.info("Initializing the state API. Non-autonomous vehicle simulation.");
        var nonAutonomousVehicleSimulation = new NonAutonomousVehicleSimulation();
        nonAutonomousVehicleSimulation.registerVehicle();
        nonAutonomousVehicleSimulation.startSimulation();
        return nonAutonomousVehicleSimulation;
    }
}
