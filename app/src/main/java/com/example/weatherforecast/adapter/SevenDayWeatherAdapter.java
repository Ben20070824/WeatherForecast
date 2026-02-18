package com.example.weatherforecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.tool.SevenDayWeather;

import java.util.List;

public class SevenDayWeatherAdapter extends RecyclerView.Adapter<SevenDayWeatherAdapter.Holder> {
    List<SevenDayWeather.WeatherData> list;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seven, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvWea.setText(list.get(position).getWea());
        holder.tvTemDay.setText(list.get(position).getTemDay());
        holder.tvTemNight.setText(list.get(position).getTemNight());
        holder.tvWin.setText(list.get(position).getWin());
        holder.tvWinSpeed.setText(list.get(position).getWinSpeed());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SevenDayWeatherAdapter(List<SevenDayWeather.WeatherData> list) {
        this.list = list;
    }

    public static class Holder extends RecyclerView.ViewHolder {
    TextView tvDate, tvWea, tvTemDay, tvTemNight, tvWin, tvWinSpeed;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWea = itemView.findViewById(R.id.wea);
            tvTemDay = itemView.findViewById(R.id.tem_day);
            tvTemNight = itemView.findViewById(R.id.tem_night);
            tvWin = itemView.findViewById(R.id.win);
            tvWinSpeed = itemView.findViewById(R.id.win_speed);
        }
    }
}
