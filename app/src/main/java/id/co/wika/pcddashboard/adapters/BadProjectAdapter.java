package id.co.wika.pcddashboard.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.co.wika.pcddashboard.R;

/**
 * Created by myyusuf on 12/17/17.
 */

public class BadProjectAdapter extends RecyclerView.Adapter<BadProjectAdapter.MyViewHolder> {
    private List<BadProject> projectList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
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

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

}
