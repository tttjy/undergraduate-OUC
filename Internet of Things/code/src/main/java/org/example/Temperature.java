package org.example;

import gnu.io.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;



public class Temperature {
    private static String commond = "010400000008F1CC";
    private static String open = "030600020001E828";
    private static String close = "030600030001B9E8";
    private static String portName = "COM10"; //本地COM口
    private static CommPortIdentifier commPortIdentifier;
    private static SerialPort serialPort;
    private static OutputStream out;
    private static InputStream in;
    private static int baud = 9600;
    public static String resultName = "E:\\桌面\\物联网\\TJY_5_1.txt";
    public static List<String> list = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        //打开串口
        serialPort = openSerialPort(portName,2000);
        // 打开输入输出流
        in = serialPort.getInputStream();
        // 设置串口参数，波特率9600，8位数据位，1位停止位，无奇偶校验
        serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        // 4. 获取输出流
        out = serialPort.getOutputStream();

//        int count = 1;
        while(true) {
            byte[] bytes = Temperature.hexStringToByteArray(commond);
            System.out.println("发送查询命令");
            out.write(bytes); // 发送数据;
            out.flush(); // 确保所有数据都被发送
            sleep(3000);
            bytes = Temperature.readData(serialPort);
            String res = Temperature.bytes2HexString(bytes);
            System.out.println("收到数据:"+ res);
            int tem = Integer.parseInt(res.substring(9,11)+res.substring(12,14), 16);
            double ntem = tem;
            System.out.println(ntem);
            double T = ntem * 100 / 65535;
            double A = ntem * 16 / 65535;

            T += 8.45;
            System.out.println("温度值："+T);
            System.out.println("电流值："+A);

            String dateTime = new SimpleDateFormat("yyyy-MM-dd, HH: mm: ss").format(new Date());
            list.add(dateTime + "温度值："+T+"℃；电流值："+A+"mA\n");

            if(T> 10){
                System.out.println("风扇打开");
                bytes = Temperature.hexStringToByteArray(open);

            }else{
                System.out.println("风扇关闭");
                bytes = Temperature.hexStringToByteArray(close);
                break;
            }
            System.out.println("发送控制命令");
            out.write(bytes); // 发送数据;
            sleep(2000); // 读取控制命令的响应数据
            Temperature.readData(serialPort);
        }
        byte[] bytes = Temperature.hexStringToByteArray(close);
        System.out.println("结束程序");
        out.write(bytes); // 发送数据;
        sleep(1000); // 读取控制命令的响应数据
        Temperature.readData(serialPort);
        Temperature.closeSerialPort(serialPort);
        writeToFile(list,resultName);

    }
    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase()+" ";
        }
        return r;
    }

    // 将十六进制字符串转换为字节数组
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll("s+", ""); // 去掉字符串中的空格
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("输入的十六进制字符串长度不合法");
        }
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            result[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return result;
    }

    public static byte[] readData(SerialPort serialPort) {
        InputStream is = null;
        byte[] bytes = null;
        try {
            is = serialPort.getInputStream();//获得串口的输入流
            int bufflenth = is.available();//获得数据长度
            while (bufflenth != 0) {
                bytes = new byte[bufflenth];//初始化byte数组
                is.read(bytes);
                bufflenth = is.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
    public static void writeToFile(List<String> finalList, String resultName) {
        FileWriter writer;
        try {
            writer = new FileWriter(resultName);
            writer.write("");//清空原文件内容
            writer.write("解析数据如下：\n");
            for(String string:finalList)
                writer.write(string + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SerialPort openSerialPort(String serialPortName, int baudRate) {
        try {
            //通过端口名称得到端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
            //打开端口，（自定义名字，打开超时时间）
            CommPort commPort = portIdentifier.open(serialPortName, 2222);
            //判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                //设置串口参数（波特率，数据位8，停止位1，校验位无）
                serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                System.out.println("开启串口成功，串口名称："+serialPortName);
                return serialPort;
            }
            else {
                //是其他类型的端口
                throw new NoSuchPortException();
            }
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void closeSerialPort(SerialPort serialPort) {
        if(serialPort != null) {
            serialPort.close();
            System.out.println("关闭串口："+serialPort.getName());
        }
    }
}
