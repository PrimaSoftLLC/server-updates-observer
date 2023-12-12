package by.aurorasoft.updatesobserver.configuration;

import by.aurorasoft.updatesobserver.util.FileUtil.FilePath;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "server-updates.file")
@NoArgsConstructor
public class ServerUpdateFilePath extends FilePath {
    public ServerUpdateFilePath(final String directoryPath, final String fileName) {
        super(directoryPath, fileName);
    }

}
