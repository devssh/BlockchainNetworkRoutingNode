package app.model;

import static app.service.KeyzManager.KEYS;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;
import static app.utils.SignatureUtils.Sign;

public class Block {
    public final String sign;
    public final String publicKey;
    public transient String data;
    public final BlockDetails blockData;

    public Block(String sign, String publicKey, BlockDetails blockData) {
        this.sign = sign;
        this.publicKey = publicKey;
        this.blockData = blockData;
        this.data = ToJSON(blockData);
    }

    public Block(String data) {
        Keyz miner = KEYS.get("Miner").peek();
        this.publicKey = miner.publicKey;
        this.data = data;
        this.sign = Sign(miner.privateKey, data);
        this.blockData = FromJSON(data, BlockDetails.class);
    }

    public static String BlockSign(String randomData) {
        //TODO: move this from here when figuring out how to remove \Miner
        Keyz miner = KEYS.get("Miner").peek();
        return Sign(miner.privateKey, randomData);
    }
}
