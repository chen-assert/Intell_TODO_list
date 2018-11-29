package jingruichen.Intell_TODOlist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.*;

import static jingruichen.Intell_TODOlist.MainActivity.llist;

public class SearchResult extends Activity {
    private ListView listView;
    List<Map<String, Object>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        String search = getIntent().getStringExtra("key");
        listView = findViewById(R.id.listview);
        String[] from = {"img", "text1", "text2", "text3"};
        int[] to = {R.id.list_imageView, R.id.list_textView, R.id.list_textView2, R.id.list_textView3};
        update(search);
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_items, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
        });
    }

    public void update(String search) {
        Map<String, Object> t;
        data.clear();
        try {
            LinkedList<Item> items = new LinkedList<>();
            for (LinkedList list1 : llist) {
                if (list1 != null && list1.size() > 1) {
                    for (Object object : list1) {
                        if (object instanceof Item) {
                            if (((Item) object).name.contains(search)) {
                                items.add((Item) object);
                            }
                        }
                    }
                }
            }


            final long timeInMillis = Calendar.getInstance().getTimeInMillis();
            /*
            Collections.sort(items, (Comparator) (o1, o2) -> {
                if (o1 instanceof String) {
                    return -1;
                } else if (o2 instanceof String) {
                    return 1;
                } else {
                    Item i1 = (Item) o1;
                    Item i2 = (Item) o2;
                    if(i1.favorite==true){
                        return -1;
                    }
                    if(i2.favorite==true){
                        return 1;
                    }
                    return Math.toIntExact((i1.time.getTimeInMillis() - timeInMillis) * i1.priority - (i2.time.getTimeInMillis() - timeInMillis) * i2.priority);
                }
            });
            */
            for (Item item : items) {
                t = new HashMap<>();
                t.put("img", (item).favorite ? R.drawable.ic_action_favorite_record : null);
                t.put("text1", (item).name);
                t.put("text2", (item).priority);
                long remain = (item).time.getTimeInMillis() - timeInMillis;
                String str = "";
                if (remain > 3600L * 1000L * 24L * 365L)
                    str = String.valueOf(remain / 3600 / 1000 / 24 / 365) + " years";
                else if (remain > 3600L * 1000L * 24L * 30L)
                    str = String.valueOf(remain / 3600 / 1000 / 24 / 30) + " months";
                else if (remain > 3600L * 1000L * 24L) str = String.valueOf(remain / 3600 / 1000 / 24) + " days";
                else if (remain > 3600L * 1000L) str = String.valueOf(remain / 3600 / 1000) + " hours";
                else if (remain > 0L) str = "<1 hour";
                else str = "overdue";
                t.put("text3", str);
                data.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
