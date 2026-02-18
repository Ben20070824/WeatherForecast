package com.example.weatherforecast.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherforecast.R;
import com.example.weatherforecast.tool.ImageUtil;
import com.example.weatherforecast.tool.UserInfo;

public class MineFragment extends Fragment {
    TextView tvNickName, tvGender, tvAge, tvAccount, tvBirth, tvCity, tvUniversity, tvSignature;
    Button btnEdit, btnLogout;
    ImageView ivAvatar;

    public MineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
        initClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() == null) return; // 防止getActivity()为空导致崩溃

        SharedPreferences spRecord = getActivity().getSharedPreferences("user_info", getContext().MODE_PRIVATE);
        tvNickName.setText(spRecord.getString("nickName", ""));
        tvGender.setText(spRecord.getString("gender", ""));
        tvAge.setText(spRecord.getString("age", ""));
        tvAccount.setText(spRecord.getString("account", ""));
        tvBirth.setText(spRecord.getString("birth", ""));
        tvCity.setText(spRecord.getString("city", ""));
        tvUniversity.setText(spRecord.getString("university", ""));
        tvSignature.setText(spRecord.getString("signature", ""));

        String imageAvatar = spRecord.getString("image_64", "");
        if (!imageAvatar.isBlank()) {
            ivAvatar.setImageBitmap(ImageUtil.base64ToImage(imageAvatar));
        }

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            UserInfo userInfo = (UserInfo) intent.getSerializableExtra("USER_INFO");
            if (userInfo == null) {
                Log.d("tag", "从未编辑过");
            } else {
                tvNickName.setText(userInfo.getNickName());
                tvGender.setText(userInfo.getGender());
                tvAge.setText(userInfo.getAge());
                tvAccount.setText(userInfo.getAccount());
                tvBirth.setText(userInfo.getBirth());
                tvCity.setText(userInfo.getCity());
                tvUniversity.setText(userInfo.getUniversity());
                tvSignature.setText(userInfo.getSignature());
            }
        }
    }

    private void initView(View view) {
        tvNickName = view.findViewById(R.id.tv_nickName);
        tvGender = view.findViewById(R.id.tv_gender);
        tvAge = view.findViewById(R.id.tv_age);
        tvAccount = view.findViewById(R.id.tv_account_text);
        tvBirth = view.findViewById(R.id.tv_birth_text);
        tvCity = view.findViewById(R.id.tv_city_text);
        tvUniversity = view.findViewById(R.id.tv_university_text);
        tvSignature = view.findViewById(R.id.tv_signature_text);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnLogout = view.findViewById(R.id.btn_logout);
        ivAvatar = view.findViewById(R.id.iv_avatar);
    }

    private void initEvent() {
        try {
            if (getActivity() == null) return;
            SharedPreferences userInfo = getActivity().getSharedPreferences("user_info", getContext().MODE_PRIVATE);
            String nickName = userInfo.getString("nickName", "昵称");
            String gender = userInfo.getString("gender", "性别");
            String age = userInfo.getString("age", "年龄");
            String account = userInfo.getString("account", "");
            String birth = userInfo.getString("birth", "");
            String city = userInfo.getString("city", "");
            String university = userInfo.getString("university", "");
            String signature = userInfo.getString("signature", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initClick() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "跳转编辑页面中", Toast.LENGTH_SHORT).show();
                EditActivity.startEditActivity(getActivity());
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }
}