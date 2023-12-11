package com.example.duan_oder_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan_oder_doan.model.HoaDon;
import com.example.duan_oder_doan.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DangKy extends AppCompatActivity {
    private EditText edt_name;
    private EditText edt_email;
    private EditText edt_phone;
    private EditText edt_pass;
    private TextView tv_login;
    private FirebaseAuth mAuth;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        edt_name = findViewById(R.id.edt_name);
        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
        tv_login = findViewById(R.id.tv_login);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_signup).setOnClickListener(v ->{
            signup();
        });

        tv_login.setOnClickListener(v ->{
            Intent intent = new Intent(this, DangNhap.class);
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        });

    }

    private void signup(){
        String fullName = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String phone = edt_phone.getText().toString();
        String password = edt_pass.getText().toString();
        String image = "https://firebasestorage.googleapis.com/v0/b/duan-oder-doan.appspot.com/o/avatar.png?alt=media&token=021e12e4-2ee4-4c37-a803-b901c6552141";
        String gender = "";
        String date_of_birth = "";
        String address = "";

        if (fullName.isEmpty()) {
            edt_name.setError("Điền đầy đủ họ tên");
            edt_name.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edt_email.setError("Nhập E-mail ");
            edt_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Vui lòng cung cấp email hợp lệ!");
            edt_email.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            edt_phone.setError("Cần có số điện thoại");
            edt_phone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edt_pass.setError("Cần có mật khẩu");
            edt_pass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edt_pass.setError("Độ dài mật khẩu tối thiểu phải là 6 ký tự!");
            edt_pass.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullName, email,  phone, password, image, gender, date_of_birth, address);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("User")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DangKy.this, "Người dùng đã đăng ký thành công!", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(DangKy.this, DangNhap.class);
                                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DangKy.this).toBundle();
                                                startActivity(intent, bundle);
                                            }
                                        }
                            });
                        } else {
                            Toast.makeText(DangKy.this, "Đăng ký không thành công! Thử lại!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}