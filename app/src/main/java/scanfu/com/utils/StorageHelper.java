package scanfu.com.utils;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageHelper {
    public static File getOutputFile(){
        File mDir=new File(Environment.getExternalStorageDirectory(),"scanfu2");
        if(!mDir.exists()){
            if(!mDir.mkdir()){
                return null;
            }
        }
        String mFileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".jpg";
        File mPictFile = null;
        mPictFile=new File(mDir.getPath()+File.separator+mFileName);
        return mPictFile;
    }
}
