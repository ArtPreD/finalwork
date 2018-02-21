package meraya.ua.com.grouper.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grouperDB";
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_GROUPS = "groups";

    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "lastname";
    private static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_IMG = "img";
    public static final String KEY_GROUP_NAME = "group_name";

    private static final String DB_USERS_CREATE =
            "create table " + TABLE_STUDENTS + "(" +
                    KEY_ID + " integer primary key autoincrement, " +
                    KEY_NAME + " text, " +
                    KEY_LAST_NAME + " text, " +
                    KEY_GROUP_ID + " integer" +
                    ");";

    private static final String DB_GROUP_CREATE =
            "create table " + TABLE_GROUPS + "(" +
                    KEY_ID + " integer primary key autoincrement, " +
                    KEY_GROUP_ID + " integer, " +
                    KEY_GROUP_NAME + " text, " +
                    KEY_IMG + " integer" +
                    ");";

    static final String LOG_TAG = "myLog";

    private final Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase SQLDataBase;

    public DataBase(Context context){
        mContext = context;
    }

    public void open() {
        mDBHelper = new DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        SQLDataBase = mDBHelper.getWritableDatabase();
    }

    public void close(){
        if (mDBHelper != null){
            mDBHelper.close();
        }
    }

    public Cursor getDataById(int id){
        //TODO
        return null;
    }

    public Cursor getAllStudByGroupId(String groupId){
        String whereClause = KEY_GROUP_ID + "=?";
        String[] whereArgs = new String[]{groupId};
        return SQLDataBase.query(TABLE_STUDENTS, null, whereClause, whereArgs, null, null, null);
    }

    public Cursor getAllGroup(){
        return SQLDataBase.query(TABLE_GROUPS, null, null, null, null, null, null);
    }

    public void addStudRec(String name, String lastName, int groupId){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_LAST_NAME, lastName);
        cv.put(KEY_GROUP_ID, groupId);
        SQLDataBase.insert(TABLE_STUDENTS, null, cv);
    }

    public void addGroupRec(String name, int groupId, int img){
        ContentValues cv = new ContentValues();
        cv.put(KEY_GROUP_NAME, name);
        cv.put(KEY_GROUP_ID, groupId);
        cv.put(KEY_IMG, img);
        SQLDataBase.insert(TABLE_GROUPS, null, cv);
    }

    public void delStudRec(long id){
        SQLDataBase.delete(TABLE_STUDENTS, KEY_ID + " = " + id, null);
        String whereClause = KEY_GROUP_ID + "=?";
        String[] whereArgs = new String[]{id+""};
        SQLDataBase.delete(TABLE_STUDENTS, whereClause, whereArgs);
    }

    public void delGroupRec(long id){
        SQLDataBase.delete(TABLE_GROUPS, KEY_ID + " = " + id, null);
    }

    public void renameStudent(String name, String lastname, int id){
        //TODO
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_LAST_NAME, lastname);
        SQLDataBase.update(TABLE_STUDENTS, cv, "id = " + id, null);
    }

    public void renameGroup(String name, long id){
        Log.d(LOG_TAG, "name = " + name + ", ID = " + id);
        ContentValues cv = new ContentValues();
        cv.put(KEY_GROUP_NAME, name);
        SQLDataBase.update(TABLE_GROUPS, cv, KEY_ID + " = " + id, null);
    }

    private class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DataBase.DB_USERS_CREATE);
            sqLiteDatabase.execSQL(DataBase.DB_GROUP_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
}
