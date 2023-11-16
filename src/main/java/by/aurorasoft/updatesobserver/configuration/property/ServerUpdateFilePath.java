package by.aurorasoft.updatesobserver.configuration.property;

import by.aurorasoft.updatesobserver.util.FileUtil.FilePath;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server-updates.file")
public class ServerUpdateFilePath extends FilePath {

}
