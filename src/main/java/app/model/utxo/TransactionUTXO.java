package app.model.utxo;

import app.model.Address;
import app.model.dto.CreateContract;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;

import static app.model.Keyz.GenerateHash;
import static app.utils.DateUtil.GetDateTimeNow;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = {"contractAddress", "contractName", "createdAt", "values"})
public class TransactionUTXO {
    public final Address contractAddress;
    public final String contractName;
    public final String createdAt;
    public final String[] values;

    public TransactionUTXO(CreateContract createTransaction) {
        this.contractAddress = new Address(0, 0);
        this.contractName = createTransaction.name;
        this.createdAt = GetDateTimeNow();

        //for this transaction values are emails
        this.values = Arrays.stream(Arrays.copyOf(createTransaction.fields, createTransaction.fields.length))
                .map(x -> x.replaceAll(" ", "").replaceAll(",", ""))
                .filter(x -> x != null && !x.equals(""))
                .toArray(String[]::new);
    }

    public static TransactionUTXO MakeTransactionUTXO(CreateContract createContract, String type) {
        return new TransactionUTXO(new CreateContract("tw@gmail.com", GenerateHash("tw@gmail.com", 6), type + createContract.name, createContract.fields));
    }
}
