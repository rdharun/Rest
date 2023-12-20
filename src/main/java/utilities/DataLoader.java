package utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public class DataLoader {
    private static final String DATA_FOLDER = "data";
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Fetches data from a JSON file located in the resource/data directory.
     * @param fileName The name of the JSON file.
     * @param keyWithIndexPath The JSON key or path to navigate to.
     * @return The value from the JSON file.
     */
    public static String getData(String fileName, String keyWithIndexPath) {
        String filePath = DATA_FOLDER + "/" + fileName + ".json";
        try (InputStream inputStream = DataLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            JsonNode customRootNode = objectMapper.readTree(inputStream);
            String[] keyParts = keyWithIndexPath.split("\\[|\\]|\\."); // Split by "[" or "]", also split by "."

            JsonNode currentNode = customRootNode;
            for (String keyPart : keyParts) {
                if (!keyPart.isEmpty()) {
                    if (currentNode.isArray() && keyPart.matches("\\d+")) {
                        int index = Integer.parseInt(keyPart);
                        if (index >= 0 && index < currentNode.size()) {
                            currentNode = currentNode.get(index);
                        } else {
                            throw new IllegalArgumentException("Array index out of bounds");
                        }
                    } else {
                        currentNode = currentNode.path(keyPart);
                    }
                }
            }

            return currentNode.isValueNode() ? currentNode.asText() : currentNode.toPrettyString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}