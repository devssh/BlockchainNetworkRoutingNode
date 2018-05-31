package app.model.view;

import app.model.Block;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class BlockchainView {
    public final ConcurrentMap<Integer, Block> BLOCKCHAIN;
}
