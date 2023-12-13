package by.aurorasoft.outagetracker.configuration;

import by.aurorasoft.outagetracker.storage.ServerOutageStorage;
import by.aurorasoft.outagetracker.storage.factory.ServerOutageStorageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerOutageStorageConfiguration {

    @Bean
    public ServerOutageStorage serverOutageStorage(ServerOutageStorageFactory factory) {
        return factory.create();
    }
}
