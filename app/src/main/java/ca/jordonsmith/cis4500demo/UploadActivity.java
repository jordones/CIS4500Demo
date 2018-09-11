package ca.jordonsmith.cis4500demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UploadActivity extends AppCompatActivity {

    private UploadReceiver uploadReceiver;
    public static final String ACTION_UPLOAD_COMPLETE = "ca.jordonsmith.cis4500demo.broadcast.UPLOAD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPLOAD_COMPLETE);
        uploadReceiver = new UploadReceiver();
        registerReceiver(uploadReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(uploadReceiver);
    }


    public void onUploadClicked(View view) {
        EditText textBox = (EditText) findViewById(R.id.uploadText);
        String content = textBox.getText().toString();
        UploadService.startActionUpload(this, content);
    }
}
