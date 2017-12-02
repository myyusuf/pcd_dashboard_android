package id.co.wika.pcddashboard.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialog;
import id.co.wika.pcddashboard.components.SimpleDatePickerDialogFragment;
import id.co.wika.pcddashboard.utils.DateDisplayUtils;

public class PrognosaPiutangActivity extends AppCompatActivity implements SimpleDatePickerDialog.OnDateSetListener{

    private TextView monthSelectLabel;
    private int selectedMonth = 1;
    private int selectedYear = 2008;
    private String token;

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

        this.updateMonthLabel();
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

//        updateData();
        updateMonthLabel();

    }

    private void updateMonthLabel(){
        monthSelectLabel.setText(DateDisplayUtils.formatMonthYear(this.selectedYear, this.selectedMonth));
    }
}
