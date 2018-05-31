package app.model;

import app.model.utxo.ContractUTXO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(of = {"name", "createdAt", "address", "fields"})
@ToString
public class Contract {
    public final String name;
    public final String createdAt;
    public final Address address;
    public final String[] fields;
    //TODO: ContractUXTO should pass the signature of user who created it

    public Contract(ContractUTXO contractUTXO, int blockDepth, int transactionDepth) {
        this.name = contractUTXO.name;
        this.createdAt = contractUTXO.createdAt;
        this.fields = contractUTXO.fields;
        this.address = new Address(blockDepth, transactionDepth);
    }
}
