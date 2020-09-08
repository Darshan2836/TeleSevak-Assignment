package in.darshanudagire.telesevak.task.TabLayout;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.darshanudagire.telesevak.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    public TextView tname,towner ,tdate;
    public Button complete;


    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);
        tname = itemView.findViewById(R.id.name);
        towner = itemView.findViewById(R.id.owner);
        tdate = itemView.findViewById(R.id.duedate);
        complete = itemView.findViewById(R.id.complete);
    }
}
