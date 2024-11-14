package com.banbta.bbogcatrlaopenaibacktesting.common;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDate;

public class LocalDateConverter implements AttributeConverter<LocalDate> {

    @Override
    public AttributeValue transformFrom(LocalDate input) {
        return AttributeValue.builder().s(input.toString()).build();
    }

    @Override
    public LocalDate transformTo(AttributeValue attributeValue) {
        return LocalDate.parse(attributeValue.s());
    }

    @Override
    public EnhancedType<LocalDate> type() {
        return EnhancedType.of(LocalDate.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
