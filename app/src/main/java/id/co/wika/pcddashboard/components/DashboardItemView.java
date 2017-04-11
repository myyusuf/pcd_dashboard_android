package id.co.wika.pcddashboard.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.models.DashboardItem;

/**
 * TODO: document your custom view class.
 */
public class DashboardItemView extends LinearLayout {
    private String title;
    private float mTextWidth;
    private float mTextHeight;

    private TextView titleTextView;
    private TextView okTextView;
    private TextView opTextView;
    private TextView lspTextView;

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

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
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        okTextView = (TextView) findViewById(R.id.ok_text_view);
        opTextView = (TextView) findViewById(R.id.op_text_view);
        lspTextView = (TextView) findViewById(R.id.lsp_text_view);

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
        titleTextView.setText(text);
    }

    public void setOkText(String text) {
        okTextView.setText(text);
    }

    public void setOpText(String text) {
        opTextView.setText(text);
    }

    public void setLspText(String text) {
        lspTextView.setText(text);
    }

    public void setDashboardItem(DashboardItem dashboardItem){
        double okValue = dashboardItem.getOk().doubleValue() / 1000;
        double opValue = dashboardItem.getOp().doubleValue() / 1000;
        double lspValue = dashboardItem.getLsp().doubleValue() / 1000;

        setTitleText(dashboardItem.getTitle());

        if(okValue < 0){
            setOkText("(" + decimalFormat.format(Math.abs(okValue)) + ")");
        }else{
            setOkText(decimalFormat.format(Math.abs(okValue)));
        }

        if(opValue < 0){
            setOpText("(" + decimalFormat.format(Math.abs(opValue)) + ")");
        }else{
            setOpText(decimalFormat.format(Math.abs(opValue)));
        }

        if(lspValue < 0){
            setLspText("(" + decimalFormat.format(Math.abs(lspValue)) + ")");
        }else{
            setLspText(decimalFormat.format(Math.abs(lspValue)));
        }
    }

}
