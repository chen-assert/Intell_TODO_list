package jingruichen.Intell_TODOlist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static jingruichen.Intell_TODOlist.MainActivity.cookie;
import static jingruichen.Intell_TODOlist.MainActivity.llist;
import static jingruichen.Intell_TODOlist.MainActivity.tabLayout;

public class AlertView {
    public void bbb(AlertDialog.Builder builder) {
        builder.create().show();
    }

    Calendar c;
    int year;
    int month;
    int day;
    int hour;
    int minute;

    public void addEvent(ViewGroup layout, AlertDialog.Builder builder, final Context context,
                         final MainActivity activity, final int pos1,final int pos2) {
        if(pos1==-1&&pos2==-1) {
            builder.setTitle("Add new event");
            builder.setIcon(R.drawable.ic_action_plus1);
            builder.setView(layout);
            final int index = tabLayout.getSelectedTabPosition();
            final EditText edit1 = layout.findViewById(R.id.name);
            final EditText edit2 = layout.findViewById(R.id.priority);
            final TextView edit3 = layout.findViewById(R.id.additional);
            final TextView text1 = layout.findViewById(R.id.timeText);
            final TextView text2 = layout.findViewById(R.id.dateText);
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            text1.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
            text2.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
            builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String name = edit1.getText().toString();
                    final String priority = edit2.getText().toString();
                    final String comment = edit3.getText().toString();

                    llist.get(index).add(new Item(name, priority, c, comment));
                    activity.update(index);
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            hour = selectedHour;
                            minute = selectedMinute;
                            text1.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month - 1);
                            c.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
            text2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            year = selectedyear;
                            month = selectedmonth + 1;
                            day = selectedday;
                            text2.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month - 1);
                            c.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }, year, month, day);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            });
            return;
        }else{
            builder.setTitle("Your event");
            builder.setIcon(R.drawable.ic_action_change);
            builder.setView(layout);
            final int index = tabLayout.getSelectedTabPosition();
            final EditText edit1 = layout.findViewById(R.id.name);
            final EditText edit2 = layout.findViewById(R.id.priority);
            final TextView edit3 = layout.findViewById(R.id.additional);
            final TextView text1 = layout.findViewById(R.id.timeText);
            final TextView text2 = layout.findViewById(R.id.dateText);
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            text1.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
            text2.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
            builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String name = edit1.getText().toString();
                    final String priority = edit2.getText().toString();
                    final String comment = edit3.getText().toString();

                    llist.get(index).add(new Item(name, priority, c, comment));
                    activity.update(index);
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            hour = selectedHour;
                            minute = selectedMinute;
                            text1.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month - 1);
                            c.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
            text2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final DatePickerDialog mDatePicker;
                    mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            year = selectedyear;
                            month = selectedmonth + 1;
                            day = selectedday;
                            text2.setText(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day));
                            c.set(Calendar.HOUR_OF_DAY, hour);
                            c.set(Calendar.MINUTE, minute);
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month - 1);
                            c.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }, year, month, day);
                    mDatePicker.setTitle("Select Date");
                    mDatePicker.show();
                }
            });
            return;
        }
    }

    public void pageName(ViewGroup layout, AlertDialog.Builder builder, final Context context,
                         final MainActivity activity) {
        builder.setTitle("Page name");
        builder.setView(layout);
        final int index = tabLayout.getSelectedTabPosition();
        final EditText edit1 = layout.findViewById(R.id.name);
        edit1.setText(tabLayout.getTabAt(index).getText());
        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String name = edit1.getText().toString();
                llist.get(index).set(0, name);
                tabLayout.getTabAt(index).setText(edit1.getText());
                activity.update(index);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //todo:repair
        /*
        builder.setNeutralButton("delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                fragmentList.remove(index);
                llist.remove(index);
                fragmentAdapter.notifyDataSetChanged();
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    tabLayout.getTabAt(i).setText((String) llist.get(i).get(0));
                }
            }
        });
        */
        return;
    }

    public void login(ViewGroup layout, AlertDialog.Builder builder, final Context context,
                      final MainActivity activity) {
        builder.setTitle("Login");
        builder.setView(layout);
        final EditText edit1 = layout.findViewById(R.id.username);
        final EditText edit2 = layout.findViewById(R.id.password);
        builder.setPositiveButton("login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {
                    public void run() {
                        try {
                            String username=edit1.getText().toString();
                            String password=edit2.getText().toString();
                            ChatHandle handle = new ChatHandle();
                            handle.InitSocket();
                            handle.send("@login@");
                            handle.send(username);
                            handle.send(password);
                            String state = handle.receive();
                            if (state.startsWith("@successful@")) {
                                MainActivity.cookie = handle.receive();
                                MainActivity.username=username;
                                MainActivity.title.setText(username);
                                activity.DB.saveObject(MainActivity.cookie,"cookie");
                                activity.DB.saveObject(MainActivity.username,"username");
                                Snackbar.make(activity.getWindow().getDecorView(), "welcome, your cookie is " + String.valueOf(MainActivity.cookie), Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(activity.getWindow().getDecorView(), "Login fail", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(activity.getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }.start();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return;
    }

}
