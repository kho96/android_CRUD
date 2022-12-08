package com.kh.ui_exam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnInsert, btnList;
    ListView listView;
    EditText edtSno, edtSname, edtSyear, edtMajor, edtScore;
    RadioGroup rdoGroup;
    View dialogView;
    SqlHelper helper;
    String sno, sname, major;
    String gender = "";
    int syear, score;
    private StudentDao dao = new StudentDao();
    ArrayList<StudentVo> studentList;
    LinearLayout linLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new SqlHelper(this, "studentsDB", null, 1);
        findViews();
        setListener();

    }// onCreate

    // 화면 재시작할 경우 목록보기 자동 호출
    @Override
    protected void onRestart() {
        super.onRestart();
        btnList.callOnClick();
    }

    // listener
    private void setListener() {
        btnInsert.setOnClickListener(this);
        btnList.setOnClickListener(this);
        listView = findViewById(R.id.listView);
    }

    // find id
    private void findViews() {
        btnInsert = findViewById(R.id.bntInsert);
        btnList = findViewById(R.id.bntList);
        linLayout = findViewById(R.id.linLayout);
    }

    @Override
    public void onClick(View view) {
        if (view == btnInsert) { // insert
            // dialogView 생성
            dialogView = View.inflate(MainActivity.this, R.layout.dialog, null);
            // find id(dialogView)
            edtSno = dialogView.findViewById(R.id.edtSno);
            edtSname = dialogView.findViewById(R.id.edtSname);
            edtSyear = dialogView.findViewById(R.id.edtSyear);
            edtMajor = dialogView.findViewById(R.id.edtMajor);
            edtScore = dialogView.findViewById(R.id.edtScore);
            rdoGroup = dialogView.findViewById(R.id.rdoGroup);
            RadioButton rdoMale = dialogView.findViewById(R.id.rdoMale);
            RadioButton rdoFemale = dialogView.findViewById(R.id.rdoFemale);
            // rdoGroup 리스너
            rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int rdoId = rdoGroup.getCheckedRadioButtonId();
                    if (rdoId == rdoMale.getId()) {
                        gender = "M";
                    } else if (rdoId == rdoFemale.getId()) {
                        gender = "F";
                    }
                }
            });// rdoGropu listener

            // dialog 설정, 보여주기
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("학생 정보 등록");
            dialog.setIcon(R.drawable.icon);
            dialog.setView(dialogView);
            dialog.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    // 입력값 받아서 Vo에 저장하기
                    sno = String.valueOf(edtSno.getText());
                    sname = String.valueOf(edtSname.getText());
                    major = String.valueOf(edtMajor.getText());
                    try { // int값 예외처리
                        if (!String.valueOf(edtSyear.getText()).equals("")) {
                            syear = Integer.parseInt(String.valueOf(edtSyear.getText()));
                        }
                        if (!String.valueOf(edtScore.getText()).equals("")) {
                            score = Integer.parseInt(String.valueOf(edtScore.getText()));
                        }
                    } catch (NumberFormatException ne) {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                        // dialog에서 저장된 값(gender) 초기화
                        gender = "";
                        return;
                    }
                    if (sno.equals("") || sname.equals("") || gender.equals("") || major.equals("")) {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                        gender = "";
                        return;
                    }
                    StudentVo vo = new StudentVo(sno, sname, syear, gender, major, score);
                    // dao insert...
                    boolean result = dao.insertStudent(helper, vo);
                    if (result) {
                        gender = "";
                        Toast.makeText(MainActivity.this, "입력 성공", Toast.LENGTH_SHORT).show();
                        btnList.callOnClick();
                    } else {
                        gender = "";
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                    }

                }// onClick(positiveBtn listener)
            });// positiveBtn
            dialog.setNegativeButton("닫기", null);
            dialog.show();

        } else if (view == btnList) { // list
            studentList = dao.showData(helper);
            MyAdapter myAdapter = new MyAdapter(this, studentList);
            listView.setAdapter(myAdapter);
            linLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), Detail.class);
                    intent.putExtra("sno", myAdapter.getItem(position).getSno());
                    startActivity(intent);
                }


            }); // onItemClick
        }// else if
    } //onClick
} //class