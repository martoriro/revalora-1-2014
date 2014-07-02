
package managedbeans.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey; 
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 

public class Crypto {
 
    private BASE64Encoder encoder;
    
    private BASE64Decoder decoder;
    
    private SecretKey key;
    
    private String keyString = "EdmundoLeiva";
    
    
    public Crypto() throws NoSuchAlgorithmException, InvalidKeySpecException, 
            InvalidKeyException, UnsupportedEncodingException {
        
        generateKey();        
    }
    
    public Crypto(String keyString) throws NoSuchAlgorithmException, 
            InvalidKeySpecException, InvalidKeyException, 
            UnsupportedEncodingException {
        
        this.keyString = keyString;
        generateKey();
    }
     
    private void generateKey() throws NoSuchAlgorithmException, 
            InvalidKeySpecException, InvalidKeyException, 
            UnsupportedEncodingException {
        
	DESKeySpec keySpec = new DESKeySpec(keyString.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        
        key = keyFactory.generateSecret(keySpec);
        encoder = new BASE64Encoder();
        decoder = new BASE64Decoder();
    }
    
    public String crypt(String msg) throws InvalidKeyException, 
            UnsupportedEncodingException, NoSuchAlgorithmException, 
            IllegalBlockSizeException, NoSuchPaddingException, 
            BadPaddingException, InvalidKeySpecException {
        
        generateKey();
        
        byte[] cleartext = msg.getBytes("UTF8");      
        Cipher cipher = Cipher.getInstance("DES");
        
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return encoder.encode(cipher.doFinal(cleartext));
    }
    
    public String decrypt(String msg) throws NoSuchAlgorithmException, 
            IOException, NoSuchPaddingException, InvalidKeyException, 
            IllegalBlockSizeException, BadPaddingException {
        
        byte[] encrypedPwdBytes = decoder.decodeBuffer(msg);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainTextPwdBytes = cipher.doFinal(encrypedPwdBytes);
        return new String(plainTextPwdBytes);
    }

}