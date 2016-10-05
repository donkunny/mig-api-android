package com.johnjhkoo.mig_android.frangments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.johnjhkoo.mig_android.GridListActivity;
import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.adapters.CategoryListAdapter;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by donkunny on 7/6/16.
 */
public class CategoryFragment extends android.support.v4.app.Fragment{

    private View view;
    private RecyclerView rv;

    private List<MIGEventVO> vo;
    public final static String TAG = "CategoryFragment";
    private LinearLayoutManager linearLayoutManager;
    private CategoryListAdapter categoryListAdapter;
    // private GridListActivity gridListActivity;

    private int[] listSize;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_category, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recycler);

        new DAOAsyncTask().execute();

        return view;
    }


    // when loading data at the first time
    class DAOAsyncTask extends AsyncTask<Integer, Void, Boolean> {

        List<MIGEventVO> list;

        @Override
        protected Boolean doInBackground(Integer... params) {
            MIGRetroDAO dao = new MIGRetroDAO();
            listSize = new int[5];

            list = dao.getCategory("연극");
            listSize[0] = list.size();
            list = dao.getCategory("전시");
            listSize[1] = list.size();
            list = dao.getCategory("영화");
            listSize[2] = list.size();
            list = dao.getCategory("공연");
            listSize[3] = list.size();
            list = dao.getCategory("교육");
            listSize[4] = list.size();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // gridListActivity = (GridListActivity)getActivity();
            linearLayoutManager = new LinearLayoutManager(getContext());
            // categoryListAdapter = new CategoryListAdapter(getActivity(), gridListActivity.getListVO());
            categoryListAdapter = new CategoryListAdapter(getActivity(), listSize);
            rv.setLayoutManager(linearLayoutManager);
            rv.setAdapter(categoryListAdapter);
        }
    }

}
