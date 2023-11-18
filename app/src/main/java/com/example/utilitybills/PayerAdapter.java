package com.example.utilitybills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utilitybills.contract.UtilityBills;
import com.example.utilitybills.utils.Utils;

import java.util.List;

public class PayerAdapter extends RecyclerView.Adapter<PayerViewHolder> {
    Context context;
    List<UtilityBills.ShortPayer> payers;

    public PayerAdapter(Context context, List<UtilityBills.ShortPayer> payers) {
        this.context = context;
        this.payers = payers;
    }

    @NonNull
    @Override
    public PayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayerViewHolder(LayoutInflater.from(context).inflate(R.layout.payer_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayerViewHolder holder, int position) {
        UtilityBills.ShortPayer payer = payers.get(position);
        String fullName = Utils.getFullName(payer);

        holder.fullNameTV.setText(fullName);
        holder.debtTV.setText(Utils.convertFromWei(payer.debt));
        holder.payer = payer;
        holder.context = context;
    }

    @Override
    public int getItemCount() {
        return payers.size();
    }
}
