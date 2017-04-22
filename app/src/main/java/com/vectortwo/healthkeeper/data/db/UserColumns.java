package com.vectortwo.healthkeeper.data.db;

/**
 *  A helper class for managing {@link android.content.ContentValues} in {@link android.database.sqlite.SQLiteDatabase}
 *  for {@link DBContract.User} table. Ensures type-safety.
 */
public class UserColumns extends DBColumns {

    public UserColumns putFirstName(String name) {
        contentValues.put(DBContract.User.FIRSTNAME, name);
        return this;
    }

    public UserColumns putSecondName(String name) {
        contentValues.put(DBContract.User.SECONDNAME, name);
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

    public UserColumns putHeight(int height) {
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

    public UserColumns putBirthday(String birthday) {
        contentValues.put(DBContract.User.BIRTHDAY, birthday);
        return this;
    }
}