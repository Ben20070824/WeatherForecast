package com.example.weatherforecast;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


public class HomeFragment extends Fragment {
    private EditText etSearch;
    private TextView tvCity;
    private RecyclerView rvWeather;
    private Button btnSearch;
    private SevenDayWeatherAdapter mAdapter;
    private MyHandler mHandler=new MyHandler();


    public HomeFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initView(View view) {
        etSearch = view.findViewById(R.id.et_search);
        rvWeather = view.findViewById(R.id.rv_weather);
        btnSearch = view.findViewById(R.id.btn_search);
        tvCity = view.findViewById(R.id.tv_city);
    }

    private void initEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://v1.yiketianqi.com/free/week?appid=47517298&appsecret=Ap7TYeAW&unescape=1&city=西安");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    connection.connect();
                    // 4. 将输入流转换为字符串
                    InputStream inputStream = connection.getInputStream();
                    String result = streamToString(inputStream);
                    // 5. 通过Handler将结果发送回主线程
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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            String city;

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"获取天气中",Toast.LENGTH_SHORT).show();
                 city= etSearch.getText().toString();
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
                            // 4. 将输入流转换为字符串
                            InputStream inputStream = connection.getInputStream();
                            String result = streamToString(inputStream);
                            // 5. 通过Handler将结果发送回主线程
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
        });
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

            // 解析基本信息
            city = root.getString("city");

            // 解析天气数据数组
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
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String responseData = msg.obj.toString();
            setText(decodeJson(responseData));
        }
    }
}