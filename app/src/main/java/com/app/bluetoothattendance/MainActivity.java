package com.app.bluetoothattendance;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import static com.app.bluetoothattendance.SaveSharedPreference.clearUserName;
import static com.app.bluetoothattendance.SaveSharedPreference.getNIM;
import static com.app.bluetoothattendance.SaveSharedPreference.getUserName;

public class MainActivity extends FragmentActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private BluetoothAdapter mBluetoothAdapter;
    public static final String SECURE_SETTINGS_BLUETOOTH_ADDRESS = "bluetooth_address";
    public static final int REQUEST_ENABLE_BT = 1;
    public static ParcelUuid Service_UUID;
    boolean doubleBackToExitPressedOnce = false;
    TextView welcometext,welcomeNIM, bt_advertise;
    private ProgressBar pbbar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
//        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);

        welcometext = findViewById(R.id.welcome_text);
        welcomeNIM = findViewById(R.id.welcome_NIM);
        bt_advertise = findViewById(R.id.bt_advertise);
        pbbar2       = findViewById(R.id.pbbar2);

        pbbar2.setVisibility(View.VISIBLE);

        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE))
                .getAdapter();

        welcometext.setText(getUserName(this));
        welcomeNIM.setText(getNIM(this));


        Service_UUID = ParcelUuid
                .fromString("aaaaaaaa-aaaa-aaaa-aaaa-aa" + getNIM(this));


        if (mBluetoothAdapter.isEnabled()) {
            bt_advertise.setText(R.string.bluetooth_on);
            Toast.makeText(this, "Don't forget to open the app before entering the seminar", Toast.LENGTH_LONG).show();
            startAdvertising();
            pbbar2.setVisibility(View.GONE);
            success();
        }
        else
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    } // End of onCreate

    public void success() {
        ConstraintLayout sampleMainLayout = findViewById(R.id.sample_main_layout);
        sampleMainLayout.setBackgroundColor(Color.parseColor("#12C069"));

        SimpleDraweeView mSimpleDraweeView = findViewById(R.id.my_image_view);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.success).build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageRequest.getSourceUri())
                .setAutoPlayAnimations(true)
                .build();
        mSimpleDraweeView.setController(controller);
    }



    public void startAdvertising() {
        Context c = MainActivity.this;
        c.startService(getServiceIntent(c));
    }

    public static Intent getServiceIntent(Context c) {
        return new Intent(c, AdvertiserService.class);
    }

    public void stopAdvertising() {
        Context c = MainActivity.this;
        c.stopService(getServiceIntent(c));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {

                    // Bluetooth is now Enabled, are Bluetooth Advertisements supported on
                    // this device?
                    if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {

                        // Everything is supported and enabled, load the fragments.
//                        setupFragments();
                        startAdvertising();
                        bt_advertise.setText(R.string.bluetooth_on);
                        Toast.makeText(this, "Don't forget to open the app before entering the seminar", Toast.LENGTH_LONG).show();
                        pbbar2.setVisibility(View.GONE);
                        success();

                    } else {

                        // Bluetooth Advertisements are not supported.
                        showErrorText(R.string.bt_ads_not_supported);
                        bt_advertise.setText("Bluetooth Advertisement is not supported");
                        Toast.makeText(this, "Bluetooth Advertisement is not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    // User declined to enable Bluetooth, exit the app.
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_LONG).show();
                    finish();
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void showErrorText(int messageId) {

        TextView view = findViewById(R.id.error_textview);
        view.setText(getString(messageId));
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            stopAdvertising();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        /**
         * Note that onDestroy is not guaranteed to be called quickly or at all. Services exist at
         * the whim of the system, and onDestroy can be delayed or skipped entirely if memory need
         * is critical.
         */
        super.onDestroy();
        stopAdvertising();

    }

    public void logout(View view) {
        stopAdvertising();
        clearUserName(MainActivity.this);
        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(i);
        Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
        finish();
    }
}