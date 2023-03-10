package com.example.royaldropapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.royaldropapp.Adapter.MyCartAdapter;
import com.example.royaldropapp.EventBus.MyUpdateCartEvent;
import com.example.royaldropapp.Listener.ICartLoadListener;
import com.example.royaldropapp.Model.CartModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {



    @BindView(R.id.recycler_cart)
    RecyclerView recyclerCart;
    @BindView(R.id.mainLayout)
    RelativeLayout cartML;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.placeorder)
    Button checkout;
    @BindView(R.id.cartempty)
    ImageView crtty;
    @BindView(R.id.emptycarts)
    TextView cty;

    String stock5,stock6,stock7,stock8;
    int CartQTY5,CartQTY6,CartQTY7,CartQTY8;
    DatabaseReference NewDrink,CartQTY;

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ICartLoadListener cartLoadListener;
    SwipeRefreshLayout swipeRefreshLayout;

    Dialog dialog;

    String dateHours;
    Calendar calendarHours;
    SimpleDateFormat sdfHours;

    @Override
    protected void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event){
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dialog=new Dialog(this);

        calendarHours = Calendar.getInstance();
        sdfHours = new SimpleDateFormat("KKmm");
        dateHours = sdfHours.format(calendarHours.getTime());

        Calendar now = Calendar.getInstance();
        String a = String.valueOf(now.get(Calendar.AM_PM));

        if((Integer.parseInt(dateHours)>=800&&a.equals("0"))||(Integer.parseInt(dateHours)<=700&&a.equals("1"))){
            System.out.println("Open");
        }else{
            dialog.setContentView(R.layout.close_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            Button btnok = dialog.findViewById(R.id.btn_understood);

            btnok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    dialog.dismiss();

                }
            });
            dialog.show();
        }

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        NewDrink = FirebaseDatabase.getInstance().getReference("NewDrink");
        NewDrink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stock5 = snapshot.child("05").child("stocks").getValue(String.class);
                stock6 = snapshot.child("06").child("stocks").getValue(String.class);
                stock7 = snapshot.child("07").child("stocks").getValue(String.class);
                stock8 = snapshot.child("08").child("stocks").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        CartQTY = FirebaseDatabase.getInstance().getReference("Cart").child(currentuser);
        CartQTY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    CartQTY5 = snapshot.child("05").child("quantity").getValue(Integer.class);
                }catch (Exception e){
                    System.out.println(e);
                }
                try{
                    CartQTY6 = snapshot.child("06").child("quantity").getValue(Integer.class);
                }catch (Exception e){
                    System.out.println(e);
                }
                try{
                    CartQTY7 = snapshot.child("07").child("quantity").getValue(Integer.class);
                }catch (Exception e){
                    System.out.println(e);
                }
                try{
                    CartQTY8 = snapshot.child("08").child("quantity").getValue(Integer.class);
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        init();
        loadCartFromFirebase();
        TotalPrice();
        content();

    }
    public void content(){

        refresh(500);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Cart").child(currentuser);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long pluscount = dataSnapshot.getChildrenCount();
                checkout.setText("Check Out (" + pluscount + ")");
                if(pluscount==0){
                    crtty.setVisibility(View.VISIBLE);
                    cty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };
        usersRef.addListenerForSingleValueEvent(valueEventListener);
    }
    private  void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                content();
            }
        };

        handler.postDelayed(runnable,milliseconds);

    }

    private void TotalPrice(){
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(currentuser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartModels.clear();
                        Double total = 0.0;
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                CartModel cartModel = ds.getValue(CartModel.class);
                                Double cost = Double.valueOf(cartModel.getTotalPrice());
                                total = total + cost;
                                txtTotal.setText(new StringBuilder("Total Price: ???").append(String.format("%.2f", total)));
                                checkout.setEnabled(total != 0.0);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadCartFromFirebase() {
        List<CartModel> cartModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child(currentuser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot cartSnapshot:snapshot.getChildren()){
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            crtty.setVisibility(View.GONE);
                            cty.setVisibility(View.GONE);
                            cartLoadListener.onCartLoadSuccess(cartModels);
                        }else{
                            crtty.setVisibility(View.VISIBLE);
                            cty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());

                    }
                });
    }

    private void init(){
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);
        recyclerCart.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int st5 = Integer.parseInt(stock5);
                int st6 = Integer.parseInt(stock6);
                int st7 = Integer.parseInt(stock7);
                int st8 = Integer.parseInt(stock8);
                if(st5>=CartQTY5&&st6>=CartQTY6&&st7>=CartQTY7&&st8>=CartQTY8){
                    Intent intent = new Intent(CartActivity.this,SummaryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else{
                    Toast.makeText(CartActivity.this, "Some of your Item has Reach it Limit Please Check your Orders!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        MyCartAdapter adapter = new MyCartAdapter(this,cartModelList);
        recyclerCart.setAdapter(adapter);
    }

    @Override
    public void onCartLoadFailed(String message) {

        Snackbar.make(cartML,message,Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}