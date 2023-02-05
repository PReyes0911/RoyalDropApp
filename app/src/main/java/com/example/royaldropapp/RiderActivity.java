package com.example.royaldropapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiderActivity extends AppCompatActivity {


    ImageView goback;

    TextInputLayout Empname1,Empname2,Empname3;
    TextInputLayout empUsername1,empUsername2,empUsername3;
    TextInputLayout empPassword1,empPassword2,empPassword3;

    Button editEmp1,editEmp2,editEmp3;

    DatabaseReference EmployeeReference;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        EmployeeReference = FirebaseDatabase.getInstance().getReference();

        goback = findViewById(R.id.returnback);
        Empname1 = findViewById(R.id.empname1);
        Empname2 = findViewById(R.id.empname2);
        Empname3 = findViewById(R.id.empname3);
        empUsername1 = findViewById(R.id.UserEmployee1);
        empUsername2 = findViewById(R.id.UserEmployee2);
        empUsername3 = findViewById(R.id.UserEmployee3);
        empPassword1 = findViewById(R.id.PassEmployee1);
        empPassword2 = findViewById(R.id.PassEmployee2);
        empPassword3 = findViewById(R.id.PassEmployee3);
        editEmp1 = findViewById(R.id.btn_emp1);
        editEmp2 = findViewById(R.id.btn_emp2);
        editEmp3 = findViewById(R.id.btn_emp3);

        dialog=new Dialog(this);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
            }
        });
        EmployeeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Emp1_name = snapshot.child("Admin").child("employees").child("Employee1").child("name").getValue(String.class);
                String Emp1_username = snapshot.child("Admin").child("employees").child("Employee1").child("username").getValue(String.class);
                String Emp1_pass = snapshot.child("Admin").child("employees").child("Employee1").child("password").getValue(String.class);

                Empname1.getEditText().setText(Emp1_name);
                empUsername1.getEditText().setText(Emp1_username);
                empPassword1.getEditText().setText(Emp1_pass);

                String Emp2_name = snapshot.child("Admin").child("employees").child("Employee2").child("name").getValue(String.class);
                String Emp2_username = snapshot.child("Admin").child("employees").child("Employee2").child("username").getValue(String.class);
                String Emp2_pass = snapshot.child("Admin").child("employees").child("Employee2").child("password").getValue(String.class);

                Empname2.getEditText().setText(Emp2_name);
                empUsername2.getEditText().setText(Emp2_username);
                empPassword2.getEditText().setText(Emp2_pass);

                String Emp3_name = snapshot.child("Admin").child("employees").child("Employee3").child("name").getValue(String.class);
                String Emp3_username = snapshot.child("Admin").child("employees").child("Employee3").child("username").getValue(String.class);
                String Emp3_pass = snapshot.child("Admin").child("employees").child("Employee3").child("password").getValue(String.class);

                Empname3.getEditText().setText(Emp3_name);
                empUsername3.getEditText().setText(Emp3_username);
                empPassword3.getEditText().setText(Emp3_pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editEmp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.update_rider1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextInputLayout name1 = dialog.findViewById(R.id.update_name);
                TextInputLayout username1 = dialog.findViewById(R.id.update_username);
                TextInputLayout password1 = dialog.findViewById(R.id.update_password);

                Button btnok = dialog.findViewById(R.id.btn_change);
                Button btnnotyet = dialog.findViewById(R.id.btn_no);

                ProgressBar progressBar = dialog.findViewById(R.id.progress_update);

                EmployeeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Emp1_name = snapshot.child("Admin").child("employees").child("Employee1").child("name").getValue(String.class);
                        String Emp1_username = snapshot.child("Admin").child("employees").child("Employee1").child("username").getValue(String.class);
                        String Emp1_pass = snapshot.child("Admin").child("employees").child("Employee1").child("password").getValue(String.class);

                        name1.getEditText().setText(Emp1_name);
                        username1.getEditText().setText(Emp1_username);
                        password1.getEditText().setText(Emp1_pass);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                name1.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp1_name = snapshot.child("Admin").child("employees").child("Employee1").child("name").getValue(String.class);
                                String Emp1_username = snapshot.child("Admin").child("employees").child("Employee1").child("username").getValue(String.class);
                                String Emp1_pass = snapshot.child("Admin").child("employees").child("Employee1").child("password").getValue(String.class);
                                btnok.setEnabled(!name1.getEditText().getText().toString().equals(Emp1_name) || !username1.getEditText().getText().toString().equals(Emp1_username) || !password1.getEditText().getText().toString().equals(Emp1_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                username1.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp1_name = snapshot.child("Admin").child("employees").child("Employee1").child("name").getValue(String.class);
                                String Emp1_username = snapshot.child("Admin").child("employees").child("Employee1").child("username").getValue(String.class);
                                String Emp1_pass = snapshot.child("Admin").child("employees").child("Employee1").child("password").getValue(String.class);
                                btnok.setEnabled(!name1.getEditText().getText().toString().equals(Emp1_name) || !username1.getEditText().getText().toString().equals(Emp1_username) || !password1.getEditText().getText().toString().equals(Emp1_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                password1.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp1_name = snapshot.child("Admin").child("employees").child("Employee1").child("name").getValue(String.class);
                                String Emp1_username = snapshot.child("Admin").child("employees").child("Employee1").child("username").getValue(String.class);
                                String Emp1_pass = snapshot.child("Admin").child("employees").child("Employee1").child("password").getValue(String.class);
                                btnok.setEnabled(!name1.getEditText().getText().toString().equals(Emp1_name) || !username1.getEditText().getText().toString().equals(Emp1_username) || !password1.getEditText().getText().toString().equals(Emp1_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name1.getEditText().getText().toString().isEmpty()&&username1.getEditText().getText().toString().isEmpty()&&password1.getEditText().getText().toString().isEmpty()){
                            name1.setError("Invalid Credentials");
                            username1.setError("Invalid Credentials");
                            password1.setError("Invalid Credentials");
                        }else{
                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    name1.setError(null);
                                    username1.setError(null);
                                    password1.setError(null);
                                    progressBar.setVisibility(View.VISIBLE);
                                    btnok.setVisibility(View.GONE);
                                    btnnotyet.setVisibility(View.GONE);
                                }

                                public void onFinish() {
                                    Toast.makeText(RiderActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                    String updatedname1 = name1.getEditText().getText().toString();
                                    String updatedusername1 = username1.getEditText().getText().toString();
                                    String updatedpassword1 = password1.getEditText().getText().toString();

                                    EmployeeReference.child("Admin").child("employees").child("Employee1").child("name").setValue(updatedname1);
                                    EmployeeReference.child("Admin").child("employees").child("Employee1").child("username").setValue(updatedusername1);
                                    EmployeeReference.child("Admin").child("employees").child("Employee1").child("password").setValue(updatedpassword1);
                                    dialog.dismiss();
                                }

                            }.start();
                        }
                    }
                });
                btnnotyet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        editEmp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.update_rider1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextInputLayout name2 = dialog.findViewById(R.id.update_name);
                TextInputLayout username2 = dialog.findViewById(R.id.update_username);
                TextInputLayout password2 = dialog.findViewById(R.id.update_password);

                Button btnok = dialog.findViewById(R.id.btn_change);
                Button btnnotyet = dialog.findViewById(R.id.btn_no);

                ProgressBar progressBar = dialog.findViewById(R.id.progress_update);

                EmployeeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Emp2_name = snapshot.child("Admin").child("employees").child("Employee2").child("name").getValue(String.class);
                        String Emp2_username = snapshot.child("Admin").child("employees").child("Employee2").child("username").getValue(String.class);
                        String Emp2_pass = snapshot.child("Admin").child("employees").child("Employee2").child("password").getValue(String.class);

                        name2.getEditText().setText(Emp2_name);
                        username2.getEditText().setText(Emp2_username);
                        password2.getEditText().setText(Emp2_pass);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                name2.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp2_name = snapshot.child("Admin").child("employees").child("Employee2").child("name").getValue(String.class);
                                String Emp2_username = snapshot.child("Admin").child("employees").child("Employee2").child("username").getValue(String.class);
                                String Emp2_pass = snapshot.child("Admin").child("employees").child("Employee2").child("password").getValue(String.class);
                                btnok.setEnabled(!name2.getEditText().getText().toString().equals(Emp2_name) || !username2.getEditText().getText().toString().equals(Emp2_username) || !password2.getEditText().getText().toString().equals(Emp2_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                username2.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp2_name = snapshot.child("Admin").child("employees").child("Employee2").child("name").getValue(String.class);
                                String Emp2_username = snapshot.child("Admin").child("employees").child("Employee2").child("username").getValue(String.class);
                                String Emp2_pass = snapshot.child("Admin").child("employees").child("Employee2").child("password").getValue(String.class);
                                btnok.setEnabled(!name2.getEditText().getText().toString().equals(Emp2_name) || !username2.getEditText().getText().toString().equals(Emp2_username) || !password2.getEditText().getText().toString().equals(Emp2_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                password2.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp2_name = snapshot.child("Admin").child("employees").child("Employee2").child("name").getValue(String.class);
                                String Emp2_username = snapshot.child("Admin").child("employees").child("Employee2").child("username").getValue(String.class);
                                String Emp2_pass = snapshot.child("Admin").child("employees").child("Employee2").child("password").getValue(String.class);
                                btnok.setEnabled(!name2.getEditText().getText().toString().equals(Emp2_name) || !username2.getEditText().getText().toString().equals(Emp2_username) || !password2.getEditText().getText().toString().equals(Emp2_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name2.getEditText().getText().toString().isEmpty()&&username2.getEditText().getText().toString().isEmpty()&&password2.getEditText().getText().toString().isEmpty()){
                            name2.setError("Invalid Credentials");
                            username2.setError("Invalid Credentials");
                            password2.setError("Invalid Credentials");
                        }else{
                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    name2.setError(null);
                                    username2.setError(null);
                                    password2.setError(null);
                                    progressBar.setVisibility(View.VISIBLE);
                                    btnok.setVisibility(View.GONE);
                                    btnnotyet.setVisibility(View.GONE);
                                }

                                public void onFinish() {
                                    Toast.makeText(RiderActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                    String updatedname2 = name2.getEditText().getText().toString();
                                    String updatedusername2 = username2.getEditText().getText().toString();
                                    String updatedpassword2 = password2.getEditText().getText().toString();

                                    EmployeeReference.child("Admin").child("employees").child("Employee2").child("name").setValue(updatedname2);
                                    EmployeeReference.child("Admin").child("employees").child("Employee2").child("username").setValue(updatedusername2);
                                    EmployeeReference.child("Admin").child("employees").child("Employee2").child("password").setValue(updatedpassword2);
                                    dialog.dismiss();
                                }

                            }.start();
                        }
                    }
                });
                btnnotyet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        editEmp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.update_rider1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextInputLayout name3 = dialog.findViewById(R.id.update_name);
                TextInputLayout username3 = dialog.findViewById(R.id.update_username);
                TextInputLayout password3 = dialog.findViewById(R.id.update_password);

                Button btnok = dialog.findViewById(R.id.btn_change);
                Button btnnotyet = dialog.findViewById(R.id.btn_no);

                ProgressBar progressBar = dialog.findViewById(R.id.progress_update);

                EmployeeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Emp3_name = snapshot.child("Admin").child("employees").child("Employee3").child("name").getValue(String.class);
                        String Emp3_username = snapshot.child("Admin").child("employees").child("Employee3").child("username").getValue(String.class);
                        String Emp3_pass = snapshot.child("Admin").child("employees").child("Employee3").child("password").getValue(String.class);

                        name3.getEditText().setText(Emp3_name);
                        username3.getEditText().setText(Emp3_username);
                        password3.getEditText().setText(Emp3_pass);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                name3.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp3_name = snapshot.child("Admin").child("employees").child("Employee3").child("name").getValue(String.class);
                                String Emp3_username = snapshot.child("Admin").child("employees").child("Employee3").child("username").getValue(String.class);
                                String Emp3_pass = snapshot.child("Admin").child("employees").child("Employee3").child("password").getValue(String.class);
                                btnok.setEnabled(!name3.getEditText().getText().toString().equals(Emp3_name) || !username3.getEditText().getText().toString().equals(Emp3_username) || !password3.getEditText().getText().toString().equals(Emp3_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                username3.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp3_name = snapshot.child("Admin").child("employees").child("Employee3").child("name").getValue(String.class);
                                String Emp3_username = snapshot.child("Admin").child("employees").child("Employee3").child("username").getValue(String.class);
                                String Emp3_pass = snapshot.child("Admin").child("employees").child("Employee3").child("password").getValue(String.class);
                                btnok.setEnabled(!name3.getEditText().getText().toString().equals(Emp3_name) || !username3.getEditText().getText().toString().equals(Emp3_username) || !password3.getEditText().getText().toString().equals(Emp3_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                password3.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        EmployeeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String Emp3_name = snapshot.child("Admin").child("employees").child("Employee3").child("name").getValue(String.class);
                                String Emp3_username = snapshot.child("Admin").child("employees").child("Employee3").child("username").getValue(String.class);
                                String Emp3_pass = snapshot.child("Admin").child("employees").child("Employee3").child("password").getValue(String.class);
                                btnok.setEnabled(!name3.getEditText().getText().toString().equals(Emp3_name) || !username3.getEditText().getText().toString().equals(Emp3_username) || !password3.getEditText().getText().toString().equals(Emp3_pass));
                                if (btnok.isEnabled()){
                                    btnok.setBackgroundColor(Color.parseColor("#00FF00"));
                                    btnok.setTextColor(Color.BLACK);
                                }else{
                                    btnok.setBackgroundColor(Color.parseColor("#009CFF"));
                                    btnok.setTextColor(Color.WHITE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name3.getEditText().getText().toString().isEmpty()&&username3.getEditText().getText().toString().isEmpty()&&password3.getEditText().getText().toString().isEmpty()){
                            name3.setError("Invalid Credentials");
                            username3.setError("Invalid Credentials");
                            password3.setError("Invalid Credentials");
                        }else{
                            new CountDownTimer(3000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    name3.setError(null);
                                    username3.setError(null);
                                    password3.setError(null);
                                    progressBar.setVisibility(View.VISIBLE);
                                    btnok.setVisibility(View.GONE);
                                    btnnotyet.setVisibility(View.GONE);
                                }

                                public void onFinish() {
                                    Toast.makeText(RiderActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                    String updatedname3 = name3.getEditText().getText().toString();
                                    String updatedusername3 = username3.getEditText().getText().toString();
                                    String updatedpassword3 = password3.getEditText().getText().toString();

                                    EmployeeReference.child("Admin").child("employees").child("Employee3").child("name").setValue(updatedname3);
                                    EmployeeReference.child("Admin").child("employees").child("Employee3").child("username").setValue(updatedusername3);
                                    EmployeeReference.child("Admin").child("employees").child("Employee3").child("password").setValue(updatedpassword3);
                                    dialog.dismiss();
                                }

                            }.start();
                        }
                    }
                });
                btnnotyet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}