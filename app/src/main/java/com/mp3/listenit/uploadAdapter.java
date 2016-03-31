package com.mp3.listenit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by YDJ on 2016-03-16.
 */
public class uploadAdapter extends BaseAdapter {


    Context context;
    ArrayList<uploadItem> items;

    public uploadAdapter(Context c, ArrayList l){

        context= c;
        items = l;
    }

    @Override
    public int getCount() {

        return items.size();
    }

    @Override
    public Object getItem(int position) {

        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.upload_items, null);
        }

        TextView txt_sName =(TextView) convertView.findViewById(R.id.txt_sName);
        txt_sName.setText(items.get(position).getSinger());


        TextView txt_mName =(TextView) convertView.findViewById(R.id.txt_mName);
        txt_mName.setText(items.get(position).getMusic());

        return convertView;
    }
}

