package com.example.utilitybills;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                R.layout.dropdown_menu_popup_item,
                getResources().getStringArray(R.array.units));
        debtUnitTV.setAdapter(adapter);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(view)
                .setTitle(R.string.add_debt)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})
                .setPositiveButton(R.string.submit, (dialogInterface, i) -> addDebtToPayer());

        return builder.show();
    }

    private void addDebtToPayer() {
        String debtUnit = debtUnitTV.getText().toString();
        String debtValueStr = debtValueET.getText().toString();
        BigInteger debtValue = Utils.convertToWei(debtValueStr, debtUnit);

        try {
            contract.AddDebt(payerAddress, debtValue).sendAsync().get();
            payers.list.get(payerPosition).debt = payers.list.get(payerPosition).debt.add(debtValue);
            payerAdapter.notifyDataSetChanged();

            Toast toastError = Toast.makeText(requireActivity(), R.string.debt_added, Toast.LENGTH_SHORT);
            toastError.show();
        } catch (Exception e) {
            Log.e("ERROR", "cannot add", e);
            Toast toastError = Toast.makeText(requireActivity(), R.string.unknown_error, Toast.LENGTH_SHORT);
            toastError.show();
        }
    }
}
