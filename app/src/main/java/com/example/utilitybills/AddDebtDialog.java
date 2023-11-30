package com.example.utilitybills;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.utilitybills.contract.ContractInstance;
import com.example.utilitybills.contract.UtilityBills;
import com.example.utilitybills.utils.PayersList;
import com.example.utilitybills.utils.Utils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigInteger;

public class AddDebtDialog extends AppCompatDialogFragment {
    PayersList payers;
    UtilityBills contract;
    String payerAddress;
    int payerPosition;
    PayerAdapter payerAdapter;
    EditText debtValueET;
    AutoCompleteTextView debtUnitTV;

    public AddDebtDialog(String address, int position, PayerAdapter adapter) {
        payerAdapter = adapter;
        payerAddress = address;
        payerPosition = position;
        payers = PayersList.get();
        contract = ContractInstance.get().contract;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_debt_dialog, null);

        debtValueET = view.findViewById(R.id.debt_value_et);
        debtUnitTV = view.findViewById(R.id.debt_unit_tv);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.dropdown_menu_item,
                getResources().getStringArray(R.array.units));
        debtUnitTV.setAdapter(adapter);
        debtUnitTV.setText(adapter.getItem(0), false);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(view)
                .setTitle(R.string.add_debt)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.submit, null);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> addDebtToPayer(dialog));
        }
    }

    private void addDebtToPayer(AlertDialog dialog) {
        String debtUnit = debtUnitTV.getText().toString();
        String debtValueStr = debtValueET.getText().toString();

        if (debtValueStr.equals("")) {
            Toast toastError = Toast.makeText(requireActivity(), R.string.fill_all_fields, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        float debtValueFloat = Float.parseFloat(debtValueStr);
        if (debtValueFloat <= 0) {
            Toast toastError = Toast.makeText(requireActivity(), R.string.debt_less_than_zero, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        if (debtUnit.equals("wei") && debtValueFloat < 1) {
            Toast toastError = Toast.makeText(requireActivity(), R.string.debt_less_than_1_wei, Toast.LENGTH_SHORT);
            toastError.show();
            return;
        }

        BigInteger debtValue = Utils.convertToWei(debtValueStr, debtUnit);

        try {
            contract.AddDebt(payerAddress, debtValue).sendAsync().get();
            payers.list.get(payerPosition).debt = payers.list.get(payerPosition).debt.add(debtValue);
            payerAdapter.notifyDataSetChanged();
            dialog.dismiss();

            Toast toastError = Toast.makeText(requireActivity(), R.string.debt_added, Toast.LENGTH_SHORT);
            toastError.show();
        } catch (Exception e) {
            Log.e("ERROR", "cannot add", e);
            Toast toastError = Toast.makeText(requireActivity(), R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }
}
