package com.example.springboothttps.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Scope("singleton")
public class User {

    private String algorithm = "AES/CBC/PKCS5Padding";
    private Map<String, Secrets> map = new ConcurrentHashMap<>();

    @Value("${encryption.rsa.publickey}")
    private String publicKey;

    @Value("${encryption.rsa.privatekey}")
    private String privateKey;


    @GetMapping("/aencrypt")
    public String getUserASymmetricEncrypted() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        byte[] publicKeyByteServer = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKeyServer = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyServer, oaepParams);
        String encoded = Base64.getEncoder().encodeToString(cipher.doFinal("Hello krishna".getBytes()));
        return encoded;
    }

    @PostMapping("/adecrypt")
    public String getUserASymmetricDecrypted(String cipherText) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
        byte[] privateKeyByteServer = Base64.getDecoder().decode(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKeyServer = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByteServer));
        cipher.init(Cipher.DECRYPT_MODE, privateKeyServer, oaepParams);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText.getBytes())));
    }

    @GetMapping("")
    public String getUserSymmetricEncrypted(String user) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        map.putIfAbsent(user, new Secrets(generateKey(), generateIv()));
        Secrets secrets = map.get(user);
        return encrypt(this.algorithm,"Hello krishna",secrets.key,secrets.spec);
    }

    @PostMapping("")
    public String getUserSymmetricDecrypted(String cipherText, String user) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String algorithm = "AES/CBC/PKCS5Padding";
        Secrets secrets = map.get(user);
        return decrypt(algorithm,cipherText, secrets.key, secrets.spec);
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

    static class Secrets {
        private SecretKey key;
        private IvParameterSpec spec;

        public Secrets(SecretKey key, IvParameterSpec spec) {
            this.key = key;
            this.spec = spec;
        }
    }

}
