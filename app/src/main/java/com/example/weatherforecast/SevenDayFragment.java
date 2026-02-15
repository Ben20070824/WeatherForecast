package com.example.weatherforecast;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherforecast.adapter.SevenDayWeatherAdapter;
import com.example.weatherforecast.main.HomeFragment;
import com.example.weatherforecast.tool.SevenDayWeatherInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SevenDayFragment extends Fragment {

    private TextView tvCity, tvDate, tvWea, tvTemDay, tvTemNight, tvWin, tvWinSpeed;
    private RecyclerView rvWeather;
    private String mParam1;
    private String mParam2;
    private MyHandler mHandler=new MyHandler();
    private SevenDayWeatherAdapter mAdapter;

    public SevenDayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_seven_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        tvCity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"FZSTK.TTF"));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeFragment)getParentFragment()).setOnSearchListener(new HomeFragment.OnSearchListener() {
            @Override
            public void transmitData(String city) {
                requestWeather(city);
            }
        });
    }

    private void requestWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://v1.yiketianqi.com/free/week?appid=47517298&appsecret=Ap7TYeAW&unescape=1&city=" + city);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    String result = streamToString(inputStream);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private void initView(View view) {
        tvCity = view.findViewById(R.id.tv_city);
        tvDate = view.findViewById(R.id.tv_date);
        tvWea = view.findViewById(R.id.wea);
        tvTemDay = view.findViewById(R.id.tem_day);
        tvTemNight = view.findViewById(R.id.tem_night);
        tvWin = view.findViewById(R.id.win);
        tvWinSpeed = view.findViewById(R.id.win_speed);
        rvWeather = view.findViewById(R.id.rv_weather);
    }

    private String streamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
        }
        reader.close();
        return stringBuilder.toString();
    }

    private SevenDayWeatherInfo decodeJson(String json) {
        String city = "";
        List<SevenDayWeatherInfo.WeatherData> WeatherDataList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);

            city = root.getString("city");

            JSONArray dataArray = root.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dayData = dataArray.getJSONObject(i);
                String date = dayData.getString("date");
                String wea = dayData.getString("wea");
                String tem_day = dayData.getString("tem_day");
                String tem_night = dayData.getString("tem_night");
                String win = dayData.getString("win");
                String win_speed = dayData.getString("win_speed");
                WeatherDataList.add(new SevenDayWeatherInfo.WeatherData(date, wea, tem_day, tem_night, win, win_speed));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SevenDayWeatherInfo(city, WeatherDataList);
    }

    private void setText(SevenDayWeatherInfo sevenDayWeatherInfo) {
        tvCity.setText(sevenDayWeatherInfo.getCity());
        mAdapter=new SevenDayWeatherAdapter(sevenDayWeatherInfo.getData());
        rvWeather.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvWeather.setAdapter(mAdapter);
    }
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String responseData = msg.obj.toString();
            setText(decodeJson(responseData));
        }
    }
}