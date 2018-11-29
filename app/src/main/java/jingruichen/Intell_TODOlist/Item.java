package jingruichen.Intell_TODOlist;

import java.io.Serializable;
import java.util.Calendar;

public class Item implements Serializable {
    public String name;
    public int priority;
    public Calendar time;
    public String comment;
    public boolean favorite;
    public int notifacation;

    public Item(String name, int priority, Calendar time,String comment,boolean favorite){
        this.name=name;
        this.priority=priority;
        this.time=time;
        this.comment=comment;
        this.favorite=favorite;
        this.notifacation=0;
    }
    public Item(String name, int priority, Calendar time,String comment,boolean favorite,int notifacation){
        this(name,priority,time,comment,favorite);
        this.notifacation=notifacation;
    }
}
