package scanfu.com.saomiao;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraPreview(Context mContext,Camera mCamera){
        super(mContext);
        this.mCamera=mCamera;
        mHolder=getHolder();
        mHolder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    public Camera getmCamera() {
        return mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(mHolder.getSurface()==null){
            return;
        }
        //更改时停止预览
        try{
            mCamera.stopPreview();

        }catch (Exception e){

        }
        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        }catch (Exception e){

        }



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
