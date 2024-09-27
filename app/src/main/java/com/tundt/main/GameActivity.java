package com.tundt.main;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tundt.adapter.TextAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class GameActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, DialogInterface.OnClickListener {

    private MyApplication app;

    private TextView txtTime;
    private TextView txtScore;
    private EditText txtText;
    private TextView txtResult;

    private EditText txtInput;

    private GridView gridText;
    private TextAdapter adapterTexts;
    private ArrayList<String>[] words;
    private ArrayList<String> common_words;
    private ArrayList<String> textsArr = new ArrayList<>();
    private ArrayList<Integer> pickedPosition = new ArrayList<>();

    private CountDownTimer timer;
    private long time;
    private int score = 0;
    private String level;
    private Ringtone alertSound;
    private String TAG = "TAG";
    private String player_name = "-";
    private List<String> alphabet;

    private MediaPlayer player;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        app = (MyApplication) getApplication();

        level = getLevel();

        time = Const.TIME_GAME_OVER_LIMIT;

        alphabet = asList(getResources().getStringArray(R.array.alphabet));

        generatePickingLetter();
        addControls();
    }

    public String getLevel() {
        return getSharedPreferences("level", MODE_PRIVATE).getString("level", "Easy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        createTimer();
        try {
            String music_source = "music.mp3";

            AssetFileDescriptor afd = getAssets().openFd("music/" + music_source);
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addControls() {
        View dialog = getLayoutInflater().inflate(R.layout.dialog_input, null);

        gridText = (GridView) findViewById(R.id.grid);
        adapterTexts = new TextAdapter(GameActivity.this, R.layout.item, textsArr);
        gridText.setAdapter(adapterTexts);
        gridText.setOnItemClickListener(this);
        txtInput = (EditText) dialog.findViewById(R.id.txtInput);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtText = (EditText) findViewById(R.id.txtText);
        txtResult = (TextView) findViewById(R.id.result);
        txtInput = (EditText) dialog.findViewById(R.id.txtInput);

        txtText.setTypeface(Typeface.createFromAsset(getAssets(), "font/KG.ttf"));
        txtScore.setText("" + score);

        ImageButton btnCheck = (ImageButton) findViewById(R.id.btnCheck);
        ImageButton btnDel = (ImageButton) findViewById(R.id.btnDel);
        btnCheck.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        alertSound = RingtoneManager.getRingtone(getApplicationContext(), notification);
        alertSound.play();
    }

    private void createTimer() {
        int delay = 1000;

        timer = new CountDownTimer(time, delay) {
            @Override
            public void onTick(long l) {
                time = l;
                long endTime = l / 1000;

                txtTime.setText("" + endTime);
                txtTime.setTextColor(Color.BLACK);
                if (endTime <= 5) {
                    txtTime.setTextColor(Color.RED);
                    alertSound.play();
                }
            }

            @Override
            public void onFinish() {
                lose();
                Vibrator v = (Vibrator) GameActivity.this.getSystemService(VIBRATOR_SERVICE);
                if (Const.vibrate) v.vibrate(500);
            }
        };
        timer.start();
    }

    private void playSound(String name) {
        MediaPlayer player = null;
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/" + name + ".mp3");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            if (Const.FX) player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generatePickingLetter() {
        if (level.equals("Easy")) {
            String words = "";
            while (words.length() < Const.NUMBER_PICKING_LETTER) {
                if(app.common_words.size() <= 0) break;
                words += app.common_words.get(new Random().nextInt(app.common_words.size()));
            }
            if (words.length() > Const.NUMBER_PICKING_LETTER) {
                words = words.substring(0, 96);
            }
            words = words.toUpperCase();
            words = shuffle(words);
            for (int i = 0; i < Const.NUMBER_PICKING_LETTER; i++) {
                String text = "" + words.charAt(i);
                textsArr.add(text);
            }
        } else if (level.equals("Hard")) {
            Random r = new Random();
            for (int i = 0; i < Const.NUMBER_PICKING_LETTER; i++) {
                String text = alphabet.get(r.nextInt(alphabet.size()));
                textsArr.add(text);
            }
        } else if(level.equals("Custom (From word list)")) {
            String words = "";
            Cursor c = Const.noteDatabase.query("notes", null, null, null, null, null, null, null);
            while (words.length() < Const.NUMBER_PICKING_LETTER) {
                while(c.moveToNext()) {
                    words += c.getString(1);
                }
                words += alphabet.get(new Random().nextInt(alphabet.size()));
            }
            if (words.length() > Const.NUMBER_PICKING_LETTER) {
                words = words.substring(0, 96);
            }
            words = words.toUpperCase();
            words = shuffle(words);
            for (int i = 0; i < Const.NUMBER_PICKING_LETTER; i++) {
                String text = "" + words.charAt(i);
                textsArr.add(text);
            }
        }
    }

    public String shuffle(String input) {
        List<Character> characters = new ArrayList<Character>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pickedPosition.add(i);
        txtText.append(textsArr.get(i));
        textsArr.set(i, "");
        playSound("tick");
        TextView txt = (TextView) view;
        txt.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCheck:
                String word = txtText.getText().toString().trim();
                if(word.isEmpty()) break;

                if (app.words[alphabet.indexOf("" + word.charAt(0))].contains(word)) {
                    playSound("true");
                    txtText.setText("");
                    int length = word.length();
                    if (level.equals("Easy") || level.equals("Custom (From word list)")) score = (int) (score + (5 * length));
                    else if (level.equals("Hard")) score = (int) (score + (8 * length));
                    txtScore.setText("" + score);
                    txtResult.append("" + word + " | ");
                    showToast(R.drawable.true_img);
                    int i = new Random().nextInt(1);
                    if (i == 0 && score >= 50 && level.equals("Easy")) {
                        int power = new Random().nextInt(2);
                        if (power == 0) {
                            showToast(R.drawable.text);
                            addText();
                        } else if (power == 1) {
                            int timeNum = 30;
                            if(level.equals("Hard")) timeNum = 15;

                            time = 30;
                            time += new Random().nextInt(timeNum);
                            showToast(R.drawable.time);
                            txtTime.setTextColor(Color.GREEN);
                            ObjectAnimator animate = ObjectAnimator.ofInt(txtTime, "textColor", Color.GREEN, Color.BLACK);
                            animate.setDuration(2000);
                            animate.setEvaluator(new ArgbEvaluator());
                            animate.start();
                        }
                    }

                    int rand = new Random().nextInt(3);
                    if (rand == 0 && score >= 75 && length >= 3 && level.equals("Hard")) {
                        int power = new Random().nextInt(2);
                        if (power == 0) {
                            showToast(R.drawable.text);
                            addText();
                        } else if (power == 1) {
                            time += new Random().nextInt(5);
                            showToast(R.drawable.time);
                            txtTime.setTextColor(Color.GREEN);
                            ObjectAnimator animate = ObjectAnimator.ofInt(txtTime, "textColor", Color.GREEN, Color.BLACK);
                            animate.setDuration(2000);
                            animate.setEvaluator(new ArgbEvaluator());
                            animate.start();
                        }
                    }
                    timer.cancel();
                    timer.start();
                } else {
                    playSound("wrong");
                    txtText.setTextColor(Color.RED);
                    ObjectAnimator anim = ObjectAnimator.ofInt(txtText, "textColor", Color.RED, Color.BLACK);
                    anim.setDuration(2000);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.start();
                    showToast(R.drawable.false_img);
                }
                txtResult.setSelected(true);
                break;
            case R.id.btnDel:
                String text = txtText.getText().toString().trim();
                if (text.length() > 0) {
                    text.charAt(text.length() - 1);
                    String lastLetter = String.valueOf(text.charAt(text.length() - 1));
                    Integer lastPositionPicked = pickedPosition.get(pickedPosition.size() - 1);
                    System.out.println(lastPositionPicked);
                    pickedPosition.remove(pickedPosition.size() - 1);
                    txtText.setText(text.substring(0, text.length() - 1));
                    textsArr.set(lastPositionPicked, lastLetter);
                    adapterTexts.notifyDataSetChanged();
                }
                break;
        }
        playSound("tick");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        player_name = txtInput.getText().toString().trim();
        if (player_name.equals("")) player_name = "-";
    }

    private void addText() {
        int pickedText = pickedPosition.size();
        int addText = 2;
        if (pickedText > 5) addText = 5;
        else addText = 2;
        int[] addedPos = new int[5];
        List<String> alphabet = asList(getResources().getStringArray(R.array.alphabet));
        Random r = new Random();
        for (int i = 0; i < addText; i++) {
            int num = new Random().nextInt(pickedPosition.size());
            int pos = pickedPosition.get(num);
            textsArr.set(pos, alphabet.get(r.nextInt(alphabet.size())));
            adapterTexts.notifyDataSetChanged();
            pickedPosition.remove(num);
        }
    }

    private boolean saveScore() {
        SaveScoreOperation saver = new SaveScoreOperation(getIntent().getStringExtra("name"));
        saver.execute();

        boolean newBest = saver.isNewBest();

        return newBest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart:
                exit(false);
                return true;
            case R.id.menu_exit:
                exit(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void exit(boolean lose) {
        if (toast != null) toast.cancel();
        timer.cancel();

        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        if (lose) {
            alertDialog.setTitle(getString(R.string.ask_title));
            alertDialog.setMessage(getString(R.string.asking) + score);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            lose();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            createTimer();
                        }
                    });
        } else {
            alertDialog.setTitle(getString(R.string.restart_title));
            alertDialog.setMessage(getString(R.string.asking) + score);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            restart();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            createTimer();
                        }
                    });
        }
        alertDialog.show();
    }

    private void restart() {
        textsArr.clear();
        adapterTexts.notifyDataSetChanged();
        generatePickingLetter();
        txtResult.setText("");
        txtText.setText("");
        timer.cancel();
        timer.start();
        player.stop();
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/music.mp3");
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void lose() {
        try {
            if (toast != null) toast.cancel();

            player.stop();
            playSound("end");

            boolean newBest = saveScore();

            if (!newBest) {
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle(getString(R.string.game_over));
                alertDialog.setMessage(getString(R.string.game_over_msg) + score);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(int imageResource) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, null);

        ImageView imgPic = (ImageView) layout.findViewById(R.id.imgPic);
        imgPic.setImageResource(imageResource);

        if (toast == null) toast = new Toast(GameActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        exit(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Const.sound) player.release();
        player = null;
        timer.cancel();
    }

    private class SaveScoreOperation extends AsyncTask<Boolean, Void, Boolean> {

        private boolean newBest = false;
        private String name;

        public SaveScoreOperation(String name) {
            this.name = name;
        }

        public boolean isNewBest() {
            return newBest;
        }

        private void setNewBest(boolean newBest) {
            this.newBest = newBest;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            setNewBest(b);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            int place = 6;
            boolean newBest = false;

            Cursor cursor = Const.scoreDatabase.query("scores", null, null, null, null, null, null);

            cursor.moveToPosition(5);

            while (cursor.moveToPrevious()) {
                int placeNum = cursor.getInt(0);
                int score = cursor.getInt(2);

                if (GameActivity.this.score > score) place = placeNum;
            }

            if (place < 6) newBest = true;

            while (cursor.moveToNext()) {
                int placeNum = cursor.getInt(0);
                String name = cursor.getString(1);
                int score = cursor.getInt(2);

                if (cursor.getPosition() >= place && newBest) {
                    ContentValues row = new ContentValues();
                    row.put("score", score);
                    row.put("name", name);
                    Const.scoreDatabase.update("scores", row, "place=?", new String[]{String.valueOf(placeNum + 1)});
                }
            }

            if (newBest) {
                ContentValues row = new ContentValues();
                row.put("score", GameActivity.this.score);
                row.put("name", this.name);
                Const.scoreDatabase.update("scores", row, "place=?", new String[]{String.valueOf(place)});
            }

            cursor.close();

            return newBest;
        }
    }
}
