package com.ec.survey.tools;
 import java.util.Enumeration;
import java.util.Hashtable;
public class ObjectPool {

 private  Hashtable<T,Long> locked;

 private  Hashtable<T,Long> unlocked;

 private  int max;

public ObjectPool(int max) {
    this.max = max;
    locked = new Hashtable<>();
    unlocked = new Hashtable<>();
}
public void checkIn(T t){
    locked.remove(t);
    unlocked.put(t, System.currentTimeMillis());
}


public void expire(T o)


public T create()


public T checkOut(){
    long now = System.currentTimeMillis();
    T t;
    if (unlocked.size() > 0) {
        Enumeration<T> e = unlocked.keys();
        if (e.hasMoreElements()) {
            t = e.nextElement();
            unlocked.remove(t);
            locked.put(t, now);
            return (t);
        }
    }
    // no objects available, create a new one
    if (locked.size() < max) {
        t = create();
        locked.put(t, now);
        return (t);
    }
    // maximum number has been reached
    return null;
}


public boolean validate(T o)


}