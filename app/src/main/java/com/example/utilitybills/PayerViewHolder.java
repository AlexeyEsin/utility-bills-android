package com.example.utilitybills;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utilitybills.contract.UtilityBills;

public class PayerViewHolder extends RecyclerView.ViewHolder {
    Context context;
    UtilityBills.ShortPayer payer;
    TextView fullNameTV, debtTV;
    ImageButton addDebtBtn;

    public PayerViewHolder(@NonNull View itemView) {
        super(itemView);
        fullNameTV = itemView.findViewById(R.id.full_name_tv);
        debtTV = itemView.findViewById(R.id.debt_tv);
        addDebtBtn = itemView.findViewById(R.id.add_debt_ib);

        addDebtBtn.setOnClickListener(v -> {
            AddDebtDialog dialog = new AddDebtDialog(
                    payer.payerAddress,
                    getBindingAdapterPosition(),
                    (PayerAdapter) getBindingAdapter());
            FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
            dialog.show(manager, "add debt dialog");
        });
    }
}
