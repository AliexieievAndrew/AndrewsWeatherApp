package comw.example.user.andrewsweatherapp.Weather4Days.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import comw.example.user.andrewsweatherapp.R;
import comw.example.user.andrewsweatherapp.Weather4Days.Models.TitleChild;
import comw.example.user.andrewsweatherapp.Weather4Days.Models.TitleParent;
import comw.example.user.andrewsweatherapp.Weather4Days.ViewHolders.TitleChildViewHolder;
import comw.example.user.andrewsweatherapp.Weather4Days.ViewHolders.TitleParentViewHolder;

public class WeatherExpandableRecyclerViewAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    LayoutInflater inflater;

    // assets/font/weather.tiff
    Typeface weatherFont;

    // assets/font/Lato-Regular.ttf
    Typeface descriptionFont;

    Context mContext;

    public WeatherExpandableRecyclerViewAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        mContext = context;

        weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/weather.ttf");
        descriptionFont = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.expandable_recycler_view_weather_four_days_parent,viewGroup,false);

        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.expandable_recycler_view_weather_four_days_child,viewGroup, false);

        return new TitleChildViewHolder(view);
    }

    // set text parent
    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent titleParent = (TitleParent) o;

        titleParentViewHolder.weatherIcon.setTypeface(weatherFont);
        titleParentViewHolder.weekDay.setTypeface(descriptionFont);
        titleParentViewHolder.simpleDate.setTypeface(descriptionFont);
        titleParentViewHolder.weatherNight.setTypeface(descriptionFont);
        titleParentViewHolder.weatherDay.setTypeface(descriptionFont);

        titleParentViewHolder.weekDay.setText(String.valueOf(titleParent.getWeekDay()));
        titleParentViewHolder.simpleDate.setText(String.valueOf(titleParent.getSimpleDate()));
        titleParentViewHolder.weatherIcon.setText(String.valueOf(titleParent.getWeatherIcon()));
        titleParentViewHolder.weatherNight.setText(String.valueOf(titleParent.getWeatherNight()) + "°");
        titleParentViewHolder.weatherDay.setText(String.valueOf(titleParent.getWeatherDay()) + "°");

    }
    // set text child
    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        TitleChild titleChild = (TitleChild) o;

        titleChildViewHolder.textChildWeatherIcon.setTypeface(weatherFont);

        titleChildViewHolder.textChildTime.setText(
                getTime(titleChild.getChildTime()));
        titleChildViewHolder.textChildWeatherIcon.setText(
                String.valueOf(titleChild.getChildWeatherIcon()));
        titleChildViewHolder.textChildDescription.setText(
                String.valueOf(titleChild.getChildDescription()));
        titleChildViewHolder.textChildTemp.setText(
                String.valueOf(titleChild.getChildTemp()) + "°");
    }
    private String getTime(long date) {
        DateFormat df = new SimpleDateFormat("HH.mm");
        return df.format(new Date(date *1000));
    }
}
