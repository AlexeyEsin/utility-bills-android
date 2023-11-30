package com.example.utilitybills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utilitybills.contract.ContractInstance;
import com.example.utilitybills.contract.UtilityBills;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    EditText usernameET, passwordET, privateKeyET;
    UtilityBills contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);
        usernameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);
        privateKeyET = findViewById(R.id.private_key_et);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.getMenu().clear();
    }

    public void onClickLogIn(View view) {
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String privateKey = privateKeyET.getText().toString();

        if (username.length() == 0 || password.length() == 0 || privateKey.length() == 0) {
            Toast toastError = Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        try {
            contract = ContractInstance.get(privateKey).contract;

            if (username.equals("admin")) {
                if (!password.equals("admin")) {
                    Toast toastError = Toast.makeText(this, R.string.incorrect_credentials, Toast.LENGTH_SHORT);
                    toastError.show();
                    return;
                }

                Boolean isLogged = contract.LogInAsAdmin().sendAsync().get();
                if (!isLogged) {
                    Toast toastError = Toast.makeText(this, R.string.incorrect_credentials, Toast.LENGTH_SHORT);
                    toastError.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            } else {
                Boolean isLogged = contract.LogIn(username, password).sendAsync().get();

                if (!isLogged) {
                    Toast toastError = Toast.makeText(this, R.string.incorrect_credentials, Toast.LENGTH_SHORT);
                    toastError.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, PayerActivity.class);
                    startActivity(intent);
                }
            }
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }

    public void onClickRegisterLink(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}
