package scanfu.com.bean;

import android.app.Application;

public class MyApplication extends Application {
    String sessionId = null;
    private static MyApplication myApplication = null;
    private String userId = null;
    public static final String ServerIP="http://192.168.43.49:8080";
    private String validuid=null;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValiduid() {
        return validuid;
    }

    public void setValiduid(String validuid) {
        this.validuid = validuid;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public static MyApplication getInstance(){
        if(myApplication == null){
            myApplication = new MyApplication();
        }
        return  myApplication;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void onCreate() {
        myApplication = MyApplication.this;
        super.onCreate();
    }
}
