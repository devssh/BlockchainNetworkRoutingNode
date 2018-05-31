package utils;

import app.model.Keyz;
import org.junit.Test;

import static app.model.Keyz.GenerateKey;
import static app.utils.SignatureUtils.Sign;
import static app.utils.SignatureUtils.Verify;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SignatureUtilsTest {


    @Test
    public void shouldGenerateSignatureAndVerify() throws Exception {
        String message = "today is a nice day";

        Keyz key = GenerateKey(Keyz.GenerateSeed());

        String sign = Sign(key.privateKey, message);

        assertEquals(96, sign.length());

        assertTrue(Verify(message, key.publicKey, sign));
    }

    @Test
    public void shouldGenerateUniqueSignatures() throws Exception {
        String message = "yo";

        Keyz key = GenerateKey(Keyz.GenerateSeed());

        String sign1 = Sign(key.privateKey, message);
        String sign2 = Sign(key.privateKey, message);
        assertNotEquals(sign2, sign1);

        assertTrue(Verify(message, key.publicKey, sign1));
        assertTrue(Verify(message, key.publicKey, sign2));
    }

    @Test
    public void shouldFailOnIncorrectSignatureOrMessageOrPubKey() throws Exception {
        String message = "hello";

        String messageTampered = "world";

        Keyz key = GenerateKey();
        String publicKey = key.publicKey;

        String publicKeyTampered = publicKey.substring(0, publicKey.length() - 4) +
                (publicKey.substring(publicKey.length() - 5,publicKey.length() - 3).equals("aq") ? "aa==" : "aq==");

        Keyz invalidKey = GenerateKey();
        String publicKeyTampered2 = invalidKey.publicKey;

        String signActual = Sign(key.privateKey, message);

        String signTampered = signActual.substring(0, signActual.length() - 4) +
                (signActual.substring(signActual.length() - 5,signActual.length() - 3).equals("aq") ? "aa==" : "aq==");
        String signTampered2 = Sign(key.privateKey, "hey");
        String signTampered3 = Sign(invalidKey.privateKey, message);


        assertFalse(Verify(message, publicKey, signTampered));
        assertFalse(Verify(message, publicKey, signTampered.substring(1)));
        assertFalse(Verify(messageTampered, publicKey, signTampered.substring(1)));
        assertFalse(Verify(messageTampered, publicKey, signTampered));
        assertFalse(Verify(message, publicKey, signTampered2));
        assertFalse(Verify(message, publicKey, signTampered3));
        assertFalse(Verify(messageTampered, publicKey, signActual));

        assertFalse(Verify(message, publicKeyTampered, signActual));
        assertFalse(Verify(message, publicKeyTampered2, signActual));

        assertFalse(Verify(messageTampered, publicKeyTampered2, signTampered2));

        assertTrue(Verify(message, key.publicKey, signActual));
    }
}
