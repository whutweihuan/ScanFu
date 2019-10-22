package scanfu.com.me;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import scanfu.com.bean.MyApplication;
import scanfu.com.count.R;
import scanfu.com.utils.BitmapsUtil;
import scanfu.com.utils.HttpUtils;
import scanfu.com.utils.ToastUnits;

public class PicCutHeadImageActivity extends AppCompatActivity {
    TextView tv_title;
    String headpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    File headFile = null;
    String headurl;
    Bitmap bitmap = null;
    ImageView iv_head_2;
    ImageView iv_head;
    RelativeLayout rl_head_menu;
    Button btn_save;
    Dialog dialog;
    Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_cut_head_image);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        headurl = getIntent().getStringExtra("headurl");
        initView();
        updateHeadImage();
    }


    //显示已有的头像
    private void updateHeadImage() {
        if (headurl == null || headurl.length() == 0)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(headurl);
                    bitmap = BitmapFactory.decodeStream(url.openStream());
                    if (bitmap != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iv_head_2.setImageBitmap(bitmap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_head_2 = (ImageView) findViewById(R.id.iv_head_2);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        rl_head_menu = (RelativeLayout) findViewById(R.id.rl_head_menu);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setText("上传");
        btn_save.setVisibility(View.VISIBLE);

        tv_title.setText("设置头像");
    }

    public void onBackUp(View v) {
        if(rl_head_menu.getVisibility()==View.GONE){
            //提示用户是否要退出？
            onShowCancelDialog();
        }else{
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)&&(event.getRepeatCount()==0)&&
                (rl_head_menu.getVisibility()==View.GONE)){
            onShowCancelDialog();
        }else{
            return super.onKeyDown(keyCode,event);
        }
        return false;
    }

    //提示用户是否要退出，在没有操作完成之前
    private void onShowCancelDialog() {
        dialog=new Dialog(this);
        View view=View.inflate(this,R.layout.dialog_cancel_upload_head_image,null);
        TextView tv_sure=(TextView)view.findViewById(R.id.tv_sure);
        TextView tv_cancel=(TextView)view.findViewById(R.id.tv_cancel);
        dialog.setContentView(view);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    //三个按钮设置同一个响应事件，这时我们需要通过ID来区分
    public void onBtnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_paizhao:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面指定调用相机拍照后照片保存的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(headFile = (new File(headpath, "head.jpg"))));
                startActivityForResult(intent, 2);
                break;
            case R.id.btn_xiangce:
                intent = new Intent(Intent.ACTION_PICK, null);
                //设置数据和类型
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case 1:
                    if (data != null) {
                        startPhotoZoom(data.getData());
                    }
                    break;
                case 2://拍照
                    if (headFile != null) {
                        startPhotoZoom(Uri.fromFile(headFile));
                    }
                    break;
                case 3:
                    if (data != null) {
                        showHeadView(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //显示裁剪后的图片
    private void showHeadView(Intent data) {
        try {
            bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));
            if (bitmap != null) {
                iv_head.setImageBitmap(bitmap);
                rl_head_menu.setVisibility(View.GONE);
                saveHeadImage(bitmap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//
//        Bundle bundle = data.getExtras();
//        if (bundle != null) {
//            bitmap = bundle.getParcelable("data");
//            if (bitmap != null) {
//                iv_head.setImageBitmap(bitmap);
//                rl_head_menu.setVisibility(View.GONE);
//                saveHeadImage(bitmap);
//            }
//        }

    }

    //保存裁剪后的头像
    private void saveHeadImage(Bitmap bitmap) {
        //如果是通过拍照headFile应该不为空，需要删除
        if (headFile != null) {
            headFile.delete();
            headFile = null;
        }
        //TODO 保存路径可以完善，添加自己APP的路径
        headFile = new File(headpath + File.separator + "temp.png");

        //如果以前有这个文件那么就把他删除
        if (headFile.exists()) {
            headFile.delete();
            try {
                headFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            OutputStream outStream = new FileOutputStream(headFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    //调用系统照片裁剪应用
    private void startPhotoZoom(Uri uri) {
        ToastUnits.show(this, "url:" + uri.getPath());
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true 是设置在开启的Intent中设置显示的View可裁剪
        intent.putExtra("crop", true);
        //aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);

//        intent.putExtra("return-data", true);
        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, 3);
    }

    public void onUpLoad(View v) {
        if (headFile == null || !headFile.exists()) {
            return;
        }

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... strings) {
                String res = null;

                //图片旋转，也就是头像朝上
                //1
                Bitmap bp = BitmapsUtil.decodeFile(headFile, 100);
                //2
                int degree = BitmapsUtil.readPictureDegree(headFile.getAbsolutePath());

                //3
                Bitmap bm = BitmapsUtil.rotaingImageView(degree, bp);
                //4
                headFile = BitmapsUtil.compressHeadPhoto(bm, headFile);

                //TODO 上传
                String url = MyApplication.ServerIP + "/ScanFuServer/rest/updateMyProfile.action";
                JSONObject json = new JSONObject();

                try {
                    json.put("fileSize", headFile.length());
                    json.put("username", MyApplication.getInstance().getUserId());
                    json.put("iconvalid", 1);
                    json.put("category", "head");

                    return HttpUtils.uploadFile(headFile, url, json.toString(), "attach", "image/jpeg");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (s == null) {
                    return;
                }
                try {
                    JSONObject json = new JSONObject(s);
                    if ("success".equals(json.optString("status"))) {
                        ToastUnits.show(PicCutHeadImageActivity.this, "上传成功！");
//                        if(headFile!=null&&headFile.exists()){
//                            try{
//                                headFile.delete();
//                            }
//                        }
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUnits.show(PicCutHeadImageActivity.this, "上传失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        }.execute();

    }

}
