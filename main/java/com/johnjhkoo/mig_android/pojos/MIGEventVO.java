package com.johnjhkoo.mig_android.pojos;

import java.util.Date;

/**
 * Created by JohnKoo on 6/22/16.
 */
public class MIGEventVO {

    private String  id;
    private Integer	originalId;
    private String 	title;
    private Date    sDate;
    private Date 	eDate;
    private String 	place;
    private	String	gAddr;
    private double 	latitude;
    private double	longitude;
    private String 	management;
    private String	feeType;
    private String	feeAmount;
    private String	tel;
    private	String	url;
    private String	description;
    private	String	posterAddr;
    private String	posterThumb;
    private	String	reserveInfo;
    private String	reserveUrl;
    private Date	pDate;
    private String  category;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Integer originalId) {
        this.originalId = originalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getsDate() {
        return sDate;
    }

    public void setsDate(Date sDate) {
        this.sDate = sDate;
    }

    public Date geteDate() {
        return eDate;
    }

    public void seteDate(Date eDate) {
        this.eDate = eDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getgAddr() {
        return gAddr;
    }

    public void setgAddr(String gAddr) {
        this.gAddr = gAddr;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterAddr() {
        return posterAddr;
    }

    public void setPosterAddr(String posterAddr) {
        this.posterAddr = posterAddr;
    }

    public String getPosterThumb() {
        return posterThumb;
    }

    public void setPosterThumb(String posterThumb) {
        this.posterThumb = posterThumb;
    }

    public String getReserveInfo() {
        return reserveInfo;
    }

    public void setReserveInfo(String reserveInfo) {
        this.reserveInfo = reserveInfo;
    }

    public String getReserveUrl() {
        return reserveUrl;
    }

    public void setReserveUrl(String reserveUrl) {
        this.reserveUrl = reserveUrl;
    }

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(Date pDate) {
        this.pDate = pDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
