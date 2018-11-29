package jingruichen.Intell_TODOlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public static Fragment newInstance(int index, String pageName) {
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, index);
        bundle.putString(KEY2, pageName);
        fragment.setArguments(bundle);
        if (llist.get(index).isEmpty()) {
            llist.get(index).add(pageName);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mIndex = bundle.getInt(KEY);
        pageName = bundle.getString(KEY2);

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
        final MyFragment myFragment = this;
        listView.setOnItemClickListener((parent, view, position, id) -> {
            //Snackbar.make(view, String.valueOf(mIndex)+"-"+String.valueOf(position), Snackbar.LENGTH_LONG).show();
            //position+1 because the 0 index saved the page name
            AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
            ViewGroup layout = (ViewGroup) getLayoutInflater().inflate(R.layout.dialogs, null);
            AlertView alert = new AlertView();
            alert.addEvent(layout, builder, parent.getContext(), null, mIndex, position + 1, myFragment, adapter);
            alert.bbb(builder);
            //adapter.notifyDataSetChanged();
            //update();
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

    public void update() {
        Map<String, Object> t;
        data.clear();
        /*
        t.put("img", R.drawable.ic_action_favorite_record);
        t.put("text1", "text1");
        t.put("text2", "text2");
        t.put("text3", "text3");
        data.add(t);
        */

        try {
            final long timeInMillis = Calendar.getInstance().getTimeInMillis();
            Collections.sort(llist.get(mIndex), (Comparator) (o1, o2) -> {
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
                    long i1remain=i1.time.getTimeInMillis() - timeInMillis;
                    long i2remain=i2.time.getTimeInMillis() - timeInMillis;
                    //if(i1remain<0)return 1;
                    //if(i2remain<0)return -1;
                    if(i1remain * i1.priority - i2remain * i2.priority>0)return 1;
                    else return -1;
                }
            });
            int count = 0;
            for (Object o : llist.get(mIndex)) {
                count++;
                if (count == 1) continue;
                t = new HashMap<>();
                t.put("img", ((Item) o).favorite ? R.drawable.ic_action_favorite_record : null);
                t.put("text1", ((Item) o).name);
                t.put("text2", ((Item) o).priority);
                long remain = ((Item) o).time.getTimeInMillis() - timeInMillis;
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