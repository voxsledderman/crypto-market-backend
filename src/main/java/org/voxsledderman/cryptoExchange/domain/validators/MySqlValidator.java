package org.voxsledderman.cryptoExchange.domain.validators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlValidator {

    public static String validateDatabaseName(String dbName){
        if(dbName == null || dbName.isBlank()){
            String defName = "crypto_exchange";
            log.error("Database name can not be blank! Trying to connect with default name [{}]", defName);
            return defName;
        }
        return dbName;
    }
    public static String validateHost(String host){
        if(host == null || host.isBlank()){
            String defHost = "localhost";
            log.error("Host name can not be blank! Trying to connect with default host name [{}]", defHost);
            return defHost;
        }
        return host;
    }
    public static String validateUser(String user){
        if(user == null || user.isBlank()){
            String defUser = "root";
            log.error("User name can not be blank! Trying to connect with default user name [{}]", defUser);
            return defUser;
        }
        return user;
    }
}