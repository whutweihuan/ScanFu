package scanfu.com.me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import scanfu.com.count.R;
import scanfu.com.utils.ToastUnits;

public class ModifyMyselfActivity extends AppCompatActivity {

    TextView tv_title;
    TextView tv_desc;
    EditText et_content;

    String title;
    String desc;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_myself);

        title=getIntent().getStringExtra("title");
        desc=getIntent().getStringExtra("desc");
        content=getIntent().getStringExtra("content");

        initView();

    }

    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_desc=(TextView)findViewById(R.id.tv_desc);
        et_content=(EditText) findViewById(R.id.et_content);

        if("修改手机号".equals(title)){
            et_content.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        tv_title.setText(title);
        tv_desc.setText(desc);

        if(content==null){
            return;
        }

        et_content.setText(content);
        et_content.setSelection(content.length());//设置光标位置

    }

    public void onSave(View view) {
        content=et_content.getText().toString().trim();
        if(content==null||content.length()==0){
            ToastUnits.show(this,"内容不能为空");
            return;
        }
        Intent intent=new Intent(this,MyselfInformationActivity.class);
        intent.putExtra("content",content);
        setResult(RESULT_OK,intent);
        finish();//必须调用finish
    }

    public void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();//必须调用finish
    }


}
