package in.darshanudagire.telesevak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import in.darshanudagire.telesevak.Projects.AddProjectDialog;
import in.darshanudagire.telesevak.Projects.ProjectDescription;
import in.darshanudagire.telesevak.Projects.ViewHolderProjects;
import in.darshanudagire.telesevak.task.TaskToDo;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<ProjectDescription, ViewHolderProjects> adapter;
    private FirebaseFirestore rootRef ;
    private FirebaseAuth auth;
    private String phone;
    private TextView empty,addprojecytxt;
    private CollectionReference cref;
    private ProgressDialog progressDialog,progressDialog1;
    private LinearLayout signout;
    private FloatingActionButton add;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUi();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //recycler view
        recyclerView = findViewById(R.id.recyclerview);
        empty = findViewById(R.id.emptytext);
        signout = findViewById(R.id.linearlayoutsignout);
        add = findViewById(R.id.fab);
        addprojecytxt = findViewById(R.id.addtext);

        //linear layout
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //reference
        rootRef = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //progressdialog
        progressDialog = new ProgressDialog(HomePage.this);
        progressDialog.setMessage("Loading..!");
        progressDialog.setCancelable(false);

        //progressdialog
        progressDialog1 = new ProgressDialog(HomePage.this);
        progressDialog1.setMessage("Signing Out..!");
        progressDialog1.setCancelable(false);

        //signout
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog1.show();
                auth.signOut();
                Intent intent = new Intent(HomePage.this,MainActivity.class);
                progressDialog1.dismiss();
                startActivity(intent);
                finish();
                Toast.makeText(HomePage.this, "Signed Out", Toast.LENGTH_LONG).show();
            }
        });


        //add set on click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProjectDialog cdd = new AddProjectDialog(HomePage.this,phone);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();

            }
        });

        updateUi();



    }
    private void updateUi() {
        progressDialog.show();
        final String uid = auth.getCurrentUser().getUid().toString();
        rootRef.collection("users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                phone = documentSnapshot.get("mobile").toString();

                //reference
                cref = rootRef.collection("Projects").document(phone).collection("User_Project");


                Query query = cref.orderBy("name",Query.Direction.ASCENDING);


                FirestoreRecyclerOptions<ProjectDescription> options = new FirestoreRecyclerOptions.Builder<ProjectDescription>()
                        .setQuery(query, ProjectDescription.class)
                        .build();

                adapter = new FirestoreRecyclerAdapter<ProjectDescription, ViewHolderProjects>(options) {

                    @NonNull
                    @Override
                    public ViewHolderProjects onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
                        return new ViewHolderProjects(view);
                    }


                    @Override
                    public void onDataChanged()
                    {
                        if(getItemCount() == 0)
                        {
                            progressDialog.dismiss();
                            empty.setVisibility(View.VISIBLE);
                            addprojecytxt.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            empty.setVisibility(View.GONE);
                            addprojecytxt.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ViewHolderProjects holder, int position, @NonNull final ProjectDescription model) {

                        holder.name.setText(model.getName());
                        holder.description.setText(model.getDescription());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(HomePage.this, TaskToDo.class);
                                intent.putExtra("name",model.getName());
                                intent.putExtra("description",model.getDescription());
                                intent.putExtra("mobile",phone);
                                startActivity(intent);
                            }
                        });
                        progressDialog.dismiss();
                    }
                };

                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
    }
}