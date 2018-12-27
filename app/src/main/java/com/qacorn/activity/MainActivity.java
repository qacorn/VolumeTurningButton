package com.qacorn.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qacorn.view.VolumeTurningButton;

public class MainActivity extends AppCompatActivity {

    private VolumeTurningButton volumeTurningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volumeTurningButton  = new VolumeTurningButton(this);
        setContentView(volumeTurningButton);
    }
}
