package by.aurorasoft.outagetracker.configuration;

import by.aurorasoft.outagetracker.util.FileUtil.FilePath;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server-updates.file")
@NoArgsConstructor
public class ServerOutageFilePath extends FilePath {
    public ServerOutageFilePath(String directoryPath, String fileName) {
        super(directoryPath, fileName);
    }
}
