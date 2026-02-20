package com.example.weatherforecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.tool.ManyDayWeather;
import com.example.weatherforecast.tool.SevenDayWeather;

import java.util.List;

public class ManyDayWeatherAdapter extends RecyclerView.Adapter<ManyDayWeatherAdapter.MyHolder> {
    private List<ManyDayWeather.WeatherInfo> list;
    public ManyDayWeatherAdapter(List<ManyDayWeather.WeatherInfo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_many, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ManyDayWeather.WeatherInfo weatherInfo = list.get(position);
        holder.tvDate.setText(weatherInfo.getDate());
        holder.tvWeaDay.setText(weatherInfo.getWeaDay());
        holder.tvWeaNight.setText(weatherInfo.getWeaNight());
        holder.tvTemDay.setText(weatherInfo.getTem_high()+"℃");
        holder.tvTemNight.setText(weatherInfo.getTem_low()+"℃");
        holder.tvWin.setText(weatherInfo.getWin());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvWeaDay, tvWeaNight, tvTemDay, tvTemNight, tvWin;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWeaDay = itemView.findViewById(R.id.tv_wea_day);
            tvWeaNight = itemView.findViewById(R.id.tv_wea_night);
            tvTemDay = itemView.findViewById(R.id.tv_tem_day);
            tvTemNight = itemView.findViewById(R.id.tv_tem_night);
            tvWin = itemView.findViewById(R.id.tv_win);
        }
    }
    public void setData(List<ManyDayWeather.WeatherInfo> newData) {
        this.list.clear();
        this.list.addAll(newData);
    }
}