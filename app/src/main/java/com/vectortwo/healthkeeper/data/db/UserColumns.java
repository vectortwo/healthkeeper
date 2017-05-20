package com.vectortwo.healthkeeper.data.db;

import android.database.Cursor;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.User} table. Ensures type-safety.
 */
public class UserColumns extends DBColumns {

    public UserColumns putFirstName(String name) {
        contentValues.put(DBContract.User.FIRSTNAME, name);
        return this;
    }

    public UserColumns putSex(String sex) {
        contentValues.put(DBContract.User.SEX, sex);
        return this;
    }

    public UserColumns putAge(int age) {
        contentValues.put(DBContract.User.AGE, age);
        return this;
    }

    public UserColumns putHeight(float height) {
        contentValues.put(DBContract.User.HEIGHT, height);
        return this;
    }

    public UserColumns putWeight(int weight) {
        contentValues.put(DBContract.User.WEIGHT, weight);
        return this;
    }

    public UserColumns putCity(String city) {
        contentValues.put(DBContract.User.CITY, city);
        return this;
    }

    public UserColumns putCountry(String country) {
        contentValues.put(DBContract.User.COUNTRY, country);
        return this;
    }

    public static String getFirstName(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.FIRSTNAME);
        return c.getString(colId);
    }

    public static String getSex(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.SEX);
        return c.getString(colId);
    }

    public static int getAge(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.AGE);
        return c.getInt(colId);
    }

    public static int getHeight(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.HEIGHT);
        return c.getInt(colId);
    }

    public static int getWeight(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.WEIGHT);
        return c.getInt(colId);
    }

    public static String getCity(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.CITY);
        return c.getString(colId);
    }

    public static String getCountry(Cursor c) {
        int colId = c.getColumnIndexOrThrow(DBContract.User.COUNTRY);
        return c.getString(colId);
    }
}