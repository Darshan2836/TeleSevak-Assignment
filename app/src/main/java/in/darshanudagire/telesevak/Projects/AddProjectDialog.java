package in.darshanudagire.telesevak.Projects;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import in.darshanudagire.telesevak.R;

public class AddProjectDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public EditText pname,pdescription;
    private String phone;
    private ProgressDialog progressDialog;

    public AddProjectDialog(@NonNull Activity context,String phone) {
        super(context);
        this.c = context;
        this.phone = phone;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_project);

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        pname = findViewById(R.id.name);
        pdescription = findViewById(R.id.description);

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
                String nametxt = pname.getText().toString();
                String desctxt = pdescription.getText().toString();
                ProjectDescription projectDescription = new ProjectDescription(nametxt,desctxt);
                CollectionReference cref = FirebaseFirestore.getInstance().collection("Projects").document(phone).collection("User_Project");
                String id = cref.document().getId();
                cref.document(id).set(projectDescription).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c,"Project Added",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        dismiss();
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
