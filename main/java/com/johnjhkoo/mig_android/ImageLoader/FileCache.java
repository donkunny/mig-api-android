package com.johnjhkoo.mig_android.ImageLoader;

import android.content.Context;

import java.io.File;

/**
 * Created by donkunny on 16. 6. 30.
 */
public class FileCache {
    private File cacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if(android.os.Environment.getExternalStorageDirectory().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TTImages_cache");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        // identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        //Another possible solution
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
}
