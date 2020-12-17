package android.gaurav21.com.myapplication;

public class UserData {
    private static String name;
    private static String location;
    private static String city;
    private static Double longitude;
    private static Double latitude;

    public static Double getLongitude() {
        return longitude;
    }

    public static void setLongitude(Double longitude) {
        UserData.longitude = longitude;
    }

    public static Double getLatitude() {
        return latitude;
    }

    public static void setLatitude(Double latitude) {
        UserData.latitude = latitude;
    }

    public static String getName(){
        return name;
    }

    public static void setName(String val){
        name = val;
    }

    public static String getLocation() {
        return location;
    }

    public static void setLocation(String val) {
        location = val;
    }

    public static String getCity() {
        return city;
    }

    public static void setCity(String val) {
        city = val;
    }


}
