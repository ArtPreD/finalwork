package meraya.ua.com.grouper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import meraya.ua.com.grouper.R;
import meraya.ua.com.grouper.service.MyService;

public class MainAuthorizationActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "myTAG";


    private EditText ETemail;
    private EditText ETpassword;
    private Button login;
    private Button signUp;

    private TextView text;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        startService(new Intent(this, MyService.class));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        ETemail = (EditText) findViewById(R.id.EnterUserName);
        ETpassword = (EditText) findViewById(R.id.EnterPassword);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        text = (TextView) findViewById(R.id.textView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.registration){
            text.setText("");
            Intent intent = new Intent(this, RegisterDialogActivity.class);
            startActivityForResult(intent, 1);
        }
        if (id == R.id.Exit){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login:
                    signIn(ETemail.getText().toString(), ETpassword.getText().toString());
                    break;
                case R.id.signUp:
                    text.setText("");
                    Intent intent = new Intent(this, RegisterDialogActivity.class);
                    startActivityForResult(intent, 1);
                    break;
            }
    }

    public void signIn(String email, String password){
        if(!email.isEmpty() && !password.isEmpty())
        mAuth.signInWithEmailAndPassword(email, hashPassword(password)).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainAuthorizationActivity.this, "Авторизация выполнена", Toast.LENGTH_SHORT).show();
                    text.setText("");
                    Intent intent = new Intent(MainAuthorizationActivity.this, GroupListActivity.class);
                    startActivity(intent);
                }else {
                    //text.setText(task.getException().getMessage());
                    if (task.getException().getMessage().equals("The email address is badly formatted.")){
                        text.setText("Неверный формат электронной почты");
                    }else if (task.getException().getMessage().equals("There is no user record corresponding to this" +
                            " identifier. The user may have been deleted.")){
                        text.setText("Такая почта не зарегестрирована. Возможно, этот аккаунт был удален");
                    }else  if (task.getException().getMessage().equals("The password is invalid or the user" +
                            " does not have a password.")){
                        text.setText("Неверно указан пароль");
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null){
            return;
        }
        if (requestCode == 1){
            String email = data.getStringExtra("email");
            String password = data.getStringExtra("password");
            mAuth.createUserWithEmailAndPassword(email, hashPassword(password));
            Toast.makeText(MainAuthorizationActivity.this, "Регистрация прошла успешно", Toast.LENGTH_LONG).show();
        }
    }


    private String hashPassword(String password){
        StringBuffer code = new StringBuffer(); //the hash code
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte bytes[] = password.getBytes();
        byte digest[] = messageDigest.digest(bytes); //create code
        for (int i = 0; i < digest.length; ++i) {
            code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
        }
        return code.toString();
    }
}
