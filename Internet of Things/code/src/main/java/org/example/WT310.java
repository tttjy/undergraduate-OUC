package org.example;
import gnu.io.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;


public class WT310 {
    private static String portName = "COM5"; //本地COM口
    private static CommPortIdentifier commPortIdentifier;
    private static SerialPort serialPort;
    private static OutputStream out;
    private static InputStream in;
    private static int baud = 9600;
    public static Boolean firstFlag = true;   // 是否第一次发送，第一次发送时将数据清空
    public static Boolean endFlag = false;  // 是否发送完毕，发送完毕后关闭串口
    public static Boolean cricleFlag = true;  // 循环
    public static int realDataLength;  // 已接受数据长度
    public static String tempResultName = "E:\\桌面\\物联网\\test2\\TempResult.txt";
    public static String resultName = "E:\\桌面\\物联网\\test2\\Result.txt";
    public static Integer dataLength = "2.2190E+02,4.3700E-01,4.8000E+01,4.2230E+03,5.0000E+01,5.0000E-01".length();


    public static void main(String[] args) throws Exception {
        //打开串口
        commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        serialPort = (SerialPort) commPortIdentifier.open(portName,2000);
        // 注册一个SerialPortEventListener事件来监听串口事件
        //serialPort.addEventListener(new SerialPortListener());

        // 数据可用则触发事件
        serialPort.notifyOnDataAvailable(true);
        // 打开输入输出流
        in = serialPort.getInputStream();
        // 设置串口参数，波特率9600，8位数据位，1位停止位，无奇偶校验
        serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        // 4. 获取输入输出流
        InputStream inputStream = serialPort.getInputStream();
        OutputStream outputStream = serialPort.getOutputStream();
        //5.设置监听器
        WT310.SerialPortToListener(serialPort, new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent arg0) {
                //Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端
                System.out.println("端口有可用数据");
                switch (arg0.getEventType()) {
                    case SerialPortEvent.DATA_AVAILABLE:
                        byte[] bytes = WT310.readData(serialPort);
                        System.out.println("收到:"+ new String(bytes));
                        FileOutputStream fos = null;
                        try {
                            if(firstFlag){//true表示在文件末尾追加
                                fos = new FileOutputStream(tempResultName,false);
                                firstFlag = false;
                            }else
                                fos = new FileOutputStream(tempResultName,true);
                            fos.write(bytes);
                            realDataLength = realDataLength + new String(bytes).length();
                            fos.close();
                            if(realDataLength >= dataLength * 10)
                                endFlag = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case SerialPortEvent.BI:
                        //Break interrupt,通讯中断
                        System.out.println("通讯中断");
                        break;
                    case SerialPortEvent.OE:
                        //Overrun error，溢位错误
                        System.out.println("溢位错误");
                        break;
                    case SerialPortEvent.FE:
                        //Framing error，传帧错误
                        System.out.println("传帧错误");
                        break;
                    case SerialPortEvent.PE:
                        //Parity error，校验错误
                        System.out.println("校验错误");
                        break;
                    case SerialPortEvent.CD:
                        //Carrier detect，载波检测
                        System.out.println("载波检测");
                        break;
                    case SerialPortEvent.CTS:
                        //Clear to send，清除发送
                        System.out.println("清除发送");
                        break;
                    case SerialPortEvent.DSR:
                        // Data set ready，数据设备就绪
                        System.out.println("数据设备就绪");
                        break;
                    case SerialPortEvent.RI:
                        //Ring indicator，响铃指示
                        System.out.println("响铃指示");
                        break;
                    case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                        // Output buffer is empty，输出缓冲区清空
                        System.err.println("监听端口出现了异常");
                        break;
                }
            }
        });
        // 6. 发送数据
        int count = 1;
        while(count++ <= 10) {
            String s = ":NUMeric:NORMal:VALue?\r\n";
            byte[] bytes = s.getBytes();
            System.out.println("发送");
            outputStream.write(bytes); // 发送数据;
            sleep(1000);
        }
        // 7. 刷新输出流,解析写入数据
        outputStream.flush(); // 确保所有数据都被发送
        while(cricleFlag){
            if(endFlag){
                WT310.closeSerialPort(serialPort);
                cricleFlag = false;
                List<String> finalList = analyzeGetData(tempResultName);// 解析数据
                writeToFile(finalList,resultName); // 写入文件
            }
            sleep(1000);
        }
        System.out.println("打开串口成功");

    }

    //设置监听器
    public static void SerialPortToListener(SerialPort serialPort,SerialPortEventListener listener){
        try {
            //给串口添加事件监听
            serialPort.addEventListener(listener);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        serialPort.notifyOnDataAvailable(true);//串口有数据监听
        serialPort.notifyOnBreakInterrupt(true);//中断事件监听

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

    /**
     * 获取系统com端口
     * @return
     */
    public static String getSystemSerialPort(){
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        String portName ="";
        while (portList.hasMoreElements()) {
            portName = portList.nextElement().getName();
            System.out.println(portName);
        }
        return portName;
    }

    //解析数据
    public static List<String> analyzeGetData(String resultName){
        List<String> list = new ArrayList<>();
        File file = new File(resultName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                list.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        // 将list解析之后的数据读入docList中
        List<String> docList = new ArrayList<>();
        for(String s:list){
            //电压、电流、功率、频率、耗电量、功率因数
            String[] split = s.split(",");
            Double[] data = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            for(int i = 0;i < split.length;i++){
                String[] split1 = split[i].split("E");
                Double trail = Double.valueOf(split1[0]);
                Integer dignt = Integer.parseInt(split1[1].substring(1));
                if(split1[1].indexOf("+") != -1){// 有正号
                    while(dignt != 0){
                        trail*=10;
                        dignt--;
                    }
                }else{
                    while(dignt != 0){
                        trail/=10;
                        dignt--;
                    }
                }
                data[i] = trail;
            }
            String dateTime = new SimpleDateFormat("yyyy-MM-dd, HH: mm: ss").format(new Date());
            docList.add(dateTime+" 测量得到数据——电压："+String.format("%.3f",data[0])+"，电流:"+String.format("%.3f",data[1])+
                    "，功率:"+String.format("%.3f",data[2])+
                    "，频率"+String.format("%.3f",data[3])+"，耗电量:"+String.format("%.3f",data[4])+"，功率因数:"+String.format("%.3f",data[5]));
        }
        return docList;
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
            writer.write("wt310解析数据如下：\n");
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
