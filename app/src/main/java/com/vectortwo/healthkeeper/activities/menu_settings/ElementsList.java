package com.vectortwo.healthkeeper.activities.menu_settings;

import com.vectortwo.healthkeeper.activities.medicament.MList;

/**
 * Created by skaper on 24.04.17.
 */
public class ElementsList {
    private String name;
    private int image;
    private boolean status;
    public ElementsList(){}

    public ElementsList(String _name, int _image, boolean _status){
        this.name = _name;
        this.image = _image;
        this.status = _status;
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
}
