package com.tundt.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import com.tundt.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

public class GuessActivity extends AppCompatActivity {

    TextView txtDef;
    private GridView gridGuess;
    private TextAdapter adapterTexts;
    private ArrayList<String> textsArr = new ArrayList<>();
    ImageButton btnGuessCheck;
    EditText txtGuessText;
    ImageButton btnGuessDel;
    TextView txtGuessTime;
    TextView txtGuessScore;

    private CountDownTimer timer;

    ArrayList<Note> noteArr;

    private ArrayList<Integer> pickedPosition = new ArrayList<>();

    private List<String> alphabet;

    String answer;
    int answerPos = -1;

    int score = 0;
    int total = 0;

    private MediaPlayer player;

    long time;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        time = Const.TIME_GAME_OVER_LIMIT;

        alphabet = asList(getResources().getStringArray(R.array.alphabet));

        addControls();
        generatePickingLetter();
    }

    private void addControls() {
        txtDef = (TextView) findViewById(R.id.txtDef);
        gridGuess = (GridView) findViewById(R.id.gridGuess);
        textsArr = new ArrayList<>();
        adapterTexts = new TextAdapter(GuessActivity.this, R.layout.item, textsArr);
        gridGuess.setAdapter(adapterTexts);
        gridGuess.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                pickedPosition.add(i);
                txtGuessText.append(textsArr.get(i));
                textsArr.set(i, "");
                playSound("tick");
                TextView txt = (TextView) view;
                txt.setText("");
            }
        });
        btnGuessCheck = (ImageButton) findViewById(R.id.btnGuessCheck);
        btnGuessDel = (ImageButton) findViewById(R.id.btnGuessDel);
        txtGuessText = (EditText) findViewById(R.id.txtGuessText);

        txtGuessTime = (TextView) findViewById(R.id.txtGuessTime);
        txtGuessScore = (TextView) findViewById(R.id.txtGuessScore);

        btnGuessCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtGuessText.getText().toString().trim().equalsIgnoreCase(answer)) {
                    score++;
                    showToast(R.drawable.true_img);
                    chooseWord();
                    txtGuessText.setText("");
                    timer.cancel();
                    timer.start();
                } else {
                    showToast(R.drawable.false_img);
                }
            }
        });

        btnGuessDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtGuessText.getText().toString().trim();
                if (text.length() > 0) {
                    text.charAt(text.length() - 1);
                    String lastLetter = String.valueOf(text.charAt(text.length() - 1));
                    Integer lastPositionPicked = pickedPosition.get(pickedPosition.size() - 1);
                    System.out.println(lastPositionPicked);
                    pickedPosition.remove(pickedPosition.size() - 1);
                    txtGuessText.setText(text.substring(0, text.length() - 1));
                    textsArr.set(lastPositionPicked, lastLetter);
                    adapterTexts.notifyDataSetChanged();
                }
            }
        });
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

    @Override
    protected void onStop() {
        super.onStop();
        if (Const.sound) player.release();
        player = null;
        timer.cancel();
    }


    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void createTimer() {
        int delay = 1000;

        timer = new CountDownTimer(time, delay) {
            @Override
            public void onTick(long l) {
                time = l;
                long endTime = l / 1000;

                txtGuessTime.setText("" + endTime);
                txtGuessTime.setTextColor(Color.BLACK);
                if (endTime <= 5) {
                    txtGuessTime.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                txtGuessText.setText("");
                timer.cancel();
                timer.start();

                showToast(R.drawable.false_img);
                chooseWord();

                Vibrator v = (Vibrator) GuessActivity.this.getSystemService(VIBRATOR_SERVICE);
                if (Const.vibrate) v.vibrate(500);
            }
        };
        timer.start();
    }

    private void generatePickingLetter() {
        noteArr = new ArrayList<>();

        Cursor c = Const.noteDatabase.query("notes", null, null, null, null, null, null);
        noteArr.clear();
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String word = c.getString(1);
            String meaning = c.getString(2);
            noteArr.add(new Note(word, meaning, id));
        }
        c.close();

        total = noteArr.size();

        chooseWord();
    }

    private void chooseWord() {
        textsArr.clear();

        if(answerPos != -1) noteArr.remove(answerPos);

        if(noteArr.size() == 0) {
            end();
            return;
        }

        answerPos = new Random().nextInt(noteArr.size());
        Note current = noteArr.get(answerPos);

        answer = current.word;
        txtDef.setText(current.getMeaning());

        String word = answer;

        word = shuffle(word);

        for(int i = 0; i < word.length(); i++) {
            textsArr.add(String.valueOf(word.charAt(i)).toUpperCase());
        }
        adapterTexts.notifyDataSetChanged();
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

    private void playSound(String name) {
        try {
            AssetFileDescriptor afd = getAssets().openFd("music/" + name + ".mp3");
            MediaPlayer player = new MediaPlayer();
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

    private void showToast(int imageResource) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, null);

        ImageView imgPic = (ImageView) layout.findViewById(R.id.imgPic);
        imgPic.setImageResource(imageResource);

        if (toast == null) toast = new Toast(GuessActivity.this);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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

        AlertDialog alertDialog = new AlertDialog.Builder(GuessActivity.this).create();
        if (lose) {
            alertDialog.setTitle(getString(R.string.ask_title));
            alertDialog.setMessage("Are you sure? You have answered " + score + "/" + total + " correctly");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            end();
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
            alertDialog.setMessage("Are you sure? You have answered " + score + "/" + total + " correctly");
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

    private void end() {
        try {
            if (toast != null) toast.cancel();

            player.stop();
            playSound("end");

            AlertDialog alertDialog = new AlertDialog.Builder(GuessActivity.this).create();
            alertDialog.setTitle(getString(R.string.game_over));
            alertDialog.setMessage("You have completed " + score + "/" + total);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restart() {
        textsArr.clear();
        adapterTexts.notifyDataSetChanged();
        generatePickingLetter();
        txtGuessText.setText("");
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
}
