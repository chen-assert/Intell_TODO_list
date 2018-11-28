package jingruichen.Intell_TODOlist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.*;

public class Util {
    public static String Base64encode(Object obj) {
        String string64=null;
        if (obj instanceof Serializable) {
            // obj must implement Serializable interface, otherwise there will be problems
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                string64 = new String(Base64.encode(baos.toByteArray(), Base64.NO_WRAP));
                string64="@test@"+string64;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("the obj must implement Serializable");
        }
        return string64;
    }
    public static Object Base64decode(String str) {
        Object obj = null;
        try {
            if (str.equals("")||!str.startsWith("@test@")) {
                return null;
            }
            str=str.substring(6);
            byte[] base64Bytes = Base64.decode(str.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    public static String readText(Uri uri,final MainActivity activity) {
        try {
            InputStream is = activity.getContentResolver().openInputStream(uri);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
            String str=reader.readLine();
            return str;
        } catch (FileNotFoundException editText) {
            Log.e("1", Log.getStackTraceString(editText));
        } catch (IOException editText) {
            Log.e("1", Log.getStackTraceString(editText));
        }
        return null;
    }
    public static int getRes(String type, String name, Activity activity) {
        Resources res = activity.getResources();
        return res.getIdentifier(name, type, activity.getPackageName());
    }
}
