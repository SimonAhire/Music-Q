package com.example.musicq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MainActivity2 extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
        mediaplayer.release();
        updateSeek.interrupt();
    }

    ImageView play,pause, next,prev;
    Button button;
    ArrayList<File> songs;
    TextView textview;
    MediaPlayer mediaplayer;
    String textContent;
    SeekBar seekbar;
    int position;
    Thread updateSeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textview = findViewById(R.id.textView);

//        play = findViewById(R.id.playImg);
//        pause = findViewById(R.id.pause);
        button = findViewById(R.id.button);
        seekbar = findViewById(R.id.seekBar1);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList)bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
         textview.setText(textContent);
         textview.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
       mediaplayer = MediaPlayer.create(this, uri);
       mediaplayer.start();

       seekbar.setMax(mediaplayer.getDuration());
       seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//               if( mediaplayer!=null && fromUser){
//                   mediaplayer.seekTo(progress*1000);
//               }
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {
            mediaplayer.seekTo(seekBar.getProgress());
           }
       });

       updateSeek = new Thread(){
           @Override
           public void run() {
               int currentPosition = 0;
               try {
                   while(currentPosition<mediaplayer.getDuration()){
                       currentPosition = mediaplayer.getCurrentPosition();
                       seekbar.setProgress(currentPosition);
                       sleep(800);
                   }
               }
               catch (Exception e){
                   e.printStackTrace();
               }
           }
       };
       updateSeek.start();


       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if (mediaplayer.isPlaying()){
                mediaplayer.pause();
                button.setText("Play");

            }else{
                mediaplayer.start();
                button.setText("Pause");
            }
           }
       });
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mediaplayer.isPlaying()){
//                    mediaplayer.pause();
//                    pause.setImageResource(R.drawable.play);
//                }else{
//                    mediaplayer.start();
//                    play.setImageResource(R.drawable.pause);
//
//                }
//            }
//        });





        prev = findViewById(R.id.prevImg);
        prev.setOnClickListener(v -> {
            mediaplayer.stop();
            mediaplayer.release();
            if(position!=0){
                position = position - 1;
            }
            else{
                position = songs.size() - 1;
            }
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaplayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaplayer.start();
           button.setText("Pause");
            seekbar.setMax(mediaplayer.getDuration());
            textContent = songs.get(position).getName().toString();
            textview.setText(textContent);

        });
        next = findViewById(R.id.nextImg);
        next.setOnClickListener(v -> {
            mediaplayer.stop();
            mediaplayer.release();
            if(position!=songs.size()-1){
                position = position + 1;
            }
            else{
                position = 0 ;
            }
            Uri uri12 = Uri.parse(songs.get(position).toString());
            mediaplayer = MediaPlayer.create(getApplicationContext(), uri12);
            mediaplayer.start();
            button.setText("Pause");
            seekbar.setMax(mediaplayer.getDuration());
            textContent = songs.get(position).getName().toString();
            textview.setText(textContent);
        });




    }

}