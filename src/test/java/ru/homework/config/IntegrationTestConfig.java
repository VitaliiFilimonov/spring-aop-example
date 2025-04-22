package ru.homework.config;

import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.apache.commons.lang3.ArrayUtils.addAll;

public class IntegrationTestConfig extends SpringBootContextLoader {

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    private static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));

    static {
        postgreSQLContainer.start();
        kafka.start();
    }

    @Override
    protected String[] getInlinedProperties(MergedContextConfiguration mergedConfig) {
        return addAll(super.getInlinedProperties(mergedConfig), getTestPropertiesForContainers());
    }

    private String[] getTestPropertiesForContainers() {
        int databasePort = postgreSQLContainer.getMappedPort(5432);
        String databaseHost = postgreSQLContainer.getHost();
        String databaseUser = postgreSQLContainer.getUsername();
        String databasePassword = postgreSQLContainer.getPassword();
        String databaseName = postgreSQLContainer.getDatabaseName();
        String kafkaBootstrapServers = kafka.getBootstrapServers();

        return new String[]{
                String.format("spring.datasource.url=jdbc:postgresql://%s:%d/%s?prepareThreshold=0", databaseHost, databasePort, databaseName),
                String.format("spring.datasource.username=%s", databaseUser),
                String.format("spring.datasource.password=%s", databasePassword),
                String.format("spring.kafka.bootstrap-servers=%s", kafkaBootstrapServers)
        };
    }
}
