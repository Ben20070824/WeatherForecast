package com.example.weatherforecast.main.childfragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapter.ManyDayWeatherAdapter;
import com.example.weatherforecast.main.HomeFragment;
import com.example.weatherforecast.tool.ManyDayWeather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ManyDayFragment extends Fragment {

    private TextView tvCity;
    private RecyclerView recyclerView;
    private MyHandler mHandler=new MyHandler(Looper.getMainLooper(),this);
    private ManyDayWeatherAdapter mAdapter;
    public ManyDayFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_many_day, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        tvCity.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"FZSTK.TTF"));
    }

    public void requestWeather(String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://v1.yiketianqi.com/free/month?appid=47517298&appsecret=Ap7TYeAW&unescape=1&city=" + city);
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

    private void initView(View view){
        tvCity=view.findViewById(R.id.tv_city);
        recyclerView=view.findViewById(R.id.rv_weather);
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

    private static ManyDayWeather decodeJson(String json) {
        String city = "";
        List<ManyDayWeather.WeatherInfo> WeatherDataList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);

            city = root.getString("city");

            JSONArray dataArray = root.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dayData = dataArray.getJSONObject(i);
                String date = dayData.getString("date");
                String wea = dayData.getString("wea");
                String tem_high = dayData.getString("tem_day");
                String tem_low = dayData.getString("tem_night");
                String win = dayData.optString("win","");
                if(win.isBlank()){
                    win ="暂无数据";
                }

                String weaDay = dayData.getString("wea_day");
                String weaNight = dayData.getString("wea_night");
                WeatherDataList.add(new ManyDayWeather.WeatherInfo(date,wea,tem_high,tem_low,win,weaDay,weaNight));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ManyDayWeather(city,WeatherDataList);
    }

    private void setText(ManyDayWeather manyDayWeather) {
        tvCity.setText(manyDayWeather.getCity());
        mAdapter=new ManyDayWeatherAdapter(manyDayWeather.getList());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private static class MyHandler extends Handler {
        WeakReference<ManyDayFragment> manyDayFragmentRef;

        public MyHandler(@NonNull Looper looper,ManyDayFragment manyDayFragment) {
            super(looper);
            this.manyDayFragmentRef = new WeakReference<>(manyDayFragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String responseData = msg.obj.toString();
            manyDayFragmentRef.get().setText(decodeJson(responseData));
        }
    }
}