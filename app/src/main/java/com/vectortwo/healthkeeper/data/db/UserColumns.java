package com.vectortwo.healthkeeper.data.db;

/**
 * Created by ilya on 29/03/2017.
 */
public class UserColumns extends DBColumns {

    public void putFirstName(String name) {
        contentValues.put(DBContract.User.FIRSTNAME, name);
    }

    public void putSecondName(String name) {
        contentValues.put(DBContract.User.SECONDNAME, name);
    }

    public void putSex(String sex) {
        contentValues.put(DBContract.User.SEX, sex);
    }

    public void putAge(int age) {
        contentValues.put(DBContract.User.AGE, age);
    }

    public void putHeight(int height) {
        contentValues.put(DBContract.User.HEIGHT, height);
    }

    public void putWeight(int weight) {
        contentValues.put(DBContract.User.WEIGHT, weight);
    }

    public void putCity(String city) {
        contentValues.put(DBContract.User.CITY, city);
    }

    public void putBirthday(String birthday) {
        contentValues.put(DBContract.User.BIRTHDAY, birthday);
    }
}