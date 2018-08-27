package com.redcity.cureprostate;

import android.content.Intent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by COREIGHT-101 on 2018/4/18.
 */

public class Test {
    private static byte[] data;
    public static void main(String[] args){
//        data = new byte[]{0, 0, 0, 115};
//
//        System.out.println(bytes2Int(data));

//        byte[] bytes = Int2ByteArray((short) 999);
//        System.out.println(Arrays.toString(bytes));


//        LinkedList<Integer> list = new LinkedList<>();
//        for (int i = 0;i<10;i++){
//            list.add(i);
//        }
//
//        System.out.println(list.toString());
//
//        while (list.get(0) != null){
//                list.remove(0);
//                System.out.println(list.toString());
//        }

        byte a = 0x06;
        byte b = (byte) 0x86;
        System.out.println(byteToInt(a));
        System.out.println(byteToInt(b));
        System.out.println(byteToInt(b) - byteToInt(a));

    }

    public static byte intToByte(int x) {
        return (byte) x;
    }
    public static int byteToInt(byte b) {
//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }


    public static short byte2Short(byte[] b){
        return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
    }

    public static int bytes2Int(byte[] src) {
        int value;
        value = (int) ( ((src[0] & 0xFF)<<24)
                |((src[1] & 0xFF)<<16)
                |((src[2] & 0xFF)<<8)
                |(src[3] & 0xFF));
        return value;
    }

    public static byte[] Int2ByteArray(short val) {
        return ByteBuffer.allocate(2).putShort(val).array();
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    public static byte[] shortToByteArray(short s){
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }
}
