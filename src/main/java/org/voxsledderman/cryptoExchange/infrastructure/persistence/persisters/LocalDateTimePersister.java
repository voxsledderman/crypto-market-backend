package org.voxsledderman.cryptoExchange.infrastructure.persistence.persisters;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimePersister extends StringType {

    private static final LocalDateTimePersister instance = new LocalDateTimePersister();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private LocalDateTimePersister() {
        super(SqlType.STRING, new Class<?>[]{ LocalDateTime.class });
    }

    public static LocalDateTimePersister getSingleton() {
        return instance;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return formatter.format((LocalDateTime) javaObject);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return LocalDateTime.parse((String) sqlArg, formatter);
    }
}
