package com.example.weatherforecast.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weatherforecast.R;
import com.example.weatherforecast.adapter.ViewPager2FragmentAdapter;
import com.example.weatherforecast.main.childfragment.DayFragment;
import com.example.weatherforecast.main.childfragment.ManyDayFragment;
import com.example.weatherforecast.main.childfragment.SevenDayFragment;
import com.example.weatherforecast.tool.DayWeather;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private EditText etSearch;
    private Button btnSearch;
    private TabLayout tabLayout;
    private OnSearchListener onSearchListener;
    private OnSearchListener currentSearchListener;
    private ViewPager2 viewPager2;
    private final String[] tabTitles = {"当天天气", "七天天气", "31天天气"};

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
        btnSearch = view.findViewById(R.id.btn_search);
        tabLayout = view.findViewById(R.id.tl_content);
        viewPager2=view.findViewById(R.id.viewpager2);
    }

    private void initEvent() {
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position){
                    case 0:return new DayFragment();
                    case 1:return new SevenDayFragment();
                    case 2:return new ManyDayFragment();
                    default:return new DayFragment();
                }
            }

            @Override
            public int getItemCount() {
                return tabTitles.length;
            }
        });
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                tab.setText(tabTitles[i]);
            }
        }).attach();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "获取天气中", Toast.LENGTH_SHORT).show();
                currentSearchListener.transmitData(etSearch.getText().toString());
            }
        });
        // 监听页面切换
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateSearchListener(position);
            }
        });
        updateSearchListener(0);
        autoRequestWeather();
    }

    public interface OnSearchListener {
        void transmitData(String city);
    }

    public  void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }
    // 根据当前位置更新监听器
    private void updateSearchListener(int position) {
        // 获取当前Fragment
        Fragment currentFragment = getChildFragmentManager()
                .findFragmentByTag("f" + position);

        if (currentFragment == null) {
            return;
        }

        if (currentFragment instanceof DayFragment) {
            currentSearchListener = new OnSearchListener() {
                @Override
                public void transmitData(String city) {
                    ((DayFragment) currentFragment).requestWeather(city);
                }
            };
        } else if (currentFragment instanceof SevenDayFragment) {
            currentSearchListener = new OnSearchListener() {
                @Override
                public void transmitData(String city) {
                    ((SevenDayFragment) currentFragment).requestWeather(city);
                }
            };
        } else if (currentFragment instanceof ManyDayFragment) {
            currentSearchListener = new OnSearchListener() {
                @Override
                public void transmitData(String city) {
                    ((ManyDayFragment) currentFragment).requestWeather(city);
                }
            };
        }
    }
    private void autoRequestWeather() {
        // 延迟一点时间执行，确保ViewPager2和Fragment完全初始化
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded() && getContext() != null) {
                    etSearch.setText("长沙");

                    if (currentSearchListener != null) {
                        Toast.makeText(getContext(), "正在获取长沙天气...", Toast.LENGTH_SHORT).show();
                        currentSearchListener.transmitData("长沙");
                    } else {
                        updateSearchListener(0);
                        if (currentSearchListener != null) {
                            Toast.makeText(getContext(), "正在获取长沙天气...", Toast.LENGTH_SHORT).show();
                            currentSearchListener.transmitData("长沙");
                        }
                    }
                }
            }
        }, 500); // 延迟500ms，确保UI完全初始化
    }
}