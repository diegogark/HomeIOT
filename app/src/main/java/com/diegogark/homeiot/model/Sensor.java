package com.diegogark.homeiot.model;

public class Sensor {
    private String sID, sTime, sNome, sUnid;
    private float  sVMax, sVMin, sValor;

    public Sensor() {
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsNome() {
        return sNome;
    }

    public void setsNome(String sNome) {
        this.sNome = sNome;
    }

    public String getsUnid() {
        return sUnid;
    }

    public void setsUnid(String sUnid) {
        this.sUnid = sUnid;
    }

    public float getsVMax() {
        return sVMax;
    }

    public void setsVMax(float sVMax) {
        this.sVMax = sVMax;
    }

    public float getsVMin() {
        return sVMin;
    }

    public void setsVMin(float sVMin) {
        this.sVMin = sVMin;
    }

    public float getsValor() {
        return sValor;
    }

    public void setsValor(float sValor) {
        this.sValor = sValor;
    }
}
