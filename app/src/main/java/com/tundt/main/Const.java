package com.tundt.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DAT_HOME_HP on 03/06/2017.
 */

public class Const {
    /*
    /Play 5 sound phù hợp khi: còn 5s, bắt đầu vào game, kết thúc game over, pick từ đúng, pick từ sai
    /
    /
     */


    public static final int TIME_GAME_OVER_LIMIT = 1000*31;

    public static final int NUMBER_PICKING_LETTER = 96;
    public static final String CORRECT_WORDS_FILE = "text/all.txt";
    public static final String COMMON_WORDS_FILE = "text/all_common.txt";

    //Những name để đặt khi lưu

    public static final String SCORE = "score";
    public static final String SETTINGS = "settings";

    public static SQLiteDatabase database = null;

    //Settings

    public static boolean vibrate = true;
    public static boolean notification = true;
    public static boolean countdown = true;
    public static boolean FX = true;
    public static boolean sound = true;
}
