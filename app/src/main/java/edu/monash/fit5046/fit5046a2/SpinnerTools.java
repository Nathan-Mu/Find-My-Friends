package edu.monash.fit5046.fit5046a2;

import android.widget.Adapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 1/5/17.
 */

public class SpinnerTools {
    public static List<String> getAllItems(Spinner sp) {
        Adapter adapter = sp.getAdapter();
        int n = adapter.getCount();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            String s = adapter.getItem(i).toString();
            list.add(s);
        }
        return list;
    }

    public static int getPosition(Spinner sp, String s) {
        Adapter adapter = sp.getAdapter();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (s.equals(adapter.getItem(i).toString()))
                return i;
        }
        return 0;
    }

    public static int count(Spinner sp)
    {
        Adapter adapter = sp.getAdapter();
        return adapter.getCount();
    }
}
