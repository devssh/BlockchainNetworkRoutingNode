package app.model;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Keyz {
    public static transient final String ECDSA = "ECDSA";
    public static transient final String SHA_1_PRNG = "SHA1PRNG";
    public static transient final String SECP_256_K_1 = "secp256k1";
    public static transient final int NUM_BYTES = 512;
    public static final String SHA_256 = "SHA-256";
    public transient PublicKey publicKeyz;
    public transient PrivateKey privateKeyz;

    public final String owner;
    public final String publicKey;
    public final String privateKey;

    public Keyz(String owner, String publicKey, String privateKey) {
        this.owner = owner;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.publicKeyz = decodePublicKeyFromString(publicKey);
        this.privateKeyz = decodePrivateKeyFromString(privateKey);
    }

    public Keyz(String owner, PublicKey publicKeyz, PrivateKey privateKeyz) {
        this.owner = owner;
        this.publicKeyz = publicKeyz;
        this.privateKeyz = privateKeyz;
        this.publicKey = encodeKeyToString(publicKeyz);
        this.privateKey = encodeKeyToString(privateKeyz);
    }


    public static String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey decodePublicKeyFromString(String key, boolean throwExceptionToCatch) {
        try {
            return KeyFactory.getInstance(ECDSA).generatePublic(new X509EncodedKeySpec(decodeKeyFromString(key)));
        } catch (Exception e) {
            if (throwExceptionToCatch) {
                throw new IllegalArgumentException();
            }
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static PublicKey decodePublicKeyFromString(String key) {
        return decodePublicKeyFromString(key, false);
    }

    public static PrivateKey decodePrivateKeyFromString(String key) {
        try {
            KeyFactory fact = KeyFactory.getInstance(ECDSA);
            return fact.generatePrivate(new PKCS8EncodedKeySpec(decodeKeyFromString(key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    private static byte[] decodeKeyFromString(String key) {
        try {
            return Base64.getDecoder().decode(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static String GenerateSeed() {
        try {
            SecureRandom random = SecureRandom.getInstance(SHA_1_PRNG);
            byte[] seed = random.generateSeed(NUM_BYTES);
            return Base64.getEncoder().encodeToString(seed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static String GenerateSeed(int noOfDigits) {
        String seedString = GenerateSeed();
        return seedString.substring(seedString.length() - (3 + noOfDigits), seedString.length() - 3);
    }

    public static String GenerateHash(String seedData, int noOfDigits) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hash = digest.digest(seedData.getBytes(StandardCharsets.UTF_8));
            String seedString = Base64.getEncoder().encodeToString(hash);
            return seedString.substring(seedString.length() - (3 + noOfDigits), seedString.length() - 3);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static Keyz GenerateKey(String seedString) {
        return GenerateKey(seedString, "random");
    }

    public static Keyz GenerateKey(String seedString, String name) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            KeyPairGenerator keyGen1 = KeyPairGenerator.getInstance(ECDSA);
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(SECP_256_K_1);
            SecureRandom random1 = SecureRandom.getInstance(SHA_1_PRNG);
            random1.setSeed(Base64.getDecoder().decode(seedString));
            keyGen1.initialize(ecSpec, random1);
            KeyPair keyPair1 = keyGen1.generateKeyPair();
            PublicKey pub1 = keyPair1.getPublic();
            PrivateKey priv1 = keyPair1.getPrivate();

            return new Keyz(name, pub1, priv1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public static Keyz GenerateKey() {
        return GenerateKey(GenerateSeed());
    }

    public static Keyz GenerateKeyWithName(String name) {
        System.out.println("Trying to generate random seed");
        String seedString = GenerateSeed();
        System.out.println("success");
        return GenerateKey(seedString, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keyz keyz = (Keyz) o;

        if (!owner.equals(keyz.owner)) return false;
        if (!publicKey.equals(keyz.publicKey)) return false;
        return privateKey.equals(keyz.privateKey);
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + publicKey.hashCode();
        result = 31 * result + privateKey.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Keyz{" +
                "owner='" + owner + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
