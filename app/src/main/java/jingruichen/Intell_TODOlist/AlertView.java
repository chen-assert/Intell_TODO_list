package jingruichen.Intell_TODOlist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.Calendar;

import static jingruichen.Intell_TODOlist.MainActivity.llist;
import static jingruichen.Intell_TODOlist.MainActivity.tabLayout;
import static jingruichen.Intell_TODOlist.Util.makedate;
import static jingruichen.Intell_TODOlist.Util.maketime;

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

    //really confusing...
    //pos1-x cor
    //pos2-y cor
    public void addEvent(ViewGroup layout, AlertDialog.Builder builder, final Context context,
                         final MainActivity activity, final int pos1, final int pos2, final MyFragment myFragment, final SimpleAdapter adapter) {
        final EditText edit1 = layout.findViewById(R.id.name);
        final EditText edit2 = layout.findViewById(R.id.priority);
        final TextView edit3 = layout.findViewById(R.id.additional);
        final TextView text1 = layout.findViewById(R.id.timeText);
        final TextView text2 = layout.findViewById(R.id.dateText);
        final Spinner spinner2 = layout.findViewById(R.id.spinner2);
        final Switch switch_ = layout.findViewById(R.id.switch1);
        final String[] notifacation_type = {"notification off", "1 week before", "1 day before", "1 hour before", "30 minutes before"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, notifacation_type);
        spinner2.setAdapter(adapter2);
        if (pos1 == -1 && pos2 == -1) {
            builder.setTitle("Add new event");
            builder.setIcon(R.drawable.ic_action_plus1);
            builder.setView(layout);
            final int index_intab = tabLayout.getSelectedTabPosition();
            final int index_inlist = Integer.parseInt((String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getContentDescription());
            c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            text1.setText(maketime(hour, minute));
            text2.setText(makedate(year, month, day));
            builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String name = edit1.getText().toString();
                    final String priority = edit2.getText().toString();
                    final String comment = edit3.getText().toString();
                    final boolean favorate = switch_.isChecked();
                    int parseInt;
                    try {
                        parseInt = Integer.parseInt(priority);
                    } catch (Exception e) {
                        parseInt = 100;
                    }
                    llist.get(index_inlist).add(new Item(name, parseInt, c, comment, favorate, spinner2.getSelectedItemPosition()));
                    activity.update(index_intab);
                    if (spinner2.getSelectedItemPosition() != 0) {
                        activity.startRemind(year, month, day, hour, minute, 100, 30, "you have a event", "name");
                    }
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
                            text1.setText(maketime(hour, minute));
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
                            text2.setText(makedate(year, month, day));
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
        } else {
            builder.setTitle("Event");
            builder.setIcon(R.drawable.ic_action_change);
            builder.setView(layout);
            Item item = (Item) llist.get(pos1).get(pos2);
            c = item.time;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            text1.setText(maketime(hour, minute));
            text2.setText(makedate(year, month, day));
            edit1.setText(item.name);
            edit2.setText(String.valueOf(item.priority));
            edit3.setText(item.comment);
            switch_.setChecked(item.favorite);
            spinner2.setSelection(item.notifacation);
            builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    final String name = edit1.getText().toString();
                    final String priority = edit2.getText().toString();
                    final String comment = edit3.getText().toString();
                    final boolean favorate = switch_.isChecked();
                    int parseInt;
                    try {
                        parseInt = Integer.parseInt(priority);
                    } catch (Exception e) {
                        parseInt = 100;
                    }
                    llist.get(pos1).set(pos2, new Item(name, parseInt, c, comment, favorate, spinner2.getSelectedItemPosition()));
                    DbHelper2 DB = new DbHelper2(context);
                    DB.saveObject(llist, "llistkey1");
                    myFragment.update();
                    adapter.notifyDataSetChanged();
                    if (spinner2.getSelectedItemPosition() != 0) {
                        activity.startRemind(year, month, day, hour, minute, 100, 30, "you have a event", "name");
                    }
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.setNeutralButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    llist.get(pos1).remove(pos2);
                    DbHelper2 DB = new DbHelper2(context);
                    DB.saveObject(llist, "llistkey1");
                    myFragment.update();
                    adapter.notifyDataSetChanged();
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
                            text1.setText(maketime(hour, minute));
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
                            text2.setText(makedate(year, month, day));
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
                         final MainActivity activity, final Handler myHandler) {
        builder.setTitle("Page name");
        builder.setView(layout);
        final int index_inlist = Integer.parseInt((String) tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getContentDescription());
        final int index_intab = tabLayout.getSelectedTabPosition();
        final EditText edit1 = layout.findViewById(R.id.name);
        edit1.setText(tabLayout.getTabAt(index_intab).getText());
        builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                final String name = edit1.getText().toString();
                llist.get(index_inlist).set(0, name);
                tabLayout.getTabAt(index_intab).setText(edit1.getText());
                activity.update(index_inlist);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton("delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                llist.set(index_inlist, null);
                myHandler.sendEmptyMessage(3);
            }
        });

        return;
    }

    public void login(ViewGroup layout, AlertDialog.Builder builder, final Context context,
                      final MainActivity activity, final Handler myHandler) {
        builder.setTitle("Login");
        builder.setView(layout);
        final EditText edit1 = layout.findViewById(R.id.username);
        final EditText edit2 = layout.findViewById(R.id.password);
        builder.setPositiveButton("login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {
                    public void run() {
                        try {
                            String username = edit1.getText().toString();
                            String password = edit2.getText().toString();
                            ChatHandle handle = new ChatHandle();
                            handle.InitSocket();
                            handle.send("@login@");
                            handle.send(username);
                            handle.send(password);
                            String state = handle.receive();
                            if (state.startsWith("@successful@")) {
                                MainActivity.cookie = handle.receive();
                                MainActivity.username = username;
                                activity.DB.saveObject(MainActivity.cookie, "cookie");
                                activity.DB.saveObject(MainActivity.username, "username");
                                Snackbar.make(activity.getWindow().getDecorView(), "welcome " + username + "(cookie:" + String.valueOf(MainActivity.cookie) + ")", Snackbar.LENGTH_LONG).show();
                                myHandler.sendEmptyMessage(30);
                                myHandler.sendEmptyMessage(20);
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
        builder.setNeutralButton("register", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new Thread() {
                    public void run() {
                        try {
                            String username = edit1.getText().toString();
                            String password = edit2.getText().toString();
                            ChatHandle handle = new ChatHandle();
                            handle.InitSocket();
                            handle.send("@register@");
                            handle.send(username);
                            handle.send(password);
                            String state = handle.receive();
                            if (state.startsWith("@successful@")) {
                                MainActivity.cookie = handle.receive();
                                MainActivity.username = username;
                                activity.DB.saveObject(MainActivity.cookie, "cookie");
                                activity.DB.saveObject(MainActivity.username, "username");
                                Snackbar.make(activity.getWindow().getDecorView(), "welcome " + username + "(cookie:" + String.valueOf(MainActivity.cookie) + ")", Snackbar.LENGTH_LONG).show();
                                myHandler.sendEmptyMessage(30);
                                myHandler.sendEmptyMessage(20);
                            } else {
                                Snackbar.make(activity.getWindow().getDecorView(), "Register fail", Snackbar.LENGTH_LONG).show();
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
