package app.model;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.Base64;

import static app.model.Keyz.GenerateHash;
import static app.model.Keyz.GenerateSeed;
import static app.model.Keyz.GenerateKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class KeyzTest {
    @Test
    public void shouldReturnDeterministicKeys() throws Exception {
        String seedString = GenerateSeed();
        Keyz random1 = GenerateKey(seedString);
        Keyz random2 = GenerateKey(seedString);
        assertEquals(random1, random2);
    }

    @Test
    public void shouldGenerateSeedAndKeysOfProperLengthAndRandomness() throws Exception {
        assertEquals(684, GenerateSeed().length());
        assertEquals(512, Base64.getDecoder().decode(GenerateSeed()).length);

        assertNotEquals(GenerateSeed(), GenerateSeed());

        assertEquals(192, GenerateKey(GenerateSeed()).privateKey.length());
        assertEquals(120, GenerateKey(GenerateSeed()).publicKey.length());

        assertNotEquals(GenerateKey(GenerateSeed()), GenerateKey(GenerateSeed()));
    }

    @Test
    public void shouldMaintainIntegrityOfBlockchainKeysWRTSeed() throws Exception {
        String seedString = "8pgaV82C40e9248l5QnxudwX71ty1yfc6M5GuFfXTWC4zOJxqcnXDlUPMVtvPTUElvNDBj5RVc13OmWp7/0BIFvNaVJcuq2ZDho1Mx+2BWpMNF/EbDRcxczUKzVm0Fmjz7KerN1iTZYsNIL4BQ2sE6giW80U0HbB7beUxBhOx+SnUi+YJbFH9o3Xq9WsTbxX9Mevi1660/8PoMXPYq72x7qaHrXyZXsO1V5EJsWy26VQLoOExM4SkM9b4HL5N+Uf8qnkx74fd+/SS8EqKKw/MLui2v2/VN97zjED5PZuPjcAPUAr8yxiU9dJIUFksNiVJo8wF75N3+t//0Hi+eq3n4iVvWBGg/VG5L91JbTGUi4v9Z/lAGNnRqXjTxJsBkPRDQuz+LofS9AuA3TZ/nnAjBM+kge2MpypChAJo+a4s/ZbVE5rdsSdKoaVvBNbhO0c37C3t0NJI3q6ofDLo/5DtEgMorIBwlNrg3Kzrzs+PDZsk01qVnatQrQQMADe7Aor93jD5gNhnsA4CLWOf3kAkgGKDdkifVZpTpsl8Okct/tKeZYJhyDK9ibN7gRPXfvNwGJZErVlT7tcSG5mYCCi3PdmqRERvQJcv72iaFsyVYyNXQDtVy7x9VbC4QmD5rId4NzLGAtrfwMlJLNcqkoewr78MDZ0EZzyn38r9rrwDHQ=";
        String publicKey = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEanQTvhTtyQ5OKXwGflVx3yZCE5Abrz2J62k3SaK/T3570oC10CtPO4sMKQNCtsefP2suKzIGAVkXYBlQiJVDuA==";
        String privateKey = "MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQgTMo3M8xeMrIOzbcI1xHJmX83JH3IkXgR42LOMUZfdf2gBwYFK4EEAAqhRANCAARqdBO+FO3JDk4pfAZ+VXHfJkITkBuvPYnraTdJor9PfnvSgLXQK087iwwpA0K2x58/ay4rMgYBWRdgGVCIlUO4";
        assertEquals(publicKey, GenerateKey(seedString).publicKey);
        assertEquals(privateKey, GenerateKey(seedString).privateKey);
    }

    @Test
    public void shouldReturnSameSeed() throws Exception {
        String seedData = "hellogmailcom";
        String actualSeedString = GenerateHash(seedData, 6);
        String expectedSeedString = "cqYWd3";
        assertEquals(expectedSeedString, actualSeedString);
    }
}
