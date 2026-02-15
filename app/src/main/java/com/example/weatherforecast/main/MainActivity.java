package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initView() {
        viewPager2=findViewById(R.id.viewpager2);
        bottomNavigationView=findViewById(R.id.bnv);
    }

    private void initEvent() {
        List<Fragment> list=new ArrayList<>();
        list.add(new HomeFragment());
        list.add(new MineFragment());
        ViewPager2Adapter viewPager2Adapter=new ViewPager2Adapter(MainActivity.this,list);
        viewPager2.setAdapter(viewPager2Adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }
        });


            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.navigation_home) {
                        viewPager2.setCurrentItem(0);
                        return true;
                    } else if (itemId == R.id.navigation_profile) {
                        viewPager2.setCurrentItem(1);
                        return true;
                    } else {
                        return false;
                    }
                }
            });
    }

    public static void startMainActivity(Context context){
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}