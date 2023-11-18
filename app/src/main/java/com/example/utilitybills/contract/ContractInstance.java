package com.example.utilitybills.contract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class ContractInstance {
    private static ContractInstance contractInstance = null;

    public UtilityBills contract;
    public String userAddress;

    protected ContractInstance(String privateKey) {
        String contractAddress = "0x34D8e4868101b73cC2Fe8A8C41e06951f0F28309";
        Web3j web3j = Web3j.build(new HttpService("HTTP://10.0.2.2:7545"));
        ContractGasProvider gasProvider = new StaticGasProvider(
                BigInteger.valueOf(20_000_000_000L),
                BigInteger.valueOf(6_721_975));
        Credentials credentials = Credentials.create(privateKey);
        userAddress = credentials.getAddress();
        contract = UtilityBills.load(contractAddress, web3j, credentials, gasProvider);
    }

    public static synchronized ContractInstance get() {
        return contractInstance;
    }

    public static synchronized ContractInstance get(String privateKey) {
        if (contractInstance == null) {
            contractInstance = new ContractInstance(privateKey);
        }
        return contractInstance;
    }

    public static synchronized void destroy() {
        contractInstance = null;
    }
}
