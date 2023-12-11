package com.example.duan_oder_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duan_oder_doan.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoiMatKhauUser extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private EditText edt_currpass, edt_newpass, edt_pass;
    private FirebaseAuth mAuth;

    private String name,email,password, phone, gender, date_of_birth, image, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau_user);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_changepassword);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        edt_currpass = findViewById(R.id.edt_currpass);
        edt_newpass = findViewById(R.id.edt_newpass);
        edt_pass = findViewById(R.id.edt_pass);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    name = userProfile.getFullName();
                    email = userProfile.getEmail();
                    password = userProfile.getPassword();
                    phone = userProfile.getPhone();
                    gender = userProfile.getGender();
                    date_of_birth = userProfile.getDate_of_birth();
                    address = userProfile.getAddress();
                    image = userProfile.getImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DoiMatKhauUser.this,"Đã xảy ra sự cố!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(v -> {
            String pass = edt_pass.getText().toString();
            String currpass = edt_currpass.getText().toString();
            String newpass = edt_newpass.getText().toString();

            if (currpass.isEmpty()) {
                edt_currpass.setError("Sai mật khẩu!");
                edt_currpass.requestFocus();
                return;
            }
            if (currpass.length() < 6) {
                edt_currpass.setError("Độ dài mật khẩu hiện tại tối thiểu phải là 6 ký tự!");
                edt_currpass.requestFocus();
                return;
            }if (!currpass.equalsIgnoreCase(password)) {
                edt_currpass.setError("Không trùng lặp với mật khẩu!");
                edt_currpass.requestFocus();
                return;
            }

            if (newpass.isEmpty()) {
                edt_newpass.setError("Cần có mật khẩu mới!");
                edt_newpass.requestFocus();
                return;
            }
            if (newpass.length() < 6) {
                edt_newpass.setError("Độ dài mật khẩu hiện tại tối thiểu phải là 6 ký tự!");
                edt_newpass.requestFocus();
                return;
            }

            if (pass.isEmpty()) {
                edt_pass.setError("Nhập mật khẩu!");
                edt_pass.requestFocus();
                return;
            }
            if (pass.length() < 6) {
                edt_pass.setError("Độ dài mật khẩu hiện tại tối thiểu phải là 6 ký tự!");
                edt_pass.requestFocus();
                return;
            }if (!pass.equalsIgnoreCase(newpass)) {
                edt_pass.setError("Không trùng lặp với mật khẩu mới!");
                edt_pass.requestFocus();
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            user.updatePassword(newpass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                User userProfile1 = new User(name, email, phone, newpass, image, gender, date_of_birth, address);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("User")
                                        .setValue(userProfile1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(DoiMatKhauUser.this, "Đổi mật khẩu thành công!", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(DoiMatKhauUser.this, ThongTinUser.class);
                                                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(DoiMatKhauUser.this).toBundle();
                                                    startActivity(intent, bundle);
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(DoiMatKhauUser.this,"Thử lại! Có điều gì đó không ổn đã xảy ra!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });
    }
}