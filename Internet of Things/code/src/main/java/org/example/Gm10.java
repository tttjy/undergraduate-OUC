package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Gm10{

    public static void main(String[] args) {
        // 从文件中读取数据
        String filePath = "gm10data.txt";
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

        // 将字符串分割为行
        String[] lineString = strReceive.toString().split("\r");

        // 解析并显示数据
        JFrame frame = new JFrame("数据展示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel tiltle = new JLabel("GM10 返回数据：");
        panel.add(tiltle);
        JLabel datetimeLabel = new JLabel("日期时间：" + lineString[1].substring(6,14) + " " + lineString[2].substring(6, 18));
        panel.add(datetimeLabel);

        for (int i = 3; i < 103; i++) {
            String scientific = lineString[i].substring(21, 34);
            String channel = lineString[i].substring(3, 7);
            double sensorData = Double.parseDouble(scientific);
            int channelNum = Integer.parseInt(channel);

            JLabel dataLabel = new JLabel("通道" + String.format("%2d", channelNum) + "： " + String.format("%.1f", sensorData) + " ℃");
            panel.add(dataLabel);
        }

        //frame.add(panel);
        // 添加滚动条
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        frame.setVisible(true);
        frame.setVisible(true);
    }
}
