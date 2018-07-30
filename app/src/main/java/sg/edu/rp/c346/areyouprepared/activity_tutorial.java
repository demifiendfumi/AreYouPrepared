package sg.edu.rp.c346.areyouprepared;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class activity_tutorial extends AppCompatActivity {
    TextView txtHeader;
    TextView txtInfo;
    ImageView imageInfo1;
    TextView txtInfo2;
    ImageView imageInfo2;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        txtHeader = (TextView)findViewById(R.id. textHeader);
        txtInfo = (TextView)findViewById(R.id. textViewInfo);
        imageInfo1 = (ImageView)findViewById(R.id. imageViewInfo);
        txtInfo2 = (TextView)findViewById(R.id. textViewInfo2);
        imageInfo2 = (ImageView)findViewById(R.id. imageViewInfo2);
        btnBack = (Button)findViewById(R.id. buttonBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
