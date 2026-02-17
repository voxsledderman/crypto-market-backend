package org.voxsledderman.cryptoExchange.infrastructure.config;

import com.j256.ormlite.support.ConnectionSource;
import lombok.extern.slf4j.Slf4j;
import org.voxsledderman.cryptoExchange.infrastructure.database.MySqlConnectionFactory;
import org.voxsledderman.cryptoExchange.infrastructure.database.SQLiteConnectionFactory;

import java.io.File;
import java.sql.SQLException;

@Slf4j
public class ApplicationBootstrap {

    private final ConfigManager config;
    private final File dataFolder;

    public ApplicationBootstrap(ConfigManager config, File dataFolder) {
        this.config = config;
        this.dataFolder = dataFolder;
    }

    public ConnectionSource connectToDB(){
        if(config.isMySqlEnabled()) {
            try {
                final MySqlConnectionFactory factory = new MySqlConnectionFactory(config.getMySqlConnectionData());
                return factory.getConnectionSource();
            } catch (SQLException e) {
                log.error("MySQL connection failed —> falling back to SQLite", e);
                log.info("Connecting to SQLite database instead");
               return connectToSqlite();
            }
        } else return connectToSqlite();
    }

    private ConnectionSource connectToSqlite() {
        if(!dataFolder.exists()) dataFolder.mkdirs();
        File dbFile = new File(dataFolder, "crypto_exchange.db");

        try {
            final SQLiteConnectionFactory factory = new SQLiteConnectionFactory(dbFile.getAbsolutePath());
            return factory.getConnectionSource();
        } catch (SQLException e) {
            log.error("Unable to connect to any database!");
            throw new InternalError();
        }
    }
}
