package scanfu.com.saomiao;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.MyConstants;
import scanfu.com.utils.StorageHelper;
import scanfu.com.utils.ToastUnits;

public class SaoZiActivity extends AppCompatActivity {

    Camera mCamera;
    CameraPreview mPreview;
    final int maxSeconds = 5;
    int currentSecond = maxSeconds;
    TextView tv_time;
    boolean isPreviewing = true;

    File mPictureFile = null;

    MyApplication app;
    AlertDialog alertDialog;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mCamera != null && isPreviewing) {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (currentSecond <= 0) {
                            if (success) {
                                mCamera.takePicture(null, null, mPictureCallback);
                                isPreviewing = false;
//                                mCamera.stopPreview();
                            } else {

                            }
                        }
                    }
                });
            }
            if (msg.what > 0) {
                currentSecond = msg.what - 1;
                tv_time.setText("" + currentSecond);
                mHandler.sendEmptyMessageDelayed(currentSecond, 1000);
            } else {
                //TODO 当msg.what==0时需要拍照获取图片

            }
        }


    };

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                mPictureFile = StorageHelper.getOutputFile();
                if (mPictureFile != null) {
                    //存储照片
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mPictureFile);
                        fos.write(data);
                        fos.close();
                        ToastUnits.show(SaoZiActivity.this, "识别成功");

                        new AsyncTask<String, Void, String>() {
                            @Override
                            protected String doInBackground(String... strings) {
                                String url = "http://" + MyConstants.IP_ADDRESS + ":8080/FuKaServer/getFuKaByRandom"
                                        + "?userid=" + app.getUserId();//等福卡界面做完把"test"替换为  app.getUserId()
                                return HttpUtils.httpGet(url);
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                if (s != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        if ("success".equals(jsonObject.optString("status"))) {
//                                            ToastUnits.show(SaoZiActivity.this,jsonObject.optString("tip"));
                                            LayoutInflater inflater = getLayoutInflater();
                                            View view = inflater.inflate(R.layout.dialog_fuka, null);
                                            alertDialog = new AlertDialog.Builder(SaoZiActivity.this).setView(view).create();
                                            TextView tv_fuka_name, tv_repeat, tv_cancel;
                                            tv_fuka_name = (TextView) view.findViewById(R.id.tv_fuka_name);
                                            tv_repeat = (TextView) view.findViewById(R.id.tv_repeat);
                                            tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

                                            tv_fuka_name.setText(jsonObject.optString("tip"));
                                            tv_repeat.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.cancel();
                                                    mCamera.startPreview();
                                                    mHandler.removeMessages(currentSecond);
                                                    currentSecond = maxSeconds;
                                                    isPreviewing=true;
                                                    mHandler.sendEmptyMessageDelayed(currentSecond,1000);
//                                                    isPreviewing=true;
//                                                    mHandler.sendEmptyMessageDelayed(currentSecond,1000);
                                                }
                                            });
                                            tv_cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertDialog.cancel();
                                                    finish();
                                                }
                                            });
                                            alertDialog.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ToastUnits.show(SaoZiActivity.this, "网络异常");
                                }
                            }
                        }.execute();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_sao_zi);

        app = MyApplication.getInstance();

        if (CheckCameraHardware(this) == false) {
            ToastUnits.show(this, "很抱歉，您的设备可能不支持摄像功能!");
            finish();
            return;
        }

        //获取相机
        mCamera = getCameraInstance();
        if (mCamera == null) {
            ToastUnits.show(this, "获取相机失败!");
            finish();
            return;
        }

        //获取预览界面
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout mFrameLayout = (FrameLayout) findViewById(R.id.fl_preview);
        tv_time = (TextView) findViewById(R.id.tv_time);
        mFrameLayout.addView(mPreview);
        mCamera.startPreview();

    }

    //申请相机资源
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {

        }
        return c;
    }

    //释放相机资源
    public void releaseCamera() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {

            }
            try {
                mCamera.release();
                mCamera = null;
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDestroy() {
        releaseCamera();
        super.onDestroy();
    }

    private boolean CheckCameraHardware(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //摄像头存在
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentSecond = maxSeconds;
        isPreviewing = true;
        mHandler.sendEmptyMessageDelayed(currentSecond, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(currentSecond);
    }
}
