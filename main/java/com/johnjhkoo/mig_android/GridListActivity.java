package com.johnjhkoo.mig_android;

import android.content.Intent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.johnjhkoo.mig_android.adapters.GridFragmentAdapter;
import com.johnjhkoo.mig_android.context.MIGActivity;
import com.johnjhkoo.mig_android.context.MIGUtils;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.frangments.CategoryFragment;
import com.johnjhkoo.mig_android.frangments.GridPageFragment;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;


public class GridListActivity extends MIGActivity {

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private GridFragmentAdapter mAdapter;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<MIGEventVO> list;
    private MIGRetroDAO dao;
    private Adapter adapter;
    private int listSize = 0;
    private int wholeListSize = 0;

//    private ProgressDialog progressDialog;

    RotateLoading rotateLoading;
//    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_list);
        Log.d("GridListAcitivity", "onCreate()");

//        progressDialog = new ProgressDialog(GridListActivity.this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setTitle("Loading...");
//        progressDialog.setMessage("Loading application View, Please wait...");
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(false);
//        progressDialog.show();

        rotateLoading = (RotateLoading) findViewById(R.id.activity_grid_list_rotateloading);
        rotateLoading.start();

//        circularProgressBar = (CircularProgressBar) findViewById(R.id.activity_grid_list_circularpb);

        //Look up Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
            setSupportActionBar(toolbar);

        // Look up Actionbar
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_home_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowTitleEnabled(false);

        }


        // Look up view Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // init for showing 10 posters
        // first index == 0
        executeAsyncTask(0, GridPageFragment.numLoadingImages);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class DAOAsyncTask extends AsyncTask<Integer, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Integer... params) {
            dao = new MIGRetroDAO();

            int startIndex = params[0];
            int pageSize = params[1];
            JsonObject obj = dao.getScrollPage(startIndex, pageSize);

            list = MIGUtils.fromJsonObjectToVOList(obj);
            listSize = list.size();
            wholeListSize = MIGUtils.fromJsonObjectToItemsCount(obj);

            adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new GridPageFragment(), "오늘의 행사");
            adapter.addFragment(new CategoryFragment(), "카테고리별");

            try{
                Thread.sleep(1000);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (viewPager != null) {
                Log.d("GridListActivity", "onPostExecute()");
                adapter.notifyDataSetChanged();
                viewPager.setAdapter(adapter);
            }

            tabLayout.setupWithViewPager(viewPager);
            rotateLoading.stop();
//            circularProgressBar.progressiveStop();
        }
    }

    // if when loading more data
    class LoadMoreItemsTask extends AsyncTask<Integer, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rotateLoading.setLoadingColor(R.color.colorAccentRed);
            rotateLoading.start();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.d("doInBackground","in LoadMOreItemsTask");
            int startIndex = params[0];
            int pageSize = params[1];

            Log.d("GridList/doInBackground", "startIndex: " + startIndex + "/ pageSize: " + pageSize);

            //list.addAll(dao.getPage(startIndex, pageSize));
            JsonObject obj = dao.getScrollPage(startIndex, pageSize);
            list.addAll(MIGUtils.fromJsonObjectToVOList(obj));
            wholeListSize = MIGUtils.fromJsonObjectToItemsCount(obj);
            listSize = list.size();

            Log.d("LoadMoreItemsTask", "After loading more, list size: " + list.size() + " /whole list size is " + wholeListSize);

            adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new GridPageFragment(), "오늘의 행사");
            adapter.addFragment(new CategoryFragment(), "카테고리별");

            try {
                Thread.sleep(500);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (viewPager != null) {
                Log.d("GridListActivity", "onPostExecute()");
                adapter.notifyDataSetChanged();
                viewPager.setAdapter(adapter);
            }

            tabLayout.setupWithViewPager(viewPager);
            rotateLoading.stop();
        }
    }


    // for showing fragment, set Fragment Pager Adapter
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm){
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }


    // if when using app bar icon listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(GridListActivity.this, "데이터가 로딩되었습니다.", Toast.LENGTH_SHORT).show();
                // executeAsyncTask(0, );
                break;
            case R.id.menu_maplist_btn:
//                Toast.makeText(GridListActivity.this, "Maplist Button Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MapListActivity.class);
                startActivity(i);

                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void executeAsyncTask(int startIndex, int pageSize){
        new DAOAsyncTask().execute(startIndex, pageSize);
    }

    public void loadMoreAsyncTask(int startIndex, int pageSize){
        new LoadMoreItemsTask().execute(startIndex, pageSize);
        // return  true;
    }

    public List<MIGEventVO> getListVO() {
        return list;
    }

    public int getListSize() {
        return listSize;
    }

    public int getWholeListSize() {
        return wholeListSize;
    }
}
