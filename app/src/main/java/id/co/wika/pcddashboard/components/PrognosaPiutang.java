package id.co.wika.pcddashboard.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.models.DashboardItem;

/**
 * Created by myyusuf on 4/3/17.
 */
public class PrognosaPiutang extends LinearLayout {
    private String title;
    private float mTextWidth;
    private float mTextHeight;

    private TextView titleTextView;
    private TextView okTextView1;
    private TextView okTextView2;
    private TextView okTextView3;

    private TextView opTextView1;
    private TextView opTextView2;
    private TextView opTextView3;

    private TextView lspTextView1;
    private TextView lspTextView2;
    private TextView lspTextView3;

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    public PrognosaPiutang(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PrognosaPiutang(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PrognosaPiutang(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View.inflate(context, R.layout.prognosa_piutang, this);
//        titleTextView = (TextView) findViewById(R.id.title_text_view);
        okTextView1 = (TextView) findViewById(R.id.ok_text_view1);
        okTextView2 = (TextView) findViewById(R.id.ok_text_view2);
        okTextView3 = (TextView) findViewById(R.id.ok_text_view3);

        opTextView1 = (TextView) findViewById(R.id.op_text_view1);
        opTextView2 = (TextView) findViewById(R.id.op_text_view2);
        opTextView3 = (TextView) findViewById(R.id.op_text_view3);

        lspTextView1 = (TextView) findViewById(R.id.lsp_text_view1);
        lspTextView2 = (TextView) findViewById(R.id.lsp_text_view2);
        lspTextView3 = (TextView) findViewById(R.id.lsp_text_view3);
//
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.DashboardItemView, defStyle, 0);
//
//        try {
//            title = a.getString(R.styleable.DashboardItemView_title);
//        } catch (Exception e) {
//            Log.e("DashboardItemView", "There was an error loading attributes.");
//        } finally {
//            a.recycle();
//        }
//
//        setTitleText(title);

    }


    public void setDashboardItem(DashboardItem rkap, DashboardItem ri, DashboardItem prognosa){

        okTextView1.setText(decimalFormat.format(rkap.getOk().doubleValue() / 1000));
        okTextView2.setText(decimalFormat.format(ri.getOk().doubleValue() / 1000));
        okTextView3.setText(decimalFormat.format(prognosa.getOk().doubleValue() / 1000));

        opTextView1.setText(decimalFormat.format(rkap.getOp().doubleValue() / 1000));
        opTextView2.setText(decimalFormat.format(ri.getOp().doubleValue() / 1000));
        opTextView3.setText(decimalFormat.format(prognosa.getOp().doubleValue() / 1000));

        lspTextView1.setText(decimalFormat.format(rkap.getLsp().doubleValue() / 1000));
        lspTextView2.setText(decimalFormat.format(ri.getLsp().doubleValue() / 1000));
        lspTextView3.setText(decimalFormat.format(prognosa.getLsp().doubleValue() / 1000));


    }
}
