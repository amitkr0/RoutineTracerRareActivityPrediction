package minorproject.knowmyself.Other;

import com.google.android.gms.maps.model.LatLng;




public class UserLocation {
    private LatLng latLng;
    private String inTime;
    private String outTime;
    private int locationType; // 0 - normal , 1 - seminormal and 2 - abnormal



    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public LatLng getLatLng(){
        return latLng;
    }

}
