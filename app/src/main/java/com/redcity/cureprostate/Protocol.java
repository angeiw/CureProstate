package com.redcity.cureprostate;

import java.nio.ByteBuffer;

/**
 * Created by COREIGHT-101 on 2018/4/18.
 */

public class Protocol {

    /* 显示控制设备通讯指令 */
    public static final byte UTRS_READ_HARDWARE_VER = (byte) 0x81;  // 读硬件版本号
    public static final byte UTRS_READ_BOOTLOADER_VER = (byte) 0x8A;  // 读Bootloader版本号
    public static final byte UTRS_READ_FIRMWARE_VER = (byte) 0x82;  // 读固件版本号
    public static final byte UTRS_WRITE_START_CURE = (byte) 0x83;  // 写开始治疗
    public static final byte UTRS_WRITE_END_CURE = (byte) 0x84;  // 写结束治疗
    public static final byte UTRS_UPLOAD_REALTIME_CURE_DATA = 0x05;  // 上传实时治疗数据，每隔1秒上传一次实时数据
    public static final byte UTRS_WRITE_CURE_PARAMETER = (byte) 0x86;  // 设置治疗参数
    public static final byte UTRS_READ_CURE_PARAMETER = (byte) 0x87;  // 读取治疗参数
    public static final byte UTRS_WRITE_ENTER_DFU_MODE = (byte) 0x8F;  // 进入DFU模式


    public static byte[] startCure(){
        byte[] data = new byte[7];
        data[0] = (byte) 0xFA;
        data[1] = (byte) 0x00;
        data[2] = (byte) 0x01;
        data[3] = UTRS_WRITE_START_CURE;
        data[4] = (byte) 0x00;
        data[5] = (byte) 0xFD;
        data[6] = (byte) 0x3B;
//        data[6] = (byte) 0x3A;
        return data;
    }

    public static byte[] stopCure(){
        byte[] data = new byte[7];
        data[0] = (byte) 0xFA;
        data[1] = (byte) 0x00;
        data[2] = (byte) 0x01;
        data[3] = UTRS_WRITE_END_CURE;
        data[4] = (byte) 0x00;
        data[5] = (byte) 0x64;
        data[6] = (byte) 0xAC;
        return data;
    }

    public static byte[] setParam(short electricity,short voltage,int coulomb,short electricityRate){
        byte[] data = new byte[17];
        data[0] = (byte) 0xFA;
        data[1] = (byte) 0x00;
        data[2] = (byte) 0x01;
        data[3] = UTRS_WRITE_CURE_PARAMETER;
        data[4] = (byte) 0x0A;
        data[5] = Short2ByteArray(voltage)[0];
        data[6] = Short2ByteArray(voltage)[1];
        data[7] = Short2ByteArray(electricity)[0];
        data[8] = Short2ByteArray(electricity)[1];
        data[9] = Int2ByteArray(coulomb)[0];
        data[10] = Int2ByteArray(coulomb)[1];
        data[11] = Int2ByteArray(coulomb)[2];
        data[12] = Int2ByteArray(coulomb)[3];
        data[13] = Short2ByteArray(electricityRate)[0];
        data[14] = Short2ByteArray(electricityRate)[1];
        data[15] = Short2ByteArray(crc16_compute(getBytes(data,1,14)))[0];
        data[16] = Short2ByteArray(crc16_compute(getBytes(data,1,14)))[1];
        return data;
    }

    public static byte[] getParam(){
        byte[] data = new byte[7];
        data[0] = (byte) 0xFA;
        data[1] = (byte) 0x00;
        data[2] = (byte) 0x01;
        data[3] = UTRS_READ_CURE_PARAMETER;
        data[4] = (byte) 0x00;
        data[5] = Short2ByteArray(crc16_compute(getBytes(data,1,4)))[0];
        data[6] = Short2ByteArray(crc16_compute(getBytes(data,1,4)))[1];
        return data;
    }

    /**
     * 将int转成byte[]
     * @param val
     * @return byte[]
     */
    public static byte[] Int2ByteArray(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }
    /**
     * 将short转成byte[]
     * @param val
     * @return byte[]
     */
    public static byte[] Short2ByteArray(short val) {
        return ByteBuffer.allocate(2).putShort(val).array();
    }


    private static short crc16_compute(byte[] p_data) {
        short s;
        short crc = (short) 0xFFFF;
        for (int i = 0; i < p_data.length; i++) {
            crc = (short) (((crc >> 8)&0x00ff) | (crc << 8));
            s = p_data[i];
            crc ^= s & 0x00ff;
            crc ^= (short) (((crc & 0x00FF) >> 4)&0x0fff);
            crc ^= (crc << 8) << 4;
            crc ^= ((crc & 0x00FF) << 4) << 1;
        }

        return crc;
    }

    public static byte[] getBytes(byte[] bytes, int start, int end) {
        if (bytes == null) {
            return null;
        }

        if (start < 0 || start >= bytes.length) {
            return null;
        }

        if (end < 0 || end >= bytes.length) {
            return null;
        }

        if (start > end) {
            return null;
        }

        byte[] newBytes = new byte[end - start + 1];

        for (int i = start; i <= end; i++) {
            newBytes[i - start] = bytes[i];
        }

        return newBytes;
    }

    public static int byteToInt(byte b) {
//Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
}
