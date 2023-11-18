package com.example.utilitybills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.utilitybills.contract.ContractInstance;
import com.example.utilitybills.contract.UtilityBills;
import com.google.android.material.appbar.MaterialToolbar;

public class RegistrationActivity extends AppCompatActivity {
    EditText firstNameET, middleNameET, lastNameET, usernameET, passwordET, privateKeyET;
    UtilityBills contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        firstNameET = findViewById(R.id.first_name_et);
        middleNameET = findViewById(R.id.middle_name_et);
        lastNameET = findViewById(R.id.last_name_et);
        usernameET = findViewById(R.id.username_et);
        passwordET = findViewById(R.id.password_et);
        privateKeyET = findViewById(R.id.private_key_et);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.getMenu().clear();
    }

    public void onClickRegister(View view) {
        String firstName = firstNameET.getText().toString();
        String middleName = middleNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String username = usernameET.getText().toString();
        String password = passwordET.getText().toString();
        String privateKey = privateKeyET.getText().toString();

        if (firstName.length() == 0 || lastName.length() == 0 || username.length() == 0
                || password.length() == 0 || privateKey.length() == 0) {
            Toast toastError = Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        if (username.length() < 4 || password.length() < 4) {
            Toast toastError = Toast.makeText(this, R.string.username_length, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        try {
            contract = ContractInstance.get(privateKey).contract;
            String userAddress = ContractInstance.get(privateKey).userAddress;
            contract.Register(userAddress, username, password, firstName, middleName, lastName).sendAsync().get();

            Toast toastError = Toast.makeText(this, R.string.registered, Toast.LENGTH_SHORT);
            toastError.show();
        } catch (Exception e) {
            Log.e("TEST", "error", e);

            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        } finally {
            ContractInstance.destroy();
        }
    }

    public void onClickLogInLink(View view) {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}