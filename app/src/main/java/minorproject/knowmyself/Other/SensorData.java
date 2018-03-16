package minorproject.knowmyself.Other;


public class SensorData {
    private static String TIME;
    private static  String ACC_X;
    private static  String ACC_Y;
    private static  String ACC_Z;
    private static String GYRO_1;
    private static String GYRO_2;
    private static String GYRO_3;
    private static String MAG_1;
    private static String MAG_2;
    private static String MAG_3;
    private static String latitude;
    private static String longitude;
    private static String speed;
    private static String response;
    private static String ID;
    private static String userid;


    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        SensorData.userid = userid;
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        SensorData.ID = ID;
    }


    public static String getTIME() {
        return TIME;
    }

    public static void setTIME(String TIME) {
        SensorData.TIME = TIME;
    }


    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        SensorData.response = response;
    }

    public static String getAccX() {
        return ACC_X;
    }

    public static void setAccX(String accX) {
        ACC_X = accX;
    }

    public static String getAccY() {
        return ACC_Y;
    }

    public static void setAccY(String accY) {
        ACC_Y = accY;
    }

    public static String getAccZ() {
        return ACC_Z;
    }

    public static void setAccZ(String accZ) {
        ACC_Z = accZ;
    }

    public static String getGyro1() {
        return GYRO_1;
    }

    public static void setGyro1(String gyro1) {
        GYRO_1 = gyro1;
    }

    public static String getGyro2() {
        return GYRO_2;
    }

    public static void setGyro2(String gyro2) {
        GYRO_2 = gyro2;
    }

    public static String getGyro3() {
        return GYRO_3;
    }

    public static void setGyro3(String gyro3) {
        GYRO_3 = gyro3;
    }

    public static String getMag1() {
        return MAG_1;
    }

    public static void setMag1(String mag1) {
        MAG_1 = mag1;
    }

    public static String getMag2() {
        return MAG_2;
    }

    public static void setMag2(String mag2) {
        MAG_2 = mag2;
    }

    public static String getMag3() {
        return MAG_3;
    }

    public static void setMag3(String mag3) {
        MAG_3 = mag3;
    }

    public static String getLatitude() {
        return latitude;
    }

    public static void setLatitude(String latitude) {
        SensorData.latitude = latitude;
    }

    public static String getLongitude() {
        return longitude;
    }

    public static void setLongitude(String longitude) {
        SensorData.longitude = longitude;
    }

    public static String getSpeed() {
        return speed;
    }

    public static void setSpeed(String speed) {
        SensorData.speed = speed;
    }
}
