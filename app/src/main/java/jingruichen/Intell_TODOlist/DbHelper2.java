package jingruichen.Intell_TODOlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DbHelper2 {
    Context context;

    public DbHelper2(Context context) {
        this.context = context;
    }

    /**
     * save
     *
     * @param object
     * @param selfkey
     */
    public void saveObject(Object object, String selfkey) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            DbHelper dbhelper = DbHelper.getInstance(context);
            SQLiteDatabase database = dbhelper.getWritableDatabase();
            database.execSQL("INSERT INTO mytable (content,selfkey) VALUES(?,?)", new Object[]{data, selfkey});
            database.execSQL("UPDATE mytable SET content=(?) WHERE selfkey =(?)", new Object[]{data, selfkey});
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getObject(String selfkey) {
        Object object = null;
        DbHelper dbhelper = DbHelper.getInstance(context);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from mytable where selfkey=(?)", new String[]{selfkey});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("content"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    object = inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return object;

    }
}