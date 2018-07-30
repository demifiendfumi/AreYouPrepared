package sg.edu.rp.c346.areyouprepared;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class MainGame extends AppCompatActivity implements
        TextToSpeech.OnInitListener{

    ImageView imageViewPicture;
    TextView txtStory;
    Button btnNext;
    Button btnBack;
    Button btnHome;
    int count = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        tts = new TextToSpeech(this,this);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewStart);
        txtStory = (TextView) findViewById(R.id.textViewStory);
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnBack = (Button) findViewById(R.id.buttonBack);
        btnHome = (Button) findViewById(R.id.buttonHome);

        final String[] mTestArray = {"You are on your way to school", "However, you spotted an unattended bag", "What would you do?"};

        txtStory.setText(mTestArray[count]);
        playSpeech(mTestArray[count]);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count += 1;
                if (count != mTestArray.length) {
                    txtStory.setText(mTestArray[count]);
                    playSpeech(mTestArray[count]);
                    btnBack.setVisibility(View.VISIBLE);
                        imageViewPicture.setImageResource(R.drawable.unattended_bag);
                }else{
                    //alert dialog
                    AlertDialog.Builder builder= new AlertDialog.Builder(MainGame.this);
                    builder.setTitle("What would you do?");
                    builder.setPositiveButton("Report it", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(MainGame.this, Report_item.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNeutralButton("Continue walking", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(MainGame.this, Continue_walking.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                }
            });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count -= 1;
                if (count != -1) {
                    txtStory.setText(mTestArray[count]);
                    playSpeech(mTestArray[count]);
                    btnBack.setVisibility(View.VISIBLE);
                    if (count == 0){
                        imageViewPicture.setImageResource(R.drawable.wnr_schoolbus);
                    }else{
                        imageViewPicture.setImageResource(R.drawable.unattended_bag);
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "You are at the start!", Toast.LENGTH_LONG);
                    toast.show();
                    btnBack.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                count = 0;
                Intent intent = new Intent(MainGame.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.UK);

            // tts.setPitch(5); // set pitch level
            // tts.setSpeechRate(2); // set speech speed rate

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported");
            } else {
                txtStory.setEnabled(true);
            }

        } else {
            Log.e("TTS", "Speech Initialisation Failed");
        }

    }
    private void playSpeech(String sentence) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(sentence);
        } else {
            ttsUnder20(sentence);
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }
}