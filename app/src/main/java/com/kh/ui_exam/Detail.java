package com.kh.ui_exam;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    String sno, sname, gender, major;
    int syear, score;
    Button btnBack, btnModify, btnDelete;
    EditText edtSno, edtSname, edtSyear, edtMajor, edtScore;
    RadioGroup rdoGroup;
    RadioButton rdoMale, rdoFemale;
    private StudentDao dao = new StudentDao();
    MainActivity main = new MainActivity();
    SqlHelper helper;
    View dialogView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        //helper = main.getHelper();
        helper = new SqlHelper(this, "studentsDB", null, 1);
        findViews();
        setListener();

        // sno만 받아서 Vo로 꺼내기
        Intent intent = getIntent();
        sno = intent.getStringExtra("sno");
        StudentVo vo = dao.showDetail(helper, sno);
        Log.d("myTag", "vo : " + vo);
        sname = vo.getSname();
        gender = vo.getGender();
        major = vo.getMajor();
        syear = vo.getSyear();
        score = vo.getScore();

        // 보여지는 EditText 에 값 설정하기
        setValue();

    }// onCreate

    // listener
    private void setListener() {
        btnBack.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    // find Id
    private void findViews() {
        btnBack = findViewById(R.id.btnBack);
        btnModify = findViewById(R.id.btnModify);
        btnDelete = findViewById(R.id.btnDelete);
        edtSno = findViewById(R.id.edtSno);
        edtSname = findViewById(R.id.edtSname);
        edtSyear = findViewById(R.id.edtSyear);
        edtMajor = findViewById(R.id.edtMajor);
        edtScore = findViewById(R.id.edtScore);
        rdoGroup = findViewById(R.id.rdoGroup);
        rdoMale = findViewById(R.id.rdoMale);
        rdoFemale = findViewById(R.id.rdoFemale);

    }

    // 세부 정보 항목 값 세팅
    private void setValue() {
        edtSno.setText(sno);
        edtSname.setText(sname);
        if (gender.equals("M")) {
            rdoMale.setChecked(true);
        } else {
            rdoFemale.setChecked(true);
        }
        edtSyear.setText(String.valueOf(syear));
        edtMajor.setText(major);
        edtScore.setText(String.valueOf(score));
    }

    // 잘못된 값 입력시 저장한 값 초기화
    private void valueInit() {
        sname = String.valueOf(edtSname.getText());
        major = String.valueOf(edtMajor.getText());
        score = Integer.parseInt(String.valueOf(edtScore.getText()));
        syear = Integer.parseInt(String.valueOf(edtSyear.getText()));
    }


    // private void getDialog(View dialogView){ -> dialog id 찾는 메서드

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            finish();
        } else if (view == btnModify) {

            // dialogView 생성
            dialogView = View.inflate(Detail.this, R.layout.dialog, null);
            // find id(dialogView)
            EditText dial_edtSno = dialogView.findViewById(R.id.edtSno);
            EditText dial_edtSname = dialogView.findViewById(R.id.edtSname);
            EditText dial_edtSyear = dialogView.findViewById(R.id.edtSyear);
            EditText dial_edtMajor = dialogView.findViewById(R.id.edtMajor);
            EditText dial_edtScore = dialogView.findViewById(R.id.edtScore);
            RadioGroup dial_rdoGroup = dialogView.findViewById(R.id.rdoGroup);
            RadioButton dial_rdoMale = dialogView.findViewById(R.id.rdoMale);
            RadioButton dial_rdoFemale = dialogView.findViewById(R.id.rdoFemale);
            // getDialog(dialogView); 아이디 찾기 (dialog)

            // dailog setting
            dial_edtSno.setEnabled(false);
            dial_edtSno.setText(sno);
            dial_edtSname.setText(sname);
            dial_edtSyear.setText(String.valueOf(syear));
            dial_edtMajor.setText(major);
            dial_edtScore.setText(String.valueOf(score));
            if (gender.equals("M")) {
                dial_rdoFemale.setChecked(false);
                dial_rdoMale.setChecked(true);
            } else {
                dial_rdoMale.setChecked(false);
                dial_rdoFemale.setChecked(true);
            }

            // dial_rdoGroup 리스너 (dialog에 있는 라디오 그룹에 리스너 설정)
            dial_rdoGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int rdoId = dial_rdoGroup.getCheckedRadioButtonId();
                    if (rdoId == dial_rdoMale.getId()) {
                        gender = "M";
                    } else if (rdoId == dial_rdoFemale.getId()) {
                        gender = "F";
                    }
                }
            });// rdoGropu listener

            // dialog 설정, 보여주기
            AlertDialog.Builder dialog = new AlertDialog.Builder(Detail.this);
            dialog.setTitle("학생 정보 수정");
            dialog.setIcon(R.drawable.icon);
            dialog.setView(dialogView);
            dialog.setPositiveButton("수정 완료", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // 입력값 받아서 Vo에 저장하기
                    // String.valueOf, toString.. -> 글자 변환
                    sno = String.valueOf(dial_edtSno.getText());
                    sname = String.valueOf(dial_edtSname.getText());
                    try { // int값 예외처리
                        if (!String.valueOf(dial_edtSyear.getText()).equals("")) {
                            syear = Integer.parseInt(String.valueOf(dial_edtSyear.getText()));
                        }
                        if (!String.valueOf(dial_edtScore.getText()).equals("")) {
                            score = Integer.parseInt(String.valueOf(dial_edtScore.getText()));
                        }
                    } catch (NumberFormatException ne) {
                        Toast.makeText(Detail.this, "수정 실패", Toast.LENGTH_SHORT).show();
                        valueInit();
                        return;
                    }
                    major = String.valueOf(dial_edtMajor.getText());
                    StudentVo vo = new StudentVo(sno, sname, syear, gender, major, score);
                    Log.d("myTag", vo.toString());
                    if (sno.equals("") || sname.equals("") || major.equals("")) {
                        Toast.makeText(Detail.this, "수정 실패", Toast.LENGTH_SHORT).show();
                        valueInit();
                        return;
                    }
                    // dao modify
                    boolean result = dao.UpdateData(helper, vo);
                    if (result) {
                        Toast.makeText(Detail.this, "수정 성공", Toast.LENGTH_SHORT).show();
                        setValue();
                    } else {
                        Toast.makeText(Detail.this, "수정 실패", Toast.LENGTH_SHORT).show();
                        valueInit();
                    } // else
                }// onclick;
            }); //positive btn
            dialog.setNegativeButton("닫기", null);
            dialog.show();

        } else if (view == btnDelete) {
            boolean result = dao.deleteData(helper, sno);
            if (result) {
                Toast.makeText(Detail.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Detail.this, "삭제 실패", Toast.LENGTH_SHORT).show();
                return;
            } // else
        } // else if
    }// onClickListener

}//class
