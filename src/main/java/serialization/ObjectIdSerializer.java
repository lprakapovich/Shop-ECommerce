package serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;

public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
    @Override
    public void serialize(ObjectId o, JsonGenerator j, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if(o == null) {
            j.writeNull();
        } else {
            j.writeString(o.toString());
        }
    }
}
