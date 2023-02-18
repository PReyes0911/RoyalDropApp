package com.example.royaldropapp;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.royaldropapp.Model.PersonalOrderModel;
import com.example.royaldropapp.databinding.ActivityAdminBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity{

    private ActivityAdminBinding binding;
    DatabaseReference reff,deletepreviewsorders;
    private DrawerLayout drawerLayout;
    long maxid = 0;

    SharedPreferences sharedPreferences;

    PersonalOrderModel personalOrderModel;

    Button logout,datetoday;

    String employeenumber;

    TextView employeeName,totalsales;

    DatabaseReference EmployeeRef;
    DatePickerDialog datePickerDialog;

    public static final String fileName = "login";
    public static final String Emp1 = "employee1";
    public static final String Emp2 = "employee2";
    public static final String Emp3 = "Employee_name3";



    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new fragment1());
        onNewIntent(getIntent());

        logout = findViewById(R.id.logout_employee);
        employeeName = findViewById(R.id.employeeName);
        datetoday = findViewById(R.id.datePicker);
        totalsales = findViewById(R.id.totalsales);

        datetoday.setText(getTodaysDate());

        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.admin_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        personalOrderModel = new PersonalOrderModel();
        EmployeeRef = FirebaseDatabase.getInstance().getReference("Admin").child("employees");

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
        if(sharedPreferences.contains(Emp1)){


            EmployeeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    employeenumber = snapshot.child("Employee1").getKey();
                    String Emp1_name = snapshot.child("Employee1").child("name").getValue(String.class);
                    employeeName.setText(Emp1_name);
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
                    loadHistoryFromFirebase();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        reff = FirebaseDatabase.getInstance().getReference().child("Data").child("OrderID");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid=(snapshot.getChildrenCount());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deletepreviewsorders = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }

                try {
                    Intent resultIntent = new Intent(getApplicationContext(), EmployeeActivity.class);
                    PendingIntent pendingIntent=PendingIntent.getActivity(EmployeeActivity.this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(EmployeeActivity.this, "My Notification");
                    builder.setContentTitle("New Order has been Placed");
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
                    builder.setSmallIcon(R.mipmap.ic_launcher_round);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
                    builder.setStyle(new NotificationCompat.BigTextStyle());

                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);

                    NotificationManagerCompat manager = NotificationManagerCompat.from(EmployeeActivity.this);
                    manager.notify(0, builder.build());
                    replaceFragment(new fragment1());

                } catch (IllegalStateException exception) {

                    System.out.println(exception);

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Orders").child(getTodaysDate());
        reference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    replaceFragment(new fragment1());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                try{
                    replaceFragment(new fragment1());
                }catch (Exception e){
                    System.out.println(e);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference Drinks = FirebaseDatabase.getInstance().getReference().child("Drink");
        Drinks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    replaceFragment(new AdminProductPriceFragment());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                try{
                    replaceFragment(new AdminProductPriceFragment());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference NewDrinks = FirebaseDatabase.getInstance().getReference().child("NewDrink");
        NewDrinks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try{
                    replaceFragment(new AdminProductPriceFragment());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                try{
                    replaceFragment(new AdminProductPriceFragment());
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Delivered");
        reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }

                try {
                    Intent resultIntent = new Intent(getApplicationContext(), EmployeeActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    resultIntent.putExtra("AHistoryFragment", "MyFragment");
                    PendingIntent pendingIntent=PendingIntent.getActivity(EmployeeActivity.this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(EmployeeActivity.this, "My Notification");
                    builder.setContentTitle("The Customer has received the Gallon");
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
                    builder.setSmallIcon(R.mipmap.ic_launcher_round);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE);
                    builder.setStyle(new NotificationCompat.BigTextStyle());
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);

                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);

                    NotificationManagerCompat manager = NotificationManagerCompat.from(EmployeeActivity.this);
                    manager.notify(0, builder.build());
                    replaceFragment(new fragment2());



                } catch (IllegalStateException exception) {

                    System.out.println(exception);

                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.orderz:
                    replaceFragment(new fragment1());
                    break;
                case R.id.deliver:
                    replaceFragment(new fragment2());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onResume() {

        deletepreviewsorders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                deletepreviewsorders.child("Orders").child(getYesterDate()).removeValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onResume();
    }
    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month,year);

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
    private String getYesterDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeYesterdayDateString(day, month,year);

    }
    private String makeYesterdayDateString(int day, int month, int year) {

        return day-1 + " " + getMonthFormat(month) + " " + year;
    }
    private void OnProcessNotification(){
        FirebaseDatabase.getInstance()
                .getReference("Orders")
                .child(getTodaysDate())
                .orderByChild("status")
                .equalTo("On Process")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot personalsnapshot:snapshot.getChildren()) {
                                PersonalOrderModel personalOrderModel = personalsnapshot.getValue(PersonalOrderModel.class);
                                personalOrderModel.setKey(personalsnapshot.getKey());
                                personalOrderModel.setTotalPrice(personalOrderModel.getTotalPrice());
                                String value = String.valueOf(personalOrderModel.getStatus());
                                if(value.equals("On Process")){
                                    if(getIntent().hasExtra("AHistoryFragment")){
                                        try {
                                            getIntent().removeExtra("AHistoryFragment");
                                            replaceFragment(new AdminHistoryFragment());
                                        } catch (Exception exception) {
                                            System.out.println(exception);
                                        }
                                    }else{
                                        try {
                                            replaceFragment(new fragment1());
                                        } catch (Exception exception) {
                                            System.out.println(exception);
                                        }
                                    }
                                }
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        onNewIntent(getIntent());
    }
}