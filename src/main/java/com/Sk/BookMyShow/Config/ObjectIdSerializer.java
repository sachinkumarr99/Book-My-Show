package com.Sk.BookMyShow.Config;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import org.bson.types.ObjectId;
import org.springframework.boot.jackson.JacksonComponent;


@JacksonComponent 
public class ObjectIdSerializer extends ValueSerializer<ObjectId> {

      @Override
    public void serialize(ObjectId value, JsonGenerator gen, SerializationContext ctxt) {
        gen.writeString(value.toHexString());
    }
}
