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
import id.co.wika.pcddashboard.models.UmurPiutangItem;

/**
 * TODO: document your custom view class.
 */
public class UmurPiutangItemView extends LinearLayout {
    private String title;
    private float mTextWidth;
    private float mTextHeight;

    private TextView titleTextView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textViewTotal;

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    public UmurPiutangItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public UmurPiutangItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public UmurPiutangItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View.inflate(context, R.layout.umur_piutang_item_view, this);
        titleTextView = (TextView) findViewById(R.id.title_text_view);
        textView1 = (TextView) findViewById(R.id.first_text_view);
        textView2 = (TextView) findViewById(R.id.second_text_view);
        textView3 = (TextView) findViewById(R.id.third_text_view);
        textView4 = (TextView) findViewById(R.id.fourth_text_view);
        textView5 = (TextView) findViewById(R.id.fifth_text_view);
        textViewTotal = (TextView) findViewById(R.id.total_text_view);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DashboardItemView, defStyle, 0);

        try {
            title = a.getString(R.styleable.DashboardItemView_title);
        } catch (Exception e) {
            Log.e("UmurPiutangItemView", "There was an error loading attributes.");
        } finally {
            a.recycle();
        }

        setTitleText(title);

    }

    public void setTitleText(String text) {
        titleTextView.setText(text);
    }

    public void setItemValue(UmurPiutangItem umurPiutangItem){
        double value1 = umurPiutangItem.getValue1().doubleValue() > 0 ? umurPiutangItem.getValue1().doubleValue() / 1000 : 0;
        double value2 = umurPiutangItem.getValue2().doubleValue() > 0 ? umurPiutangItem.getValue2().doubleValue() / 1000 : 0;
        double value3 = umurPiutangItem.getValue3().doubleValue() > 0 ? umurPiutangItem.getValue3().doubleValue() / 1000 : 0;
        double value4 = umurPiutangItem.getValue4().doubleValue() > 0 ? umurPiutangItem.getValue4().doubleValue() / 1000 : 0;
        double value5 = umurPiutangItem.getValue5().doubleValue() > 0 ? umurPiutangItem.getValue5().doubleValue() / 1000 : 0;
        double total = umurPiutangItem.getTotal().doubleValue() > 0 ? umurPiutangItem.getTotal().doubleValue() / 1000 : 0;

        setTitleText(umurPiutangItem.getTitle());

        if(value1 < 0){
            textView1.setText("(" + decimalFormat.format(Math.abs(value1)) + ")");
        }else{
            textView1.setText(decimalFormat.format(Math.abs(value1)));
        }

        if(value2 < 0){
            textView2.setText("(" + decimalFormat.format(Math.abs(value2)) + ")");
        }else{
            textView2.setText(decimalFormat.format(Math.abs(value2)));
        }

        if(value3 < 0){
            textView3.setText("(" + decimalFormat.format(Math.abs(value3)) + ")");
        }else{
            textView3.setText(decimalFormat.format(Math.abs(value3)));
        }

        if(value4 < 0){
            textView4.setText("(" + decimalFormat.format(Math.abs(value4)) + ")");
        }else{
            textView4.setText(decimalFormat.format(Math.abs(value4)));
        }

        if(value5 < 0){
            textView5.setText("(" + decimalFormat.format(Math.abs(value5)) + ")");
        }else{
            textView5.setText(decimalFormat.format(Math.abs(value5)));
        }

        if(total < 0){
            textViewTotal.setText("(" + decimalFormat.format(Math.abs(total)) + ")");
        }else{
            textViewTotal.setText(decimalFormat.format(Math.abs(total)));
        }
    }

}
