package id.co.wika.pcddashboard.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Locale;

import id.co.wika.pcddashboard.DashboardConstant;
import id.co.wika.pcddashboard.LoginActivity;
import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.components.PrognosaPiutang;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.models.PrognosaPiutangItem;
import id.co.wika.pcddashboard.models.UmurPiutangItem;
import id.co.wika.pcddashboard.services.RestRequestService;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class PrognosaPiutangActivity extends AppCompatActivity implements SimpleDatePickerDialog.OnDateSetListener{

    private TextView monthSelectLabel;
    private int selectedMonth = 1;
    private int selectedYear = 2008;
    private String token;

    RestRequestService restRequestService = new RestRequestService();

    private PrognosaPiutang prognosaPiutang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prognosa_piutang);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        //IMPORTANT!!!
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.android_back_button);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle1 = (TextView) findViewById(R.id.tool_bar_title1);
        toolbarTitle1.setText("Prognosa Piutang");

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

                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(PrognosaPiutangActivity.this.selectedYear,
                        PrognosaPiutangActivity.this.selectedMonth);

                datePickerDialogFragment.setOnDateSetListener(PrognosaPiutangActivity.this);
                datePickerDialogFragment.show(PrognosaPiutangActivity.this.getSupportFragmentManager(), null);
            }
        });

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
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            this.token = "";
            finish();
            Intent intent = new Intent(PrognosaPiutangActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.umur_piutang) {
            Intent intent = new Intent(PrognosaPiutangActivity.this, UmurPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.prognosa_piutang) {
            Intent intent = new Intent(PrognosaPiutangActivity.this, PrognosaPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.bad) {
            Intent intent = new Intent(PrognosaPiutangActivity.this, BadActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        }  else if (id == R.id.cashflow) {
            Intent intent = new Intent(PrognosaPiutangActivity.this, CashFlowActivity.class);
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
        String url = DashboardConstant.BASE_URL + "projections/proyeksi/" + selectedYear + "/" + (selectedMonth + 1);

        Log.v("URL", "URL : " + url);

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                Log.v("Response", response.toString());
                if (response.length() > 0) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        PrognosaPiutangItem item = new PrognosaPiutangItem();
                        item.setValue1(new BigDecimal(jsonObject.getDouble("pdp")));
                        item.setValue2(new BigDecimal(jsonObject.getDouble("tagihanBruto")));
                        item.setValue3(new BigDecimal(jsonObject.getDouble("piutangUsaha")));
                        item.setValue4(new BigDecimal(jsonObject.getDouble("piutangRetensi")));
                        prognosaPiutang.setItem(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        restRequestService.getRequestForArray(url, PrognosaPiutangActivity.this.token, listener, getApplicationContext());
    }
}
