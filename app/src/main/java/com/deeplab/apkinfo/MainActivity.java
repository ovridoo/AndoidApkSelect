package com.deeplab.apkinfo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private int APK = 1;
    private Uri selectAPK;
    private long apkFileSize = 0;
    private Button mApkSelect;
    private TextView mSize,mPackName,mVersiyonName,mVersiyonCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSize = findViewById(R.id.mSize);
        mPackName = findViewById(R.id.mPackName);
        mVersiyonName = findViewById(R.id.mVersiyonName);
        mVersiyonCode = findViewById(R.id.mVersiyonCode);


        mApkSelect = findViewById(R.id.mApkSelect);
        mApkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},APK);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/vnd.android.package-archive");
                    startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), APK);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == APK){
                try {
                    selectAPK = data.getData();
                    File file = new File(selectAPK.getPath().replace("document/raw:",""));

                    long fileSizeInBytes = file.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    String mb = " MB";
                    apkFileSize = fileSizeInKB / 1024;
                    if(apkFileSize > 1024){
                        mb = " GB";
                        apkFileSize = apkFileSize / 1024;
                    }
                    String size = String.valueOf(apkFileSize);


                    //Mevcut uygulama bilgilerini bu şekilde görebiliyorum
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
                    String packet = pInfo.packageName;
                    String version = pInfo.versionName;
                    String versiyonCode = String.valueOf(pInfo.versionCode);

                    mSize.setText(size + mb);
                    mPackName.setText(packet);
                    mVersiyonName.setText(version);
                    mVersiyonCode.setText(versiyonCode);

                }catch (Exception e){
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
