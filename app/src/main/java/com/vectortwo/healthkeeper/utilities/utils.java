package com.vectortwo.healthkeeper.utilities;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by skaper on 24.04.17.
 */
public class utils {
    private Context contextThis;
    public utils(Context context){
        this.contextThis = context;
    }

    public int dpToPx(int value){
        return  (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value, contextThis.getResources().getDisplayMetrics());
    }
}
