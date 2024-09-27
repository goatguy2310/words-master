package com.tundt.main;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imgSplash;
    Animation fade_up;
    Animation fade_down;
    Animation idle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imgSplash = (ImageView) findViewById(R.id.imgSplash);
        fade_up = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_up);
        fade_down = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_down);
        idle = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.idle);
        fade_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgSplash.startAnimation(idle);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        idle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imgSplash.startAnimation(fade_down);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fade_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent it = new Intent(SplashActivity.this, MainActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(it);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imgSplash.startAnimation(fade_up);
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

    @Override
    protected void onResume() {
        super.onResume();
        playSound("goat");
    }
}
