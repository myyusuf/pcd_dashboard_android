package id.co.wika.pcddashboard.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import id.co.wika.pcddashboard.DashboardConstant;
import id.co.wika.pcddashboard.LoginActivity;
import id.co.wika.pcddashboard.MainActivity;
import id.co.wika.pcddashboard.Piutang2Activity;
import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.components.CustomMarkerView;
import id.co.wika.pcddashboard.components.DashboardItemView;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.components.UmurPiutangItemView;
import id.co.wika.pcddashboard.models.DashboardItem;
import id.co.wika.pcddashboard.models.UmurPiutangItem;
import id.co.wika.pcddashboard.services.RestRequestService;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class UmurPiutangActivity extends AppCompatActivity implements
        SimpleDatePickerDialog.OnDateSetListener {

    RestRequestService restRequestService = new RestRequestService();

    List<BarEntry> firstDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> secondDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> thirdDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> fourthDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> fifthDataEntries = new ArrayList<BarEntry>();

    private UmurPiutangItemView umurPiutangItemView1;
    private UmurPiutangItemView umurPiutangItemView2;
    private UmurPiutangItemView umurPiutangItemView3;
    private UmurPiutangItemView umurPiutangItemView4;

    private BarChart mChart;

    private TextView monthSelectLabel;
    private int selectedMonth = 1;
    private int selectedYear = 2008;

    private static final String[] XAXIS_TITLE = new String[]{"PDP", "TAG BRUTO", "PIUTANG USAHA", "PIUTANG RETENSI"};

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umur_piutang);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        //IMPORTANT!!!
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.android_back_button);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle1 = (TextView) findViewById(R.id.tool_bar_title1);
        toolbarTitle1.setText("Umur Piutang");

        Intent intent = getIntent();
        this.token = intent.getStringExtra("token");
        this.selectedYear = intent.getIntExtra("selectedYear", this.selectedYear);
        this.selectedMonth = intent.getIntExtra("selectedMonth", this.selectedMonth);

        monthSelectLabel = (TextView) findViewById(R.id.month_select_label);
        monthSelectLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SimpleDatePickerDialogFragment datePickerDialogFragment;
                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(UmurPiutangActivity.this.selectedYear,
                        UmurPiutangActivity.this.selectedMonth);

                datePickerDialogFragment.setOnDateSetListener(UmurPiutangActivity.this);
                datePickerDialogFragment.show(UmurPiutangActivity.this.getSupportFragmentManager(), null);
            }
        });

        this.updateMonthLabel();

        mChart = (BarChart) findViewById(R.id.piutang2_chart);
        initUmurPiutangItemViews();

        fetchData();
    }

    private void initUmurPiutangItemViews () {
        umurPiutangItemView1 = (UmurPiutangItemView) findViewById(R.id.umur_piutang_item_view_1);
        umurPiutangItemView2 = (UmurPiutangItemView) findViewById(R.id.umur_piutang_item_view_2);
        umurPiutangItemView3 = (UmurPiutangItemView) findViewById(R.id.umur_piutang_item_view_3);
        umurPiutangItemView4 = (UmurPiutangItemView) findViewById(R.id.umur_piutang_item_view_4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            this.token = "";
            finish();
            Intent intent = new Intent(UmurPiutangActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.umur_piutang) {
            Intent intent = new Intent(UmurPiutangActivity.this, UmurPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.prognosa_piutang) {
            Intent intent = new Intent(UmurPiutangActivity.this, PrognosaPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.bad) {
            Intent intent = new Intent(UmurPiutangActivity.this, BadActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        }  else if (id == R.id.cashflow) {
            Intent intent = new Intent(UmurPiutangActivity.this, CashFlowActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else  if (id == R.id.dashboard) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void drawChart(
            List<BarEntry> firstDataEntries,
            List<BarEntry> secondDataEntries,
            List<BarEntry> thirdDataEntries,
            List<BarEntry> fourthDataEntries,
            List<BarEntry> fifthDataEntries
    ){
        this.firstDataEntries = firstDataEntries;
        this.secondDataEntries = secondDataEntries;
        this.thirdDataEntries = thirdDataEntries;
        this.fourthDataEntries = fourthDataEntries;
        this.fifthDataEntries = fifthDataEntries;

        BarChart barChart = mChart;

        mChart.setMinimumHeight(450);
        mChart.getAxisLeft().setSpaceBottom(0);
        mChart.setDrawBorders(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setGridColor(Color.GRAY);
        mChart.getLegend().setEnabled(false);

        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        mChart.getXAxis().setDrawAxisLine(false);

        mChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mChart.getAxisLeft().setEnabled(false);
        mChart.setViewPortOffsets(0, 15, 0, 0);

        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);

        barChart.setDrawBarShadow(false);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);

        DefaultAxisValueFormatter formatter = new DefaultAxisValueFormatter(0) {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int valueInInt = (int) value;
                if(valueInInt >= 0 && valueInInt < UmurPiutangActivity.this.XAXIS_TITLE.length){
                    return XAXIS_TITLE[valueInInt] + "";
                }else{
                    return "";
                }
            }

            @Override
            public int getDecimalDigits() {  return 0; }
        };

        xl.setValueFormatter(formatter);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(formatter);
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinimum(-0.01f); // this replaces setStartAtZero(true
        barChart.getAxisRight().setEnabled(false);

        /*
        //IMPORTANT!!!!
        //data
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"
        */

        //IMPORTANT!!!!
        //data
        float groupSpace = 0.05f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.17f; // x2 dataset
        // (0.18 + 0.02) * 5 + 0.05 = 1.00 -> interval per "group"


        List<BarEntry> yVals1 = firstDataEntries;
        List<BarEntry> yVals2 = secondDataEntries;
        List<BarEntry> yVals3 = thirdDataEntries;
        List<BarEntry> yVals4 = fourthDataEntries;
        List<BarEntry> yVals5 = fifthDataEntries;

        BarDataSet set1, set2, set3, set4, set5;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet)barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet)barChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet)barChart.getData().getDataSetByIndex(2);
            set4 = (BarDataSet)barChart.getData().getDataSetByIndex(3);
            set5 = (BarDataSet)barChart.getData().getDataSetByIndex(4);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            set4.setValues(yVals4);
            set5.setValues(yVals5);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            // create 2 datasets with different types
            set1 = new BarDataSet(yVals1, "Company A");
            set1.setColor(Color.parseColor("#7ED321"));
            set2 = new BarDataSet(yVals2, "Company B");
            set2.setColor(Color.parseColor("#92E9FF"));
            set3 = new BarDataSet(yVals3, "Company C");
            set3.setColor(Color.parseColor("#9339FF"));

            set4 = new BarDataSet(yVals4, "Company D");
            set4.setColor(Color.parseColor("#7337FF"));

            set5 = new BarDataSet(yVals5, "Company E");
            set5.setColor(Color.parseColor("#2119FF"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set2);
            dataSets.add(set1);
            dataSets.add(set3);
            dataSets.add(set4);
            dataSets.add(set5);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }

        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);
        set4.setDrawValues(false);
        set5.setDrawValues(false);

        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(4);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();

        mChart.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChart.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        CustomMarkerView mv = new CustomMarkerView(this.getBaseContext(), R.layout.custom_marker_view);
        mChart.setMarkerView(mv);

    }

    @Override
    public void onDateSet(int year, int monthOfYear) {
        this.selectedYear = year;
        this.selectedMonth = monthOfYear;

        updateData();

    }

    private void updateData(){
//        this.getDashboardData();
//        this.getDashboardChartData();
        this.fetchData();
        this.updateMonthLabel();
    }

    private void updateMonthLabel(){
        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(this.selectedYear, this.selectedMonth));
    }

    private void fetchData() {
        String url = DashboardConstant.BASE_URL + "piutangs/piutang/" + selectedYear + "/" + (selectedMonth + 1);

        Log.v("URL", "URL : " + url);

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                Log.v("Response", response.toString());
                firstDataEntries.clear();
                secondDataEntries.clear();
                thirdDataEntries.clear();
                fourthDataEntries.clear();
                fifthDataEntries.clear();
                double totalPdp1 = 0d;
                double totalPdp2 = 0d;
                double totalPdp3 = 0d;
                double totalPdp4 = 0d;
                double totalPdp5 = 0d;

                double totalTagihanBruto1 = 0d;
                double totalTagihanBruto2 = 0d;
                double totalTagihanBruto3 = 0d;
                double totalTagihanBruto4 = 0d;
                double totalTagihanBruto5 = 0d;

                double totalPiutangUsaha1 = 0d;
                double totalPiutangUsaha2 = 0d;
                double totalPiutangUsaha3 = 0d;
                double totalPiutangUsaha4 = 0d;
                double totalPiutangUsaha5 = 0d;

                double totalPiutangRetensi1 = 0d;
                double totalPiutangRetensi2 = 0d;
                double totalPiutangRetensi3 = 0d;
                double totalPiutangRetensi4 = 0d;
                double totalPiutangRetensi5 = 0d;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        totalPdp1 += jsonObject.getDouble("pdp1");
                        totalPdp2 += jsonObject.getDouble("pdp2");
                        totalPdp3 += jsonObject.getDouble("pdp3");
                        totalPdp4 += jsonObject.getDouble("pdp4");
                        totalPdp5 += jsonObject.getDouble("pdp5");

                        totalTagihanBruto1 += jsonObject.getDouble("tagihanBruto1");
                        totalTagihanBruto2 += jsonObject.getDouble("tagihanBruto2");
                        totalTagihanBruto3 += jsonObject.getDouble("tagihanBruto3");
                        totalTagihanBruto4 += jsonObject.getDouble("tagihanBruto4");
                        totalTagihanBruto5 += jsonObject.getDouble("tagihanBruto5");

                        totalPiutangUsaha1 += jsonObject.getDouble("piutangUsaha1");
                        totalPiutangUsaha2 += jsonObject.getDouble("piutangUsaha2");
                        totalPiutangUsaha3 += jsonObject.getDouble("piutangUsaha3");
                        totalPiutangUsaha4 += jsonObject.getDouble("piutangUsaha4");
                        totalPiutangUsaha5 += jsonObject.getDouble("piutangUsaha5");

                        totalPiutangRetensi1 += jsonObject.getDouble("piutangRetensi1");
                        totalPiutangRetensi2 += jsonObject.getDouble("piutangRetensi2");
                        totalPiutangRetensi3 += jsonObject.getDouble("piutangRetensi3");
                        totalPiutangRetensi4 += jsonObject.getDouble("piutangRetensi4");
                        totalPiutangRetensi5 += jsonObject.getDouble("piutangRetensi5");

                    }
                    firstDataEntries.add(new BarEntry(1, new Float(totalPdp1)));
                    secondDataEntries.add(new BarEntry(1, new Float(totalPdp2)));
                    thirdDataEntries.add(new BarEntry(1, new Float(totalPdp3)));
                    fourthDataEntries.add(new BarEntry(1, new Float(totalPdp4)));
                    fifthDataEntries.add(new BarEntry(1, new Float(totalPdp5)));

                    firstDataEntries.add(new BarEntry(2, new Float(totalTagihanBruto1)));
                    secondDataEntries.add(new BarEntry(2, new Float(totalTagihanBruto2)));
                    thirdDataEntries.add(new BarEntry(2, new Float(totalTagihanBruto3)));
                    fourthDataEntries.add(new BarEntry(2, new Float(totalTagihanBruto4)));
                    fifthDataEntries.add(new BarEntry(2, new Float(totalTagihanBruto5)));

                    firstDataEntries.add(new BarEntry(3, new Float(totalPiutangUsaha1)));
                    secondDataEntries.add(new BarEntry(3, new Float(totalPiutangUsaha2)));
                    thirdDataEntries.add(new BarEntry(3, new Float(totalPiutangUsaha3)));
                    fourthDataEntries.add(new BarEntry(3, new Float(totalPiutangUsaha4)));
                    fifthDataEntries.add(new BarEntry(3, new Float(totalPiutangUsaha5)));

                    firstDataEntries.add(new BarEntry(4, new Float(totalPiutangRetensi1)));
                    secondDataEntries.add(new BarEntry(4, new Float(totalPiutangRetensi2)));
                    thirdDataEntries.add(new BarEntry(4, new Float(totalPiutangRetensi3)));
                    fourthDataEntries.add(new BarEntry(4, new Float(totalPiutangRetensi4)));
                    fifthDataEntries.add(new BarEntry(4, new Float(totalPiutangRetensi5)));

                    UmurPiutangItem umurPiutangItem1 = new UmurPiutangItem();
                    umurPiutangItem1.setTitle("PDP");
                    umurPiutangItem1.setValue1(new BigDecimal(totalPdp1));
                    umurPiutangItem1.setValue2(new BigDecimal(totalPdp2));
                    umurPiutangItem1.setValue3(new BigDecimal(totalPdp3));
                    umurPiutangItem1.setValue4(new BigDecimal(totalPdp4));
                    umurPiutangItem1.setValue5(new BigDecimal(totalPdp5));

                    UmurPiutangItem umurPiutangItem2 = new UmurPiutangItem();
                    umurPiutangItem2.setTitle("Tagihan Bruto");
                    umurPiutangItem2.setValue1(new BigDecimal(totalTagihanBruto1));
                    umurPiutangItem2.setValue2(new BigDecimal(totalTagihanBruto2));
                    umurPiutangItem2.setValue3(new BigDecimal(totalTagihanBruto3));
                    umurPiutangItem2.setValue4(new BigDecimal(totalTagihanBruto4));
                    umurPiutangItem2.setValue5(new BigDecimal(totalTagihanBruto5));

                    UmurPiutangItem umurPiutangItem3 = new UmurPiutangItem();
                    umurPiutangItem3.setTitle("Piutang Usaha");
                    umurPiutangItem3.setValue1(new BigDecimal(totalPiutangUsaha1));
                    umurPiutangItem3.setValue2(new BigDecimal(totalPiutangUsaha2));
                    umurPiutangItem3.setValue3(new BigDecimal(totalPiutangUsaha3));
                    umurPiutangItem3.setValue4(new BigDecimal(totalPiutangUsaha4));
                    umurPiutangItem3.setValue5(new BigDecimal(totalPiutangUsaha5));

                    UmurPiutangItem umurPiutangItem4 = new UmurPiutangItem();
                    umurPiutangItem4.setTitle("Piutang Retensi");
                    umurPiutangItem4.setValue1(new BigDecimal(totalPiutangRetensi1));
                    umurPiutangItem4.setValue2(new BigDecimal(totalPiutangRetensi2));
                    umurPiutangItem4.setValue3(new BigDecimal(totalPiutangRetensi3));
                    umurPiutangItem4.setValue4(new BigDecimal(totalPiutangRetensi4));
                    umurPiutangItem4.setValue5(new BigDecimal(totalPiutangRetensi5));

                    umurPiutangItemView1.setItemValue(umurPiutangItem1);
                    umurPiutangItemView2.setItemValue(umurPiutangItem2);
                    umurPiutangItemView3.setItemValue(umurPiutangItem3);
                    umurPiutangItemView4.setItemValue(umurPiutangItem4);
                    drawChart(firstDataEntries, secondDataEntries, thirdDataEntries, fourthDataEntries, fifthDataEntries);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        restRequestService.getRequestForArray(url, UmurPiutangActivity.this.token, listener, getApplicationContext());
    }
}
