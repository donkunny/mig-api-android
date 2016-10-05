package com.johnjhkoo.mig_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnjhkoo.mig_android.CategoryActivity;
import com.johnjhkoo.mig_android.DetailActivity;
import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by donkunny on 7/7/16.
 */
public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder>{
    public final static String TAG = "CategoryListAdapter";
    private Context context;
    private String[] title = {"연극", "전시", "영화", "공연", "교육"};
    private CategoryListAdapter.ViewHolder viewHolder;
    private List<MIGEventVO> list;
    private int[] listSize;

    public CategoryListAdapter(Context context, List<MIGEventVO> vo) {
        this.context = context;
    }

    public CategoryListAdapter(Context context, int[] listSize){
        this.context = context;
        this.listSize = listSize;
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.ViewHolder holder, final int position) {
        viewHolder = holder;
        holder.title.setText(this.title[position]);

        switch (position){
            case 0:
                // holder.img_icon.setImageResource(R.drawable.category_play);
                holder.img_bg.setImageResource(R.drawable.theatre1);
                viewHolder.numItems.setText(listSize[0]+" items");
                break;
            case 1:
                // holder.img_icon.setImageResource(R.drawable.category_display);
                holder.img_bg.setImageResource(R.drawable.performance);
                viewHolder.numItems.setText(listSize[1]+" items");
                break;
            case 2:
                // holder.img_icon.setImageResource(R.drawable.category_movie);
                holder.img_bg.setImageResource(R.drawable.movies);
                viewHolder.numItems.setText(listSize[2]+" items");
                break;
            case 3:
                // holder.img_icon.setImageResource(R.drawable.categorya_lec);
                holder.img_bg.setImageResource(R.drawable.education1);
                viewHolder.numItems.setText(listSize[3]+" items");
                break;
            case 4:
                // holder.img_icon.setImageResource(R.drawable.category_edu);
                holder.img_bg.setImageResource(R.drawable.exhibition);
                viewHolder.numItems.setText(listSize[4]+" items");
                break;
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CategoryActivity.class);
                i.putExtra(TAG, position);
                v.getContext().startActivity(i);
                // Log.d("TAG", "numItems"+ urlImgs.length);
            }
        });
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView img_bg;
        public final ImageView img_icon;
        public final TextView title;
        public final TextView numItems;

        public ViewHolder(View view){
            super(view);
            this.view = view;
            img_bg = (ImageView)view.findViewById(R.id.cat_backimg);
            img_icon = (ImageView)view.findViewById(R.id.cat_circle_icon);
            title = (TextView)view.findViewById(R.id.cat_title);
            numItems = (TextView)view.findViewById(R.id.cat_subtitle);
        }
    }
}
