package com.johnjhkoo.mig_android.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.ImageLoader.ImageLoader;

/**
 * Created by donkunny on 16. 6. 29.
 * for testing LinearListActivity
 */
public class TestListAdapter extends BaseAdapter{
    private Context context;
    private String[] title;
    private String[] data;
    private static LayoutInflater inflater=null;
    private ImageView imageView;
    private ImageLoader imageLoader;

    public TestListAdapter(Context context, String[] d, String[] t){
        this.context = context;
        imageLoader = new ImageLoader(context);
        title = t;
        data = d;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null)
            view = inflater.inflate(R.layout.test_list_item, null);

        TextView text = (TextView)view.findViewById(R.id.test_text);
        imageView = (ImageView)view.findViewById(R.id.test_image);
        text.setText(title[position]);
        text.setTextColor(Color.BLACK);
        // Picasso.with(context).load(data[position]).into(imageView);
        imageLoader.DisplayImage(data[position], imageView, 70);
        return view;
    }
}
