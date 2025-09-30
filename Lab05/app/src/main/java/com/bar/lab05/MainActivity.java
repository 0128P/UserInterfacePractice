package com.bar.lab05;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bar.lab05.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ActivityMainBinding binding;

    private float initX;
    private long initTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bellName.setOnClickListener(this);
        binding.label.setOnClickListener(this);
        binding.repeatCheck.setOnCheckedChangeListener(this);
        binding.vibrate.setOnCheckedChangeListener(this);
        binding.onOff.setOnCheckedChangeListener(this);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        if (v == binding.bellName) showToast("bell text click event...");
        else if (v == binding.label) showToast("label text click event...");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == binding.repeatCheck) showToast("repeat checkbox is " + isChecked);
        else if (buttonView == binding.vibrate) showToast("vibrate checkbox is " + isChecked);
        else if (buttonView == binding.onOff) showToast("switch is " + isChecked);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) initX = event.getRawX();
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            float diffX = initX - event.getRawX();

            if (diffX > 30) showToast("왼쪽으로 화면을 밀었습니다.");
            else if (diffX < -30) showToast("오른쪽으로 화면을 밀었습니다.");
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime > 3000) {
                //back button을 누른지 3초가 지난거라면..
                showToast("종료할려면 한번 더 누르세요.");
                //현재 시간 저장
                initTime = System.currentTimeMillis();
            } else {
                //3초 이내에 Back button 이 두번 눌린 경우. Activity 종료
                finish();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}