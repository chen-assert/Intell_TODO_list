package jingruichen.Intell_TODOlist;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
        final Spinner font2Spinner;
        backSpinner = (Spinner) layout.findViewById(R.id.spinner1);
        font1Spinner = (Spinner) layout.findViewById(R.id.spinner2);
        font2Spinner = (Spinner) layout.findViewById(R.id.spinner3);
        final String[] back = {"white", "orange", "blue", "red"};
        final String[] font1 = {"black", "purple", "green","grey"};
        final String[] font2 = {"DEFAULT", "DEFAULT_BOLD", "MONOSPACE","SANS_SERIF"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, back);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, font1);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, font2);
        backSpinner.setAdapter(adapter1);
        font1Spinner.setAdapter(adapter2);
        font2Spinner.setAdapter(adapter3);
        backSpinner.setSelection(smap.containsKey(1)?smap.get(1):0);
        font1Spinner.setSelection(smap.containsKey(2)?smap.get(2):0);
        font2Spinner.setSelection(smap.containsKey(3)?smap.get(3):0);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                smap.put(1,backSpinner.getSelectedItemPosition());
                smap.put(2,font1Spinner.getSelectedItemPosition());
                smap.put(3,font2Spinner.getSelectedItemPosition());
                final DbHelper2 DB = new DbHelper2(context);
                DB.saveObject(smap, "setkey1");
                activity.update(0);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        return;
    }

}
