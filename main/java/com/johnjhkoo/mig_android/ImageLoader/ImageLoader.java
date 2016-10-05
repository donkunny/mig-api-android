package com.johnjhkoo.mig_android.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.johnjhkoo.mig_android.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by donkunny on 16. 6. 30.
 */
public class ImageLoader {
    private MemoryCache memoryCache=new MemoryCache();
    private FileCache fileCache;
    private Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private ExecutorService executorService;
    private Handler handler = new Handler();

    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }

    final int stub_id= R.mipmap.ic_launcher;
    public void DisplayImage(String url, ImageView imageView, int required_size){
        imageViews.put(imageView, url);
        // Log.d("DisplayImage", "url is " + url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            // Log.d("DisplayImage", "Bitmap is absent");
            queuePhoto(url, imageView, required_size);

            if(getBitmap(url, required_size) != null){
                imageView.setImageBitmap(getBitmap(url, required_size));
            } else {
                // Log.d("DisplayImage", "getBitmap is null");
                imageView.setImageResource(stub_id);
            }
        }
    }

    private void queuePhoto(String url, ImageView imageView, int required_size){
        PhotoToLoad p=new PhotoToLoad(url, imageView, required_size);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url, int required_size){
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f, required_size);
        if(b!=null) {
            // Log.d("ImageLoader", "bitmap is exist in File");
            return b;
        }

        //from web
        try {
            // Log.d("ImageLoader", "from web / the url is " + url);
            Bitmap bitmap=null;
            // URL imageURL = new URL(url);
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f, required_size);
            return bitmap;
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, int required_size){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            //int width_tmp=o.outWidth, height_tmp=o.outHeight;
            /*
            final int REQUIRED_SIZE=300;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
*/

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = setImageSizes(o.outWidth, o.outHeight, required_size);
            o2.inPurgeable = true;
            //o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    private int setImageSizes(int width, int height, int requiered_size){
        int scale=1;
        while(true){
            if(width/2<requiered_size || height/2<requiered_size)
                break;
            width/=2;
            height/=2;
            scale*=2;
        }
        return scale;
    }


    //Task for the queue
    private class PhotoToLoad{
        public String url;
        public ImageView imageView;
        public int required_size;

        public PhotoToLoad(String u, ImageView i, int r){
            url=u;
            imageView=i;
            required_size = r;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url, photoToLoad.required_size);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            handler.post(bd);
            //Activity a=(Activity)photoToLoad.imageView.getContext();
            //a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
