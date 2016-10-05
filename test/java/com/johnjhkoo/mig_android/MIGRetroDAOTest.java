package com.johnjhkoo.mig_android;

import android.graphics.Bitmap;
import android.media.Image;

import com.google.gson.JsonObject;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import org.junit.Test;

import java.util.List;

/**
 * Created by JohnKoo on 6/27/16.
 */
public class MIGRetroDAOTest {

    @Test
    public void list_daoTest(){
        MIGRetroDAO dao = new MIGRetroDAO();
        List<MIGEventVO> obj = dao.getAllData();

        for (MIGEventVO vo : obj) {
            System.out.println(vo.getTitle());
        }
    }

    @Test
    public void thumb_daoTest(){
        String id = "5431487B895B4D6A9C48197A28BE04B1";

        MIGRetroDAO dao = new MIGRetroDAO();
        Bitmap img = dao.getThumbnail(id.toLowerCase());

        System.out.println(img.getByteCount());
    }
}
