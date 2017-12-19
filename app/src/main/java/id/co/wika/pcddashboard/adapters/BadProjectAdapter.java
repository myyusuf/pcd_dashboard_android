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

public class BadProjectAdapter extends RecyclerView.Adapter<BadProjectAdapter.MyViewHolder> {
    private List<BadProject> projectList;

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###.00");

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView piutangUsaha;
        public TextView tagihanBruto;
        public TextView piutangRetensi;
        public TextView pdp;
        public TextView bad;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            piutangUsaha = (TextView) view.findViewById(R.id.piutang_usaha);
            tagihanBruto = (TextView) view.findViewById(R.id.tagihan_bruto);
            piutangRetensi = (TextView) view.findViewById(R.id.piutang_retensi);
            pdp = (TextView) view.findViewById(R.id.pdp);
            bad = (TextView) view.findViewById(R.id.bad);
        }
    }

    public BadProjectAdapter(List<BadProject> projectList) {
        this.projectList = projectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bad_project_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BadProject project = projectList.get(position);
        holder.title.setText(project.getName());
        holder.piutangUsaha.setText(decimalFormat.format(project.getPiutangUsaha() / 1000));
        holder.tagihanBruto.setText(decimalFormat.format(project.getTagihanBruto() / 1000));
        holder.piutangRetensi.setText(decimalFormat.format(project.getPiutangRetensi() / 1000));
        holder.pdp.setText(decimalFormat.format(project.getPdp() / 1000));
        holder.bad.setText(decimalFormat.format(project.getBad() / 1000));

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

}
