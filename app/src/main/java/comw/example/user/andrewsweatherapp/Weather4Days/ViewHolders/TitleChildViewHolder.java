package comw.example.user.andrewsweatherapp.Weather4Days.ViewHolders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import comw.example.user.andrewsweatherapp.R;

public class TitleChildViewHolder extends ChildViewHolder {

    public TextView textChildTime, textChildWeatherIcon, textChildDescription,
            textChildTemp;

    public TitleChildViewHolder(View itemView) {
        super(itemView);

        textChildTime = (TextView) itemView.findViewById(R.id.textChildTime);
        textChildWeatherIcon = (TextView) itemView.findViewById(R.id.textChildWeatherIcon);
        textChildDescription = (TextView) itemView.findViewById(R.id.textChildDescription);
        textChildTemp = (TextView) itemView.findViewById(R.id.textChildTemp);
    }
}
