package com.berenberg.library.config;

import com.berenberg.library.model.ItemCopy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ItemCopySerializer extends JsonSerializer<ItemCopy> {
    @Override
    public void serialize(ItemCopy itemCopy, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("uniqueID", itemCopy.getUniqueID());

        if (itemCopy.getReturnDate() != null) {
            jsonGenerator.writeObjectField("returnDate", itemCopy.getReturnDate());
        }

        jsonGenerator.writeObjectField("isAvailable", itemCopy.isAvailable());

        if (itemCopy.getBorrowedByUserId() != 0) {
            jsonGenerator.writeObjectField("borrowedByUsedId", itemCopy.getBorrowedByUserId());
        }

        jsonGenerator.writeEndObject();
    }
}

