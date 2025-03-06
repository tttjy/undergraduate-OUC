package org.example;

import gnu.io.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;



public class DataCollector {
    public static Integer dataLength = "15010000001701031401810102020100F903000080048100F9050001C2".length();
    public static Integer dataLength2 = "15010000000401100008".length();
    static DecimalFormat df = new DecimalFormat("#.0");
    private static String commond = "15010000000601030000000A";
    //实现风机、电灯、水龙头、二氧化碳发生器启动
    private static String open = "15010000000B01100000000810A140FFFFA240FFFFA340FFFFA440FFFF";
    //实现风机、电灯、水龙头、二氧化碳发生器关闭
    private static String close = "15010000000B01100000000810A1400000A2400000A3400000A4400000";
    private static String host = "192.168.2.3"; // 替换为您的服务器IP地址
    private static int port = 10005; // 替换为您的服务器端口号
    private static int baud = 9600;
    public static String resultName = "E:\\桌面\\物联网\\TJY_5_4.txt";
    public static List<String> list = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(host, port);
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        int count = 1;
        while(count++ <= 10) {
            byte[] bytes = DataCollector.hexStringToByteArray(commond);
            System.out.println("发送查询命令");
            out.write(bytes); // 发送数据;
            out.flush(); // 确保所有数据都被发送
            sleep(3000);
            byte[] resbytes = new byte[dataLength];
            in.read(resbytes);
            String res = DataCollector.bytes2HexString(resbytes);
            System.out.println("收到数据:"+ res);
            int tem1 = Integer.parseInt(res.substring(33,35)+res.substring(36,38), 16);
            double temp = tem1;
            System.out.println("温度值:"+temp);
            int tem2 = Integer.parseInt(res.substring(45,47)+res.substring(48,50), 16);
            double wet = tem2;
            System.out.println("湿度值:"+wet);
            int tem3 = Integer.parseInt(res.substring(57,59)+res.substring(60,62), 16);
            double light = tem3;
            System.out.println("照度值:"+light);
            int tem4 = Integer.parseInt(res.substring(69,71)+res.substring(72,74), 16);
            double soil = tem4;
            System.out.println("土壤温度值:"+soil);
            int tem5 = Integer.parseInt(res.substring(81,83)+res.substring(84,86), 16);
            double co2 = tem5;
            System.out.println("CO2浓度值:"+co2);

            String dateTime = new SimpleDateFormat("yyyy-MM-dd, HH: mm: ss").format(new Date());
            list.add(dateTime + "温度值："+df.format(temp/10)+"℃;湿度值："+df.format(wet/10)+"%RH;照度值："+light+"Lux;土壤温度值："+df.format(soil/10)+"℃;CO2浓度："+co2*10+"PPm\n");

        }
        //实现控制
        System.out.println("风机、电灯、水龙头、二氧化碳发生器启动");
        byte[] bytes1 = DataCollector.hexStringToByteArray(open);
        System.out.println("发送查询命令");
        out.write(bytes1); // 发送数据;
        out.flush(); // 确保所有数据都被发送
        sleep(3000);
        byte[] resbytes1 = new byte[dataLength2];
        in.read(resbytes1);
        String res1 = DataCollector.bytes2HexString(resbytes1);
        System.out.println("收到数据:"+ res1);
        System.out.println("风机、电灯、水龙头、二氧化碳发生器关闭");
        byte[] bytes2 = DataCollector.hexStringToByteArray(close);
        System.out.println("发送查询命令");
        out.write(bytes2); // 发送数据;
        out.flush(); // 确保所有数据都被发送
        sleep(3000);
        byte[] resbytes2 = new byte[dataLength2];
        in.read(resbytes2);
        String res2 = DataCollector.bytes2HexString(resbytes1);
        System.out.println("收到数据:"+ res2);

        System.out.println("结束程序");
        sleep(1000); // 读取控制命令的响应数据
        socket.close();
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
