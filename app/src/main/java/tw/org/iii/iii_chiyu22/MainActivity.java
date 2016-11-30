package tw.org.iii.iii_chiyu22;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private File sdroot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        sdroot = Environment.getExternalStorageDirectory();
        permission();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent myPic) {
        super.onActivityResult(requestCode, resultCode, myPic);
        if(requestCode == 11 && resultCode == RESULT_OK){
            take1(myPic);
        } else if(requestCode == 22 && resultCode == RESULT_OK){
            take2();
        } else if(requestCode == 33 && resultCode == RESULT_OK) {
            take3();
        }
    }


    // >>>>>>>> 叫別人的app - 縮圖 <<<<<<<<
    public void test1(View v){
        // 這兩行會讓手機去呼叫其他隻APP完成相機
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(it,11);
    }
    private void take1(Intent it){
        // 東西(回傳的縮圖)固定擺在 data
        Bitmap bp = (Bitmap)it.getExtras().get("data");
        iv.setImageBitmap(bp);
    }

    // >>>>>>>> 叫別人的app - 原圖 <<<<<<<<
    private File photoFile;
    public void test2(View v){
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new File(sdroot, "chiyu.jpg");
        Uri photoUri = Uri.fromFile(photoFile);
        it.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(it,22);
    }
    private void take2(){
        Bitmap bp = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        iv.setImageBitmap(bp);
    }


    // >>>>>>>> 自定義相機 <<<<<<<<
    public void test3(View v){
        Intent it = new Intent(this,CameraView.class);
        startActivityForResult(it,33);
    }
    private void take3(){
        Bitmap bp = BitmapFactory.decodeFile(new File(sdroot, "chiyu.jpg").getAbsolutePath());
        iv.setImageBitmap(bp);
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
        }
    }
}
