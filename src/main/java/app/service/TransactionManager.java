package app.service;

import app.model.Transaction;
import app.model.dto.CreateContract;
import app.model.utxo.TransactionUTXO;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Keyz.GenerateHash;
import static app.model.utxo.TransactionUTXO.MakeTransactionUTXO;

public class TransactionManager {

    public static final ConcurrentMap<String, Transaction> Transactions = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, TransactionUTXO> TransactionUTXOs = new ConcurrentHashMap<>();

    public static void CreateTransactionUTXO(CreateContract createContract) {
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(createContract, "Create-");
        TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
    }

    public static void CreateCreateTransactionUTXO(CreateContract createContract) {
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(createContract, "Create-");
        TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
    }

    public static boolean CreateCompleteTransactionUTXO(CreateContract createContract) {
        String[] fields = Arrays.copyOfRange(createContract.fields, 0, createContract.fields.length - 1);
        TransactionUTXO transactionUTXO = MakeTransactionUTXO(new CreateContract(createContract.email,
                createContract.sessionToken, createContract.name, fields), "Complete-");
        if (Transactions.keySet().contains("Create-" + createContract.name) &&
                GenerateHash(createContract.fields[2], 6).equals(createContract.fields[createContract.fields.length - 1])) {
            if (Transactions.keySet().contains("Complete-" + createContract.name)) {
                throw new IllegalArgumentException();
            }

            TransactionUTXOs.putIfAbsent(transactionUTXO.contractName, transactionUTXO);
            return true;
        }
        return false;
    }

    public static void CreateTransactions(List<Transaction> transactions) {
        transactions.forEach(transaction -> Transactions.putIfAbsent(transaction.contractName, transaction));
    }
}
