package scanfu.com.UI;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 *
 */
public class ToolBarRadioGroup extends RadioGroup implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager;

    public ToolBarRadioGroup(Context context) {
        super(context);
    }

    public ToolBarRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
    }

//调用子View的layout方法实现布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        for (int i = 0; i < getChildCount(); i++) {
            final int position = i;
            getChildAt(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClickedViewChecked(position);
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(position, false);
                    }
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateGradient(position, positionOffset);
    }

    private void updateGradient(int position, float offset) {
        if (offset > 0) {
            ((ToolBarRadioButton) getChildAt(position)).updateAlpha(255 * (1 - offset));
            ((ToolBarRadioButton) getChildAt(position + 1)).updateAlpha(255 * offset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setSelectedViewChecked(position);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setSelectedViewChecked(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            ((ToolBarRadioButton) getChildAt(i)).setChecked(false);
        }
        ((ToolBarRadioButton) getChildAt(position)).setChecked(true);
    }

    private void setClickedViewChecked(int position) {
        for (int i = 0; i < getChildCount(); i++) {
            ((ToolBarRadioButton) getChildAt(i)).setRadioButtonChecked(false);
        }
        ((ToolBarRadioButton) getChildAt(position)).setRadioButtonChecked(true);
    }
}
