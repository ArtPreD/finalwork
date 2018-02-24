package meraya.ua.com.grouper.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import meraya.ua.com.grouper.R;
import meraya.ua.com.grouper.dialoge.CreateGroupActivity;
import meraya.ua.com.grouper.util.DataBase;
import meraya.ua.com.grouper.util.MyCursorLoader;

public class GroupListActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;
    private static final int CM_RENAME_ID = 2;

    ListView lvData;
    DataBase db;
    SimpleCursorAdapter scAdapter;
    static int groupId = 0;
    AdapterContextMenuInfo acmi;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        db = new DataBase(this);
        db.open();

        String[] from = new String[] { DataBase.KEY_IMG, DataBase.KEY_GROUP_NAME };
        int[] to = new int[] { R.id.ivImg, R.id.tvText };

        scAdapter = new SimpleCursorAdapter(this, R.layout.content_group_list, null, from, to, 0);
        lvData = (ListView) findViewById(R.id.lvData);
        lvData.setAdapter(scAdapter);

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String name = "";
                Cursor c = db.getAllGroup();
                if (c.moveToFirst()) {
                    do {
                        if (c.getInt(c.getColumnIndex("_id")) == id){
                            name = c.getString(c.getColumnIndex("group_name"));
                            break;
                        }
                    }while (c.moveToNext());
                }
                 Toast toast = Toast.makeText(GroupListActivity.this, "Позиция в листе: " + position + ", имя группы: "
                 + name + ", ID группы: " + id, Toast.LENGTH_SHORT);
                  toast.show();
                   Intent intent = new Intent(GroupListActivity.this, ListStudByIdActivity.class);
                   intent.putExtra("groupId", "" + id);
                   intent.putExtra("groupName", name);
                   startActivity(intent);
            }
        });

        registerForContextMenu(lvData);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
        menu.add(0, CM_RENAME_ID, 0, "Переименовать");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            db.delGroupRec(acmi.id);
            getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        if (item.getItemId() == CM_RENAME_ID){
            acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivityForResult(intent, 2);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

          if(id == R.id.create_group){
             Intent intent = new Intent(this, CreateGroupActivity.class);
             startActivityForResult(intent, 1);
        }

         if (id == R.id.exit_app) {
             finish();
            }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }
        String name = data.getStringExtra("name");
        if (requestCode == 1) {
             db.addGroupRec(name, scAdapter.getCount(), R.drawable.ic_launcher);
            groupId++;
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
        if (requestCode == 2){
            db.renameGroup(name, acmi.id);
            getSupportLoaderManager().getLoader(0).forceLoad();
        }
    }
}
