package minorproject.knowmyself.Other;

/**
 * Created by Dell on 05-04-2018.
 */

public class ToDoBean {
    String event;
    String inTime;
    String outTime;
    String date;
    String location;

    public ToDoBean(String event,String date,String inTime,String outTime,String location){
        this.date=date;
        this.event=event;
        this.outTime=outTime;
        this.inTime=inTime;
        this.location=location;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEvent() {
        return event;

    }

    public void setEvent(String event) {
        this.event = event;
    }
}