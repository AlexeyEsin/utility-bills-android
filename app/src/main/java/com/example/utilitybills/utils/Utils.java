package com.example.utilitybills.utils;

import androidx.annotation.NonNull;

import com.example.utilitybills.contract.UtilityBills;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class Utils {
    @NonNull
    public static String getFullName(@NonNull UtilityBills.Payer payer) {
        if (payer.middleName != null && !payer.middleName.equals("")) {
            return String.join(" ", payer.lastName, payer.firstName, payer.middleName);
        }
        return String.join(" ", payer.lastName, payer.firstName);
    }

    @NonNull
    public static String getFullName(@NonNull UtilityBills.ShortPayer payer) {
        if (payer.middleName != null && !payer.middleName.equals("")) {
            return String.join(" ", payer.lastName, payer.firstName, payer.middleName);
        }
        return String.join(" ", payer.lastName, payer.firstName);
    }

    @NonNull
    public static String convertFromWei(@NonNull BigInteger amount) {
        String amountStr = amount.toString();
        BigDecimal amountWei = new BigDecimal(amount);

        if (amountStr.length() > 18) {
            return Convert.fromWei(amountWei, Convert.Unit.ETHER).toString() + " ether";
        }
        if (amountStr.length() > 15) {
            return Convert.fromWei(amountWei, Convert.Unit.FINNEY).toString() + " finney";
        }
        if (amountStr.length() > 9) {
            return Convert.fromWei(amountWei, Convert.Unit.GWEI).toString() + " gwei";
        }
        return amountStr + " wei";
    }

    @NonNull
    public static BigInteger convertToWei(@NonNull String amount, String unit) {
        if (Objects.equals(unit, "ether")) {
            return Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        }
        if (Objects.equals(unit, "finney")) {
            return Convert.toWei(amount, Convert.Unit.FINNEY).toBigInteger();
        }
        if (Objects.equals(unit, "gwei")) {
            return Convert.toWei(amount, Convert.Unit.GWEI).toBigInteger();
        }
        return Convert.toWei(amount, Convert.Unit.WEI).toBigInteger();
    }
}
