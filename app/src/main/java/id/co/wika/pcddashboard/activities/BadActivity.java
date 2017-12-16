package id.co.wika.pcddashboard.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import id.co.wika.pcddashboard.DashboardConstant;
import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.adapters.BadProject;
import id.co.wika.pcddashboard.adapters.BadProjectAdapter;
import id.co.wika.pcddashboard.components.CustomMarkerView;
import id.co.wika.pcddashboard.components.PrognosaPiutang;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.models.PrognosaPiutangItem;
import id.co.wika.pcddashboard.models.UmurPiutangItem;
import id.co.wika.pcddashboard.services.RestRequestService;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class BadActivity extends AppCompatActivity implements SimpleDatePickerDialog.OnDateSetListener{

    private TextView monthSelectLabel;
    private int selectedMonth = 1;
    private int selectedYear = 2008;
    private String token;

    List<BarEntry> firstDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> secondDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> thirdDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> fourthDataEntries = new ArrayList<BarEntry>();
    List<BarEntry> fifthDataEntries = new ArrayList<BarEntry>();

    private BarChart mChart;
    private RecyclerView recyclerView;
    private BadProjectAdapter badProjectAdapter;

    RestRequestService restRequestService = new RestRequestService();

    private PrognosaPiutang prognosaPiutang;

    private List<BadProject> badProjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        //IMPORTANT!!!
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.android_back_button);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle1 = (TextView) findViewById(R.id.tool_bar_title1);
        toolbarTitle1.setText("Piutang & BAD");

        prognosaPiutang = (PrognosaPiutang) findViewById(R.id.prognosa_piutang);

        Intent intent = getIntent();
        this.token = intent.getStringExtra("token");
        this.selectedYear = intent.getIntExtra("selectedYear", this.selectedYear);
        this.selectedMonth = intent.getIntExtra("selectedMonth", this.selectedMonth);

        monthSelectLabel = (TextView) findViewById(R.id.month_select_label);
        monthSelectLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SimpleDatePickerDialogFragment datePickerDialogFragment;

                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(BadActivity.this.selectedYear,
                        BadActivity.this.selectedMonth);

                datePickerDialogFragment.setOnDateSetListener(BadActivity.this);
                datePickerDialogFragment.show(BadActivity.this.getSupportFragmentManager(), null);
            }
        });

        mChart = (BarChart) findViewById(R.id.bad_chart);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        badProjectAdapter = new BadProjectAdapter(badProjectList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(badProjectAdapter);

        this.updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.dashboard:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(int year, int monthOfYear) {
        this.selectedYear = year;
        this.selectedMonth = monthOfYear;

        updateData();

    }

    private void updateMonthLabel(){
        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(this.selectedYear, this.selectedMonth));
    }

    private void updateData() {
        updateMonthLabel();
        fetchData();
    }

    private void fetchData() {
        String url = DashboardConstant.BASE_URL + "bads/all/" + selectedYear + "/" + (selectedMonth + 1);

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
                badProjectList.clear();
                String[] titles = new String[response.length()];

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        double piutangUsaha = jsonObject.getDouble("piutangUsaha");
                        double tagihanBruto = jsonObject.getDouble("tagihanBruto");
                        double piutangRetensi = jsonObject.getDouble("piutangRetensi");
                        double pdp = jsonObject.getDouble("pdp");
                        double bad = jsonObject.getDouble("bad");
                        String title = jsonObject.getJSONObject("Project").getString("code");
                        firstDataEntries.add(new BarEntry((i + 1), new Float(piutangUsaha)));
                        secondDataEntries.add(new BarEntry((i + 1), new Float(tagihanBruto)));
                        thirdDataEntries.add(new BarEntry((i + 1), new Float(piutangRetensi)));
                        fourthDataEntries.add(new BarEntry((i + 1), new Float(pdp)));
                        fifthDataEntries.add(new BarEntry((i + 1), new Float(bad)));
                        titles[i] = title;

                        BadProject project = new BadProject(title);
                        badProjectList.add(project);

                    }

                    badProjectAdapter.notifyDataSetChanged();
                    drawChart(firstDataEntries, secondDataEntries, thirdDataEntries, fourthDataEntries, fifthDataEntries, titles);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        restRequestService.getRequestForArray(url, BadActivity.this.token, listener, getApplicationContext());
    }

    private void drawChart(
            List<BarEntry> firstDataEntries,
            List<BarEntry> secondDataEntries,
            List<BarEntry> thirdDataEntries,
            List<BarEntry> fourthDataEntries,
            List<BarEntry> fifthDataEntries,
            final String[] axisTitles){
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
                if(valueInInt >= 0 && valueInInt < axisTitles.length){
                    return axisTitles[valueInInt] + "";
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
//        mChart.getXAxis().setAxisMaximum(4);
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
}
