package com.stereci.memgame;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class zoroyun extends AppCompatActivity implements View.OnClickListener {
    private int numberOfElements;
    private MemoryButton[] buttons;

    private int[] buttonGraphicLocaitons;
    private int[] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;

    public int score = 0;
    public int stop = 0;

    Button start;
    Button cancel;
    static CountDownTimer countDownTimer;
    static TextView time;
    long kalan;

    public View.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            /*
            switch (v.getId()){
                case R.id.start:
                    start();
                    break;
                case R.id.cancel:
                    cancel();
                    break;
            }*/
            if (stop == 0 && v.getId() == R.id.cancel) {
                cancel();
                stop++;
                cancel.onWindowSystemUiVisibilityChanged(0);
                cancel.setText("STOP");
                score-=5;
                Toast.makeText(getApplicationContext(),"-5 points!",Toast.LENGTH_SHORT).show();
            }
            if (stop == 1 && v.getId() == R.id.start) {
                start();
                stop++;
                start.onWindowSystemUiVisibilityChanged(0);
                start.setText("RESTART");
            }

        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoroyun);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(btnClickListener);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(btnClickListener);
        time = (TextView) findViewById(R.id.time);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid_layout_6x6);
        int numColumns = gridLayout.getColumnCount();
        int numRows = gridLayout.getRowCount();

        numberOfElements = numColumns * numRows;
        buttons = new MemoryButton[numberOfElements];
        buttonGraphics = new int[numberOfElements / 2];

        buttonGraphics[0] = R.drawable.buttona;
        buttonGraphics[1] = R.drawable.buttonb;
        buttonGraphics[2] = R.drawable.buttonc;
        buttonGraphics[3] = R.drawable.buttond;
        buttonGraphics[4] = R.drawable.buttone;
        buttonGraphics[5] = R.drawable.buttonf;
        buttonGraphics[6] = R.drawable.buttonx;
        buttonGraphics[7] = R.drawable.buttonu;
        buttonGraphics[8] = R.drawable.buttono;
        buttonGraphics[9] = R.drawable.buttonh;
        buttonGraphics[10] = R.drawable.buttonp;
        buttonGraphics[11] = R.drawable.buttont;
        buttonGraphics[12] = R.drawable.buttons;
        buttonGraphics[13] = R.drawable.buttonl;
        buttonGraphics[14] = R.drawable.buttonm;
        buttonGraphics[15] = R.drawable.buttonk;
        buttonGraphics[16] = R.drawable.buttong;
        buttonGraphics[17] = R.drawable.buttony;

        buttonGraphicLocaitons = new int[numberOfElements];
        shuffleButtonGraphics();

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                MemoryButton tempButton = new MemoryButton(this, r, c, buttonGraphics[buttonGraphicLocaitons[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons[r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }
        /*
        Button buttonScore=(Button)findViewById(R.id.buttonScore);
        buttonScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),result.class);
                intent.putExtra("SCORE",score);
                startActivity(intent);
            }
        });*/
    }

    boolean isRunning = false;

    public void start() {
        time.setText("50");
        countDownTimer = new CountDownTimer(50 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isRunning = true;
                kalan=millisUntilFinished;
                time.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                //time.setText("Done!");
                Intent intent = new Intent(getApplicationContext(), result.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
            }

        };
        countDownTimer.start();
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /*
    public void resume() throws InterruptedException {
        countDownTimer.wait();
    }*/
    public void shuffleButtonGraphics() {
        Random rand = new Random();
        for (int i = 0; i < numberOfElements; i++) {
            buttonGraphicLocaitons[i] = i % (numberOfElements / 2);
        }
        for (int i = 0; i < numberOfElements; i++) {
            int temp = buttonGraphicLocaitons[i];
            int swapIndex = rand.nextInt(36);
            buttonGraphicLocaitons[i] = buttonGraphicLocaitons[swapIndex];
            buttonGraphicLocaitons[swapIndex] = temp;
        }
    }

    @Override
    public void onClick(View view) {
        if (stop != 1) {
            if (isBusy) {
                return;
            }
            if (!isRunning) {
                start();
                return;
            }
            MemoryButton button = (MemoryButton) view;
            if (button.isMatched) {
                return;
            }
            if (selectedButton1 == null) {
                selectedButton1 = button;
                selectedButton1.flip();
                return;
            }
            if (selectedButton1.getId() == button.getId()) {
                return;
            }
            if (selectedButton1.getFrontImageDrawableId() == button.getFrontImageDrawableId()) {
                button.flip();
                button.setMatched(true);
                selectedButton1.setMatched(true);

                selectedButton1.setEnabled(false);
                button.setEnabled(false);

                selectedButton1 = null;
                score += 10;
                if (countDownTimer != null) {
                    Toast.makeText(getApplicationContext(),"+2 seconds!",Toast.LENGTH_SHORT).show();
                    countDownTimer.cancel();
                    countDownTimer = new CountDownTimer(kalan + 2000, 1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            isRunning = true;
                            kalan=millisUntilFinished;
                            time.setText(""+millisUntilFinished/1000);
                        }

                        @Override
                        public void onFinish() {
                            isRunning = false;
                            //time.setText("Done!");
                            Intent intent=new Intent(getApplicationContext(),result.class);
                            intent.putExtra("SCORE",score);
                            startActivity(intent);
                        }

                    };
                    countDownTimer.start();
                }
                if (score == 180 || score == 175) {
                    Intent intent = new Intent(getApplicationContext(), kazan.class);
                    intent.putExtra("SCORE", score);
                    score = 0;
                    cancel();
                    startActivity(intent);
                }
                return;
            } else {
                selectedButton2 = button;
                selectedButton2.flip();
                isBusy = true;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectedButton2.flip();
                        selectedButton1.flip();
                        selectedButton1 = null;
                        selectedButton2 = null;
                        isBusy = false;
                    }
                }, 500);
            }
        }
    }
}
