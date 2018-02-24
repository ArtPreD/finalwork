package meraya.ua.com.grouper.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import meraya.ua.com.grouper.R;
import meraya.ua.com.grouper.dialoge.CreateStudentActivity;
import meraya.ua.com.grouper.util.DataBase;

public class ListStudByIdActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    LinearLayout odd, even, delimiter;
    int count;
    LinearLayout.LayoutParams lParams;
    String groupId;
    CheckBox checkCheckBox;
    int id;
    Button buttonCheckAll, buttonReady;
    RadioButton radioStandard;
    boolean isCheckAll;
    ArrayList<String> studList;
    Toast toast;

    DataBase db;
    Cursor c;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stud_by_id);
        odd = (LinearLayout) findViewById(R.id.odd);
        even = (LinearLayout) findViewById(R.id.even);
        delimiter = (LinearLayout) findViewById(R.id.delimiter);
        count = 0;
        lParams = new LinearLayout.LayoutParams(
                wrapContent, wrapContent);
        lParams.setMargins(10,10,10,10);
        db = new DataBase(this);
        db.open();
        intent = getIntent();

        groupId = intent.getStringExtra("groupId");
        String name = intent.getStringExtra("groupName");
        TextView view = (TextView) findViewById(R.id.label);
        view.setText("Группа " + "\"" +  name + "\"");
        createList();

        buttonCheckAll = (Button) findViewById(R.id.checkAll);
        buttonCheckAll.setOnClickListener(this);

        buttonReady = (Button) findViewById(R.id.ready);
        buttonReady.setOnClickListener(this);

        radioStandard = (RadioButton) findViewById(R.id.standard);
        radioStandard.setChecked(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_stud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.addStudent:
                Intent intent = new Intent(this, CreateStudentActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.deleteStud:
                deleteStudents();
                createList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        String name = data.getStringExtra("name");
        String lastName = data.getStringExtra("lastname");
        if (requestCode == 1){
            db.addStudRec(name, lastName, Integer.parseInt(groupId));
            Cursor c = db.getAllStudByGroupId(groupId);
            c.moveToLast();
            id = c.getInt(c.getColumnIndex("_id"));
            addStudent(name, lastName, id);
        }

    }

    private void addStudent(String name, String lastname, int id){
        if(count % 2 == 0) {
            CheckBox box = new CheckBox(this);
            box.setText(name + " " + lastname);
            box.setTextColor(Color.parseColor("#000000"));
            box.setLayoutParams(lParams);
            box.setId(id);
            even.addView(box);
            count++;
        }else {
            CheckBox box = new CheckBox(this);
            box.setText(name + " " + lastname);
            box.setTextColor(Color.parseColor("#000000"));
            box.setLayoutParams(lParams);
            box.setId(id);
            TextView textView = new TextView(this);
            textView.setText("\t");
            delimiter.addView(textView);
            odd.addView(box);
            count++;
        }
    }

    private void deleteStudents(){
        for (int i = 0; i < even.getChildCount(); i++){
            checkCheckBox = (CheckBox) even.getChildAt(i);
            if (checkCheckBox.isChecked()){
                db.delStudRec(checkCheckBox.getId());
                even.removeViewAt(i);
            }
        }
        for (int i = 0; i < odd.getChildCount(); i++){
            checkCheckBox = (CheckBox) odd.getChildAt(i);
            if (checkCheckBox.isChecked()){
                db.delStudRec(checkCheckBox.getId());
                odd.removeViewAt(i);
                delimiter.removeViewAt(i);
            }
        }
    }

    private void createList(){
        even.removeAllViews();
        odd.removeAllViews();
        c = db.getAllStudByGroupId(groupId);
        if (c.moveToFirst()){
            do {
                addStudent(c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("lastname")), c.getInt(c.getColumnIndex("_id")));
            }while (c.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkAll:
                checkOrUncheck(isCheckAll);
                break;

            case R.id.ready:
                studList = createStudList();

                if(studList.isEmpty() && studList.size() == 0){
                    toast = Toast.makeText(ListStudByIdActivity.this, R.string.toast_mess, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 180);
                    toast.show();
                }else {
                    if (radioStandard.isChecked()){
                        runStandard(studList);
                    }else {
                        runRandom(studList);
                    }
                }
        }
    }

    private void runStandard(ArrayList<String> studList) {
    }

    private void runRandom(ArrayList<String> studList) {
    }

    private ArrayList<String> createStudList() {
        return new ArrayList<String>();
    }

    private void checkOrUncheck(boolean isCheckAll) {
        if (isCheckAll) {
            uncheckAll();
            this.isCheckAll = false;
            buttonCheckAll.setText("Выбрать всех");
        } else {
            checkAll();
            this.isCheckAll = true;
            buttonCheckAll.setText("Убрать всех");
        }
    }

    private void checkAll() {
        for (int i = 0; i < even.getChildCount(); i++){
            checkCheckBox = (CheckBox) even.getChildAt(i);
            checkCheckBox.setChecked(true);
        }
        for (int i = 0; i < odd.getChildCount(); i++){
            checkCheckBox = (CheckBox) odd.getChildAt(i);
            checkCheckBox.setChecked(true);
        }
    }

    private void uncheckAll() {
        for (int i = 0; i < even.getChildCount(); i++){
            checkCheckBox = (CheckBox) even.getChildAt(i);
            checkCheckBox.setChecked(false);
        }
        for (int i = 0; i < odd.getChildCount(); i++){
            checkCheckBox = (CheckBox) odd.getChildAt(i);
            checkCheckBox.setChecked(false);
        }
    }
}
