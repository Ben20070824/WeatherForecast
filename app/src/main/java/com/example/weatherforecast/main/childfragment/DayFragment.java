package com.example.weatherforecast.main.childfragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.tool.DayWeather;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


public class DayFragment extends Fragment {
    private TextView tvCity, tvDate, tvWea, tvTemHigh, tvTemLow, tvWin, tvWinSpeed, tvWinMeter, tvAir, tvPressure, tvHumidity;
    private MyHandler mHandler = new MyHandler(Looper.getMainLooper(),this);

    public DayFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        tvCity.setTypeface(Typeface.createFromAsset(getParentFragment().getActivity().getAssets(),"FZSTK.TTF"));
    }

    public void requestWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://v1.yiketianqi.com/free/day?appid=47517298&appsecret=Ap7TYeAW&unescape=1&city=" + city);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    connection.connect();
                    InputStream inputStream = connection.getInputStream();

                    String result = streamToString(inputStream);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = result;

                    // Message target目标Handler Message -> Handler
                    // -> Looper -> Message Queue -> Message -> Handler ->
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

    private static class MyHandler extends Handler {
        // GC 回收 可达性判断 GO Root -> ..... -> Fragment
        // MyHandler ->DayFragment
        // GCRoot -> Looper -> MessageQueue -> Message -> Handeler -> Fragment
        // 内存泄漏 ：长生命周期对象 持有 短生命周期对象 的强引用

        private WeakReference<DayFragment> fragmentRef;

        public MyHandler(@NonNull Looper looper, DayFragment fragment) {
            super(looper);
            this.fragmentRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            String responseData = msg.obj.toString();
            fragmentRef.get().setText(decodeJson(responseData));
        }
    }
    //根据数据更新控件
    private void setText(DayWeather dayWeather) {
        tvCity.setText(dayWeather.getCity());
        tvDate.setText(dayWeather.getDate());
        tvWea.setText( dayWeather.getWea());
        tvTemHigh.setText(dayWeather.getTem_high()+"℃");
        tvTemLow.setText(dayWeather.getTem_low()+"℃");
        tvWin.setText(dayWeather.getWin());
        tvWinSpeed.setText(dayWeather.getWin_speed());
        tvWinMeter.setText("风速:"+dayWeather.getWin_meter());
        tvAir.setText(dayWeather.getAir());
        tvPressure.setText(dayWeather.getPressure()+"hPa");
        tvHumidity.setText(dayWeather.getHumidity());
    }
    //解析JSON数据
    private static DayWeather decodeJson(String responseData) {
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