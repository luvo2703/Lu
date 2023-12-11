package com.example.duan_oder_doan.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_oder_doan.R;
import com.example.duan_oder_doan.TrangChuUser;
import com.example.duan_oder_doan.model.Food_HoaDonChiTiet;
import com.example.duan_oder_doan.model.HoaDon;
import com.example.duan_oder_doan.model.HoaDonChiTiet;
import com.example.duan_oder_doan.model.TheLoai;
import com.example.duan_oder_doan.model.User;
import com.example.duan_oder_doan.view_holder.View_Holder_Detailed_Invoice_User;
import com.example.duan_oder_doan.view_holder.View_Holder_Receipt_User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Adapter_Detailed_Invoice_User extends RecyclerView.Adapter<View_Holder_Detailed_Invoice_User> {
    private List<HoaDonChiTiet> hoaDonChiTietList;

    public Adapter_Detailed_Invoice_User(List<HoaDonChiTiet> hoaDonChiTietList) {
        this.hoaDonChiTietList = hoaDonChiTietList;
    }

    @NonNull
    @Override
    public View_Holder_Detailed_Invoice_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row =inflater.inflate(R.layout.item_detailed_invoice_user, parent, false);

        View_Holder_Detailed_Invoice_User viewHolderDetailedInvoiceUser = new View_Holder_Detailed_Invoice_User(row);
        return viewHolderDetailedInvoiceUser;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder_Detailed_Invoice_User holder, int position) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietList.get(position);
        
        holder.tvDate.setText(hoaDonChiTiet.getDate());
        holder.tvPrice.setText(hoaDonChiTiet.getSum_Price());
        holder.line_item.setOnClickListener(v ->{
            final Dialog dialog = new Dialog(v.getContext(), androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
            dialog.setContentView(R.layout.dialog_hodonchitiet);

            TextView tv_name = dialog.findViewById(R.id.tv_name);
            TextView tv_phone = dialog.findViewById(R.id.tv_phone);
            TextView tv_address = dialog.findViewById(R.id.tv_address);
            TextView tv_date = dialog.findViewById(R.id.tv_date);
            TextView tv_sum = dialog.findViewById(R.id.tv_sum);


            tv_name.setText("Tên: "+ hoaDonChiTiet.getName());
            tv_phone.setText("SDT: "+ hoaDonChiTiet.getPhone());
            tv_address.setText("Địa chỉ: "+ hoaDonChiTiet.getAddress());
            tv_date.setText("Ngày: "+ hoaDonChiTiet.getDate());
            tv_sum.setText("Tổng: "+ hoaDonChiTiet.getSum_Price() +" K");

            RecyclerView recyclerView = dialog.findViewById(R.id.rcv_detailed_invoice);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(v.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            List<Food_HoaDonChiTiet> food_hoaDonChiTietList = new ArrayList<>();
            Adapter_Food_HoaDonChiTiet adapter = new Adapter_Food_HoaDonChiTiet(food_hoaDonChiTietList);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Users");

            reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Food_Detailed_Invoices")
                    .child(String.valueOf(hoaDonChiTiet.getId())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Food_HoaDonChiTiet foodHoaDonChiTiet = dataSnapshot.getValue(Food_HoaDonChiTiet.class);
                                food_hoaDonChiTietList.add(foodHoaDonChiTiet);
                            }
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(v.getContext(), "Get list faild!", Toast.LENGTH_LONG).show();
                        }
                    });

            dialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return hoaDonChiTietList == null ? 0 : hoaDonChiTietList.size();
    }
}
