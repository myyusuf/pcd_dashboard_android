package id.co.wika.pcddashboard.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.models.DashboardItem;
import id.co.wika.pcddashboard.models.PrognosaPiutangItem;

/**
 * Created by myyusuf on 4/3/17.
 */
public class PrognosaPiutang extends LinearLayout {
    private String title;
    private float mTextWidth;
    private float mTextHeight;

    private TextView pdpTextView;
    private TextView tagBrutoTextView;
    private TextView pUsahaTextView;
    private TextView pRetensiTextView;
    private TextView totalTextView;

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
        pdpTextView = (TextView) findViewById(R.id.pdp_ri);
        tagBrutoTextView = (TextView) findViewById(R.id.tag_bruto_ri);
        pUsahaTextView = (TextView) findViewById(R.id.p_usaha_ri);
        pRetensiTextView = (TextView) findViewById(R.id.p_retensi_ri);
        totalTextView = (TextView) findViewById(R.id.total_ri);
    }

    public void setItem(PrognosaPiutangItem prognosaPiutangItem){

        double pdp = prognosaPiutangItem.getValue1().doubleValue() > 0 ? prognosaPiutangItem.getValue1().doubleValue() / 1000 : 0;
        double tagBruto = prognosaPiutangItem.getValue2().doubleValue() > 0 ? prognosaPiutangItem.getValue2().doubleValue() / 1000 : 0;
        double pUsaha = prognosaPiutangItem.getValue3().doubleValue() > 0 ? prognosaPiutangItem.getValue3().doubleValue() / 1000 : 0;
        double pRetensi = prognosaPiutangItem.getValue4().doubleValue() > 0 ? prognosaPiutangItem.getValue4().doubleValue() / 1000 : 0;
        double total = prognosaPiutangItem.getTotal().doubleValue() > 0 ? prognosaPiutangItem.getTotal().doubleValue() / 1000 : 0;

        pdpTextView.setText(decimalFormat.format(pdp));
        tagBrutoTextView.setText(decimalFormat.format(tagBruto));
        pUsahaTextView.setText(decimalFormat.format(pUsaha));
        pRetensiTextView.setText(decimalFormat.format(pRetensi));
        totalTextView.setText(decimalFormat.format(total));

    }
}
