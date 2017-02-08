package id.co.wika.pcddashboard.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.models.DashboardItem;

/**
 * TODO: document your custom view class.
 */
public class DashboardItemView extends LinearLayout {
    private String title;
    private float mTextWidth;
    private float mTextHeight;

    private TextView textView;

    public DashboardItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DashboardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DashboardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View.inflate(context, R.layout.dashboard_item_view, this);
        textView = (TextView) findViewById(R.id.textView);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DashboardItemView, defStyle, 0);

        try {
            title = a.getString(R.styleable.DashboardItemView_title);
        } catch (Exception e) {
            Log.e("DashboardItemView", "There was an error loading attributes.");
        } finally {
            a.recycle();
        }

        setTitleText(title);

    }

    public void setTitleText(String text) {
        textView.setText(text);
    }

    public void setDashboardItem(DashboardItem dashboardItem){
        setTitleText(dashboardItem.getTitle());
    }

}
