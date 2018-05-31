package app.model.utxo;

import app.model.dto.CreateContract;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;

@AllArgsConstructor
@ToString
public class ContractUTXO {
    public final String name;
    public final String createdAt;
    public final String[] fields;

    public ContractUTXO(CreateContract other) {
        this.name = other.name;
        this.fields = Arrays.stream(Arrays.copyOf(other.fields, other.fields.length))
                .map(x -> x.replaceAll(" ", "").replaceAll(",", ""))
                .filter(x -> x != null && !x.equals(""))
                .toArray(String[]::new);
        this.createdAt = LocalDateTime.now().toString();
    }

}
