package id.co.wika.pcddashboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import id.co.wika.pcddashboard.R;

/**
 * Created by myyusuf on 12/17/17.
 */

public class CashFlowAdapter extends RecyclerView.Adapter<CashFlowAdapter.MyViewHolder> {
    private List<CashFlow> cashFlowList;

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView rkap;
        public TextView rencana;
        public TextView prognosa;
        public TextView realisasi;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            rkap = (TextView) view.findViewById(R.id.rkap);
            rencana = (TextView) view.findViewById(R.id.rencana);
            prognosa = (TextView) view.findViewById(R.id.prognosa);
            realisasi = (TextView) view.findViewById(R.id.realisasi);
        }
    }

    public CashFlowAdapter(List<CashFlow> cashFlowList) {
        this.cashFlowList = cashFlowList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cash_flow_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CashFlow cashFlow = cashFlowList.get(position);
        holder.title.setText(cashFlow.getName());
        holder.rkap.setText(decimalFormat.format(cashFlow.getRkap() / 1000));
        holder.rencana.setText(decimalFormat.format(cashFlow.getRencana() / 1000));
        holder.prognosa.setText(decimalFormat.format(cashFlow.getPrognosa() / 1000));
        holder.realisasi.setText(decimalFormat.format(cashFlow.getRealisasi() / 1000));

    }

    @Override
    public int getItemCount() {
        return cashFlowList.size();
    }

}
