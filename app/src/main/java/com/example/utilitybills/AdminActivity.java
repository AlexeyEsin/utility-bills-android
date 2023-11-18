package com.example.utilitybills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utilitybills.contract.ContractInstance;
import com.example.utilitybills.contract.UtilityBills;
import com.example.utilitybills.utils.PayersList;
import com.example.utilitybills.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;

import java.math.BigInteger;

public class AdminActivity extends AppCompatActivity {
    PayersList payers;
    UtilityBills contract;
    RecyclerView payersRV;
    TextView balanceTV, noPayersTV;
    BigInteger balanceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        payersRV = findViewById(R.id.payers_rv);
        balanceTV = findViewById(R.id.balance_value_tv);
        noPayersTV = findViewById(R.id.no_payers_tv);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.setOnMenuItemClickListener((menuItem) -> {
            if (menuItem.getItemId() == R.id.log_out) {
                logOut();
                return true;
            }
            return false;
        });

        payers = PayersList.get();
        contract = ContractInstance.get().contract;
        getBalance();
        getPayersList();
    }

    private void getBalance() {
        try {
            BigInteger balance = contract.GetBalance().sendAsync().get();
            balanceValue = balance;
            String balanceStr = Utils.convertFromWei(balance);
            balanceTV.setText(balanceStr);
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }

    private void getPayersList() {
        try {
            payers.list = contract.GetPayers().sendAsync().get();
            if (payers.list.size() == 0) {
                noPayersTV.setVisibility(View.VISIBLE);
            } else {
                payersRV.setLayoutManager(new LinearLayoutManager(this));
                payersRV.setAdapter(new PayerAdapter(this, payers.list));
                payersRV.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }

    public void logOut() {
        ContractInstance.destroy();
        PayersList.destroy();
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickWithdraw(View view) {
        try {
            contract.Withdraw(balanceValue).sendAsync().get();
            balanceValue = BigInteger.valueOf(0);
            String balanceStr = Utils.convertFromWei(balanceValue);
            balanceTV.setText(balanceStr);

            Toast toastError = Toast.makeText(this, R.string.withdrawed, Toast.LENGTH_SHORT);
            toastError.show();
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }
}
