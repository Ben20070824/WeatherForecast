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
    private MyHandler mHandler;
    private WeatherCallback callback;

    // 回调接口：数据加载完成后通知HomeFragment
    public interface WeatherCallback {
        void onWeatherLoaded(DayWeather data);
    }

    public DayFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler(Looper.getMainLooper(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (getActivity() != null) {
            tvCity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "FZSTK.TTF"));
        }
    }

    // 带回调的请求方法
    public void requestWeather(String city, WeatherCallback callback) {
        this.callback = callback;
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

                    int responseCode = connection.getResponseCode();
                    if (responseCode != 200) {
                        if (callback != null) callback.onWeatherLoaded(null);
                        return;
                    }

                    InputStream inputStream = connection.getInputStream();
                    String result = streamToString(inputStream);
                    Message msg = mHandler.obtainMessage();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) callback.onWeatherLoaded(null);
                } finally {
                    if (connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    // 主动刷新UI（供外部调用）
    public void updateUI(DayWeather dayWeather) {
        if (getActivity() == null || !isAdded()) return;
        setText(dayWeather);
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
        private WeakReference<DayFragment> fragmentRef;

        public MyHandler(@NonNull Looper looper, DayFragment fragment) {
            super(looper);
            this.fragmentRef = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            DayFragment fragment = fragmentRef.get();
            if (fragment == null || !fragment.isAdded() || fragment.getActivity() == null) {
                return;
            }
            String responseData = msg.obj.toString();
            DayWeather dayWeather = decodeJson(responseData);
            // 回调给HomeFragment缓存数据
            if (fragment.callback != null) fragment.callback.onWeatherLoaded(dayWeather);
            fragment.setText(dayWeather);
        }
    }

    private void setText(DayWeather dayWeather) {
        if (dayWeather == null) {
            tvCity.setText("获取天气失败，请重试");
            tvDate.setText("");
            tvWea.setText("");
            tvTemHigh.setText("");
            tvTemLow.setText("");
            return;
        }
        if (tvCity != null) tvCity.setText(dayWeather.getCity());
        if (tvDate != null) tvDate.setText(dayWeather.getDate());
        if (tvWea != null) tvWea.setText(dayWeather.getWea());
        if (tvTemHigh != null) tvTemHigh.setText(dayWeather.getTem_high() + "℃");
        if (tvTemLow != null) tvTemLow.setText(dayWeather.getTem_low() + "℃");
        if (tvWin != null) tvWin.setText(dayWeather.getWin());
        if (tvWinSpeed != null) tvWinSpeed.setText(dayWeather.getWin_speed());
        if (tvWinMeter != null) tvWinMeter.setText("风速:" + dayWeather.getWin_meter());
        if (tvAir != null) tvAir.setText(dayWeather.getAir());
        if (tvPressure != null) tvPressure.setText(dayWeather.getPressure() + "hPa");
        if (tvHumidity != null) tvHumidity.setText(dayWeather.getHumidity());
    }

    private static DayWeather decodeJson(String responseData) {
        if (responseData == null || responseData.isEmpty()) {
            return null;
        }
        DayWeather dayWeather = null;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
        callback = null; // 清空回调，避免内存泄漏
    }
}