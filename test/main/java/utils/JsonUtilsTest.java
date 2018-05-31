package utils;

import app.model.Address;
import app.model.Contract;
import app.model.utxo.ContractUTXO;
import org.junit.Test;

import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    public static final Contract CONTRACT = new Contract(new ContractUTXO("abc", "time", new String[]{"hi"}), 1, 1);

    @Test
    public void shouldSerializeToJSON() throws Exception {
        assertEquals("{\"name\":\"abc\",\"createdAt\":\"time\",\"address\":{\"blockDepth\":1,\"transactionDepth\":1},\"fields\":[\"hi\"]}", ToJSON(CONTRACT));
    }

    @Test
    public void shouldDeserializeFromJSON() throws Exception {
        assertEquals(CONTRACT, FromJSON("{\"name\":\"abc\",\"createdAt\":\"time\",\"address\":{\"blockDepth\":1,\"transactionDepth\":1},\"fields\":[\"hi\"]}", Contract.class));
    }
}
