package id.co.wika.pcddashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.co.wika.pcddashboard.models.DashboardItem;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private EmployeeAdapter adapter;

    private ArrayList<DashboardItem> dashboardItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //IMPORTANT!!!
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler_view);

        adapter = new EmployeeAdapter();
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);

        getDashboardData();
    }

    private void getDashboardData(){

        String baseURL = DashboardConstant.BASE_URL + "dashboard";

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {

                MainActivity.this.dashboardItemList.clear();

                JSONObject obj1 = null;
                try {
                    obj1 = response.getJSONObject("data1");
                    DashboardItem dashboardItem = new DashboardItem();

                    dashboardItem.setOk(new BigDecimal(obj1.getString("ok")));
                    dashboardItem.setOp(new BigDecimal(obj1.getString("op")));
                    dashboardItem.setLk(new BigDecimal(obj1.getString("lk")));

                    MainActivity.this.dashboardItemList.add(dashboardItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
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

        private TextView dashboardItemLabel1;
//        private TextView dashboardItemLabel2;
//        private TextView dashboardItemLabel3;

        public ItemViewHolder(View itemView) {
            super(itemView);
            dashboardItemLabel1 = (TextView)itemView.findViewById(R.id.dashboard_item_label1);
//            dashboardItemLabel2 = (TextView)itemView.findViewById(R.id.dashboard_item_label2);
//            dashboardItemLabel3 = (TextView)itemView.findViewById(R.id.dashboard_item_label3);

        }

        public void updateUI(DashboardItem dashboardItem) {

            dashboardItemLabel1.setText(dashboardItem.getOk().toString());
//            dashboardItemLabel1.setText(dashboardItem.getBirthDate());
//            dashboardItemLabel1.setText(dashboardItem.getWorkUnit());
        }
    }
}
