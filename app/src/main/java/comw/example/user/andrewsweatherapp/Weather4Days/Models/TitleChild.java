package comw.example.user.andrewsweatherapp.Weather4Days.Models;

public class TitleChild {

    public int w00, w03, w06, w09, w12, w15, w18, w21;

    private long childTime;
    private String childWeatherIcon, childDescription;
    private int childTemp;

    public TitleChild (long childTime, String childWeatherIcon, String childDescription, int childTemp){
        this.childTime = childTime;
        this.childWeatherIcon = childWeatherIcon;
        this.childDescription = childDescription;
        this.childTemp = childTemp;
    }

    public long getChildTime() {
        return childTime;
    }

    public void setChildTime(long childTime) {
        this.childTime = childTime;
    }

    public String getChildWeatherIcon() {
        return childWeatherIcon;
    }

    public void setChildWeatherIcon(String childWeatherIcon) {
        this.childWeatherIcon = childWeatherIcon;
    }

    public String getChildDescription() {
        return childDescription;
    }

    public void setChildDescription(String childDescription) {
        this.childDescription = childDescription;
    }

    public int getChildTemp() {
        return childTemp;
    }

    public void setChildTemp(int childTemp) {
        this.childTemp = childTemp;
    }
}
