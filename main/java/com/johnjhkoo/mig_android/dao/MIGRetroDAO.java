package com.johnjhkoo.mig_android.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.JsonObject;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;
import com.johnjhkoo.mig_android.pojos.MIGEventVOWithDist;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by JohnKoo on 6/27/16.
 */
public class MIGRetroDAO {

    private static final String TAG = "MIGRetroDAO";
    private static final String API_ADDRESS = "http://tomcat.johnjhkoo.com/migapi/";


    private class ServiceGenerator {

        private OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();

        private Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create());

        public <S> S createService(Class<S> serviceClass) {
            Retrofit retrofit = builder.client(httpClient.build()).build();
            return retrofit.create(serviceClass);
        }
    }

    public interface MigApiClient {

        @GET("mydb.getall2")
        Call<List<MIGEventVO>> getAllData();

        @GET("mydb.thumb")
        Call<ResponseBody> getThumbnail(@Query("id") String id);

        @POST("mydb.one")
        Call<MIGEventVO> getEntry(@Query("id") String id);

        @GET("mydb.places")
        Call<List<MIGEventVOWithDist>> getPlaces(@Query("lat") double lat, @Query("lon") double lon, @Query("num") int num);

        @GET("mydb.mget")
        Call<List<MIGEventVO>> getPage(@Query("startIndex") int startIndex, @Query("pageSize") int pageSize);

        @POST("mydb.get")
        Call<JsonObject> getScrollPage(@Query("startIndex") int startIndex, @Query("pageSize") int pageSize);

        @GET("mydb.category")
        Call<List<MIGEventVO>> getCategory(@Query("category") String category);

    }

    public JsonObject getScrollPage(int startIndex, int pageSize) {
        JsonObject obj = null;

        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);

        Call<JsonObject> call = client.getScrollPage(startIndex, pageSize);

        try {
            obj = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public List<MIGEventVO> getCategory(String category){
        List<MIGEventVO> list = null;

        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);

        Call<List<MIGEventVO>> call = client.getCategory(category);

        try {
            list = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<MIGEventVO> getPage(int startIndex, int pageSize) {
        List<MIGEventVO> list = null;
        Log.w(TAG, "startIndex : " + startIndex + " pageSize : " + pageSize);

        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);

        Call<List<MIGEventVO>> call = client.getPage(startIndex, pageSize);

        try {
            list = call.execute().body();
            Log.e(TAG, String.valueOf(list == null));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e(TAG, list.size() + "");

        return list;

    }


    public List<MIGEventVO> getAllData(){

        List<MIGEventVO> list = null;

        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);

        Call<List<MIGEventVO>> call = client.getAllData();

        try {
            list = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Bitmap getThumbnail(String id){

        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);
        Bitmap dedcodedByte = null;
        InputStream is = null;

        Call<ResponseBody> call = client.getThumbnail(id);
        Request req = call.request();

        try {
            Response<ResponseBody> response = call.execute();
            is = response.body().byteStream();

            System.out.println(is.read());


        } catch (IOException e) {
            e.printStackTrace();
        }
        dedcodedByte = BitmapFactory.decodeStream(is);

        return dedcodedByte;
    }

    public MIGEventVO getEntry(String id) {
        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);

        MIGEventVO vo = null;

        Call<MIGEventVO> call = client.getEntry(id);
//        Request req = call.request();

        try {
            vo = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vo;
    }

    public List<MIGEventVOWithDist> getPlaces(double lat, double lon, int num) {
        MigApiClient client = new ServiceGenerator().createService(MigApiClient.class);
        List<MIGEventVOWithDist> list = null;

        Call<List<MIGEventVOWithDist>> call = client.getPlaces(lat, lon, num);

        try {
            list = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public String getThumnailAddr(String id){
        String apiCall = API_ADDRESS + "mydb.thumb?id=";
//        id.replace(".jpg", "");

        Log.d(TAG, apiCall + id.replace(".jpg", ""));

        return apiCall + id.replace(".jpg", "");
    }
}
