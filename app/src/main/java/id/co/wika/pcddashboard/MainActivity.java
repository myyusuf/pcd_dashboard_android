package id.co.wika.pcddashboard;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import id.co.wika.pcddashboard.components.DashboardItemView;
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
        AdapterView.OnItemSelectedListener,
        SimpleDatePickerDialog.OnDateSetListener{

    RecyclerView mRecyclerView;
    private EmployeeAdapter adapter;

    TextView monthSelectLabel;

    FragmentPagerAdapter monthSelectAdapterViewPager;
    FragmentPagerAdapter dashboardAdapterViewPager;

    ViewPager monthSelectPager;
    ViewPager dashboardPager;

    private LkFragment lkFragment;
    private OkFragment okFragment;
    private OpFragment opFragment;
    private LspFragment lspFragment;

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
    private int maxYear = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //IMPORTANT!!!
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        monthSelectLabel = (TextView) findViewById(R.id.month_select_label);

        monthSelectLabel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.v("reload dashboard data", "reload dashboard data");
//                getDashboardData();
//
//                MainActivity.this.okFragment.reload(1, 2017);
//                MainActivity.this.opFragment.reload(1, 2017);
//                MainActivity.this.lkFragment.reload(1, 2017);
//                MainActivity.this.lspFragment.reload(1, 2017);

                SimpleDatePickerDialogFragment datePickerDialogFragment;
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                datePickerDialogFragment = SimpleDatePickerDialogFragment.getInstance(
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
                datePickerDialogFragment.setOnDateSetListener(MainActivity.this);
                datePickerDialogFragment.show(MainActivity.this.getSupportFragmentManager(), null);
            }
        });

//        mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler_view);
//
//        adapter = new EmployeeAdapter();
//        mRecyclerView.setAdapter(adapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//        mRecyclerView.setLayoutManager(layoutManager);

        Calendar calendar = Calendar.getInstance();
        this.selectedYear = calendar.get(Calendar.YEAR);
        this.selectedMonth = calendar.get(Calendar.MONTH);
        if(this.selectedMonth == 0){
            this.selectedYear = this.selectedYear - 1;
            this.selectedMonth = 11;
        }else{
            this.selectedMonth = this.selectedMonth - 1;
        }

        this.maxYear = this.selectedYear;

        makeSpinner();

        getDashboardData();

        dashboardPager = (ViewPager) findViewById(R.id.dashboard_pager);
        dashboardAdapterViewPager = new DashboardPagerAdapter(getSupportFragmentManager());
        dashboardPager.setAdapter(dashboardAdapterViewPager);
        dashboardPager.setOffscreenPageLimit(3);
        dashboardPager.setCurrentItem(0);

        monthSelectPager = (ViewPager) findViewById(R.id.month_select_pager);
        monthSelectAdapterViewPager = new MonthSelectPagerAdapter(getSupportFragmentManager());
        monthSelectPager.setAdapter(monthSelectAdapterViewPager);
        monthSelectPager.setCurrentItem(0);

        dashboardItemView1 = (DashboardItemView) findViewById(R.id.dashboard_item_view_1);
        dashboardItemView2 = (DashboardItemView) findViewById(R.id.dashboard_item_view_2);
        dashboardItemView3 = (DashboardItemView) findViewById(R.id.dashboard_item_view_3);
        dashboardItemView4 = (DashboardItemView) findViewById(R.id.dashboard_item_view_4);
        dashboardItemView5 = (DashboardItemView) findViewById(R.id.dashboard_item_view_5);

        //-------------

        rkapOkTextView = (TextView) findViewById(R.id.dashboard_text11);
        rkapOpTextView = (TextView) findViewById(R.id.dashboard_text12);
        rkapLspTextView = (TextView) findViewById(R.id.dashboard_text13);

        riOkTextView = (TextView) findViewById(R.id.dashboard_text21);
        riOpTextView = (TextView) findViewById(R.id.dashboard_text22);
        riLspTextView = (TextView) findViewById(R.id.dashboard_text23);

        prognosaOkTextView = (TextView) findViewById(R.id.dashboard_text31);
        prognosaOpTextView = (TextView) findViewById(R.id.dashboard_text32);
        prognosaLspTextView = (TextView) findViewById(R.id.dashboard_text33);
    }

    @Override
    public void onDateSet(int year, int monthOfYear) {
        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(year, monthOfYear));
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

        String url = DashboardConstant.BASE_URL + "dashboard";

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

        restRequestService.getRequest(url, listener, getApplicationContext());

    }

    private void updateDashboardItemView(){
        dashboardItemView1.setDashboardItem(this.dashboardItemList.get(3));
        dashboardItemView2.setDashboardItem(this.dashboardItemList.get(4));
        dashboardItemView3.setDashboardItem(this.dashboardItemList.get(5));
        dashboardItemView4.setDashboardItem(this.dashboardItemList.get(6));
        dashboardItemView5.setDashboardItem(this.dashboardItemList.get(7));

        DashboardItem rkap = this.dashboardItemList.get(0);
        rkapOkTextView.setText(decimalFormat.format(rkap.getOk().doubleValue()));
        rkapOpTextView.setText(decimalFormat.format(rkap.getOp().doubleValue()));
        rkapLspTextView.setText(decimalFormat.format(rkap.getLsp().doubleValue()));

        DashboardItem ri = this.dashboardItemList.get(1);
        riOkTextView.setText(decimalFormat.format(ri.getOk().doubleValue()));
        riOpTextView.setText(decimalFormat.format(ri.getOp().doubleValue()));
        riLspTextView.setText(decimalFormat.format(ri.getLsp().doubleValue()));

        DashboardItem prognosa = this.dashboardItemList.get(2);
        prognosaOkTextView.setText(decimalFormat.format(prognosa.getOk().doubleValue()));
        prognosaOpTextView.setText(decimalFormat.format(prognosa.getOp().doubleValue()));
        prognosaLspTextView.setText(decimalFormat.format(prognosa.getLsp().doubleValue()));
    }

    public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public EmployeeAdapter() {
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.updateUI(MainActivity.this.dashboardItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return MainActivity.this.dashboardItemList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false);

            return new ItemViewHolder(card);

        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView dashboardItemTitle;
        private TextView dashboardItemLabel1;
        private TextView dashboardItemLabel2;
        private TextView dashboardItemLabel3;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dashboardItemTitle = (TextView)itemView.findViewById(R.id.dashboard_item_title);
            dashboardItemLabel1 = (TextView)itemView.findViewById(R.id.dashboard_item_label1);
            dashboardItemLabel2 = (TextView)itemView.findViewById(R.id.dashboard_item_label2);
            dashboardItemLabel3 = (TextView)itemView.findViewById(R.id.dashboard_item_label3);

        }

        public void updateUI(DashboardItem dashboardItem) {

            dashboardItemTitle.setText(dashboardItem.getTitle());
            dashboardItemLabel1.setText(decimalFormat.format(dashboardItem.getOk().doubleValue()));
            dashboardItemLabel2.setText(decimalFormat.format(dashboardItem.getOp().doubleValue()));
            dashboardItemLabel3.setText(decimalFormat.format(dashboardItem.getLsp().doubleValue()));

        }
    }

    //------------------------

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

    public void updateDashboardData(int month) {

//        this.selectedMonth = month;
//        this.updateDashboardData();
    }

    private void makeSpinner() {
        Spinner spinner1 = (Spinner) findViewById(R.id.project_month_spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.project_month_array,
                R.layout.project_spinner_item_right
        );

//        List<String> months = new ArrayList<String>();
//        months.add("January");

//        ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter(this, R.layout.project_spinner_item_right, 0,  months);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);

        List<Integer> years = new ArrayList<Integer>();
        for(int i = DashboardConstant.MIN_YEAR; i<= this.maxYear; i++){
            years.add(i);
        }

        Spinner spinner2 = (Spinner) findViewById(R.id.project_year_spinner);

//        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
//                this,
//                R.array.project_year_array,
//                R.layout.project_spinner_item
//        );

        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, R.layout.project_spinner_item, 0,  years);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);

//        spinner1.setSelection(selectedMonth);
//        spinner2.setSelection(selectedYear);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        Spinner spinner = (Spinner) parent;
        Log.v("Spinner : ", "Pos : " + pos);

        if(spinner.getId() == R.id.project_month_spinner){
//            ProjectActivity.this.selectedFilter = pos;
//            ProjectActivity.this.filterProjectList();
        }else if(spinner.getId() == R.id.project_year_spinner){
//            Log.v("spinner", "sort: " + pos);
//
//            ProjectActivity.this.selectedSort = pos;
//            ProjectActivity.this.sortProjectList();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
