package com.kh.ui_exam;

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
    String gender = "";
    View dialogView;
    SqlHelper helper;
    String sno, sname, major;
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

    }

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

    // helper
    public SqlHelper getHelper() {
        return helper;
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
                    try { // int값 예외처리
                        if (!String.valueOf(edtSyear.getText()).equals("")) {
                            syear = Integer.parseInt(String.valueOf(edtSyear.getText()));
                        }
                    } catch (NumberFormatException ne) {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    major = String.valueOf(edtMajor.getText());
                    try { // int값 예외처리
                        if (!String.valueOf(edtScore.getText()).equals("")) {
                            score = Integer.parseInt(String.valueOf(edtScore.getText()));
                        }
                    } catch (NumberFormatException ne) {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StudentVo vo = new StudentVo(sno, sname, syear, gender, major, score);
                    Log.d("myTag", vo.toString());
                    if (sno.equals("") || sname.equals("") || gender.equals("") || major.equals("")) {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // dao insert...
                    boolean result = dao.insertStudent(helper, vo);
                    if (result) {
                        Toast.makeText(MainActivity.this, "입력 성공", Toast.LENGTH_SHORT).show();
                        btnList.callOnClick();
                    } else {
                        Toast.makeText(MainActivity.this, "입력 실패", Toast.LENGTH_SHORT).show();
                    }

                }
            });
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
                    intent.putExtra("sname", myAdapter.getItem(position).getSname());
                    intent.putExtra("syear", myAdapter.getItem(position).getSyear());
                    intent.putExtra("gender", myAdapter.getItem(position).getGender());
                    intent.putExtra("major", myAdapter.getItem(position).getMajor());
                    intent.putExtra("score", myAdapter.getItem(position).getScore());
                    startActivity(intent);
                }


            }); // onItemClick
        }
    } //onClick
}