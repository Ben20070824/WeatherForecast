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
    public static final int REQUEST_CODE_GALLERY = 2; // 添加相册请求代码
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 3; // 存储权限请求代码
    private EditText etNickName, etGender, etAge, etAccount, etBirth, etCity, etUniversity, etSignature;
    private Button btnEdit, btnChangeAvatar,btnBack;
    private ImageView ivAvatar;
    private Uri imageUri;
    private String imageBase64;


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_CAMERA){
            if(resultCode==RESULT_OK){
                try {
                    InputStream inputStream=getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ivAvatar.setImageBitmap(bitmap);
                    String imageToBase64=ImageUtil.imageToBase64(bitmap);
                    imageBase64=imageToBase64;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if(requestCode==REQUEST_CODE_GALLERY){
            if(resultCode==RESULT_OK){
                if(data != null && data.getData() != null){
                    Uri selectedImageUri = data.getData();
                    try {
                        InputStream inputStream=getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ivAvatar.setImageBitmap(bitmap);
                        String imageToBase64=ImageUtil.imageToBase64(bitmap);
                        imageBase64=imageToBase64;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "无法读取图片", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void initView() {
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
        btnBack=findViewById(R.id.btn_back);
        ivAvatar=findViewById(R.id.iv_avatar);
    }

    private void initEvent() {
        SharedPreferences spRecord = getSharedPreferences("user_info", MODE_PRIVATE);
        String nickName = spRecord.getString("nickName", "昵称");
        String gender = spRecord.getString("gender", "性别");
        String age = spRecord.getString("age", "年龄");
        String account = spRecord.getString("account", "");
        String birth = spRecord.getString("birth", "");
        String city = spRecord.getString("city", "");
        String university = spRecord.getString("university", "");
        String signature = spRecord.getString("signature", "");
        String avatar=spRecord.getString("image_64","");
        etNickName.setText(nickName);
        etGender.setText(gender);
        etAge.setText(age);
        etAccount.setText(account);
        etBirth.setText(birth);
        etCity.setText(city);
        etUniversity.setText(university);
        etSignature.setText(signature);
        Bitmap bitmap=ImageUtil.base64ToImage(avatar);
        ivAvatar.setImageBitmap(bitmap);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String saveAvatar = imageBase64;
                if (saveAvatar == null || saveAvatar.isEmpty()) {
                    saveAvatar = spRecord.getString("image_64", "");
                }

                UserInfo userInfo = new UserInfo(etNickName.getText().toString(),
                        etGender.getText().toString(),
                        etAccount.getText().toString(),
                        etAge.getText().toString(),
                        etBirth.getText().toString(),
                        etCity.getText().toString(),
                        etUniversity.getText().toString(),
                        etSignature.getText().toString(),
                        saveAvatar
                );

                SharedPreferences.Editor editor = spRecord.edit();
                editor.putString("nickName", userInfo.getNickName());
                editor.putString("gender", userInfo.getGender());
                editor.putString("age", userInfo.getAge());
                editor.putString("account", userInfo.getAccount());
                editor.putString("birth", userInfo.getBirth());
                editor.putString("city", userInfo.getCity());
                editor.putString("university", userInfo.getUniversity());
                editor.putString("signature", userInfo.getSignature());
                editor.putString("image_64", saveAvatar); // 这里也修复
                editor.apply();

                EditActivity.this.finish();
                MainActivity.startMainActivity(EditActivity.this);
            }
        });

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarChangeMenu(v);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("提示");
                builder.setMessage("编辑内容还未保存，确定要返回吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditActivity.this.finish();
                        MainActivity.startMainActivity(EditActivity.this);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                showStorageRationaleDialog();
            } else {
                requestPermissions(new String[]{permission}, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GALLERY);
    }
    private void showStorageRationaleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("需要读取存储权限来访问相册照片");
        builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Android 13+ 使用 READ_MEDIA_IMAGES
                    requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                            REQUEST_CODE_STORAGE_PERMISSION);
                } else {
                    // Android 12 及以下使用 READ_EXTERNAL_STORAGE
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EditActivity.this, "需要存储权限才能选择照片", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
        } else if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            // 添加存储权限处理
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(EditActivity.this, "获取存储权限失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public static void startEditActivity(Context context) {
        Intent intent = new Intent(context, EditActivity.class);
        context.startActivity(intent);
    }
}