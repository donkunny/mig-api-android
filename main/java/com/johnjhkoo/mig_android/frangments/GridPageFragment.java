package com.johnjhkoo.mig_android.frangments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johnjhkoo.mig_android.GridListActivity;
import com.johnjhkoo.mig_android.Listeners.EndlessRecyclerScrollListener;
import com.johnjhkoo.mig_android.R;
import com.johnjhkoo.mig_android.adapters.GridFragmentAdapter;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by donkunny on 16. 7. 2.
 */
public class GridPageFragment extends Fragment {

    public final static int numLoadingImages = 10;

    private List<MIGEventVO> list;
    private RecyclerView rv;
    private GridFragmentAdapter gridFragmentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridListActivity gridListActivity;
    private View view;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int wholeItemsNum = 0;
    private int totalItemCount = 0;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // to remove all the SwipeRefreshLayout views when the fragment view gets destroyed:
        swipeRefreshLayout.removeAllViews();
        // gridFragmentAdapter.getImageLoader().clearCache(); // for preventing out of memory
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recycler);

        // data which sent from activity
        gridListActivity = (GridListActivity) getActivity();
        wholeItemsNum = gridListActivity.getWholeListSize();
        Log.d("GridPageFragment", "wholeItemsNum: " + wholeItemsNum);

        gridFragmentAdapter = new GridFragmentAdapter(getActivity(), gridListActivity.getListVO());
        // gridFragmentAdapter = new GridFragmentAdapter(getActivity(), strID, strURL, strTitle);
        //rv.setLayoutManager(new GridLayoutManager(getActivity(), 2)); // this is just grid layout

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setAdapter(gridFragmentAdapter);
        rv.addOnScrollListener(new EndlessRecyclerScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // totalItemCount = totalItemsCount;
                if((wholeItemsNum -  totalItemsCount) > numLoadingImages) {
                    gridListActivity.loadMoreAsyncTask(totalItemsCount, numLoadingImages);
                } else if((wholeItemsNum - totalItemsCount) < numLoadingImages && (wholeItemsNum - totalItemsCount) > 0) {
                    gridListActivity.loadMoreAsyncTask(totalItemsCount, wholeItemsNum-totalItemsCount);
                } else{

                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.contentView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridListActivity.executeAsyncTask(0, numLoadingImages);
                        gridFragmentAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        // swipeRefreshLayout.setEnabled(false);
        return view;
    }

}