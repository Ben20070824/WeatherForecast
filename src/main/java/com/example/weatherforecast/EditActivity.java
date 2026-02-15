package com.example.weatherforecast;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class EditActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CAMERA = 1;
    EditText etNickName, etGender, etAge, etAccount, etBirth, etCity, etUniversity, etSignature;
    Button btnEdit, btnChangeAvatar;
    ImageView ivAvatar;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initEvent();
    }

    private void initView() {
        // 绑定布局ID，对应正确命名的 EditText 变量
        etNickName = findViewById(R.id.et_nickName);
        etGender = findViewById(R.id.et_gender);
        etAge = findViewById(R.id.et_age);
        etAccount = findViewById(R.id.et_account_text);
        etBirth = findViewById(R.id.et_birth_text);
        etCity = findViewById(R.id.et_city_text);
        etUniversity = findViewById(R.id.et_university_text);
        etSignature = findViewById(R.id.et_signature_text);
        btnEdit = findViewById(R.id.btn_edit);
        btnChangeAvatar = findViewById(R.id.btn_change);
        ivAvatar=findViewById(R.id.iv_avatar);

        SharedPreferences spRecord = getSharedPreferences("spRecord", MODE_PRIVATE);
        String nickName = spRecord.getString("nickName", "昵称");
        String gender = spRecord.getString("gender", "性别");
        String age = spRecord.getString("age", "年龄");
        String account = spRecord.getString("account", "");
        String birth = spRecord.getString("birth", "");
        String city = spRecord.getString("city", "");
        String university = spRecord.getString("university", "");
        String signature = spRecord.getString("signature", "");
        etNickName.setText(nickName);
        etGender.setText(gender);
        etAge.setText(age);
        etAccount.setText(account);
        etBirth.setText(birth);
        etCity.setText(city);
        etUniversity.setText(university);
        etSignature.setText(signature);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAMERA){
            if(resultCode==RESULT_OK){
                try {
                    InputStream inputStream=getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initEvent() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserInfo userInfo = new UserInfo(etNickName.getText().toString(),
                        etGender.getText().toString(),
                        etAccount.getText().toString(),
                        etAge.getText().toString(),
                        etBirth.getText().toString(),
                        etCity.getText().toString(),
                        etUniversity.getText().toString(),
                        etSignature.getText().toString()
                );

                SharedPreferences spRecord = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = spRecord.edit();
                editor.putString("nickName", userInfo.getNickName());
                editor.putString("gender", userInfo.getGender());
                editor.putString("age", userInfo.getAge());
                editor.putString("account", userInfo.getAccount());
                editor.putString("birth", userInfo.getBirth());
                editor.putString("city", userInfo.getCity());
                editor.putString("university", userInfo.getUniversity());
                editor.putString("signature", userInfo.getSignature());
                editor.apply();
                EditActivity.this.finish();
                MainActivity.startActivity(EditActivity.this, userInfo);
            }
        });

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarChangeMenu(v);
            }
        });

    }

    private void showAvatarChangeMenu(View view) {
        PopupMenu menu = new PopupMenu(EditActivity.this, view);
        menu.getMenuInflater().inflate(R.menu.avatar_change_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.camera) {
                    camera();
                    return true;
                } else if (id == R.id.photo) {
                    photo();
                    return true;
                } else {
                    return false;
                }
            }
        });
        menu.show();
    }

    private void camera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showRationaleDialog();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        File imageTemp=new File(getExternalCacheDir(),"imageOut.jpeg");
        if(imageTemp.exists()) imageTemp.delete();
        try {
            imageTemp.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > 24) {
            imageUri = FileProvider.getUriForFile(this, "com.example.weatherforecast.fileprovider", imageTemp);
        } else {
            imageUri = Uri.fromFile(imageTemp);
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    private void showRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("需要摄像机权限来拍摄头像照片");
        builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditActivity.this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void photo() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(EditActivity.this, "获取摄像头权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void startEditActivity(Context context) {
        Intent intent = new Intent(context, EditActivity.class);
        context.startActivity(intent);
    }

}