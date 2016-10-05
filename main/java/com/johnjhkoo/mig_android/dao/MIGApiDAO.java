package com.johnjhkoo.mig_android.dao;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JohnKoo on 6/22/16.
 */
public class MIGApiDAO {
    private final static String TAG = "MIGApiDAO";
    JsonObject data;

    public MIGApiDAO() {
        data = migConvertStringToJsonObject(migSimpleHttpRequestToJsonString("test"));
    }

    public JsonObject getData() {
        return this.data;
    }

    private JsonObject migConvertStringToJsonObject(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(jsonStr);
        return elem.getAsJsonObject();
    }

    private String migSimpleHttpRequestToJsonString(String httpRequest) {

        String sampleUrl = "http://tomcat.johnjhkoo.com/migapi/mydb.getall";
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(sampleUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

            is = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return migGetStringFromInputStream(is);
    }

    private String migGetStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                Log.v(TAG, line);
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
