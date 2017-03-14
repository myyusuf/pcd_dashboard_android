package id.co.wika.pcddashboard;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.co.wika.pcddashboard.components.DashboardItemView;
import id.co.wika.pcddashboard.fragments.LkFragment;
import id.co.wika.pcddashboard.fragments.LspFragment;
import id.co.wika.pcddashboard.fragments.MonthSelectFragment;
import id.co.wika.pcddashboard.fragments.OkFragment;
import id.co.wika.pcddashboard.fragments.OpFragment;
import id.co.wika.pcddashboard.fragments.RkapTabFragment;
import id.co.wika.pcddashboard.models.DashboardItem;
import id.co.wika.pcddashboard.services.RestRequestService;

import android.support.v4.app.FragmentTabHost;

public class MainActivity extends AppCompatActivity implements
        LkFragment.OnLkFragmentInteractionListener,
        OkFragment.OnOkFragmentInteractionListener,
        OpFragment.OnOpFragmentInteractionListener,
        LspFragment.OnLspFragmentInteractionListener,
        MonthSelectFragment.OnMonthSelectFragmentInteractionListener{

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

    private ArrayList<DashboardItem> dashboardItemList = new ArrayList<>();

    RestRequestService restRequestService = new RestRequestService();

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    private FragmentTabHost mTabHost;

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
                getDashboardData();

                MainActivity.this.okFragment.reload(1, 2017);
                MainActivity.this.opFragment.reload(1, 2017);
                MainActivity.this.lkFragment.reload(1, 2017);
                MainActivity.this.lspFragment.reload(1, 2017);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler_view);

        adapter = new EmployeeAdapter();
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

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

        //-------------

//        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
//
//        mTabHost.addTab(
//                mTabHost.newTabSpec("tab1").setIndicator("RKAP", null),
//                RkapTabFragment.class, null);
//        mTabHost.addTab(
//                mTabHost.newTabSpec("tab2").setIndicator("Ri", null),
//                RkapTabFragment.class, null);
//        mTabHost.addTab(
//                mTabHost.newTabSpec("tab3").setIndicator("Prog", null),
//                RkapTabFragment.class, null);
//
//        for(int i=0;i<mTabHost.getTabWidget().getChildCount();i++)
//        {
//            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
//            tv.setText("Test");
//            tv.setTextColor(Color.parseColor("#ffffff"));
//        }

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        host.addTab(spec);
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

                for(int i=1; i<=4; i++) {
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

                adapter.notifyDataSetChanged();
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
        dashboardItemView1.setDashboardItem(this.dashboardItemList.get(0));
        dashboardItemView2.setDashboardItem(this.dashboardItemList.get(1));
        dashboardItemView3.setDashboardItem(this.dashboardItemList.get(2));
        dashboardItemView4.setDashboardItem(this.dashboardItemList.get(3));
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
}
