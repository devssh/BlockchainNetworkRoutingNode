package app.utils;

import app.model.Block;
import app.model.Keyz;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

import static app.model.Keyz.decodePrivateKeyFromString;

public class SignatureUtils {
    public static transient final String signatureAlgo = "SHA1withECDSA";
    public static transient final String ENCODING = "UTF-8";

    public static String Sign(PrivateKey privateKey, String message) {
        try {
            Signature dsa = Signature.getInstance(signatureAlgo);
            dsa.initSign(privateKey);
            dsa.update(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(message.getBytes(ENCODING))));
            return Base64.getEncoder().encodeToString(dsa.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

//    public static String SignWith(String signedBy, String message) {
//        return Sign(GetKey(signedBy).privateKeyz, message);
//    }

    public static String Sign(String privKey, String message) {
        return Sign(decodePrivateKeyFromString(privKey), message);
    }

    public static boolean Verify(Block block) {
        return Verify(block.data, block.publicKey, block.sign);
    }

    public static boolean Verify(String blockMessage, PublicKey publicKey, String sign) {
        try {
            Signature ecdsa = Signature.getInstance(signatureAlgo);
            ecdsa.initVerify(publicKey);
            ecdsa.update(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(blockMessage.getBytes(ENCODING))));
            return ecdsa.verify(Base64.getDecoder().decode(sign));
        } catch (SignatureException | IllegalArgumentException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static boolean Verify(String blockMessage, String pubKey, String sign) {
        try {
            return Verify(blockMessage, Keyz.decodePublicKeyFromString(pubKey, true), sign);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

//    public static boolean VerifyWith(String blockMessage, String signedBy, String sign) {
//        return Verify(blockMessage, GetKey(signedBy).publicKeyz, sign);
//    }
}
