package com.rychlici.wifilocator.backend;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

public class WifiScannerService extends IntentService {
    private static final String ACTION_DATA = "com.rychlici.wifilocator.backend.action.DATA";

    public WifiScannerService() {
        super("WifiScannerService");
    }

    /**
     * Start Intent Action for read data from WiFi adapter.
     * @param context App context
     */
    public static void startActionReadData(Context context) {
        Intent intent = new Intent(context, WifiScannerService.class);
        intent.setAction(ACTION_DATA);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DATA.equals(action)) {
                handleActionReadData();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread
     */
    private void handleActionReadData() {
        // TODO
    }
}
