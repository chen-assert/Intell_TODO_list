package jingruichen.Intell_TODOlist;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.String.valueOf;
import static jingruichen.Intell_TODOlist.Util.Base64decode;
import static jingruichen.Intell_TODOlist.Util.readText;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected static final int FILE_REQUESTCODE = 10;
    public static Map<Integer, Integer> smap = new HashMap<>();
    public static LinkedList<LinkedList> llist;
    public final DbHelper2 DB = new DbHelper2(this);
    public final int[] colors = {0xa0ef5350, 0xa0EC407A, 0xa0AB47BC, 0xa07E57C2, 0xa05C6BC0, 0xa042A5F5, 0xa029B6F6,
            0xa026C6DA, 0xa026A69A, 0xa066BB6A, 0xa09CCC65, 0xa0D4E157, 0xa0FFEE58, 0xa0FFCA28, 0xa0FFA726, 0xa0FF7043,
            0xa08D6E63, 0xa078909C};
    public static FragmentAdapter fragmentAdapter;
    public static LinkedList<MyFragment> fragmentList;
    public static TabLayout tabLayout;
    public AVLoadingIndicatorView avi;
    public static String cookie;
    public static String username;
    public static TextView title;

    private void init(int sta) {
        cookie = (String) DB.getObject("cookie");
        username = (String) DB.getObject("username");
        if (sta == -1) {
            llist = new LinkedList<>();
            for (int i = 0; i < 20; i++) {
                llist.add(new LinkedList<>());
            }
            for (int i = 0; i < 20; i++) {
                llist.get(i).clear();
            }
            fragmentList = new LinkedList<>();
        } else if (sta == 0) {
            llist = (LinkedList<LinkedList>) DB.getObject("llistkey1");
            if (llist == null) {
                llist = new LinkedList<>();
                for (int i = 0; i < 20; i++) {
                    llist.add(new LinkedList<>());
                }
            }
        }
        ViewPager viewPager = findViewById(R.id.vp_content);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        if (fragmentList == null) fragmentList = new LinkedList<>();
        fragmentAdapter = new FragmentAdapter(manager, fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout = findViewById(R.id.tl_tab);
        tabLayout.setupWithViewPager(viewPager);
        fragmentList.add((MyFragment) MyFragment.newInstance(0, "Page 1"));
        fragmentList.add((MyFragment) MyFragment.newInstance(1, "Page 2"));
        fragmentList.add((MyFragment) MyFragment.newInstance(2, "Page 3"));
        fragmentList.add((MyFragment) MyFragment.newInstance(3, "Page 4"));
        fragmentList.add((MyFragment) MyFragment.newInstance(4, "Page 5"));
        fragmentAdapter.notifyDataSetChanged();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText((String) llist.get(i).get(0));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_super);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup layout = (ViewGroup) getLayoutInflater().inflate(R.layout.dialogs, null);
                AlertView alert = new AlertView();
                alert.addEvent(layout, builder, MainActivity.this, MainActivity.this, -1,-1,null,null);
                alert.bbb(builder);
            }
        });
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        init(0);

        NavigationView navView = navigationView.findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        title = headerView.findViewById(R.id.nav_title);
        if (username != null) title.setText(username);
        else title.setText("guest");


        avi = findViewById(R.id.avi);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            /**
             * Called when a tab enters the selected state.
             * @param tab The tab that was selected
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            /**
             * Called when a tab exits the selected state.
             * @param tab The tab that was unselected
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            /**
             * Called when a tab that is already selected is chosen again by the user. Some applications
             * may use this action to return to the top level of a category.
             * @param tab The tab that was reselected.
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup layout = (ViewGroup) getLayoutInflater().inflate(R.layout.dialogs_page, null);
                AlertView alert = new AlertView();
                alert.pageName(layout, builder, MainActivity.this, MainActivity.this);
                alert.bbb(builder);
            }
        });

    }

    public void update(int index) {
        for (int i = Math.max(0, index - 1); i <= Math.min(index + 1, fragmentList.size() - 1); i++) {
            Fragment frg = fragmentList.get(i);
            if (frg.isAdded()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setReorderingAllowed(false);
                transaction.detach(frg);
                transaction.attach(frg);
                transaction.commit();
            } else {
            }
        }
        //fragmentAdapter.notifyDataSetChanged();
        DB.saveObject(llist, "llistkey1");
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText((String) llist.get(i).get(0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //event of submit buttom
                //todo:search(hard)
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        searchView.setQueryHint("input to search");

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        AlertView alert;
        AlertView_setting alert_setting;
        switch (item.getItemId()) {
            case R.id.action_share:
                //todo:pricture
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Util.Base64encode(llist));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.action_clear:
                init(-1);
                update(tabLayout.getSelectedTabPosition());
                Toast.makeText(getApplicationContext(), "clear all", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                builder = new AlertDialog.Builder(MainActivity.this);
                ViewGroup layout4 = (ViewGroup) getLayoutInflater().inflate(R.layout.dialogs_setting, null);
                alert_setting = new AlertView_setting();
                alert_setting.showSett(layout4, builder, MainActivity.this, MainActivity.this);
                alert_setting.bbb(builder);
                break;
            case R.id.action_advertise:
                AdView mAdView = findViewById(R.id.adView);
                if (mAdView.getVisibility() == View.VISIBLE) {
                    mAdView.setVisibility(View.INVISIBLE);
                } else {
                    mAdView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.action_notification:
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                mCalendar.add(Calendar.SECOND, 10);
                startRemind(mCalendar.get(Calendar.DAY_OF_WEEK) - 1, mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE), 100, 0, "testcode", "testcourse");
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_import_2) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, FILE_REQUESTCODE);
        } else if (id == R.id.nav_export_2) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, Util.Base64encode(llist));
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.microsoft.skydrive");
            if (CheckApkExist.checkApkExist(this, "com.microsoft.skydrive")) {
                startActivity(sendIntent);
            } else {
                Snackbar.make(getWindow().getDecorView(), "You need install a OneDrive first", Snackbar.LENGTH_LONG).setAction("Dowload", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse("https://onedrive.live.com");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }).show();
            }
        } else if (id == R.id.nav_login) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ViewGroup layout = (ViewGroup) getLayoutInflater().inflate(R.layout.dialogs_login, null);
            AlertView alert = new AlertView();
            alert.login(layout, builder, MainActivity.this, MainActivity.this);
            alert.bbb(builder);
        } else if (id == R.id.nav_logout) {
            username = null;
            cookie = null;
            title.setText("guest");
            Snackbar.make(getWindow().getDecorView(), "You have logout", Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.nav_import) {
            new Thread() {
                public void run() {
                    myHandler.sendEmptyMessage(1);
                    try {
                        ChatHandle handle = new ChatHandle();
                        handle.InitSocket("qwe.chenassert.xyz", 9549);
                        handle.send("@download@");
                        handle.send(cookie);
                        String str = handle.receive();
                        Object t = Base64decode(str);
                        if (t != null) {
                            llist = (LinkedList<LinkedList>) t;
                            //DB.saveObject(llist, "llistkey1");
                        } else {
                            Snackbar.make(getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Snackbar.make(getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(3);
                    myHandler.sendEmptyMessage(2);
                }
            }.start();

        } else if (id == R.id.nav_export) {
            new Thread() {
                public void run() {
                    myHandler.sendEmptyMessage(1);
                    try {
                        ChatHandle handle = new ChatHandle();
                        handle.InitSocket("qwe.chenassert.xyz", 9549);
                        handle.send("@upload@");
                        handle.send(cookie);
                        handle.send(Util.Base64encode(llist));
                    } catch (Exception e) {
                        Snackbar.make(getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myHandler.sendEmptyMessage(2);
                }
            }.start();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    final Handler myHandler = new Handler() {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                avi.show();
            } else if (msg.what == 2) {
                avi.hide();
            } else if (msg.what == 3) {
                update(tabLayout.getSelectedTabPosition());
            }
        }
    };

    //Intent callback
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            Snackbar.make(getWindow().getDecorView(), "Not select file!", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (requestCode == FILE_REQUESTCODE) {
            //Snackbar.make(getWindow().getDecorView(), "uri:" + data.getData().getPath(), Snackbar.LENGTH_LONG).show();
            String str = readText(data.getData(), this);
            Object t = Base64decode(str);
            if (t != null) {
                llist = (LinkedList<LinkedList>) t;
            } else {
                Snackbar.make(getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
            }
        }
        new Thread() {
            public void run() {
                myHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                myHandler.sendEmptyMessage(3);
                myHandler.sendEmptyMessage(2);
            }
        }.start();
    }

    public void sthwrong() {
        Snackbar.make(getWindow().getDecorView(), "Opps, seems something wrong happened", Snackbar.LENGTH_LONG).show();
    }

    private void startRemind(int day, int hour, int minute, int code, int before, String coursecode, String coursename) {
        Calendar mCalendar = Calendar.getInstance();
        long systemTime = System.currentTimeMillis();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // sunday-1 monday-2 ... saturday-7
        day++;
        if (day == 8)
            day = 1;
        mCalendar.set(Calendar.DAY_OF_WEEK, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        mCalendar.add(Calendar.MINUTE, -before);
        mCalendar.add(Calendar.DAY_OF_MONTH, -7);
        long selectTime = mCalendar.getTimeInMillis();
        // may be would be use, be sure can trigger
        int f = 0;
        while (systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 7);
            selectTime = mCalendar.getTimeInMillis();
            if (++f > 30) break;
        }
        //selectTime = mCalendar.getTimeInMillis();
        // AlarmReceiver.class is the broadcast receiver
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("id", code);
        intent.putExtra("coursecode", coursecode);
        intent.putExtra("coursename", coursename);
        intent.putExtra("before", valueOf(before));

        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, code, intent, 0);
        //get the AlarmManager instance
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        /**
         * notice once after mCalendar.getTimeInMillis()
         */
        // am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24 * 7), pi);

    }

    private void stopRemind(int code) {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, code, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pi);
        Toast.makeText(this, "close notification", Toast.LENGTH_SHORT).show();
    }

}
