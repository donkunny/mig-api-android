package com.johnjhkoo.mig_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.johnjhkoo.mig_android.adapters.CategoryAdapter;
import com.johnjhkoo.mig_android.adapters.CategoryListAdapter;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.frangments.CategoryFragment;
import com.johnjhkoo.mig_android.frangments.GridPageFragment;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MIGEventVO> list;
    private MIGRetroDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_category_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.single_category_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        setSupportActionBar((Toolbar) findViewById(R.id.single_category_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.back_white);

         new DAOAsyncTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    // when loading data at the first time
    class DAOAsyncTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            dao = new MIGRetroDAO();

            Intent intent = getIntent();
            int position = intent.getIntExtra(CategoryListAdapter.TAG, -1);

            switch (position){
                case 0:
                    list = dao.getCategory("연극");
                    break;
                case 1:
                    list = dao.getCategory("전시");
                    break;
                case 2:
                    list = dao.getCategory("영화");
                    break;
                case 3:
                    list = dao.getCategory("공연");
                    break;
                case 4:
                    list = dao.getCategory("교육");
                    break;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mAdapter = new CategoryAdapter(getApplicationContext(), list);
            mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);
        }

    }

}
