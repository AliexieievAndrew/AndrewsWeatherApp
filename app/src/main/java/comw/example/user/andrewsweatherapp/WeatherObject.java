package comw.example.user.andrewsweatherapp;


/*
 * Class, stores information about the weather at a certain point in time
 */
public class WeatherObject {
    private long dateTime;
    private int temp;
    private int weatherId;
    private int humidity;
    private String weatherMain;
    private String weatherDescription;

    public WeatherObject(long dateTime, int temp, int weatherId,
                         int humidity, String weatherMain, String weatherDescription) {
        this.dateTime = dateTime;
        this.temp = temp;
        this.weatherId = weatherId;
        this.humidity = humidity;
        this.weatherMain = weatherMain;
        this.weatherDescription = weatherDescription;
    }

    public long getDateTime() {
        return dateTime;
    }

    public int getTemp() {
        return temp;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    @Override
    public String toString() {
        return "WeatherObjectData{" +
                "dateTime=" + dateTime +
                ", temp=" + temp +
                ", weatherId=" + weatherId +
                ", humidity=" + humidity +
                ", weatherMain='" + weatherMain + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                '}';
    }
}
