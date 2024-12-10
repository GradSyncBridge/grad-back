package backend.config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@Component
public class DataSourceInitializer {

    private final Logger logger = Logger.getLogger(DataSourceInitializer.class.getName());

    private final DataSource dataSource;

    public DataSourceInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void init() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connection established: " + connection.getMetaData().getDatabaseProductName());
        }
    }
}

