package app.model.view;

import app.model.Transaction;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class TransactionView {
    public final ConcurrentMap<String, Transaction> transactions;
}
