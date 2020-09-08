package in.darshanudagire.telesevak.Projects;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.darshanudagire.telesevak.R;

public class ViewHolderProjects extends RecyclerView.ViewHolder {

    public TextView name , description;

    public ViewHolderProjects(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
    }
}
