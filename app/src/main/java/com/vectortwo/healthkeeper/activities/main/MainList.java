package com.vectortwo.healthkeeper.activities.main;

/**
 * Created by skaper on 24.04.17.
 */
public class MainList {
    private String name;
    private int image;
    private boolean status;
    private String type;
    public MainList(){}

    public MainList(String _name, int _image, boolean _status, String _type){
        this.name = _name;
        this.image = _image;
        this.status = _status;
        this.type = _type;
    }

    public MainList(String _type){
        this.type = _type;
    }

    public String getName(){
        return name;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int _image){
        this.image = _image;
    }

    public boolean getStatus(){
        return status;
    }

    public void setStatus(boolean _status){
        this.status = _status;
    }

    public String getType(){
        return type;
    }
}
