package gosec.xpose_detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import utils.HttpUtils;
import utils.StaticData;

public class MainActivity extends AppCompatActivity {
    public void setContents (final String contents) {
        ClipboardManager clipboardManager =
                (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText("adasd");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText urlEdit = (EditText) findViewById(R.id.url);
        Button sendButton = (Button) findViewById(R.id.send);
        Button classLoadButton = (Button) findViewById(R.id.class_load);
        TextView sinkView = (TextView) findViewById(R.id.sink);
        urlEdit.setText(StaticData.sinkURL);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = urlEdit.getText().toString();
                            String resp = HttpUtils.downloadJSONByOkHttp(path);
                            sinkView.setText(resp);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //temp
        classLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
            }
        });
    }


}