package dk.bearsoft.scaries;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bearsoft.scaries.model.ScaryMessage;
import java.io.IOException;

public class TestHelper {
    public String toJson(ScaryMessage message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(message);
    }
}
