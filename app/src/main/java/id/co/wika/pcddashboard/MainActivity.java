package id.co.wika.pcddashboard;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.wika.pcddashboard.fragments.ProgFragment;
import id.co.wika.pcddashboard.fragments.RiFragment;
import id.co.wika.pcddashboard.fragments.RkapFragment;
import id.co.wika.pcddashboard.models.DashboardItem;

public class MainActivity extends AppCompatActivity implements
        RkapFragment.OnRkapFragmentInteractionListener,
        RiFragment.OnRiFragmentInteractionListener,
        ProgFragment.OnProgFragmentInteractionListener{

    RecyclerView mRecyclerView;
    private EmployeeAdapter adapter;

    TextView monthSelectLabel;

    FragmentPagerAdapter dashboardAdapterViewPager;
    ViewPager dashboardPager;

    private RkapFragment rkapFragment;
    private RiFragment riFragment;
    private ProgFragment progFragment;

    private ArrayList<DashboardItem> dashboardItemList = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

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
        dashboardPager.setCurrentItem(1);
    }

    @Override
    public void onRkapFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRiFragmentInteraction(Uri uri) {

    }

    @Override
    public void onProgFragmentInteraction(Uri uri) {

    }

    public  class DashboardPagerAdapter extends FragmentPagerAdapter {
        private  int NUM_ITEMS = 3;


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
                    return RkapFragment.newInstance("", "");
                case 1:
                    return RiFragment.newInstance("", "");
                case 2:
                    return ProgFragment.newInstance("", "");
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
                    rkapFragment = (RkapFragment) createdFragment;
                    break;
                case 1:
                    riFragment = (RiFragment) createdFragment;
                    break;
                case 2:
                    progFragment = (ProgFragment) createdFragment;
                    break;

            }
            return createdFragment;
        }


    }

    private void getDashboardData(){

        String baseURL = DashboardConstant.BASE_URL + "dashboard";

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

            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.v("Error", "error");
            }

        };
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, baseURL, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String key = "Authorization";
                String encodedString = Base64.encodeToString(String.format("%s:%s", "username", "userpassword").getBytes(), Base64.NO_WRAP);
                String value = String.format("Basic %s", encodedString);
                map.put(key, value);

                return map;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

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
}
