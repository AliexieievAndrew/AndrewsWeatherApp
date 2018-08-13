package comw.example.user.andrewsweatherapp.Weather4Days.Models;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

public class TitleParent implements ParentObject {

    private List<Object> weatherChildrenList;
    private UUID _id;
    private String weekDay;
    private String simpleDate;
    private int weatherDay;
    private int weatherNight;
    private String weatherIcon;

    public TitleParent(String weekDay, String simpleDate, int weatherDay, int weatherNight, String weatherIcon) {

        this.weekDay = weekDay;
        this.weatherDay = weatherDay;
        this.weatherNight = weatherNight;
        this.weatherIcon = weatherIcon;
        this.simpleDate = simpleDate;

        _id = UUID.randomUUID();
    }

    public UUID get_id() {
        return _id;
    }

    public void set_id(UUID _id) {
        this._id = _id;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public int getWeatherDay() {
        return weatherDay;
    }

    public int getWeatherNight() {
        return weatherNight;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getSimpleDate() {
        return simpleDate;
    }
    @Override
    public List<Object> getChildObjectList() {
        return weatherChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        weatherChildrenList = list;
    }
}
