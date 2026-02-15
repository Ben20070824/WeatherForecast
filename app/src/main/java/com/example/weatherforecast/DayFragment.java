package com.example.weatherforecast;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.main.HomeFragment;
import com.example.weatherforecast.tool.DayWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DayFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView tvCity, tvDate, tvWea, tvTemHigh, tvTemLow, tvWin, tvWinSpeed, tvWinMeter, tvAir, tvPressure, tvHumidity;
    private MyHandler mHandler=new MyHandler();

    public DayFragment() {
    }

    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment parentFragment = getParentFragment();
        if (parentFragment == null) {
            Toast.makeText(getContext(), "未找到父Fragment", Toast.LENGTH_SHORT).show();
            return;
        }

        if (parentFragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) parentFragment;
            homeFragment.setOnSearchListener(new HomeFragment.OnSearchListener() {
                @Override
                public void transmitData(String city) {
                    requestWeather(city);
                }
            });
        }
    }

    private void requestWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection  connection=null;
                try {
                    URL url=new URL("http://v1.yiketianqi.com/free/day?appid=47517298&appsecret=Ap7TYeAW&unescape=1&city="+city);
                    connection=(HttpURLConnection) url.openConnection();
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
                }finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    private void initView(View view) {
        tvCity = view.findViewById(R.id.tv_city);
        tvDate = view.findViewById(R.id.tv_date);
        tvWea = view.findViewById(R.id.tv_wea);
        tvTemHigh = view.findViewById(R.id.tv_tem_high);
        tvTemLow = view.findViewById(R.id.tv_tem_low);
        tvWin = view.findViewById(R.id.tv_win);
        tvWinSpeed = view.findViewById(R.id.tv_win_speed);
        tvWinMeter = view.findViewById(R.id.tv_win_meter);
        tvAir = view.findViewById(R.id.tv_air);
        tvPressure = view.findViewById(R.id.tv_pressure);
        tvHumidity = view.findViewById(R.id.tv_humidity);
    }

    private void initEvent() {

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
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String responseData = msg.obj.toString();
            setText(decodeJson(responseData));
        }
    }

    private void setText(DayWeather dayWeather) {
        tvCity.setText(dayWeather.getCity());
        tvDate.setText(dayWeather.getDate());
        tvWea.setText("天气"+dayWeather.getWea());
        tvTemHigh.setText("高温："+dayWeather.getTem_high());
        tvTemLow.setText("低温："+dayWeather.getTem_low());
        tvWin.setText("风向："+dayWeather.getWin());
        tvWinSpeed.setText("风级："+dayWeather.getWin_speed());
        tvWinMeter.setText("风速："+dayWeather.getWin_meter());
        tvAir.setText("空气质量："+dayWeather.getAir());
        tvPressure.setText("气压："+dayWeather.getPressure());
        tvHumidity.setText("湿度："+dayWeather.getHumidity());
    }

    private DayWeather decodeJson(String responseData) {
            DayWeather dayWeather = null;

            if (responseData == null || responseData.isEmpty()) {
                return null;
            }

            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String city = jsonObject.optString("city", "");
                String date = jsonObject.optString("date", "");
                String wea = jsonObject.optString("wea", "");
                String temHigh = jsonObject.optString("tem_day", "");
                String temLow = jsonObject.optString("tem_night", "");
                String win = jsonObject.optString("win", "");
                String winSpeed = jsonObject.optString("win_speed", "");
                String winMeter = jsonObject.optString("win_meter", "");
                String air = jsonObject.optString("air", "");
                String pressure = jsonObject.optString("pressure", "");
                String humidity = jsonObject.optString("humidity", "");

                    dayWeather = new DayWeather(city, date, wea, temHigh, temLow, win, winSpeed, winMeter, air, pressure, humidity);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dayWeather;
        }

}