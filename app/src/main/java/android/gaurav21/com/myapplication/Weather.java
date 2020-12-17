package android.gaurav21.com.myapplication;

public class Weather {
    private static String description;
    private static Double temperature;
    private static Double wind;
    private static Double humidity;
    private static Double pressure;

    public static String getDescription() {
        return description;
    }

    public static Double getTemperature() {
        return temperature;
    }

    public static Double getWind() {
        return wind;
    }

    public static Double getHumidity() {
        return humidity;
    }

    public static Double getPressure() {
        return pressure;
    }

    public static void setDescription(String description) {
        Weather.description = description;
    }

    public static void setTemperature(Double temperature) {
        Weather.temperature = temperature;
    }

    public static void setWind(Double wind) {
        Weather.wind = wind;
    }

    public static void setHumidity(Double humidity) {
        Weather.humidity = humidity;
    }

    public static void setPressure(Double pressure) {
        Weather.pressure = pressure;
    }
}
