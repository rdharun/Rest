package utilities;

import java.util.Random;
import java.util.UUID;

public class RandomEmailGenerator {

    public static String generateRandomEmail() {
        Random random = new Random();
        int randomNumber = random.nextInt(90000) + 10000; // Generates a random 5-digit number

        return "test" + randomNumber + "@example.com";
    }

    public static String generateRandomEmailId() {
        return UUID.randomUUID().toString() + "@gmail.com";
    }
}
