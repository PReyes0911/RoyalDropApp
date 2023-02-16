package com.example.royaldropapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.royaldropapp.Adapter.MyOnProcessAdapter;
import com.example.royaldropapp.Adapter.MyOrderAdapter;
import com.example.royaldropapp.Adapter.MyRiderAdapter;
import com.example.royaldropapp.Listener.IDeliverLoadListener;
import com.example.royaldropapp.Listener.IProductLoadListener;
import com.example.royaldropapp.Listener.IRiderLoadListener;
import com.example.royaldropapp.Model.DeliverModel;
import com.example.royaldropapp.Model.ProductModel;
import com.example.royaldropapp.Model.RiderModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminRiderActivity extends AppCompatActivity implements IProductLoadListener, IDeliverLoadListener  {

    @BindView(R.id.rider_RL)
    RelativeLayout fragML;
    @BindView(R.id.rider_RV_accept)
    RecyclerView fragRecy;
    @BindView(R.id.rider_RV_finish)
    RecyclerView fragFinish;
    IRiderLoadListener riderLoadListener;

    IProductLoadListener productLoadListener;
    IDeliverLoadListener deliverLoadListener;


    SharedPreferences sharedPreferences;

    DatabaseReference EmployeeRef;

    public static final String fileName = "login";
    public static final String Emp1 = "employee1";
    public static final String Emp2 = "employee2";
    public static final String Emp3 = "Employee_name3";

    Button logout,datetoday;

    Button accept,finish;

    String employeenumber;

    TextView employeeName,totalsales;


    DatePickerDialog datePickerDialog;

    String NAMER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rider);

        logout = findViewById(R.id.logout_employee);
        employeeName = findViewById(R.id.employeeName);
        datetoday = findViewById(R.id.datePicker);
        totalsales = findViewById(R.id.totalsales);
        accept = findViewById(R.id.btnRiderAccept);
        finish = findViewById(R.id.btnRiderFinish);

        datetoday.setText(getTodaysDate());

        EmployeeRef = FirebaseDatabase.getInstance().getReference("Admin").child("employees");

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        datetoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancelAll();
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragRecy.setVisibility(View.VISIBLE);
                fragFinish.setVisibility(View.GONE);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragRecy.setVisibility(View.GONE);
                fragFinish.setVisibility(View.VISIBLE);
            }
        });
        if(sharedPreferences.contains(Emp1)){


            EmployeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    employeenumber = snapshot.child("Employee1").getKey();
                    String Emp1_name = snapshot.child("Employee1").child("name").getValue(String.class);
                    employeeName.setText(Emp1_name);
                    loadOrderFromFirebase();
                    loadDeliverFromFirebase();
                    loadHistoryFromFirebase();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else if(sharedPreferences.contains(Emp2)){


            EmployeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    employeenumber = snapshot.child("Employee2").getKey();
                    String Emp2_name = snapshot.child("Employee2").child("name").getValue(String.class);
                    employeeName.setText(Emp2_name);
                    loadOrderFromFirebase();
                    loadDeliverFromFirebase();
                    loadHistoryFromFirebase();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if(sharedPreferences.contains(Emp3)){


            EmployeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    employeenumber = snapshot.child("employees").child("Employee3").getKey();
                    String Emp3_name = snapshot.child("employees").child("Employee3").child("name").getValue(String.class);
                    employeeName.setText(Emp3_name);
                    loadOrderFromFirebase();
                    loadHistoryFromFirebase();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        init();
        init2();
        initDatePicker();

    }
    private void loadRiderFromFirebase(){
        List<RiderModel> riderModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Admin").child("employees").child(employeenumber).child("DeliveryList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot ridersnapshot:snapshot.getChildren())
                            {
                                RiderModel riderModel = ridersnapshot.getValue(RiderModel.class);
                                riderModel.setKey(ridersnapshot.getKey());
                                riderModel.setTotalPrice(riderModel.getTotalPrice());
                                riderModels.add(riderModel);
                            }
                            riderLoadListener.onRiderLoadSuccess(riderModels);
                        }else{
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        riderLoadListener.onRiderLoadFailed(error.getMessage());

                    }
                });
    }
    private void loadOrderFromFirebase(){
        List<ProductModel> productModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(getTodaysDate())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot productsnapshot:snapshot.getChildren())
                            {
                                ProductModel productModel = productsnapshot.getValue(ProductModel.class);
                                productModel.setKey(productsnapshot.getKey());
                                productModel.setTotalPrice(productModel.getTotalPrice());
                                productModels.add(productModel);
                            }
                            productLoadListener.onProductLoadSuccess(productModels);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        productLoadListener.onProductLoadFailed(error.getMessage());

                    }
                });
    }
    private void loadDeliverFromFirebase(){
        List<DeliverModel> deliverModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Delivered")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot deliversnapshot:snapshot.getChildren())
                            {
                                DeliverModel deliverModel = deliversnapshot.getValue(DeliverModel.class);
                                deliverModel.setKey(deliversnapshot.getKey());
                                deliverModel.setTotalPrice(deliverModel.getTotalPrice());
                                deliverModels.add(deliverModel);
                            }
                            deliverLoadListener.onDeliverLoadSuccess(deliverModels);
                        }else{
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        deliverLoadListener.onDeliverLoadFailed(error.getMessage());

                    }
                });
    }
    private void init(){
        ButterKnife.bind(this);

        productLoadListener = this;
        deliverLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        fragRecy.setLayoutManager(layoutManager);
        fragRecy.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
    }
    private void init2(){
        ButterKnife.bind(this);

        deliverLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        fragFinish.setLayoutManager(layoutManager);
        fragFinish.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
    }


    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month,year);

    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day,month,year);
                datetoday.setText(date);
                loadHistoryFromFirebase();
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);

    }
    private String makeDateString(int day, int month, int year) {

        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "Mar";
        if(month == 4)
            return "Apr";
        if(month == 5)
            return "May";
        if(month == 6)
            return "Jun";
        if(month == 7)
            return "Jul";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sep";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";

        return "Jan";

    }

    private void loadHistoryFromFirebase(){

        FirebaseDatabase.getInstance()
                .getReference("Admin")
                .child("employees").child(employeenumber).child("TotalSales")
                .child(datetoday.getText().toString()).child("totalsales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            Double sales = snapshot.getValue(Double.class);
                            totalsales.setText("₱"+String.format("%.2f",sales));
                        }else{

                            totalsales.setText("₱0.00");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    @Override
    public void onDeliverLoadSuccess(List<DeliverModel> deliverModelList) {

        MyOnProcessAdapter adapter = new MyOnProcessAdapter(getApplicationContext(),deliverModelList);
        fragFinish.setAdapter(adapter);

    }

    @Override
    public void onDeliverLoadFailed(String message) {

        Snackbar.make(fragML,message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onProductLoadSuccess(List<ProductModel> productModelList) {
        MyOrderAdapter adapter = new MyOrderAdapter(getApplicationContext(),productModelList,deliverLoadListener);
        fragRecy.setAdapter(adapter);

    }

    @Override
    public void onProductLoadFailed(String message) {
        Snackbar.make(fragML,message,Snackbar.LENGTH_LONG).show();
    }

}