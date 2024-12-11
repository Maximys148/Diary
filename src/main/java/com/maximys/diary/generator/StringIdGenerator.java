package com.maximys.diary.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StringIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        String newId = String.valueOf(Math.random());
        UUID uuid = UUID.nameUUIDFromBytes(newId.getBytes(StandardCharsets.UTF_8));
        return uuid.toString().toUpperCase();
    }
}

