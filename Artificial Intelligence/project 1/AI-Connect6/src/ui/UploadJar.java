package ui;

import core.player.AI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.HashSet;

public class UploadJar extends JFrame {

    private String hostIp = "114.215.139.240";
    private int port = 6668;

    private JTextField className;//AI���֣�Jar�����֣�
    private JTextField AIName;//AI����
    private JTextField fileAddress;//ѡ����ļ�����
    private JLabel example;//ʾ������
    private String filepath = null;//�ļ���ַ
    File[] arrfiles = null;//���͵��ļ�

    public UploadJar()  {
        //JFrame jframe = new JFrame("�ϴ��ļ�");// ʵ����һ��JFrame
        this.setTitle("�ϴ��ļ�");
        init();
        // ���ò���Ϊ���Զ�λ
        this.setLayout(null);
        this.setSize(360, 340);
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        this.setLocation((screen_width - this.getWidth()) / 2, (screen_height - this.getHeight()) / 2);
        // �����С���ܸı�
        this.setResizable(false);
        // ������ʾ
        this.setLocationRelativeTo(null);
        //���ô���ɼ�
        this.setVisible(true);
        //close�ķ�ʽ,���ز��ͷŴ���
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * ���������ʼ��
     */
    public void init() {
        Container container = this.getContentPane();
        JLabel CName = new JLabel();
        CName.setBounds(40, 40, 130, 30);
        CName.setText("����������AI���֣�");
        className = new JTextField("�磺Group01");
        className.setBounds(170, 40, 150, 30);

        JLabel AName = new JLabel("�磺baseline.player.AI");
        AName.setBounds(40, 100, 130, 30);
        AName.setText("����������AI������");
        AIName = new JTextField();
        AIName.setBounds(170, 100, 150, 30);

        fileAddress = new JTextField("��ѡ�������ļ�");
        fileAddress.setBounds(40, 165, 180, 30);
        fileAddress.setEnabled(false);
        JButton fileChoose = new JButton("ѡ��");
        fileChoose.setFont(new Font("����", Font.PLAIN, 12));
        fileChoose.setBounds(240, 167, 80, 25);


        example = new JLabel();
        example.setBounds(55, 205, 300, 30);
        example.setForeground(new Color(178, 48, 96));

        JButton confirm = new JButton("ȷ��");
        // �����������ɫ������ָ��
        confirm.setFont(new Font("����", Font.PLAIN, 12));
        confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirm.setBounds(30, 250, 140, 25);
        JButton cancel = new JButton("ȡ��");
        // �����������ɫ������ָ��
        cancel.setFont(new Font("����", Font.PLAIN, 12));
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.setBounds(190, 250, 140, 25);

        // �������������װ��
        container.add(CName);
        container.add(className);
        container.add(AName);
        container.add(AIName);
        container.add(fileAddress);
        container.add(fileChoose);
        container.add(example);
        container.add(confirm);
        container.add(cancel);


        // ��ȡ����ť����¼�
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFrame().dispose();
            }
        });

        //��ѡ���ļ���ť����¼�
        fileChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //����ѡ����
                JFileChooser chooser = new JFileChooser();
                //���ö�ѡ
                chooser.setMultiSelectionEnabled(true);
                //�����ļ�����
                FileNameExtensionFilter filter = new FileNameExtensionFilter("jar",
                        "jar", "txt");
                chooser.setFileFilter(filter);
                //�Ƿ���ļ�ѡ���
                int returnVal = chooser.showOpenDialog(confirm);
                if (returnVal == JFileChooser.APPROVE_OPTION) {   //��������ļ�����
                    filepath = chooser.getSelectedFile().getAbsolutePath();  //��ȡ����·��
                    arrfiles = chooser.getSelectedFiles();    //�õ�ѡ����ļ�
                    if (arrfiles == null || arrfiles.length == 0) {
                        return;
                    }
                    fileAddress.setText(filepath);
                    //System.out.println(arrfiles);
                }
            }
        });

        // ���ύ��ť��ӵ���¼�
        confirm.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                try {

                    eventOnImport(new JButton());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("5555");
                }
            }
        });

    }

    private void eventOnImport(JButton confirm) throws IOException {
        if (className.getText().equals("") || AIName.getText().equals("") || fileAddress.getText().equals("")) {
            example.setText("��������������д��Ϻ����ύ");
            //className.setBackground(new Color(250,128,114));
        } else {
            getIPInfo();
            //����socket����
            System.out.println(hostIp+port);
            Socket fileSocket = new Socket(hostIp, port);// ���ݶ˿ںźͷ�����ip��������
            System.out.println(port + "�˿��Ѿ����ӳɹ�");
            //ʹ��Socket�еķ���getOutputStream����ȡ�����ֽ������OutputStream����
            OutputStream socketOutputStream = fileSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(socketOutputStream);
            //BufferedWriter OutputStream = new BufferedWriter(new OutputStreamWriter(fileSocket.getOutputStream()));
            // �����û�����Ļ�����Ϣ(jar�����֡�AI�������ϴ���ַ)
            writer.println(className.getText() + "@" + AIName.getText() + "@" + fileAddress.getText());
            writer.flush();

            //���շ������Ϣ
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileSocket.getInputStream()));
            String SeverMes = null;
            SeverMes = reader.readLine();// ���տͻ�����Ϣ
            System.out.println(SeverMes);

            if (SeverMes.equals("HADclassname")) {
                example.setText("�Ѿ��и�AI���ƣ�������������ύ");
            } else if (arrfiles != null) {
                FileInputStream input = null;
                String path = System.getProperty("user.dir") + "\\lib";
                try {
                    for (File f : arrfiles) {
                        File dir = new File(path);
                        /** Ŀ���ļ��� * */
                        File[] fs = dir.listFiles();
                        HashSet<String> set = new HashSet<String>();
                        if (fs != null) {
                            for (File file : fs) {
                                set.add(file.getName());
                            }
                        }
                        /** �ж��Ƿ����и��ļ�* */
                        if (set.contains(f.getName())) {
                            JOptionPane.showMessageDialog(new JDialog(),
                                    f.getName() + ":���ļ��Ѵ��ڣ�");
                            return;
                        }
                        input = new FileInputStream(f);
                        byte[] buffer = new byte[1024 * 1024];
                        int len = 0;
                        while (-1 != (len = input.read(buffer))) {
                            socketOutputStream.write(buffer, 0, len);
                            socketOutputStream.flush();
                        }
                        fileSocket.shutdownOutput();
                    }
                    SeverMes = reader.readLine();// ���տͻ�����Ϣ
                    if (SeverMes.equals("UPLOADOK")) {
                        System.out.println(SeverMes);
                        JOptionPane.showMessageDialog(null, "�ϴ��ɹ���", "��ʾ",
                                JOptionPane.INFORMATION_MESSAGE);
                        getFrame().dispose();
                        input.close();
                        fileSocket.close();

                    }
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, "�ϴ�ʧ�ܣ�", "��ʾ",
                            JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "�ϴ�ʧ�ܣ�", "��ʾ",
                            JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }
    }

    public UploadJar getFrame() {
        return this;
    }


    //��ȡip�����ļ�
    private void getIPInfo() {
        // TODO Auto-generated method stub
        BufferedReader reader = null;
        //���������������ļ��ж�ȡ
        try {
            reader = new BufferedReader(new FileReader(new File("IPsettings.txt")));
            //reader = new BufferedReader(new FileReader(new File("../AIContest/IPsettings.txt")));
            this.hostIp = reader.readLine().replace("ip:", "").trim(); //���ֺ���
            //this.port = Integer.parseInt(reader.readLine().replace("port:", "").trim());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }



}



