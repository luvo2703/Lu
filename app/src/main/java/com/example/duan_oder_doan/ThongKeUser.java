package com.example.duan_oder_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan_oder_doan.adapter.Adapter_Detailed_Invoice_User;
import com.example.duan_oder_doan.model.HoaDonChiTiet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThongKeUser extends AppCompatActivity {

    private HoaDonChiTiet hoaDonChiTiet;
    private List<HoaDonChiTiet> hoaDonChiTietList;
    private TextView tv_sumoder, tv_summoney;
    private int sum1, sum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke_user);

        tv_sumoder = findViewById(R.id.tv_sumoder);
        tv_summoney = findViewById(R.id.tv_summoney);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_statistical);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        hoaDonChiTietList = new ArrayList<>();
        getList();
    }

    private void getList(){
        hoaDonChiTietList.clear();
        sum1 =0;
        sum2 = 0;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Chi tiết_Hóa đơn").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            hoaDonChiTiet = dataSnapshot.getValue(HoaDonChiTiet.class);
                            hoaDonChiTietList.add(hoaDonChiTiet);
                            sum1 = hoaDonChiTietList.size();
                            sum2 = sum2+ Integer.parseInt(hoaDonChiTiet.getSum_Price());
                        }
                        tv_sumoder.setText(String.valueOf(sum1));
                        tv_summoney.setText((String.valueOf(sum2) +" K "));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ThongKeUser.this, "Lấy danh sách không thành công!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}