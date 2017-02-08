package id.co.wika.pcddashboard.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import id.co.wika.pcddashboard.DashboardConstant;
import id.co.wika.pcddashboard.R;
import id.co.wika.pcddashboard.components.CustomMarkerView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LspFragment.OnLspFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LspFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LspFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnLspFragmentInteractionListener mListener;

    private LineChart mChart;
    private String[] MONTHS;

    public LspFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LspFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LspFragment newInstance(String param1, String param2) {
        LspFragment fragment = new LspFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.MONTHS = new String[14];
        this.MONTHS[0] = "";
        this.MONTHS[1] = DashboardConstant.MONTHS[0];
        this.MONTHS[2] = DashboardConstant.MONTHS[1];
        this.MONTHS[3] = DashboardConstant.MONTHS[2];
        this.MONTHS[4] = DashboardConstant.MONTHS[3];
        this.MONTHS[5] = DashboardConstant.MONTHS[4];
        this.MONTHS[6] = DashboardConstant.MONTHS[5];
        this.MONTHS[7] = DashboardConstant.MONTHS[6];
        this.MONTHS[8] = DashboardConstant.MONTHS[7];
        this.MONTHS[9] = DashboardConstant.MONTHS[8];
        this.MONTHS[10] = DashboardConstant.MONTHS[9];
        this.MONTHS[11] = DashboardConstant.MONTHS[10];
        this.MONTHS[12] = DashboardConstant.MONTHS[11];
        this.MONTHS[13] = "";


        final View view = inflater.inflate(R.layout.fragment_lsp, container, false);

        mChart = (LineChart)view.findViewById(R.id.lsp_chart);

        List<Entry> planDataEntries = new ArrayList<Entry>();
        List<Entry> actualDataEntries = new ArrayList<Entry>();

        planDataEntries.add(new Entry(1, new Float(1)));
        planDataEntries.add(new Entry(2, new Float(4)));
        planDataEntries.add(new Entry(3, new Float(2)));
        planDataEntries.add(new Entry(4, new Float(7)));


        actualDataEntries.add(new Entry(1, new Float(1)));
        actualDataEntries.add(new Entry(2, new Float(3)));

        drawChart(planDataEntries, actualDataEntries);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLspFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLspFragmentInteractionListener) {
            mListener = (OnLspFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLspFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLspFragmentInteraction(Uri uri);
    }

    private void drawChart(List<Entry> planDataEntries, List<Entry> actualDataEntries){

        if(planDataEntries.size() > 11) {
            int lastIndex = planDataEntries.size() - 1;
            float lastValue = planDataEntries.get(lastIndex).getY();
            planDataEntries.add(new Entry(13, lastValue));

        }
        planDataEntries.add(0, new Entry(0, 0));

        if(actualDataEntries.size() > 11) {
            int lastIndex = actualDataEntries.size() - 1;
            float lastValue = actualDataEntries.get(lastIndex).getY();
            actualDataEntries.add(new Entry(13, lastValue));
        }
        actualDataEntries.add(0, new Entry(0, 0));

//        this.planDataEntries = planDataEntries;
//        this.actualDataEntries = actualDataEntries;

        mChart.setMinimumHeight(450);
//        mChart.setDescription("");
//        mChart.setNoDataTextDescription("No data.");


        LineDataSet dataSet = new LineDataSet(planDataEntries, "Plan");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(true);
//        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.parseColor("#8CEAFF"));
        dataSet.setFillColor(Color.parseColor("#8CEAFF"));
        dataSet.setLineWidth(5f);
        dataSet.setCircleColorHole(Color.parseColor("#000000"));
        dataSet.setCircleColor(Color.parseColor("#ffffff"));
        dataSet.setCircleHoleRadius(4f);
        dataSet.setCircleRadius(7f);

        dataSet.setFillAlpha(70);

        LineDataSet dataSet2 = new LineDataSet(actualDataEntries, "Actual");
        dataSet2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet2.setDrawCircles(true);
//        dataSet2.setDrawFilled(true);
        dataSet2.setDrawValues(false);
        dataSet2.setColor(Color.parseColor("#F7E81C"));
        dataSet2.setFillColor(Color.parseColor("#F7E81C"));
        dataSet2.setFillAlpha(200);

        dataSet2.setLineWidth(5f);
        dataSet2.setCircleColorHole(Color.parseColor("#000000"));
        dataSet2.setCircleColor(Color.parseColor("#ffffff"));
        dataSet2.setCircleHoleRadius(4f);
        dataSet2.setCircleRadius(7f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet2);
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);


        DefaultAxisValueFormatter formatter = new DefaultAxisValueFormatter(0) {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int valueInInt = (int) value;
                if(valueInInt >= 0 && valueInInt < LspFragment.this.MONTHS.length){
                    return LspFragment.this.MONTHS[(int) value];
                }else{
                    return "";
                }

            }

            @Override
            public int getDecimalDigits() {  return 0; }
        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        mChart.getAxisLeft().setSpaceBottom(0);
        mChart.setDrawBorders(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setGridColor(Color.WHITE);
        mChart.getXAxis().enableAxisLineDashedLine(4f, 2f, 1f);
        mChart.getLegend().setEnabled(false);

        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
//        mChart.getXAxis().setTextSize(18f);
        mChart.getXAxis().setDrawAxisLine(false);

        mChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        mChart.getAxisLeft().setEnabled(false);
        mChart.setViewPortOffsets(50, 15, 50, 30);

        mChart.setPinchZoom(true);

        mChart.setData(lineData);
        mChart.invalidate();

        mChart.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mChart.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        CustomMarkerView mv = new CustomMarkerView(getActivity().getBaseContext(), R.layout.custom_marker_view);
        mChart.setMarkerView(mv);

//        updateSelectedMonth(selectedMonth);

    }
}