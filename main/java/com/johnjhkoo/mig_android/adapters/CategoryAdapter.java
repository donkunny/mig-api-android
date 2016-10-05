package com.johnjhkoo.mig_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.johnjhkoo.mig_android.DetailActivity;
import com.johnjhkoo.mig_android.ImageLoader.ImageLoader;
import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by donkunny on 7/6/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Context context;
    private ImageLoader imageLoader;
    private List<MIGEventVO> vo;

    private String[] imgURL;
    private String[] titles;
    private String[] ids;

    public CategoryAdapter(Context context, List<MIGEventVO> vo){
        this.context = context;
        this.vo = vo;
        imageLoader = new ImageLoader(context);
    }

    public CategoryAdapter(Context context, String[] imgURL, String[] titles, String[] ids){
        this.context = context;
        this.imgURL = imgURL;
        this.titles = titles;
        this.ids = ids;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getItemCount() {
        return vo.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        MIGRetroDAO dao = new MIGRetroDAO();
//        imageLoader.DisplayImage(vo.get(position).getPosterAddr(), holder.imageView, 100);
        imageLoader.DisplayImage(dao.getThumnailAddr(vo.get(position).getPosterThumb()), holder.imageView, 100);
        holder.textView.setText(vo.get(position).getTitle());
//        MIGRetroDAO dao = new MIGRetroDAO();
//        imageLoader.DisplayImage(dao.getThumnailAddr(imgURL[position]), holder.imageView, 100);
//        holder.textView.setText(titles[position]);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(DetailActivity.TAG, vo.get(position).getId());
                //intent.putExtra(DetailActivity.TAG, ids[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public final ImageView imageView;
        public final TextView textView;

        public ViewHolder(View view){
            super(view);
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.cat_item_thumbnail);
            textView = (TextView) view.findViewById(R.id.cat_item_title);
        }
    }

}
