import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import gnu.io.*;

public class test6 {
    public static Integer dataLength = "2.2190E+02,4.3700E-01,4.8000E+01,4.2230E+03,5.0000E+01,5.0000E-01".length();
    public static Integer sendCount = 10;  // 规定的发送次数
    public static Boolean firstFlag = true;   // 是否第一次发送，第一次发送时将数据清空
    public static String tempResultName = "E:\\桌面\\物联网\\test6TempResult.txt";
    public static String resultName = "E:\\桌面\\物联网\\test6Result.txt";
    public static int realDataLength;  // 真实已接受数据长度
    public static Boolean endFlag = false;  // 是否发送完毕，发送完毕后关闭串口
    public static Boolean cricleFlag = true;  // 循环

    public static void main(String[] args) {
        //开启端口COM，波特率9600
        final SerialPort serialPort = test6.openSerialPort("COM5",9600);
        //向串口发送数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 1;
                while(count++ <= sendCount) {
                    String s = ":NUMeric:NORMal:VALue?\r\n";
                    byte[] bytes = s.getBytes();
                    System.out.println("发送");
                    test6.sendData(serialPort, bytes);//发送数据
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //开启进程每2秒查看是否数据接收完毕
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(cricleFlag){
                    if(endFlag){
                        test6.closeSerialPort(serialPort);
                        cricleFlag = false;
                        List<String> finalList = analyzeGetData(tempResultName);// 解析数据
                        writeToFile(finalList,resultName); // 写入文件
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //设置串口的监听器
        test6.setListenerToSerialPort(serialPort, new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent arg0) {
                if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//数据通知
                    byte[] bytes = test6.readData(serialPort);
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
                        if(realDataLength >= dataLength * sendCount)
                            endFlag = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 解析数据，从103.79E+00,1.0143E+00,105.27E+00,50.001E+00,655.27E+00,0.27E+00到实际结果
     *           电压、电流、功率、频率、耗电量、功率因数
     * resultName 等待解析的数据
     * 返回解析后的结果
     * */
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
            //s = 103.79E+00,1.0143E+00,105.27E+00,50.001E+00,655.27E+00,0.27E+00
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

    /**把finalList内容写到路径为resultName的文件里面
     * finalList  最终的结果文件
     * resultName 存放最终文件的路径
     * */
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

    /**
     * 开启串口
     * serialPortName 串口名称
     * baudRate 波特率
     * 返回 串口对象
     */
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

    /**
     * 关闭串口
     * erialPort 要关闭的串口对象
     */
    public static void closeSerialPort(SerialPort serialPort) {
        if(serialPort != null) {
            serialPort.close();
            System.out.println("关闭串口："+serialPort.getName());
        }
    }

    /**
     * 向串口发送数据
     * serialPort 串口对象
     * data 发送的数据
     */
    public static void sendData(SerialPort serialPort, byte[] data) {
        OutputStream os = null;
        try {
            os = serialPort.getOutputStream();//获得串口的输出流
            os.write(data);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从串口读取数据
     * serialPort 要读取的串口
     * 返回读取的数据
     */
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

    /**
     * 给串口设置监听器
     *  serialPort  开监听器的串口
     * listener 对应监听器
     */
    public static void setListenerToSerialPort(SerialPort serialPort, SerialPortEventListener listener) {
        try {
            //给串口添加事件监听
            serialPort.addEventListener(listener);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        serialPort.notifyOnDataAvailable(true);//串口有数据监听
        serialPort.notifyOnBreakInterrupt(true);//中断事件监听
    }
}