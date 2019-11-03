package com.kondiewtc.broker;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class CheckSum {

    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash).toLowerCase();
    }

    public static String generateChecksum(String s)
    {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(s.getBytes("UTF-8"));
            return bytesToHex(hash);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    static boolean isIntact(String s, String checksum)
    {
        String generated_checksum = generateChecksum(s);
        if (generated_checksum.equals(checksum))
        {
            return true;
        }
        else
        {
            Logger.log("The message is not intact");
            return false;
        }
    }
}
