package com.recordsplitvideoandroid;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private Button mButton;
    private static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.record_video);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra("android.intent.extra.durationLimit", 60);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            //mVideoView.setVideoURI(videoUri);
            String realPath = null;
            /*if (Build.VERSION.SDK_INT <= 18)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, videoUri);
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, videoUri);*/
            Log.i(TAG, videoUri.getPath());
            Log.i(TAG, getRealPathFromURI(videoUri, this));
        }
    }

    private String getRealPathFromURI(Uri contentUri, Context activity) {
        String path = null;
        try {
            final String[] proj = {MediaStore.MediaColumns.DATA};
            final Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path != null && path.length() > 0) {
            return path;
        } else return contentUri.getPath();
    }
}
