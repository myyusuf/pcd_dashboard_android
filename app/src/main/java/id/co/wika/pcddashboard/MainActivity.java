package id.co.wika.pcddashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.co.wika.pcddashboard.components.DashboardItemView;
import id.co.wika.pcddashboard.components.DashboardRkap;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.fragments.LkFragment;
import id.co.wika.pcddashboard.fragments.LspFragment;
import id.co.wika.pcddashboard.fragments.MonthSelectFragment;
import id.co.wika.pcddashboard.fragments.OkFragment;
import id.co.wika.pcddashboard.fragments.OpFragment;
import id.co.wika.pcddashboard.models.DashboardItem;
import id.co.wika.pcddashboard.services.RestRequestService;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class MainActivity extends AppCompatActivity implements
        LkFragment.OnLkFragmentInteractionListener,
        OkFragment.OnOkFragmentInteractionListener,
        OpFragment.OnOpFragmentInteractionListener,
        LspFragment.OnLspFragmentInteractionListener,
        MonthSelectFragment.OnMonthSelectFragmentInteractionListener,
        SimpleDatePickerDialog.OnDateSetListener{

    TextView monthSelectLabel;

    FragmentPagerAdapter monthSelectAdapterViewPager;
    FragmentPagerAdapter dashboardAdapterViewPager;

    ViewPager monthSelectPager;
    ViewPager dashboardPager;

    private LkFragment lkFragment;
    private OkFragment okFragment;
    private OpFragment opFragment;
    private LspFragment lspFragment;

    private DashboardRkap dashboardRkap1;
    private DashboardItemView dashboardItemView1;
    private DashboardItemView dashboardItemView2;
    private DashboardItemView dashboardItemView3;
    private DashboardItemView dashboardItemView4;
    private DashboardItemView dashboardItemView5;

    private ArrayList<DashboardItem> dashboardItemList = new ArrayList<>();

    RestRequestService restRequestService = new RestRequestService();

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    private TextView rkapOkTextView;
    private TextView rkapOpTextView;
    private TextView rkapLspTextView;

    private TextView riOkTextView;
    private TextView riOpTextView;
    private TextView riLspTextView;

    private TextView prognosaOkTextView;
    private TextView prognosaOpTextView;
    private TextView prognosaLspTextView;

    private int selectedMonth = 1;
    private int selectedYear = 2008;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        //IMPORTANT!!!
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        monthSelectLabel = (TextView) findViewById(R.id.month_select_label);

        monthSelectLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SimpleDatePickerDialogFragment datePickerDialogFragment;
                Calendar calendar = Calendar.getInstance(Locale.getDefault());

//                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(
//                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));

                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(MainActivity.this.selectedYear,
                        MainActivity.this.selectedMonth);

                datePickerDialogFragment.setOnDateSetListener(MainActivity.this);
                datePickerDialogFragment.show(MainActivity.this.getSupportFragmentManager(), null);
            }
        });


        Calendar calendar = Calendar.getInstance();
        this.selectedYear = calendar.get(Calendar.YEAR);
        this.selectedMonth = calendar.get(Calendar.MONTH);
        if(this.selectedMonth == 0){
            this.selectedYear = this.selectedYear - 1;
            this.selectedMonth = 11;
        }else{
            this.selectedMonth = this.selectedMonth - 1;
        }

        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(this.selectedYear, this.selectedMonth));

        getDashboardData();
        getDashboardChartData();

        dashboardPager = (ViewPager) findViewById(R.id.dashboard_pager);
        dashboardAdapterViewPager = new DashboardPagerAdapter(getSupportFragmentManager());
        dashboardPager.setAdapter(dashboardAdapterViewPager);
        dashboardPager.setOffscreenPageLimit(3);
        dashboardPager.setCurrentItem(0);

        monthSelectPager = (ViewPager) findViewById(R.id.month_select_pager);
        monthSelectAdapterViewPager = new MonthSelectPagerAdapter(getSupportFragmentManager());
        monthSelectPager.setAdapter(monthSelectAdapterViewPager);
        monthSelectPager.setCurrentItem(0);

        dashboardRkap1 = (DashboardRkap) findViewById(R.id.dashboard_rkap_1);
        dashboardItemView1 = (DashboardItemView) findViewById(R.id.dashboard_item_view_1);
        dashboardItemView2 = (DashboardItemView) findViewById(R.id.dashboard_item_view_2);
        dashboardItemView3 = (DashboardItemView) findViewById(R.id.dashboard_item_view_3);
        dashboardItemView4 = (DashboardItemView) findViewById(R.id.dashboard_item_view_4);
        dashboardItemView5 = (DashboardItemView) findViewById(R.id.dashboard_item_view_5);

        //-------------
//
//        rkapOkTextView = (TextView) findViewById(R.id.dashboard_text11);
//        rkapOpTextView = (TextView) findViewById(R.id.dashboard_text12);
//        rkapLspTextView = (TextView) findViewById(R.id.dashboard_text13);
//
//        riOkTextView = (TextView) findViewById(R.id.dashboard_text21);
//        riOpTextView = (TextView) findViewById(R.id.dashboard_text22);
//        riLspTextView = (TextView) findViewById(R.id.dashboard_text23);
//
//        prognosaOkTextView = (TextView) findViewById(R.id.dashboard_text31);
//        prognosaOpTextView = (TextView) findViewById(R.id.dashboard_text32);
//        prognosaLspTextView = (TextView) findViewById(R.id.dashboard_text33);

    }

    @Override
    public void onDateSet(int year, int monthOfYear) {
        this.selectedYear = year;
        this.selectedMonth = monthOfYear;

        updateDashboardData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            this.token = "";
            finish();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLkFragmentInteraction(Uri uri) {

    }

    @Override
    public void onOkFragmentInteraction(Uri uri) {

    }

    @Override
    public void onOpFragmentInteraction(Uri uri) {

    }

    @Override
    public void onLspFragmentInteraction(Uri uri) {

    }

    public  class DashboardPagerAdapter extends FragmentPagerAdapter {
        private  int NUM_ITEMS = 4;


        public DashboardPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return OkFragment.newInstance("", "");
                case 1:
                    return OpFragment.newInstance("", "");
                case 2:
                    return LkFragment.newInstance("", "");
                case 3:
                    return LspFragment.newInstance("", "");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    okFragment = (OkFragment) createdFragment;
                    break;
                case 1:
                    opFragment = (OpFragment) createdFragment;
                    break;
                case 2:
                    lkFragment = (LkFragment) createdFragment;
                    break;
                case 3:
                    lspFragment = (LspFragment) createdFragment;
                    break;

            }
            return createdFragment;
        }


    }

    private void getDashboardData(){

        String url = DashboardConstant.BASE_URL + "dashboard/" + selectedYear + "/" + (selectedMonth + 1);

        Log.v("URL", "URL : " + url);

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                MainActivity.this.dashboardItemList.clear();

                JSONObject obj = null;

                for(int i=1; i<=8; i++) {
                    try {
                        obj = response.getJSONObject("data" + i);
                        DashboardItem dashboardItem = new DashboardItem();

                        String title = obj.getString("title") != null ? obj.getString("title") : "-";
                        String ok = obj.getString("ok") != null ? obj.getString("ok") : "0.0";
                        String op = obj.getString("op") != null ? obj.getString("op") : "0.0";
                        String lsp = obj.getString("lsp") != null ? obj.getString("lsp") : "0.0";

                        dashboardItem.setTitle(title);
                        dashboardItem.setOk(new BigDecimal(ok));
                        dashboardItem.setOp(new BigDecimal(op));
                        dashboardItem.setLsp(new BigDecimal(lsp));

                        MainActivity.this.dashboardItemList.add(dashboardItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                adapter.notifyDataSetChanged();
                selectPageOnMonthSelectPager();
                updateDashboardItemView();

            }

        };
//        Response.ErrorListener errorListener = new Response.ErrorListener(){
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // TODO Auto-generated method stub
//                Log.v("Error", "error");
//                error.printStackTrace();
//            }
//
//        };
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, listener, errorListener){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                String key = "Authorization";
//                String encodedString = Base64.encodeToString(String.format("%s:%s", "username", "userpassword").getBytes(), Base64.NO_WRAP);
//                String value = String.format("Basic %s", encodedString);
//                map.put(key, value);
//
//                return map;
//            }
//        };
//
//        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

        restRequestService.getRequest(url, MainActivity.this.token, listener, getApplicationContext());

    }

    private void getDashboardChartData(){

        String url = DashboardConstant.BASE_URL + "dashboard/charts/" + selectedYear;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                try {
                    okFragment.drawChartFromJSONArray(response.getJSONArray("okData"),
                            MainActivity.this.selectedMonth, MainActivity.this.selectedYear);
                    opFragment.drawChartFromJSONArray(response.getJSONArray("opData"),
                            MainActivity.this.selectedMonth, MainActivity.this.selectedYear);
                    lkFragment.drawChartFromJSONArray(response.getJSONArray("lkData"),
                            MainActivity.this.selectedMonth, MainActivity.this.selectedYear);
                    lspFragment.drawChartFromJSONArray(response.getJSONArray("lspData"),
                            MainActivity.this.selectedMonth, MainActivity.this.selectedYear);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        };

        restRequestService.getRequest(url, MainActivity.this.token, listener, getApplicationContext());
    }

    private void updateDashboardItemView(){
        dashboardItemView1.setDashboardItem(this.dashboardItemList.get(3));
        dashboardItemView2.setDashboardItem(this.dashboardItemList.get(4));
        dashboardItemView3.setDashboardItem(this.dashboardItemList.get(5));
        dashboardItemView4.setDashboardItem(this.dashboardItemList.get(6));
        dashboardItemView5.setDashboardItem(this.dashboardItemList.get(7));

        DashboardItem rkap = this.dashboardItemList.get(0);
//        rkapOkTextView.setText(decimalFormat.format(rkap.getOk().doubleValue()));
//        rkapOpTextView.setText(decimalFormat.format(rkap.getOp().doubleValue()));
//        rkapLspTextView.setText(decimalFormat.format(rkap.getLsp().doubleValue()));

        DashboardItem ri = this.dashboardItemList.get(1);
//        riOkTextView.setText(decimalFormat.format(ri.getOk().doubleValue()));
//        riOpTextView.setText(decimalFormat.format(ri.getOp().doubleValue()));
//        riLspTextView.setText(decimalFormat.format(ri.getLsp().doubleValue()));

        DashboardItem prognosa = this.dashboardItemList.get(2);
//        prognosaOkTextView.setText(decimalFormat.format(prognosa.getOk().doubleValue()));
//        prognosaOpTextView.setText(decimalFormat.format(prognosa.getOp().doubleValue()));
//        prognosaLspTextView.setText(decimalFormat.format(prognosa.getLsp().doubleValue()));

        dashboardRkap1.setDashboardItem(rkap, ri, prognosa);
    }


    private MonthSelectFragment monthSelectFragment1;
    private MonthSelectFragment monthSelectFragment2;
    public class MonthSelectPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        public MonthSelectPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return MonthSelectFragment.newInstance(0, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return MonthSelectFragment.newInstance(1, "Page # 2");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    monthSelectFragment1 = (MonthSelectFragment) createdFragment;
                    break;
                case 1:
                    monthSelectFragment2 = (MonthSelectFragment) createdFragment;
                    break;
            }

            return createdFragment;
        }

    }

    @Override
    public void onMonthSelectFragmentInteraction(Uri uri) {
//        selectPageOnMonthSelectPager();
    }

    private void selectPageOnMonthSelectPager(){

        if(this.monthSelectFragment1 != null && this.monthSelectFragment2 != null){
            this.monthSelectFragment1.clearButtonColor();
            this.monthSelectFragment2.clearButtonColor();

            if(this.selectedMonth < 6){
                this.monthSelectFragment1.setActiveButtonColor(this.selectedMonth);
                monthSelectPager.setCurrentItem(0, true);
            }else{
                this.monthSelectFragment2.setActiveButtonColor(this.selectedMonth-6);
                monthSelectPager.setCurrentItem(1, true);
            }
        }

    }


    public void updateDashboardData(int month) {
        this.selectedMonth = month;
        this.updateDashboardData();
    }

    public void updateDashboardData(){
        this.getDashboardData();
        this.getDashboardChartData();
        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(this.selectedYear, this.selectedMonth));
    }


}
