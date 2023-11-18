package com.example.utilitybills.utils;

import com.example.utilitybills.contract.UtilityBills;

import java.util.List;

public class PayersList {
    private static PayersList instance = null;
    public List<UtilityBills.ShortPayer> list;

    protected PayersList() { }

    public static synchronized PayersList get() {
        if (instance == null) {
            instance = new PayersList();
        }
        return instance;
    }

    public static synchronized void destroy() {
        instance = null;
    }
}
