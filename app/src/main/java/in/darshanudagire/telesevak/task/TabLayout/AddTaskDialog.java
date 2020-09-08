package in.darshanudagire.telesevak.task.TabLayout;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import in.darshanudagire.telesevak.R;

public class AddTaskDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText tname,towner,tdate;
    public Boolean comp;
    private String phone;
    private ProgressDialog progressDialog;
    private String statusspinner,pname;
    private Spinner spinner;

    public AddTaskDialog(@NonNull Activity context, String phone,String pname) {
        super(context);
        this.c = context;
        this.phone = phone;
        this.pname = pname;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_task);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        tname = findViewById(R.id.name);
        towner = findViewById(R.id.owner);
        tdate = findViewById(R.id.duedate);
         spinner = findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(c, R.array.status, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusspinner = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        progressDialog = new ProgressDialog(c);
        progressDialog.setMessage("Adding Data !");
        progressDialog.setCancelable(false);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_yes:
                progressDialog.show();
                final String nametxt = tname.getText().toString();
                final String ownertxt = towner.getText().toString();
                final String duedatetxt = tdate.getText().toString();


                if(statusspinner.equals("true"))
                {
                    comp = true;
                }
                else
                {
                    comp = false;
                }
                //reference
                final CollectionReference cref = FirebaseFirestore.getInstance().collection("Projects").document(phone).collection("User_Project");
                Query query = cref.whereEqualTo("name",pname);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                          if (task.isSuccessful()) {
                                                              String id = null;
                                                              for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                                  id = documentSnapshot.getId();
                                                              }
                                                               TaskDescription taskDescription = new TaskDescription(nametxt,duedatetxt,ownertxt,comp);
                                                              CollectionReference cref1 = FirebaseFirestore.getInstance().collection("Projects").document(phone).collection("User_Project").document(id).collection("tasks");
                                                              String newid = cref1.document().getId();
                                                              cref1.document(newid).set(taskDescription).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                  @Override
                                                                  public void onSuccess(Void aVoid) {
                                                                      Toast.makeText(c,"Task Added",Toast.LENGTH_LONG).show();
                                                                      progressDialog.dismiss();
                                                                      dismiss();
                                                                  }
                                                              });
                                                          }
                                                      }
                });


                break;

            case R.id.btn_no:
                dismiss();
                break;

            default:
                break;

        }
    }
}