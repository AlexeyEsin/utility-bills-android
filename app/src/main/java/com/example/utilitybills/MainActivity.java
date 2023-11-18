package com.example.utilitybills;

import androidx.appcompat.app.AppCompatActivity;

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
        setContentView(R.layout.activity_main);
        usernameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);
        privateKeyET = findViewById(R.id.private_key_et);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.getMenu().clear();

        // admin
        usernameET.setText("admin");
        passwordET.setText("admin");
        privateKeyET.setText("0x7a7b7606577410c032351f06621b666ac124ba8ea752860bfbf92d522a5f0329");

//         egor
//        usernameET.setText("egor");
//        passwordET.setText("egor");
//        privateKeyET.setText("0xc35cf7a2884f1871ec145325f38c1494655aa13c599b5ed164cae2d6449c6e1e");
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
