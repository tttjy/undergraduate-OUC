package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class KLW6000 {
    public static void main(String[] args) {
        // 从文件中读取数据
        String filePath = "KL-W6000.txt";
        StringBuilder strReceive = new StringBuilder();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                strReceive.append(line).append("\r\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            createAndShowGUI(filePath);
        });

    }
    private static void createAndShowGUI(String filePath) {
        JFrame frame = new JFrame("KL-W6000 返回数据：");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        parseKLW6000DataFile(filePath,textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.getContentPane().add(scrollPane);

        frame.setVisible(true);
    }

    private static void parseKLW6000DataFile(String filePath, JTextArea textArea) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] hexData = line.trim().split("\\s+"); // 假设十六进制数据之间是用空格分隔的
                if (hexData.length >= 12) {
                    String deviceAddress = hexData[0];
                    String functionCode = hexData[1];
                    String databytes = hexData[2] ;
                    String register1 = hexData[3] + hexData[4] ;
                    String register2 = hexData[5]+ hexData[6];
                    String register3 = hexData[7] + hexData[8];
                    String register4 = hexData[9] + hexData[10];
                    String crc = hexData[11] + hexData[12];
                    // 将十六进制转换为二进制
                    String binary1 = hexToBinary(hexData[4]);
                    double  binary2 = Integer.parseInt(register3, 16); // 转换为十进制;
                    double  binary3 = Integer.parseInt(register4, 16); // 转换为十进制;
                    // 按位存储数字
                    int[] bits1 = storeBits(binary1);
                    String smoke = bits1[0] == 0? "关":"开";
                    String fire = bits1[1] == 0?"关":"开";
                    String infrared = bits1[2] == 0?"关":"开";
                    String water = bits1[3] == 0?"关":"开";
                    String temp = String.valueOf(binary2/10.0);
                    String wet = String.valueOf(binary3/10.0);
                    displayData(smoke,fire, infrared,water,temp,wet,textArea);
                } else {
                    System.out.println("Invalid data format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayData(String smoke, String fire, String infrared, String water, String temp, String wet, JTextArea textArea) {
        textArea.append("KL-W6000 返回数据：" + System.lineSeparator());
        textArea.append("烟感状态：" + smoke + System.lineSeparator());
        textArea.append("火感状态：" + fire + System.lineSeparator());
        textArea.append("红外状态：" + infrared + System.lineSeparator());
        textArea.append("水浸状态：" + water + System.lineSeparator());
        textArea.append("环境温度：" + temp + " ℃"+System.lineSeparator());
        textArea.append("环境适度：" + wet +"%"+ System.lineSeparator());
        textArea.append(System.lineSeparator()); // 添加一个空行作为分隔
    }

    //十六进制转换为二进制并按位存储
    private static String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int decimal = Integer.parseInt(String.valueOf(c), 16);
            String binaryChunk = Integer.toBinaryString(decimal);
            // 补零到4位
            while (binaryChunk.length() < 4) {
                binaryChunk = "0" + binaryChunk;
            }
            binary.append(binaryChunk);
        }
        return binary.toString();
    }

    private static int[] storeBits(String binary) {
        int[] bits = new int[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            char c = binary.charAt(i);
            int bit = Integer.parseInt(String.valueOf(c));
            bits[i] = bit;
        }
        return bits;
    }
}
