package jingruichen.Intell_TODOlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.*;

import static jingruichen.Intell_TODOlist.MainActivity.llist;

public class MyFragment extends Fragment {

    private int mIndex;
    private String pageName;
    static final String KEY = "key";
    static final String KEY2 = "key2";
    View view;
    ListView listView;
    SimpleAdapter adapter;

    List<Map<String, Object>> data = new ArrayList<>();

    public static Fragment newInstance(int index,String pageName) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, index);
        bundle.putString(KEY2, pageName);
        fragment.setArguments(bundle);
        if(llist.get(index).isEmpty()) {
            llist.get(index).add(pageName);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mIndex = bundle.getInt(KEY);
        pageName=bundle.getString(KEY2);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.list, container, false);
        listView = view.findViewById(R.id.listview);
        update();
        String[] from = {"img", "text1", "text2", "text3"};
        int[] to = {R.id.list_imageView, R.id.list_textView, R.id.list_textView2, R.id.list_textView3};
        adapter = new SimpleAdapter(this.getContext(), data, R.layout.list_items, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Snackbar.make(view, String.valueOf(mIndex)+"-"+String.valueOf(position), Snackbar.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //update();
        } else {
        }
    }

    private void update() {
        Map<String, Object> t = new HashMap<>();
        data.clear();
        /*
        t.put("img", R.drawable.ic_action_favorite_record);
        t.put("text1", "text1");
        t.put("text2", "text2");
        t.put("text3", "text3");
        */
        data.add(t);
        int count=0;
        for (Object o : llist.get(mIndex)) {
            count++;
            if(count==1)continue;
            t = new HashMap<>();
            t.put("img", R.drawable.ic_action_favorite_record);
            t.put("text1", ((Item) o).name);
            t.put("text2", ((Item) o).priority);
            long remain = ((Item) o).time.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            String str = "";
            if (remain > 3600 * 1000) str = String.valueOf(remain / 3600 / 1000) + " hours";
            else if (remain > 0) str = "<1 hour";
            else str = "overdue";
            t.put("text3", str);
            data.add(t);
        }
    }
}