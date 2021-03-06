package org.dynjs.debugger.agent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dynjs.debugger.Debugger;
import org.dynjs.runtime.JSObject;
import org.dynjs.runtime.NameEnumerator;
import org.dynjs.runtime.PropertyDescriptor;
import org.dynjs.runtime.Types;

import java.io.IOException;

/**
 * @author Bob McWhirter
 */
public class HandleSerializer extends JsonSerializer<Object> {

    private Debugger debugger;

    HandleSerializer(Debugger debugger) {
        super();
        this.debugger = debugger;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        serializeBody(value, jgen, provider);
        jgen.writeEndObject();
    }

    public void serializeAsMapEntry(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int ref = this.debugger.getReferenceManager().getReference(value);
        jgen.writeFieldName( "" + ref );
        serialize( value, jgen, provider );
    }

    public void serializeBody(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        int ref = this.debugger.getReferenceManager().getReference(value);
        jgen.writeNumberField("handle", ref);

        if (value == Types.UNDEFINED) {
            jgen.writeStringField("type", "undefined");
        } else if (value == Types.NULL) {
            jgen.writeStringField("type", "null");
        } else if (value instanceof Boolean) {
            jgen.writeStringField("type", "boolean");
            jgen.writeBooleanField("value", (Boolean) value);
        } else if (value instanceof Double) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField("value", (Double) value);
        } else if (value instanceof Long) {
            jgen.writeStringField("type", "number");
            jgen.writeNumberField("value", (Long) value);
        } else if (value instanceof String) {
            jgen.writeStringField("type", "string");
            jgen.writeStringField("value", (String) value);
        } else if (value instanceof JSObject) {
            jgen.writeStringField("type", "object");
            serializeJSObject((JSObject) value, jgen, provider);
        }
    }

    private void serializeJSObject(JSObject result, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeFieldName("properties");

        jgen.writeStartArray();

        NameEnumerator enumerator = result.getAllEnumerablePropertyNames();

        while (enumerator.hasNext()) {
            String name = enumerator.next();
            Object propResult = result.getProperty(null, name);

            if (propResult == Types.UNDEFINED) {
                jgen.writeStartObject();
                jgen.writeStringField("name", name);
                jgen.writeNumberField("ref", this.debugger.getReferenceManager().getReference(Types.UNDEFINED));
                jgen.writeEndObject();
            } else {
                PropertyDescriptor prop = (PropertyDescriptor) propResult;

                if (prop.hasValue()) {

                    Object value = prop.getValue();
                    jgen.writeStartObject();
                    jgen.writeStringField("name", name);
                    jgen.writeNumberField("ref", this.debugger.getReferenceManager().getReference(prop.getValue()));

                    if (value == Types.UNDEFINED) {
                        jgen.writeStringField("type", "undefined");
                    } else if (value == Types.NULL) {
                        jgen.writeStringField("type", "null");
                    } else if (value instanceof Boolean) {
                        jgen.writeStringField("type", "boolean");
                        jgen.writeBooleanField("value", (Boolean) value);
                    } else if (value instanceof Double) {
                        jgen.writeStringField("type", "number");
                        jgen.writeNumberField("value", (Double) value);
                    } else if (value instanceof Long) {
                        jgen.writeStringField("type", "number");
                        jgen.writeNumberField("value", (Long) value);
                    } else if (value instanceof String) {
                        jgen.writeStringField("type", "string");
                        jgen.writeStringField("value", (String) value);
                    }
                    jgen.writeEndObject();

                } else {
                    // WHAT?
                }
            }
        }

        jgen.writeEndArray();

    }


}
