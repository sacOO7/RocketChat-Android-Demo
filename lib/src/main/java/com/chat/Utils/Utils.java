package com.chat.Utils;


import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * Created by SAC on 13-03-2017.
 */
public class Utils {


    public static String getDigest(String password) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

//    public static String generateShortUuid() {
//        UUID uuid = UUID.randomUUID();
//
//        long lsb = uuid.getLeastSignificantBits();
//        long msb = uuid.getMostSignificantBits();
//
//        byte[] uuidBytes = ByteBuffer.allocate(16).putLong(msb).putLong(lsb).array();
//
//        // Strip down the '==' at the end and make it url friendly
//        return Base64.encode(uuidBytes)
//                .substring(0, 22)
//                .replace("/", "_")
//                .replace("+", "-");
//    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

}
