package org.voxsledderman.cryptoExchange.infrastructure.config;

public record MySqlConnectionData(String host, int port, String database, String username, String password, int maxConnections) {
}
