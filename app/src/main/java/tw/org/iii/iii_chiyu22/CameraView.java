package tw.org.iii.iii_chiyu22;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class CameraView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        Log.v("chiyu", "CameraView onCreate");
        permission();
    }

    Camera c1;
    FrameLayout frameLayout;
    myPreview myCamPreview;
    private void init(){
        frameLayout = (FrameLayout) findViewById(R.id.frameL);

        // 存取照相機
        // 特別注意要 import 這個 android.hardware.Camera 檔案才對
        int num = Camera.getNumberOfCameras();
        Log.v("chiyu",num + "");
        // 開照相機
        //c1 = Camera.open(); // 前照相機
        c1 = Camera.open(1); // 後照相機
        // new 出一個 SurfaceView 物件 準備處理 照相機的 preview
        myCamPreview = new myPreview(this,c1);
        // 把這個 SurfaceView 物件 放到 frameLayout 去顯示
        frameLayout.addView(myCamPreview);

    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    123);
        } else {
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }
}
