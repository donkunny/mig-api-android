package com.johnjhkoo.mig_android.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.johnjhkoo.mig_android.frangments.GridPageFragment;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by donkunny on 16. 7. 2.
 */
public class GridFragmentAdapter extends RecyclerView.Adapter<GridFragmentAdapter.ViewHolder>{

    private String[] imgURL;
    private String[] title;
    private Context context;
    private String[] id;
    private ImageLoader imageLoader;
    private List<MIGEventVO> vo;


    public GridFragmentAdapter(Context context){
        imageLoader = new ImageLoader(context);
    }

    public GridFragmentAdapter(Context context, String[] id, String[] imgURL, String[] title){
        this.context = context;
        this.id = id;
        this.imgURL = imgURL;
        this.title = title;
        imageLoader = new ImageLoader(context);
    }

    public GridFragmentAdapter(Context context, List<MIGEventVO> vo){
        this.context = context;
        this.vo = vo;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        //imageLoader.DisplayImage(imgURL[position], holder.imageView, 350);
        //holder.textView.setText(title[position]);
        MIGRetroDAO dao = new MIGRetroDAO();
        imageLoader.DisplayImage(dao.getThumnailAddr(vo.get(position).getPosterThumb()), holder.imageView, 350);
//        imageLoader.DisplayImage(vo.get(position).getPosterAddr(), holder.imageView, 350);

        holder.textView.setText(vo.get(position).getTitle());

        holder.date.setText(new SimpleDateFormat("yyyy/MM/dd").format(vo.get(position).geteDate()) + " 까지");

        //set OnClickListener
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail_info", vo.get(position).getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
            return vo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View view;
        public final ImageView imageView;
        public final TextView textView;
        public final TextView date;

        public ViewHolder(View view){
            super(view);
            this.view = view;
            imageView =(ImageView) view.findViewById(R.id.grid_image);
            textView = (TextView) view.findViewById(R.id.grid_text);
            date = (TextView) view.findViewById(R.id.grid_date);
        }
    }
}
