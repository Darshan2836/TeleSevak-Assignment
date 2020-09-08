package in.darshanudagire.telesevak.task.TabLayout;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import in.darshanudagire.telesevak.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pending#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pending extends Fragment {
    private FloatingActionButton add;
    private TextView empty,addprojecytxt;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<TaskDescription, TaskViewHolder> adapter;
    private FirebaseFirestore rootRef ;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private String pname,phone;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Pending() {
        // Required empty public constructor
    }


    public Pending(String pname, String phone) {
        this.pname = pname;
        this.phone = phone;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pending.
     */
    // TODO: Rename and change types and number of parameters
    public static Pending newInstance(String param1, String param2) {
        Pending fragment = new Pending();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
        View rootview  =inflater.inflate(R.layout.fragment_pending, container, false);

        empty = rootview.findViewById(R.id.emptytext);
        add = rootview.findViewById(R.id.fab);
        addprojecytxt = rootview.findViewById(R.id.addtext);
        recyclerView = rootview.findViewById(R.id.recyclerview);



        //linear layout
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //reference
        rootRef = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //progressdialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..!");
        progressDialog.setCancelable(false);

        updateUi(rootview);

        //add on click listner
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialog cdd = new AddTaskDialog(getActivity(),phone,pname);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();

            }
        });



        return rootview;
    }
    private void updateUi(View rootview) {
        progressDialog.show();

                //reference
               final CollectionReference cref = rootRef.collection("Projects").document(phone).collection("User_Project");
               Query query = cref.whereEqualTo("name",pname);
               query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful())
                       {
                           String id = null;
                           for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                           {
                               id = documentSnapshot.getId();
                           }
                           CollectionReference cref2 = cref.document(id).collection("tasks");
                           Query query1 = cref2.whereEqualTo("isCompleted",false);


                           FirestoreRecyclerOptions<TaskDescription> options = new FirestoreRecyclerOptions.Builder<TaskDescription>()
                                   .setQuery(query1, TaskDescription.class)
                                   .build();

                           final String finalId = id;
                           adapter = new FirestoreRecyclerAdapter<TaskDescription, TaskViewHolder>(options) {

                               @NonNull
                               @Override
                               public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview2, parent, false);
                                   return new TaskViewHolder(view);
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
                               protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull final TaskDescription model) {

                                   final String txttname = model.getName();
                                   holder.tname.setText(txttname);
                                   holder.towner.setText(model.getOwner());
                                   holder.tdate.setText(model.getDue_date());
                                   progressDialog.dismiss();
                                   holder.complete.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           progressDialog.show();
                                           final CollectionReference cref2 = cref.document(finalId).collection("tasks");
                                           Query query2 = cref2.whereEqualTo("name",txttname);
                                           query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   if(task.isSuccessful()) {
                                                       String idtask = null;
                                                       for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                           idtask = documentSnapshot.getId();
                                                       }
                                                       DocumentReference cref3 = cref2.document(idtask);
                                                       cref3.update("isCompleted",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void aVoid) {
                                                               Toast.makeText(getActivity(),"Task Completed",Toast.LENGTH_LONG).show();
                                                               progressDialog.dismiss();

                                                           }
                                                       });
                                                   }
                                               }
                                           });

                                       }
                                   });
                               }
                           };

                           recyclerView.setAdapter(adapter);
                           adapter.startListening();


                       }
                   }
               });



            }
    public interface OnFragmentInteractionListener {
    }
}