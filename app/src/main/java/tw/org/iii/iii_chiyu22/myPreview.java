package tw.org.iii.iii_chiyu22;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class myPreview extends SurfaceView implements SurfaceHolder.Callback{

    private Camera camera;
    private SurfaceHolder holder;

    public myPreview(Context context, Camera camera1) {
        super(context);
        this.camera = camera1;

        // 因為不能直接控制 SurfaceView
        // 所以要跟系統拿到控制 SurfaceView 的 Holder
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.v("chiyu", "surface Created");
        try {
            // 把 （camera 的 Preview 畫面） 關連到 （控制 SurfaceView 的 Holder）
            // 重點句
            camera.setPreviewDisplay(holder);
            // 開始相機的 preview
            // 如果沒有這行 雖然看不到畫面 但是照相機也是能拍照
            // 因為照相機的拍照 在 Camera 就被 Camera.open(); 開起來了
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.v("chiyu", "surface Changed");
        try {
            camera.startPreview();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.v("chiyu", "surfaceChanged : " + e.toString());
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.v("chiyu", "surface Destroyed");
        // 把相機的資源釋放掉
        // 不然其他的APP會不能使用
        // 所以當這隻程式不正常關閉 (例如當機) 就會卡住其他程式
        camera.release();
        // 指針清空 物件才會消失
        camera = null;
    }
}
