package edu.monash.fit5046.fit5046a2;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/4/17.
 */

public class StudentAdapter extends BaseAdapter {
    private Context context;
    private List<Student> list;

    public StudentAdapter(Context context, List<Student> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_list_matched_student, null);
        }

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvGender = (TextView) view.findViewById(R.id.tvGender);
        TextView tvDob = (TextView) view.findViewById(R.id.tvDob);

        tvName.setText(list.get(i).getFName() + " " + list.get(i).getLName());
        if (list.get(i).getGender().equals("m")) {
            tvGender.setText("Male");
        } else {
            tvGender.setText("Female");
        }
        tvDob.setText(Time.toString(Time.toDate(list.get(i).getDob(), "yyyy-MM-dd'T'HH:mm:ssZZZZZ"), "dd/MMM/yyyy"));

        return view;
    }
}
