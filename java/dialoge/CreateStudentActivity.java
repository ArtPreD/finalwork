package meraya.ua.com.grouper.dialoge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import meraya.ua.com.grouper.R;

public class CreateStudentActivity extends AppCompatActivity implements View.OnClickListener {

    Button ok, cancel;
    EditText eName, eLastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);



        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        eName = (EditText) findViewById(R.id.enterName);
        eLastname = (EditText) findViewById(R.id.enterLastname);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ok:
                Intent intent = new Intent();
                intent.putExtra("name", eName.getText().toString());
                intent.putExtra("lastname", eLastname.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.cancel:
                finish();
                break;
            default:
                finish();
                break;
        }
    }
}
