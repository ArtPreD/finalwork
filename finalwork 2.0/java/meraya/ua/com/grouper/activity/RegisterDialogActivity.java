package meraya.ua.com.grouper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import meraya.ua.com.grouper.R;
import meraya.ua.com.grouper.util.DataBase;
import meraya.ua.com.grouper.util.MyCursorLoader;

public class RegisterDialogActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editName, editLastName, editEmail, editPassword,
            editConfirmPassword, editPhone;
//    private RadioGroup sexRadio;
    private Button signUp, cancel;

    private String name, lastName, email, password;

    private DataBase dataBase;
    private MyCursorLoader cursorLoader;

    private FirebaseAuth mAuth;

    private TextView result;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_dialog);

        editName = (EditText) findViewById(R.id.editName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
       // editPhone = (EditText) findViewById(R.id.editPhone);

        signUp = (Button) findViewById(R.id.signUpReg);
        signUp.setOnClickListener(this);

        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

//        sexRadio = (RadioGroup) findViewById(R.id.sexRadio);

        result = (TextView) findViewById(R.id.result);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpReg:
                if (validateData()) {
                    Intent intent = new Intent();
                    intent.putExtra("email", editEmail.getText().toString());
                    intent.putExtra("password", editPassword.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }



    public void registration(String email, String password){
        if(!email.isEmpty() && !password.isEmpty())
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterDialogActivity.this, "Регистрация выполнена", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegisterDialogActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private boolean validateData(){
        boolean name = false;
        boolean email = false;
//        boolean phone = false;
        boolean password = false;

        switch (isNameValid(editName.getText().toString(), editLastName.getText().toString())){
            case 0:
                this.name = editName.getText().toString();
                this.lastName = editLastName.getText().toString();
                name = true;
                break;
            case 1:
                result.setText("Имя и фамилия должны содержать только русские буквы");
        }
//        switch (isNumberValid()) {
//            case 0:
//                this.phone = editPhone.getText().toString();
//                phone = true;
//                break;
//            case 1:
//                result.setText("Номер должен начинаться с +380");
//                break;
//            case 2:
//                result.setText("Номер должен содержать только цифры");
//                break;
//            case 3:
//                result.setText("Номер должен содержать 13 знаков (считая +38)");
//        }
        switch (isEmailValid()){
            case 0:
                this.email = editEmail.getText().toString();
                email = true;
                break;
            case 1:
                result.setText("Неправильный формать электронного адреса");
        }

        switch (isPasswordConfirm()){
            case 0:
                this.password = editPassword.getText().toString();
                password = true;
                break;
            case 1:
                result.setText("Пароли не совпадают");
        }

        return (name && email && password);
    }

//    private int isNumberValid(){
//        if (!editPhone.getText().toString().contains("+380")) {
//            return 1;
//        }
//            Pattern p = Pattern.compile("[0-9+]+");
//            Matcher m = p.matcher(editPhone.getText());
//            if (!m.matches()) {
//                return 2;
//            }
//            if (editPhone.getText().toString().length() != 13){
//                return 3;
//            }
//        return 0;
//    }

    private int isNameValid(String name, String lastName){
        String temp = name + lastName;
        Pattern p = Pattern.compile("[a-zA-Zа-яА-Я]+");
        Matcher m = p.matcher(temp);
        if (!m.matches()) {
            return 1;
        }
        return 0;
    }

    private int isEmailValid(){
        Pattern p = Pattern.compile("^[-a-z0-9!#$%&'*+/=?^_`{|}~]+(?:\\.[-a-z0-9!#$%&'*+/=?^_`{|}~]+)*@(?:[a-z0-9]([-a-z0-9]{0,61}[a-z0-9])?\\.)*(?:aero|arpa|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|[a-z][a-z])$");
        Matcher m = p.matcher(editEmail.getText().toString());
        if (!m.matches()) {
            return 1;
        }
        return 0;
    }

    private int isPasswordConfirm(){
        String pass = editPassword.getText().toString();
        String confirm = editConfirmPassword.getText().toString();
        if (!pass.equals(confirm)){
            return 1;
        }
        return 0;
    }
}
