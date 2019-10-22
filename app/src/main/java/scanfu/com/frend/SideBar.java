package scanfu.com.frend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import scanfu.com.count.R;

public class SideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int choose = -1; //选中
    private Paint paint = new Paint();
    private TextView mTextDialog;

    public TextView getTextDialog() {
        return mTextDialog;
    }

    public OnTouchingLetterChangedListener getOnTouchingLetterChangedListener() {
        return onTouchingLetterChangedListener;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public void setTextDialog(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideBar(Context context) {
        super(context);
    }

    // 重写这个方法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色
        int height = getHeight(); //获取对应高度
        int width = getWidth();   // 获取对应宽度
        int singleHight = height / b.length; // 获取每一个字母的高度
        int singleWidth = width / b.length; // 获取每一个字母的宽度
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.rgb(33, 65, 98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHight * i + singleHight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    // 触摸事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY(); //点击 y 坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        final int c = (int) (y / getHeight() * b.length); //点击 y 坐标占总高度的比例 * b数组的长度就等于点击 b 中个数
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(INVISIBLE);
                }
                break;
            default:// 设置字母列表【A,B,C,D...] 颜色
                setBackgroundResource(R.drawable.sidebar_shape_bg);
                if (choose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.OnTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(VISIBLE);

                        }
                        choose = c;
                        invalidate();
                    }
                    break;
                }
        }
        return true;
    }

    //接口
    public interface OnTouchingLetterChangedListener {
        public void OnTouchingLetterChanged(String s);

    }


}
