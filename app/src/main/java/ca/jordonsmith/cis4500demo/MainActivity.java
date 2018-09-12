package ca.jordonsmith.cis4500demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // For when you click the upload button
    public void onClickUpload(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);

    }

    // For when you click the download button
    public void onClickDownload(View view) {
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }
}
