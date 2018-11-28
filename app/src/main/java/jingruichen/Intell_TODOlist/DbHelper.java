package jingruichen.Intell_TODOlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper dbhelper = null;

    public static DbHelper getInstance(Context context) {
        if (dbhelper == null) {
            dbhelper = new DbHelper(context);
        }
        return dbhelper;
    }

    private DbHelper(Context context) {
        super(context, "datebase.db", null, 1);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_class_table = "create table if not exists " +
                "mytable(_id integer primary key autoincrement,content text,selfkey text)";
        sqLiteDatabase.execSQL(sql_class_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}