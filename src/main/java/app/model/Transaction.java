package app.model;

import app.model.utxo.TransactionUTXO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(of={"address"})
@ToString
public class Transaction {
    public final Address address;
    public final Address contractAddress;
    public final String contractName;
    public final String createdAt;
    public final String[] values;

    public Transaction(TransactionUTXO transactionUTXO, int blockDepth, int transactionDepth){
        this.address = new Address(blockDepth, transactionDepth);
        this.contractAddress = transactionUTXO.contractAddress;
        this.contractName = transactionUTXO.contractName;
        this.createdAt = transactionUTXO.createdAt;
        this.values = transactionUTXO.values;
    }
}
