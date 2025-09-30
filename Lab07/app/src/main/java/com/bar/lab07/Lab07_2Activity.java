package com.bar.lab07;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bar.lab07.databinding.ActivityLab072Binding;

import java.util.Calendar;

public class Lab07_2Activity extends AppCompatActivity {
    //이벤트 처리를 위해 dialog 객체를 멤버변수로 선언
    private AlertDialog alertDialog;
    private AlertDialog listDialog;
    private AlertDialog customDialog;

    //Dialog Button 이벤트 처리
    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == customDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("custom dialog 확인 click.....");
            } else if (dialog == listDialog) {
                //목록 dialog의 항목이 선택되었을 때 항목 문자열 획득
                String[] data = getResources().getStringArray(R.array.dialog_array);
                showToast(data[which] + " 선택 하셨습니다.");
            } else if (dialog == alertDialog && which == DialogInterface.BUTTON_POSITIVE) {
                showToast("alert dialog ok click.....");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ActivityLab072Binding binding = ActivityLab072Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.alertButton.setOnClickListener(view -> {
            //AlertDialog 띄우기
            alertDialog = new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("알림")
                    .setMessage("정말 종료 하시겠습니까?")
                    .setPositiveButton("OK", dialogListener)
                    .setNegativeButton("NO", null)
                    .create();

            alertDialog.show();
        });

        binding.listButton.setOnClickListener(view -> {
            //목록 다이얼로그 띄우기
            listDialog = new AlertDialog.Builder(this)
                    .setTitle("알람 벨소리")
                    .setSingleChoiceItems(R.array.dialog_array, 0, dialogListener)
                    .setPositiveButton("확인", null)
                    .setNegativeButton("취소", null)
                    .create();

            listDialog.show();
        });

        binding.dateButton.setOnClickListener(view -> {
            //현재 날짜로 dialog를 띄우기 위해 날짜를 구함
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dateDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            showToast(year + ":" + (monthOfYear + 1) + ":" + dayOfMonth);
                        }
                    }, year, month, day);

            dateDialog.show();
        });

        binding.timeButton.setOnClickListener(view -> {
            //현재 시간으로 Dialog를 띄우기 위해 시간을 구함
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timeDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            showToast(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);

            timeDialog.show();
        });

        binding.customButton.setOnClickListener(view -> {
            //custom dialog를 위한 layout xml 초기화
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_layout, null);

            customDialog = new AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setPositiveButton("확인", dialogListener)
                    .setNegativeButton("취소", null)
                    .create();

            customDialog.show();
        });
    }

    //매개변수의 문자열을 Toast로 띄우는 개발자 함수
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}