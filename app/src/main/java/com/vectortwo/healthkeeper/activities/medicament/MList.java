package com.vectortwo.healthkeeper.activities.medicament;

/**
 * Created by skaper on 01.04.17.
 */
public class MList {
    private String mName;
    private String mDescription;
    private String  mDose;
    private String mNextTime;

    public MList(){}

    public MList(String name, String description,  String nextTime, String dose){
        this.mName = name;
        this.mDescription = description;
        this.mNextTime = nextTime;
        this.mDose = dose;
    }

    public String getmName(){return mName;}

    public void setmName(String name){this.mName = name;}

    public String getmDescription(){return mDescription;}

    public void setmDescription(String description){this.mDescription = description;}

    public String getmNextTime(){return mNextTime;}

    public void setmNextTime(String nextTime){this.mNextTime = nextTime;}

    public String getmDose(){return  mDose;}

    public void setmDose(String dose) {this.mDose = dose;}
}
