package id.co.wika.pcddashboard.activities;

import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import id.co.wika.pcddashboard.DashboardConstant;
import id.co.wika.pcddashboard.LoginActivity;
import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.adapters.BadProject;
import id.co.wika.pcddashboard.adapters.BadProjectAdapter;
import id.co.wika.pcddashboard.adapters.CashFlow;
import id.co.wika.pcddashboard.adapters.CashFlowAdapter;
import id.co.wika.pcddashboard.components.PrognosaPiutang;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.models.PrognosaPiutangItem;
import id.co.wika.pcddashboard.services.RestRequestService;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class CashFlowActivity extends AppCompatActivity implements SimpleDatePickerDialog.OnDateSetListener{

    private TextView monthSelectLabel;
    private int selectedMonth = 1;
    private int selectedYear = 2008;
    private String token;

    private RecyclerView recyclerView;
    private CashFlowAdapter cashFlowAdapter;
    private List<CashFlow> cashFlowList = new ArrayList<>();

    RestRequestService restRequestService = new RestRequestService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_flow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        //IMPORTANT!!!
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.android_back_button);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle1 = (TextView) findViewById(R.id.tool_bar_title1);
        toolbarTitle1.setText("Cash Flow");

        Intent intent = getIntent();
        this.token = intent.getStringExtra("token");
        this.selectedYear = intent.getIntExtra("selectedYear", this.selectedYear);
        this.selectedMonth = intent.getIntExtra("selectedMonth", this.selectedMonth);

        monthSelectLabel = (TextView) findViewById(R.id.month_select_label);
        monthSelectLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SimpleDatePickerDialogFragment datePickerDialogFragment;

                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(CashFlowActivity.this.selectedYear,
                        CashFlowActivity.this.selectedMonth);

                datePickerDialogFragment.setOnDateSetListener(CashFlowActivity.this);
                datePickerDialogFragment.show(CashFlowActivity.this.getSupportFragmentManager(), null);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cashFlowAdapter = new CashFlowAdapter(cashFlowList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cashFlowAdapter);

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
            Intent intent = new Intent(CashFlowActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.umur_piutang) {
            Intent intent = new Intent(CashFlowActivity.this, UmurPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.prognosa_piutang) {
            Intent intent = new Intent(CashFlowActivity.this, PrognosaPiutangActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        } else if (id == R.id.bad) {
            Intent intent = new Intent(CashFlowActivity.this, BadActivity.class);
            intent.putExtra("token", this.token);
            intent.putExtra("selectedYear", this.selectedYear);
            intent.putExtra("selectedMonth", this.selectedMonth);
            startActivity(intent);

            return true;
        }  else if (id == R.id.cashflow) {
            Intent intent = new Intent(CashFlowActivity.this, CashFlowActivity.class);
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
        String url = DashboardConstant.BASE_URL + "cashflows/all/" + selectedYear + "/" + (selectedMonth + 1);

        Log.v("URL", "URL : " + url);

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>(){

            @Override
            public void onResponse(JSONArray response) {
                Log.v("Response", response.toString());
                String[] titles = new String[response.length()];

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Integer typeCode = jsonObject.getJSONObject("CashFlow").getInt("typeCode");
                        String name = "";
                        switch (typeCode) {
                            case 1:
                                name = "Saldo Awal";
                                break;
                            case 2:
                                name = "Penerimaan";
                                break;
                            case 3:
                                name = "Pengeluaran";
                                break;
                            case 4:
                                name = "Kelebihan / (Kekurangan) Kas";
                                break;
                            case 5:
                                name = "Setoran / Pinjaman / TTP";
                                break;
                            case 6:
                                name = "Saldo Kas Akhir Bulan";
                                break;
                            case 7:
                                name = "Saldo Awal R / K";
                                break;
                            case 8:
                                name = "Jumlah Mutasi";
                                break;
                            case 9:
                                name = "Jumlah R / K";
                                break;
                            case 10:
                                name = "Jumlah Pemakaian Dana";
                                break;
                            case 11:
                                name = "Total Bunga";
                                break;
                            case 12:
                                name = "Saldo Akhir Pemakaian Dana";
                                break;
                        }

                        CashFlow cashFlow = new CashFlow(name);
                        cashFlow.setRkap(jsonObject.getJSONObject("CashFlow").getDouble("rkap"));
                        cashFlow.setRencana(jsonObject.getDouble("ra"));
                        cashFlow.setPrognosa(jsonObject.getDouble("prog"));
                        cashFlow.setRealisasi(jsonObject.getDouble("ri"));
                        cashFlowList.add(cashFlow);

                    }

                    cashFlowAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        restRequestService.getRequestForArray(url, CashFlowActivity.this.token, listener, getApplicationContext());
    }
}
