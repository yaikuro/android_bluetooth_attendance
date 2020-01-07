package com.app.bluetoothattendance;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import static com.app.bluetoothattendance.SaveSharedPreference.getUserName;
import static com.app.bluetoothattendance.SaveSharedPreference.setNIM;
import static com.app.bluetoothattendance.SaveSharedPreference.setUserName;


public class RegisterActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TAG = "RegisterActivity";
    private static final String KEY_NAME = "Nama";
    private static final String KEY_NIM  = "NIM";

    private ProgressBar pbbar;
    public String NIM;
    public EditText etfullname, etNIM;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getUserName(this).length()!=0)
        {
            loadDashboard2();
        }
        etfullname  = findViewById(R.id.etFullName);
        etNIM       = findViewById(R.id.etNIM);
        btnRegister = findViewById(R.id.btnRegister);
        pbbar       = findViewById(R.id.pbbar);

        pbbar.setVisibility(View.GONE);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });

        etNIM.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                    return true;
                }
                return false;
            }
        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
    } // End of onCreate


    public class DoLogin extends AsyncTask<String, String, String>
    {
        String z = "";
        Boolean isSuccess = false;

        String userid = etfullname.getText().toString();
        String NIM = etNIM.getText().toString();

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(RegisterActivity.this,z, Toast.LENGTH_SHORT).show();

            if(isSuccess) {
                loadDashboard();
                setUserName(RegisterActivity.this, etfullname.getText().toString());
                setNIM(RegisterActivity.this, etNIM.getText().toString());
            }
            pbbar.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(String... params) {

            if(userid.equals("")&&NIM.equals("")) {
                z = "Please enter full name and NIM";
            }
            else if(userid.equals("")){
                z = "Name cannot be empty";
            }
            else if(NIM.equals("")){
                z = "NIM cannot be empty";
            }
            else if(NIM.length() != 10) {
                z = "NIM should be 10 digits";
            }
            else
            {
                z = "Register successful";
                isSuccess=true;
//                if (isOnline()) {
//                    registerNewUser();
////                    saveUserData();
//
//
//                }
//                else {
//                    z = "Check internet connection";
//                }
            }
            return z;
        }
//        void registerNewUser() {
//            String newUserId2 = userid.replaceAll("\\s+", "") + NIM + "@base.com";
//            String newNIM = NIM.replaceAll("\\s+", "");
//            mAuth.createUserWithEmailAndPassword(newUserId2, newNIM)
//                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//        void saveUserData() {
//            Map<String, Object> userData = new HashMap<>();
//            userData.put(KEY_NIM, NIM);
//            userData.put(KEY_NAME, userid);
//
//            db.collection("UserData").document(NIM)
//                    .set(userData)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "DocumentSnapshot successfully written!");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error writing document", e);
//                        }
//                    });
//        }
    }
    private void loadDashboard(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        setNIM(this,NIM);
        startActivity(i);
        finish();
    }
    private void loadDashboard2(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }


}
