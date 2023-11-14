package by.aurorasoft.updatesobserver.configuration;

import by.aurorasoft.updatesobserver.storage.ServerUpdateStorage;
import by.aurorasoft.updatesobserver.storage.factory.ServerUpdateStorageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerUpdateStorageConfiguration {

    @Bean
    public ServerUpdateStorage serverUpdateStorage(final ServerUpdateStorageFactory factory) {
        return factory.create();
    }

}
