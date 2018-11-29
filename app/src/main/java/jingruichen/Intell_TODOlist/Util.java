package jingruichen.Intell_TODOlist;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.*;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class Util {
    public static String Base64encode(Object obj) {
        String string64 = null;
        if (obj instanceof Serializable) {
            // obj must implement Serializable interface, otherwise there will be problems
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                string64 = new String(Base64.encode(baos.toByteArray(), Base64.NO_WRAP));
                string64 = "@test@" + string64;
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
            if (str.equals("") || !str.startsWith("@test@")) {
                return null;
            }
            str = str.substring(6);
            byte[] base64Bytes = Base64.decode(str.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String readText(Uri uri, final MainActivity activity) {
        try {
            InputStream is = activity.getContentResolver().openInputStream(uri);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(input);
            String str = reader.readLine();
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getRes(String type, String name, Activity activity) {
        Resources res = activity.getResources();
        return res.getIdentifier(name, type, activity.getPackageName());
    }

    /**
     * Activity screenCap
     *
     * @param activity
     * @return ref:https://www.jianshu.com/p/820b2f1597d1
     */
    public static Bitmap activityShot(Activity activity) {
        /*获取windows中最顶层的view*/
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        WindowManager windowManager = activity.getWindowManager();

        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                height - statusBarHeight);

        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }


    private static final String SCREENSHOTS_DIR_NAME = "Pictures/Screenshots";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot%s.jpg";
    private static final String SCREENSHOT_FILE_PATH_TEMPLATE = "%s/%s/%s";

    /**
     * 存储到sdcard
     *
     * @param bmp
     * @return
     */
    public static String saveToSD(Bitmap bmp) {
        //判断sd卡是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件名
            long systemTime = System.currentTimeMillis();
            String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(systemTime));
            String mFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);

            File dir = new File(SCREENSHOTS_DIR_NAME);
            //判断文件是否存在，不存在则创建
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //文件全名
            String mstrRootPath = Environment.getExternalStorageDirectory().toString();
            String mFilePath = String.format(SCREENSHOT_FILE_PATH_TEMPLATE, mstrRootPath,
                    SCREENSHOTS_DIR_NAME, mFileName);

            Log.i(TAG, "file path:" + mFilePath);
            File file = new File(mFilePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.i(TAG, "file path:" + file.getAbsolutePath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                if (fos != null) {
                    //第一参数是图片格式，第二参数是图片质量，第三参数是输出流
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return mFilePath;
        }
        return null;
    }


    public static String makedate(int year, int month, int day) {
        return String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day);
    }

    public static String maketime(int hour, int minute) {
        return String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }
}
