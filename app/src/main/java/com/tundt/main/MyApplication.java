package com.tundt.main;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Created by admin on 7/8/2017.
 */

public class MyApplication extends Application {
    public ArrayList<String>[] words;
    public ArrayList<String> common_words;

    public void loadText() {
        new LoadTextOperation().execute();
    }

    private class LoadTextOperation extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            common_words = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            words = new ArrayList[26];
            try {
                InputStream is = getAssets().open(Const.CORRECT_WORDS_FILE);
                Reader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);

                String line = null;
                int max = 0, i = 0;
                words[0] = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    int s = words[i].size();
                    if (s > 0) {
                        if (words[i].get(s - 1).charAt(0) != line.charAt(0)) {
                            i++;
                            words[i] = new ArrayList<>();
                        }
                    }
                    words[i].add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream is = getAssets().open(Const.COMMON_WORDS_FILE);
                Reader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);

                String line = null;
                while ((line = br.readLine()) != null) {
                    common_words.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
