package in.darshanudagire.telesevak.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.darshanudagire.telesevak.MainActivity;
import in.darshanudagire.telesevak.Projects.ProjectDescription;
import in.darshanudagire.telesevak.Projects.ViewHolderProjects;
import in.darshanudagire.telesevak.R;
import in.darshanudagire.telesevak.task.TabLayout.Completed;
import in.darshanudagire.telesevak.task.TabLayout.Pending;

public class TaskToDo extends AppCompatActivity  implements Completed.OnFragmentInteractionListener, Pending.OnFragmentInteractionListener{

    private RecyclerView recyclerView;
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private TextView pname,pdesc;
    private Bundle extras;



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_to_do);

        //recycler view
        recyclerView = findViewById(R.id.recyclerview);
        tableLayout =  findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        pname = findViewById(R.id.myproject);
        pdesc = findViewById(R.id.mydescription);

        extras = getIntent().getExtras();
        String pnamet = extras.getString("name");
        String pdecst = extras.getString("description");
        String phone = extras.getString("mobile");

        pname.setText(pnamet);
        pdesc.setText(pdecst);



        //tablayout
        tableLayout.addTab(tableLayout.newTab().setText("Pending"));
        tableLayout.addTab(tableLayout.newTab().setText("Completed"));
        tableLayout.setTabGravity(tableLayout.GRAVITY_FILL);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(),tableLayout.getTabCount(),pnamet,phone);
        viewPager.setAdapter(pageAdapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
        tableLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}