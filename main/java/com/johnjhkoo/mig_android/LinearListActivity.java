package com.johnjhkoo.mig_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.johnjhkoo.mig_android.adapters.TestListAdapter;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

public class LinearListActivity extends AppCompatActivity {

    private final String TAG = "LinearListActivity";
    private List<MIGEventVO> list;

    private ListView simple_listview;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);
        simple_listview = (ListView) findViewById(R.id.test_list);
        new DAOAsyncTask().execute();
    }

    class DAOAsyncTask extends AsyncTask<String, Void, Boolean> {

        String[] strURL;
        String[] strTitle;

        @Override
        protected Boolean doInBackground(String... params) {

            MIGRetroDAO dao = new MIGRetroDAO();
            list = dao.getAllData();
            // Log.d(TAG, String.valueOf(list.size()));
            strURL = new String[list.size()];
            strTitle = new String[list.size()];
            for(int i=0; i<list.size();i++){
                strURL[i] = dao.getThumnailAddr(list.get(i).getPosterThumb());
                strTitle[i] = list.get(i).getTitle();
                // Log.d("LinearListActivity", strURL[i]);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            adapter = new TestListAdapter(getApplicationContext(), strURL, strTitle);
            simple_listview.setAdapter(adapter);
        }
    }

}
