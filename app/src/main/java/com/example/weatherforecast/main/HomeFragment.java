package com.example.weatherforecast.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.DayFragment;
import com.example.weatherforecast.ManyDayFragment;
import com.example.weatherforecast.R;
import com.example.weatherforecast.SevenDayFragment;
import com.example.weatherforecast.adapter.SevenDayWeatherAdapter;
import com.example.weatherforecast.adapter.ViewPager2Adapter;
import com.example.weatherforecast.tool.ManyDayWeather;
import com.example.weatherforecast.tool.SevenDayWeatherInfo;
import com.google.android.material.tabs.TabLayout;

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
    private Button btnSearch;
    private TabLayout tabLayout;
    private OnSearchListener onSearchListener;
    private FragmentContainerView fragmentContainerView;

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
        fragmentContainerView=view.findViewById(R.id.fcv);
    }

    private void initEvent() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fcv,new DayFragment()).commit();
        tabLayout.addTab(tabLayout.newTab().setText("当日天气"));
        tabLayout.addTab(tabLayout.newTab().setText("七天天气"));
        tabLayout.addTab(tabLayout.newTab().setText("还不够？"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if(position==0){
                    fragmentTransaction.replace(R.id.fcv,new DayFragment()).commit();
                } else if (position==1) {
                    fragmentTransaction.replace(R.id.fcv,new SevenDayFragment()).commit();
                }
                else if(position==2){
                    fragmentTransaction.replace(R.id.fcv,new ManyDayFragment()).commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "获取天气中", Toast.LENGTH_SHORT).show();
                onSearchListener.transmitData(etSearch.getText().toString());
            }
        });
    }

    public interface OnSearchListener {
        void transmitData(String city);
    }

    public  void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }
}