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
import java.util.Random;

public class Report_item extends AppCompatActivity implements
        TextToSpeech.OnInitListener{

    ImageView imageViewPicture;
    TextView txtStory;
    Button btnNext;
    Button btnBack;
    Button btnHome;
    int count = 0;
    String message = "";
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);
        tts = new TextToSpeech(this,this);

        imageViewPicture = (ImageView) findViewById(R.id.imageViewReport);
        txtStory = (TextView) findViewById(R.id.textViewStoryReport);
        btnNext = (Button) findViewById(R.id.buttonReportNext);
        btnBack = (Button) findViewById(R.id.buttonReportBack);
        btnHome = (Button) findViewById(R.id.buttonHomeReport);
        Random rnd = new Random();
        final int n = rnd.nextInt(2);

        final String[] mResult;

        if(n == 0){
            mResult = new String[]{"You got the security to check on the bag", "Apparently the bag was a bomb!", "The security thanks you for spotting the bag.", "You felt really proud of yourself."};
            message = "Thanks to your initiative,\nYou saved many lives that day.";
            imageViewPicture.setImageResource(R.drawable.search);
        }else{
            mResult = new String[]{"You got the security to check on the bag", "A student came running up to the bag and grabbed it.", "Dashing into the school exclaiming: 'I'm late!'"};
            message = "It seems like the bag is a safe object.\nBut thanks to your initiative,\nWho knows, you might save lives.";
            imageViewPicture.setImageResource(R.drawable.search);
        }

        txtStory.setText(mResult[count]);
        playSpeech(mResult[count]);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count += 1;
                if (count != mResult.length) {
                    txtStory.setText(mResult[count]);
                    playSpeech(mResult[count]);
                    btnBack.setVisibility(View.VISIBLE);
                    if (n == 1){
                        if (count == 0){
                            imageViewPicture.setImageResource(R.drawable.search);
                        }else{
                            imageViewPicture.setImageResource(R.drawable.late_to_class);
                        }
                    }
                }else{
                    //alert dialog
                    AlertDialog.Builder builder= new AlertDialog.Builder(Report_item.this);
                    builder.setTitle("Results");
                    builder.setMessage(message);
                    builder.setPositiveButton("Home", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(Report_item.this, MainActivity.class);
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
                    txtStory.setText(mResult[count]);
                    playSpeech(mResult[count]);
                    btnBack.setVisibility(View.VISIBLE);
                    if(n == 0){
                        imageViewPicture.setImageResource(R.drawable.boom);
                    }else{
                        if (count == 0){
                            imageViewPicture.setImageResource(R.drawable.search);
                        }else{
                            imageViewPicture.setImageResource(R.drawable.late_to_class);
                        }
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "You can't go back any further.", Toast.LENGTH_LONG);
                    toast.show();
                    btnBack.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                count = 0;
                Intent intent = new Intent(Report_item.this, MainActivity.class);
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
