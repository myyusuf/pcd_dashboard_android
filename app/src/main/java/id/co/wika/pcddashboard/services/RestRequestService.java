package id.co.wika.pcddashboard.services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by myyusuf on 2/8/17.
 */
public class RestRequestService {

    public void getRequest(String url, final String token, Response.Listener<JSONObject> listener, Context context){
        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.v("Error", "error");
                error.printStackTrace();
            }

        };
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String key = "Authorization";
//                String encodedString = Base64.encodeToString(String.format("%s:%s", "username", "userpassword").getBytes(), Base64.NO_WRAP);
//                String value = String.format("Bearer %s", encodedString);
                String value = String.format("Bearer %s", token);
                map.put(key, value);

                return map;
            }
        };

        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public void getRequestForArray(String url, final String token, Response.Listener<JSONArray> listener, Context context){
        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.v("Error", "error");
                error.printStackTrace();
            }

        };
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, listener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String key = "Authorization";
//                String encodedString = Base64.encodeToString(String.format("%s:%s", "username", "userpassword").getBytes(), Base64.NO_WRAP);
//                String value = String.format("Bearer %s", encodedString);
                String value = String.format("Bearer %s", token);
                map.put(key, value);

                return map;
            }
        };

        Volley.newRequestQueue(context).add(jsonRequest);
    }
}
