package jingruichen.Intell_TODOlist;

import java.io.Serializable;
import java.util.Calendar;

public class Item implements Serializable {
    public String name;
    public String priority;
    public Calendar time;
    public String comment;
    public boolean favorite;
    public int notifacation;

    public Item(String name, String priority, Calendar time,String comment){
        this.name=name;
        this.priority=priority;
        this.time=time;
        this.comment=comment;
        this.favorite=false;
        this.notifacation=-1;
    }
}
