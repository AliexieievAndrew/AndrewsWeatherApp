package comw.example.user.andrewsweatherapp.Weather4Days.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import comw.example.user.andrewsweatherapp.R;

public class TitleParentViewHolder extends ParentViewHolder {
    public TextView weekDay, weatherDay, weatherNight, weatherIcon, simpleDate;

    public TitleParentViewHolder(View itemView) {
        super(itemView);

        weekDay = (TextView) itemView.findViewById(R.id.weekDay);
        weatherDay = (TextView) itemView.findViewById(R.id.weatherDay);
        weatherNight = (TextView) itemView.findViewById(R.id.weatherNight);
        weatherIcon = (TextView) itemView.findViewById(R.id.weatherIcon);
        simpleDate = (TextView) itemView.findViewById(R.id.simpleDate);
    }
}
