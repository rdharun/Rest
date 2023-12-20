package utilities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
public class EndpointConfig {
    private static final String CONFIG_FILE = "endpoints.json";
    private static JsonNode jsonNode;
    static {
        try (InputStream inputStream = EndpointConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEndpoint(String key, String property) {
        JsonNode endpointNode = jsonNode.path(key).path(property);
        return endpointNode.asText();
    }


}
