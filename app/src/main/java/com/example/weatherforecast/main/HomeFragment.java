package com.example.weatherforecast.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.weatherforecast.R;
import com.example.weatherforecast.main.childfragment.DayFragment;
import com.example.weatherforecast.main.childfragment.ManyDayFragment;
import com.example.weatherforecast.main.childfragment.SevenDayFragment;
import com.example.weatherforecast.tool.DayWeather;
import com.example.weatherforecast.tool.ManyDayWeather;
import com.example.weatherforecast.tool.SevenDayWeather;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private EditText etSearch;
    private Button btnSearch;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private final String[] tabTitles = {"当天天气", "七天天气", "31天天气"};

    // 全局缓存所有天气数据
    private DayWeather dayWeatherData;
    private SevenDayWeather sevenDayWeatherData;
    private ManyDayWeather manyDayWeatherData;

    // 全局持有Fragment实例
    private DayFragment dayFragment;
    private SevenDayFragment sevenDayFragment;
    private ManyDayFragment manyDayFragment;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        btnSearch = view.findViewById(R.id.btn_search);
        tabLayout = view.findViewById(R.id.tl_content);
        viewPager2 = view.findViewById(R.id.viewpager2);


    }

    private void initEvent() {
        // 关键1：强制缓存所有Fragment，避免重建
        viewPager2.setOffscreenPageLimit(3); // 缓存3个页面（全部）
        // 初始化Fragment
        dayFragment = new DayFragment();
        sevenDayFragment = new SevenDayFragment();
        manyDayFragment = new ManyDayFragment();

        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0: return dayFragment;
                    case 1: return sevenDayFragment;
                    case 2: return manyDayFragment;
                    default: return dayFragment;
                }
            }

            @Override
            public int getItemCount() {
                return tabTitles.length;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabTitles[position]);
        }).attach();

        // 关键2：切换页面时强制刷新当前Fragment的数据
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 切换页面后，用缓存的数据刷新当前页面
                refreshCurrentFragment(position);
            }
        });

        btnSearch.setOnClickListener(v -> {
            String city = etSearch.getText().toString().trim();
            if (city.isEmpty()) {
                Toast.makeText(getContext(), "请输入城市名称", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "获取天气中", Toast.LENGTH_SHORT).show();

            // 关键3：统一请求+缓存数据+刷新所有Fragment
            requestAllWeather(city);
        });

        // 初始化默认搜索长沙
        etSearch.setText("长沙");
        btnSearch.callOnClick();
    }

    // 统一请求所有天气数据并缓存
    private void requestAllWeather(String city) {
        // 请求当天天气
        dayFragment.requestWeather(city, new DayFragment.WeatherCallback() {
            @Override
            public void onWeatherLoaded(DayWeather data) {
                dayWeatherData = data;
                dayFragment.updateUI(data); // 主动刷新UI
            }
        });

        // 请求7天天气
        sevenDayFragment.requestWeather(city, new SevenDayFragment.WeatherCallback() {
            @Override
            public void onWeatherLoaded(SevenDayWeather data) {
                sevenDayWeatherData = data;
                sevenDayFragment.updateUI(data); // 主动刷新UI
            }
        });

        // 请求31天天气
        manyDayFragment.requestWeather(city, new ManyDayFragment.WeatherCallback() {
            @Override
            public void onWeatherLoaded(ManyDayWeather data) {
                manyDayWeatherData = data;
                manyDayFragment.updateUI(data); // 主动刷新UI
            }
        });
    }

    // 切换页面时刷新当前Fragment
    private void refreshCurrentFragment(int position) {
        switch (position) {
            case 0:
                if (dayWeatherData != null) dayFragment.updateUI(dayWeatherData);
                break;
            case 1:
                if (sevenDayWeatherData != null) sevenDayFragment.updateUI(sevenDayWeatherData);
                break;
            case 2:
                if (manyDayWeatherData != null) manyDayFragment.updateUI(manyDayWeatherData);
                break;
        }
    }
}