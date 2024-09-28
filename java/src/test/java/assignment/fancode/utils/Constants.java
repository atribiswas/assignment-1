package assignment.fancode.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.testng.Assert;

public class Constants {
    public static String urlHost;
    public static Double threshold;
    public static String[] cities;

    static {
        try (InputStream input = Constants.class.getClassLoader()
                .getResourceAsStream("test.properties")) {

            Properties properties = new Properties();
            Assert.assertNotNull(input, "unable to find application.properties");
            properties.load(input);

            urlHost = properties.getProperty("urlHost");
            threshold = Double.parseDouble(properties.getProperty("threshold", "0.5"));
            cities = properties.getProperty("cities").split(",");

            LoggerSingleton.logger.info("properties loaded successfully.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String whatAreMyConstants() {
        return String.format("Properties are {URL Host: %s, Threshold: %s, Cities: %s}", urlHost,
                threshold.toString(),
                Arrays.toString(cities));
    }
}
