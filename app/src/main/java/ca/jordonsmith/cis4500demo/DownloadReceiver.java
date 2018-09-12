package ca.jordonsmith.cis4500demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String statusMessage = intent.getStringExtra("message");
        Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
    }
}
