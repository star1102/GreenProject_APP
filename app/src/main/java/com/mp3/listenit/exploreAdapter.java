package com.mp3.listenit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YDJ on 2016-03-10.
 */

public class exploreAdapter extends BaseAdapter {

    Context context;
    ArrayList mlist;

    public exploreAdapter(Context c, ArrayList l){

        mlist=l;
        context=c;

    }

    @Override
    public int getCount() {

        return mlist.size();
    }

    @Override
    public Object getItem(int position) {

        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.explore_items, null);
        }
            TextView txt_fName = (TextView) convertView.findViewById(R.id.txt_fName);
            txt_fName.setText(mlist.get(position).toString());
        return convertView;
    }
}
