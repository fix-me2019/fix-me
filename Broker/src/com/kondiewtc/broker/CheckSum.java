package com.kondiewtc.broker;

public class CheckSum {

    public static int generateChecksum(String s)
    {
        String hex_value;
        int x, i, checksum = 0;

        for (i = 0; i < s.length() - 2; i += 2)
        {
            x = (s.charAt(i));
            hex_value = Integer.toHexString(x);
            x = (s.charAt(i + 1));
            hex_value = hex_value + Integer.toHexString(x);

            x = Integer.parseInt(hex_value, 16);

            checksum += x;
        }
        if (s.length() % 2 == 0)
        {
            x = (s.charAt(i));
            hex_value = Integer.toHexString(x);
            x = (s.charAt(i + 1));
            hex_value = hex_value + Integer.toHexString(x);
            x = Integer.parseInt(hex_value, 16);
        }
        else
        {
            x = (s.charAt(i));
            hex_value = "00" + Integer.toHexString(x);
            x = Integer.parseInt(hex_value, 16);
        }
        checksum += x;

        hex_value = Integer.toHexString(checksum);

        if (hex_value.length() > 4)
        {
            int carry = Integer.parseInt(("" + hex_value.charAt(0)), 16);
            hex_value = hex_value.substring(1, 5);
            checksum = Integer.parseInt(hex_value, 16);
            checksum += carry;
        }
        checksum = generateComplement(checksum);

        return checksum;
    }

    static boolean isIntact(String s, int checksum)
    {
        int generated_checksum = generateChecksum(s);
        generated_checksum = generateComplement(generated_checksum);
        int syndrome = generated_checksum + checksum;
        syndrome = generateComplement(syndrome);
        if (syndrome == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    static int generateComplement(int checksum)
    {
        checksum = Integer.parseInt("FFFF", 16) - checksum;
        return checksum;
    }
}
