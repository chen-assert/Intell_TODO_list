package jingruichen.Intell_TODOlist;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static jingruichen.Intell_TODOlist.MainActivity.smap;

public class AlertView_setting {
    public void bbb(AlertDialog.Builder builder) {
        builder.create().show();
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void showSett(ViewGroup layout, AlertDialog.Builder builder, final Context context, final MainActivity activity) {

        builder.setTitle("Setting");
        builder.setIcon(R.drawable.ic_action_setting);
        builder.setView(layout);
        final Spinner backSpinner;
        final Spinner font1Spinner;
        final Spinner fontSpinner;
        backSpinner = (Spinner) layout.findViewById(R.id.spinner1);
        //font1Spinner = (Spinner) layout.findViewById(R.id.spinner2);
        fontSpinner = (Spinner) layout.findViewById(R.id.spinner3);
        final String[] theme = {"theme1", "theme2", "theme3"};
        //final String[] font1 = {"black", "purple", "green","grey"};
        final String[] font2 = {"DEFAULT", "DEFAULT_BOLD", "MONOSPACE", "SANS_SERIF"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, theme);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, font1);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, font2);
        backSpinner.setAdapter(adapter1);
        //font1Spinner.setAdapter(adapter2);
        fontSpinner.setAdapter(adapter3);
        backSpinner.setSelection(smap.containsKey(1) ? smap.get(1) : 0);
        //font1Spinner.setSelection(smap.containsKey(2)?smap.get(2):0);
        fontSpinner.setSelection(smap.containsKey(3) ? smap.get(3) : 0);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                smap.put(1, backSpinner.getSelectedItemPosition());
                //smap.put(2,font1Spinner.getSelectedItemPosition());
                smap.put(3, fontSpinner.getSelectedItemPosition());
                final DbHelper2 DB = new DbHelper2(context);
                DB.saveObject(smap, "setkey1");

                //restart
                Intent mStartActivity = new Intent(context, MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return;
    }

}
