package com.tugasakhir.rakha.tugasakhir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String TAG = "Message";
    EditText txtEmail,txtPwd;
    Button btnLogin,btnRegister,btnGoogle;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        //btnGoogle = (Button)findViewById(R.id.btnGoogle);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPwd= (EditText)findViewById(R.id.txtPwd);



        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sharedPref.getString("username","") != ""){

            Intent keMainMenu = new Intent(getApplicationContext(), Home.class);
            startActivity(keMainMenu);

            finish();
        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txtEmail.getText().toString().isEmpty() && !txtPwd.getText().toString().isEmpty()){
                    (mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPwd.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                               Intent i = new Intent(MainActivity.this, Home.class);
                                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putString("username",txtEmail.getText().toString());
                                editor.putString("password", txtPwd.getText().toString());
                                editor.putString("user", "user");
                                editor.apply();

                                System.out.println("Userbiasa");
                               startActivity(i);
                            }else{

                            }
                        }
                    });
                }else{

                }
            }
        });

        SignInButton loginGoogle = (SignInButton) findViewById(R.id.loginGoogle);
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
    }

    //method untuk mengintegrasikan login dengan google
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void updateUI(FirebaseUser currentUser){
        //Melakukan perpindahan halaman setelah uodate UI ke main activity

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    //untuk mendapatkan token ID dari objek GoogleSignInAccount dan
    //ditukarkan dengan kredensial Firebase, dan dilakukan autentikasi dengan Firebase menggunakan kredensial Firebase yang didapatkan
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //untuk login google
        //hasil yang dikembalikan dari intent yang berasal dari GoogleSignInApi
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                //Jika google sign in berhasil akan dilakukan
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    //untuk proses login menggunakan email dan password firebase
    private void signin(String emails, String passwords){
        //melakukan signin dengan email dan oassword
        mAuth.signInWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            //Menset user yang berhasil login ke variabel
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //menampilkan pesan dengan toast
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


}
