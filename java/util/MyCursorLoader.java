package meraya.ua.com.grouper.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

  public class MyCursorLoader extends CursorLoader {

    DataBase db;

    public MyCursorLoader(Context context, DataBase db) {
        super(context);
        this.db = db;
    }

    @Override
    public Cursor loadInBackground() {
        return db.getAllGroup();
    }
}
