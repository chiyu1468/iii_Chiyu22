package tw.org.iii.iii_chiyu22;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.effect.Effect;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class CameraView extends AppCompatActivity {
    private File sdroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        Log.v("chiyu", "CameraView onCreate");
        permission();
        sdroot = Environment.getExternalStorageDirectory();
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
        c1 = Camera.open(); // 前照相機
        //c1 = Camera.open(1); // 後照相機
        // new 出一個 SurfaceView 物件 準備處理 照相機的 preview
        myCamPreview = new myPreview(this,c1);
        // 把這個 SurfaceView 物件 放到 frameLayout 去顯示
        frameLayout.addView(myCamPreview);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // takePicture(按下快門要做的事,存取照片原始檔(bmp),存取照片壓縮檔(jpg))
                // 通常 照片原始檔(bmp檔) 太大 不會去處理 所以用 null
                c1.takePicture(new picardShutter(),null,new picardPicCallBack());
            }
        });

        // 可以預設相機的各種效果
        initCamera();

    }

    private void initCamera(){
        Camera.Parameters param = c1.getParameters();
        // 也可以查詢相機支援的畫質
        List<Camera.Size> sizes = param.getSupportedPictureSizes();
        for (Camera.Size s1 : sizes){
            int w = s1.width, h = s1.height;
            Log.v("chiyu", w + " x " + h);
        }

        List<String> effects = param.getSupportedColorEffects();
        for (String e1 : effects){
            Log.v("chiyu", e1);
        }

        //
        param.setColorEffect("sketch");

    }

    private class picardShutter implements Camera.ShutterCallback {
        @Override
        public void onShutter() {
            Log.v("chiyu", "按下快門拉～～～");
        }
    }
    private class picardPicCallBack implements Camera.PictureCallback {
        // 圖片會被放在 byte[] bytes 這裡
        @Override
        public void onPictureTaken(byte[] picBytes, Camera camera) {
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(new File(sdroot,"chiyu.jpg"));
                fout.write(picBytes);
                fout.flush();
                fout.close();
                Toast.makeText(CameraView.this,"save OK",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.v("chiyu","picardPicCallBack Error : " + e.toString());
            }

        }
    }

    //
    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
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
