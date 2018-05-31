package app.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BlockDetails {
    public Integer nonce;
    public final Integer depth;
    public String blockCreatedAt;
    public final String previousBlockSign;
    public final String merkleRoot;
    public final Integer difficulty;
    public final String relayedBy;
    public final Integer numberOfTransactions;
}
