package com.example.utilitybills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utilitybills.contract.ContractInstance;
import com.example.utilitybills.contract.UtilityBills;
import com.example.utilitybills.utils.Utils;
import com.google.android.material.appbar.MaterialToolbar;

import java.math.BigInteger;

public class PayerActivity extends AppCompatActivity {
    UtilityBills contract;
    TextView welcomeTV, debtLabelTV, debtValueTV;
    BigInteger debtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payer);
        welcomeTV = findViewById(R.id.welcome_tv);
        debtLabelTV = findViewById(R.id.debt_sum_label_tv);
        debtValueTV = findViewById(R.id.debt_sum_value_tv);

        MaterialToolbar topAppBar = findViewById(R.id.top_app_bar);
        topAppBar.setOnMenuItemClickListener((menuItem) -> {
            if (menuItem.getItemId() == R.id.log_out) {
                logOut();
                return true;
            }
            return false;
        });

        contract = ContractInstance.get().contract;
        getPayerInfo(ContractInstance.get().userAddress);
        updateMakePaymentButton();
    }

    private void updateMakePaymentButton() {
        if (debtValue.intValue() == 0) {
            Button makePaymentBtn = findViewById(R.id.make_payment_btn);
            makePaymentBtn.setVisibility(View.GONE);
        }
    }

    private void getPayerInfo(String userAddress) {
        try {
            UtilityBills.Payer payerInfo = contract.GetPayerInfo(userAddress).sendAsync().get();
            String fullName = Utils.getFullName(payerInfo);
            String debtValueStr = Utils.convertFromWei(payerInfo.debt);
            debtValue = payerInfo.debt;
            welcomeTV.setText(String.format(getString(R.string.welcome), fullName));
            debtValueTV.setText(debtValueStr);
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }

    public void logOut() {
        ContractInstance.destroy();
        Intent intent = new Intent(PayerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickMakePayment(View view) {
        try {
            contract.MakePayment(debtValue).sendAsync();
            debtValue = BigInteger.valueOf(0);
            debtValueTV.setText(Utils.convertFromWei(debtValue));
            updateMakePaymentButton();
            Toast toastError = Toast.makeText(this, R.string.debt_repaid, Toast.LENGTH_SHORT);
            toastError.show();
        } catch (Exception e) {
            Toast toastError = Toast.makeText(this, R.string.not_enough_funds, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }
}
