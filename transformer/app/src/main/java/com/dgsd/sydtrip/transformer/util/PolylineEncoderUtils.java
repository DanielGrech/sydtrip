package com.dgsd.sydtrip.transformer.util;

import java.util.List;

public class PolylineEncoderUtils {

    public static String encode(List<Float> coords) {
        if (coords == null || coords.isEmpty())
            return null;

        StringBuilder result = new StringBuilder();

        int plat = 0;
        int plng = 0;
        for (int i = 0, size = coords.size(); i < size; i += 2) {
            int late5 = floor1e5(coords.get(i));
            int lnge5 = floor1e5(coords.get(i + 1));

            int dlat = late5 - plat;
            int dlng = lnge5 - plng;

            plat = late5;
            plng = lnge5;

            result.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng));
        }

        return result.toString();
    }

    private static int floor1e5(float coordinate) {
        return (int) Math.floor(coordinate * 1e5);
    }

    private static String encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return (encodeNumber(sgn_num));
    }

    private static String encodeNumber(int num) {

        StringBuilder encodeString = new StringBuilder();

        while (num >= 0x20) {
            int nextValue = (0x20 | (num & 0x1f)) + 63;
            encodeString.append((char) (nextValue));
            num >>= 5;
        }

        num += 63;
        encodeString.append((char) (num));

        return encodeString.toString();
    }
}
