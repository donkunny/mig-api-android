package com.johnjhkoo.mig_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by JohnKoo on 6/24/16.
 */
public class MIGListAdapter extends ArrayAdapter<MIGEventVO> {

    private List<MIGEventVO> eventList;
    private Context context;

    public MIGListAdapter(Context ctx, List<MIGEventVO> list) {
        super(ctx, android.R.layout.simple_list_item_1, list);
        context = ctx;
        eventList = list;
    }

    public int getCount(){
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public MIGEventVO getItem(int position) {
        if (eventList != null) {
            return eventList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if (eventList != null) {
            return eventList.get(position).hashCode();
        }
        return 0;
    }

    public List<MIGEventVO> getEventList(){
        return eventList;
    }

    public void setEventList(List<MIGEventVO> list) {
        eventList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.maplist_list_item_layout, null);
        }

        MIGEventVO vo = eventList.get(position);

        TextView text = (TextView) v.findViewById(R.id.maplist_list_item_layout_title);
        text.setText(vo.getTitle());

        return v;
    }


}
