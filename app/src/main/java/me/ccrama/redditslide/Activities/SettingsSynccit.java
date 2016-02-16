package me.ccrama.redditslide.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import me.ccrama.redditslide.R;
import me.ccrama.redditslide.SettingValues;
import me.ccrama.redditslide.Synccit.MySynccitReadTask;
import me.ccrama.redditslide.Synccit.MySynccitUpdateTask;
import me.ccrama.redditslide.Synccit.SynccitRead;


/**
 * Created by l3d00m on 11/13/2015.
 */
public class SettingsSynccit extends BaseActivityAnim {


    EditText name;
    EditText auth;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyColorTheme();
        setContentView(R.layout.activity_settings_synccit);
        setupAppBar(R.id.toolbar, "Synccit Integration", true, true);


        name = (EditText) findViewById(R.id.name);
        auth = (EditText) findViewById(R.id.auth);

        name.setText(SettingValues.synccitName);
        auth.setText(SettingValues.synccitAuth);

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SettingValues.synccitName = name.getText().toString();
                    SettingValues.synccitAuth = auth.getText().toString();

                    new MySynccitUpdateTask().execute("16noez");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            new MySynccitReadTask().execute("16noez");
                            if (SynccitRead.visitedIds.contains("16noez")) {
                                //success

                                SharedPreferences.Editor e = SettingValues.prefs.edit();

                                e.putString(SettingValues.SYNCCIT_NAME, SettingValues.synccitName);
                                e.putString(SettingValues.SYNCCIT_AUTH, SettingValues.synccitAuth);
                                e.apply();
                                new AlertDialogWrapper.Builder(SettingsSynccit.this)
                                        .setTitle("Connected successfully!")
                                        .setMessage("Synccit is now active for this device")
                                        .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        finish();
                                    }
                                }).show();
                            } else {

                                new AlertDialogWrapper.Builder(SettingsSynccit.this)
                                        .setTitle("Could not connect to Synccit servers")
                                        .setMessage("Make sure your username and authentication key are correct!")
                                        .setPositiveButton("Ok!", null).show();
                            }
                        }
                    }, 3000);

                } catch (Exception e) {
                    new AlertDialogWrapper.Builder(SettingsSynccit.this)
                            .setTitle("Could not connect to Synccit servers")
                            .setMessage("Make sure your username and authentication key are correct!")
                            .setPositiveButton("Ok!", null).show();
                }


            }
        });

    }


}