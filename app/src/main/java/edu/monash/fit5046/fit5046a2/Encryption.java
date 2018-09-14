package edu.monash.fit5046.fit5046a2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.R.attr.key;

/**
 * Created by nathan on 28/4/17.
 */

public class Encryption {

    public static String encryptToSHA(String string) {
        byte[] digesta = null;
        String strDes = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-512");
            alga.update(string.getBytes());
            digesta = alga.digest();
            strDes = bytes2Hex(digesta);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
