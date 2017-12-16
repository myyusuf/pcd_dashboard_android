package id.co.wika.pcddashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        usernameEditText = (EditText) findViewById(R.id.username_text);
        usernameEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(usernameEditText, InputMethodManager.SHOW_IMPLICIT);

        passwordEditText = (EditText) findViewById(R.id.password_text);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    LoginActivity.this.login();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void showDialogWindow(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void login() throws Exception{

        this.username = usernameEditText.getText().toString();
        this.password = passwordEditText.getText().toString();

        if(this.username.trim().equals("") || this.password.trim().equals("")){
            showDialogWindow("Login Error", "Wrong Username or Password");
            return;
        }

        String baseURL = DashboardConstant.BASE_URL + "security/signin";

        Response.Listener<String> listener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject loginObject = new JSONObject(response);
                    String token = loginObject.getString("token");
                    Log.v("Token", token);
                    if(token != null){
                        finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }else{
                        showDialogWindow("Login Error", "Wrong Username or Password");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("Login", "Error login status : " + error.getMessage());
                showDialogWindow("Login Error", error.getMessage());
            }

        };

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, baseURL, listener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", LoginActivity.this.username);
                map.put("password", LoginActivity.this.password);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.v("KAT", "MyAuth: authenticating with username : " + LoginActivity.this.username + ", password : " + LoginActivity.this.password);
                Map<String, String> map = new HashMap<String, String>();
//                String key = "Authorization";
//                String encodedString = Base64.encodeToString(
//                        String.format("%s:%s", LoginActivity.this.username, LoginActivity.this.password)
//                                .getBytes(), Base64.NO_WRAP);
//                String value = String.format("Basic %s", encodedString);
//                map.put(key, value);

                map.put("Content-Type", "application/x-www-form-urlencoded");

                return map;
            }
        };

        Volley.newRequestQueue(this).add(jsonRequest);
    }

//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        return super.onCreateView(name, context, attrs);
//    }
}
