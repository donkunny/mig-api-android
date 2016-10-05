package com.johnjhkoo.mig_android;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.johnjhkoo.mig_android.ImageLoader.ImageLoader;
import com.johnjhkoo.mig_android.adapters.GridFragmentAdapter;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;
import com.victor.loading.rotate.RotateLoading;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    // private List<MIGEventVO> list;
    public static final String TAG = "detail_info";
    private ImageLoader imageLoader;
    private MIGRetroDAO dao;

    private CollapsingToolbarLayout collapsingToolbar;

    private ImageView mainImage;

    private TextView detail_title;
    private  TextView detail_place;
    private ImageView img_big;
    private TextView detail_cost;
    private TextView detail_host;
    private TextView detail_phone;
    private ImageView img_phone;
    private TextView detail_homepage;
    private TextView detail_reserve;
    private TextView detail_exp;
    private ImageView detail_map;
    private TextView detail_time;

//    private ProgressDialog detailDialog;

    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        rotateLoading = (RotateLoading) findViewById(R.id.activity_detail_rotateloading);
        rotateLoading.start();

        /**
         * Action Bar Configuration
         */
        setSupportActionBar((Toolbar)findViewById(R.id.detail_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.back_white);
        actionBar.setShowHideAnimationEnabled(true);


        /**
         * Collapsing Toolbar Configuration
         */
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.detail_collaping_toolbar);
//        collapsingToolbar.setTitleEnabled(false);

        /**
         *  DetailActivity Main Image
         */
         mainImage = (ImageView) findViewById(R.id.detail_image);

        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_place = (TextView) findViewById(R.id.detail_place);
        img_big = (ImageView) findViewById(R.id.detail_bigImg);
//        detail_cost = (TextView) findViewById(R.id.detail_cost);
//        detail_host = (TextView) findViewById(R.id.detail_host);
////        detail_phone = (TextView) findViewById(R.id.detail_phone);
////        img_phone = (ImageView) findViewById(R.id.detail_phone_img);
//        detail_homepage = (TextView) findViewById(R.id.detail_homepage);
//        detail_reserve = (TextView) findViewById(R.id.detail_reservation);
        detail_exp = (TextView) findViewById(R.id.detail_explains);
        detail_map = (ImageView) findViewById(R.id.detail_map);
        detail_time = (TextView) findViewById(R.id.detail_time);
        detail_phone = (TextView) findViewById(R.id.detail_phone_text);
        imageLoader = new ImageLoader(getApplicationContext());
        Intent intent = getIntent();
        String id = intent.getStringExtra(TAG);
        new DAOAsyncTask().execute(id);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDetailInfo(final MIGEventVO vo){
        detail_title.setText(vo.getTitle());
//        DetailActivity.this.setTitle(detail_title.getText());

        final String event_place = vo.getPlace();
        detail_place.setText(event_place);
        imageLoader.DisplayImage(vo.getPosterAddr(), img_big, 400);
        final String imgURL = vo.getPosterAddr();
        img_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWebPage(imgURL);
            }
        });

        final double longitude = vo.getLongitude();
        final double latitude = vo.getLatitude();
        detail_map.setImageResource(R.drawable.loc_map_icon);
        detail_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+latitude + "," + longitude+"?q="+event_place);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            }
        });

        String start_date = setDate(vo.getsDate());
        String end_date = setDate(vo.geteDate());
        detail_time.setText(start_date + " ~ " + end_date);
        detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    Log.d("DetailAcitivity", "permission granted");
                    // String uri = detail_phone.getText().toString();
                    String uri = vo.getTel().toString();
                    callPhone(uri);
                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(DetailActivity.this, Manifest.permission.CALL_PHONE)) {
                        Log.d("DetailActivity", "need permission granted");
                    } else {
                        ActivityCompat.requestPermissions(DetailActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                    }
                }
            }
        });

        detail_exp.setText(vo.getDescription());
    }

    // for permission of phone call
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Detail/Request", "permission granted");
                    String uri = detail_phone.getText().toString();
                    callPhone(uri);
                } else {
                    Log.d("Detail/Request"," permission denied");
                }
                return;
            }
        }
    }

    // if success phone call permission
    private void callPhone(String uri){
        Log.d("detail_callPhone", uri);
        uri = "tel:" + uri;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
        try{
            startActivity(intent);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showWebPage(String url){
        try{
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String setDate(Date date){
        String d = new SimpleDateFormat("yyyy-MM-dd").format(date);
        d = d.replace(d.charAt(4), '년');
        d = d.replace(d.charAt(7), '월');
        d = d.concat("일");
        Log.d("date_detail", d +"/" + d.charAt(4) + d.charAt(7));
        return d;
    }

    class DAOAsyncTask extends AsyncTask<String, Void, MIGEventVO> {
        String id;
        MIGEventVO vo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            detailDialog = new ProgressDialog(DetailActivity.this);
//            // detailDialog.setTitle("Loading");
//            detailDialog.setMessage("데이터를 받아오는 중입니다");
//            detailDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            detailDialog.setCanceledOnTouchOutside(false);
//            detailDialog.show();
        }

        @Override
        protected MIGEventVO doInBackground(String... params) {

            dao = new MIGRetroDAO();
            id = params[0];
            vo = dao.getEntry(id);

            try {
                Thread.sleep(1000);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            return vo;
        }

        @Override
        protected void onPostExecute(MIGEventVO vo) {
            //  super.onPostExecute(aBoolean);
            setDetailInfo(vo);

            /**
             *  Set background image for detail view
             */
            switch (vo.getCategory()) {
                case "공연" :
                    mainImage.setImageResource(R.drawable.performance);
                    break;
                case "전시":
                    mainImage.setImageResource(R.drawable.exhibition);
                    break;
                case "교육":
                    mainImage.setImageResource(R.drawable.education1);
                    break;
                case "연극":
                    mainImage.setImageResource(R.drawable.theatre1);
                    break;
                case "영화":
                    mainImage.setImageResource(R.drawable.movies);
                    break;
            }

            collapsingToolbar.setTitle(vo.getTitle());
            collapsingToolbar.setCollapsedTitleGravity(View.TEXT_ALIGNMENT_VIEW_END);
//            detailDialog.dismiss();
//            detailDialog = null;
            rotateLoading.stop();
//            Toast.makeText(DetailActivity.this, "로딩이 완료되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

}
