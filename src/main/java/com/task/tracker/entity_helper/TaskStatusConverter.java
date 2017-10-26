package com.task.tracker.entity_helper;


import com.task.tracker.model.Task;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TaskStatusConverter implements AttributeConverter<Task.Status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Task.Status attribute) {
        return attribute.getCode();
    }

    @Override
    public Task.Status convertToEntityAttribute(Integer dbData) {
        return Task.Status.findByCode(dbData);
    }
}
