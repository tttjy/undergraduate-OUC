package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import core.game.Game;
import core.player.Player;
import entity.delegate.SocketDelegate;
import entity.delegate.SocketPlayer;
import entity.GameInfo;
import entity.User;

public class HomePage {
	private JFrame frame;
	private JList playerList;
	private JMenuBar mb;
	private JMenu more, edit, function,upload;
	private JMenuItem serverBattle, updateGames,uploadjar;// ����˵�ѡ��
	private JPanel northPanel;
	private JLabel welcome;
	private JScrollPane playersScroll;
	private DefaultListModel listModel;
	private JPanel chessPanel;

	private boolean isConnected = false;
	private Thread messageThread;// ���������Ϣ���߳�
	private Thread heartThread;// ���������������߳�
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private Map<String, User> onLineUsers = new ConcurrentHashMap<String, User>();// ���������û�
	private String username;// ��ǰ�û�
	private SocketDelegate serverPlayer;
	private int gameId;// ��ʼ����Ϊ������ 1
	private boolean isFirst;
	private JScrollPane rightScroll;
	private DefaultTableModel model;
	private JTable table;
	private ArrayList<GameInfo> games;
	private Vector datas;
	private Vector bt;//����б����
	
	public HomePage(User user, Socket socket, String[] allPlayer, ArrayList<GameInfo> games, boolean isFirst) {
		this.username = user.getName();
		this.gameId = 1;
		mb = new JMenuBar();
		more = new JMenu(" �����ս��ʽ");
		serverBattle = new JMenuItem(" �����������ֶ�ս");
		more.add(serverBattle);

		function = new JMenu("����");
		updateGames = new JMenuItem("ˢ������б�");
		function.add(updateGames);

		upload = new JMenu("�ϴ�");
		uploadjar = new JMenuItem("�ϴ�����jar��");
		upload.add(uploadjar);


		mb.add(more);
		mb.add(function);
		mb.add(upload);
		welcome = new JLabel("   ��ӭ " + this.username + "ͬѧ��");
		welcome.setPreferredSize(new Dimension(200, 40));
		northPanel = new JPanel();
		northPanel.setBounds(0, 0, 600, 30);
		northPanel.setLayout(new GridLayout(1, 7));
		northPanel.add(welcome);

		chessPanel = new JPanel();

		JButton connect6 = new JButton("������");
		connect6.setBackground(new Color(176, 224, 230));
		connect6.setBounds(20, 30, 150, 100);
		JButton Gobang = new JButton("������");
		Gobang.setBackground(new Color(64, 224, 208));
		Gobang.setBounds(20, 150, 150, 100);
		JButton go = new JButton("Χ��");
		go.setBackground(new Color(135, 206, 235));
		go.setBounds(20, 270, 150, 100);
		JButton chineseChess = new JButton("�й�����");
		chineseChess.setBackground(new Color(127, 255, 212));
		chineseChess.setBounds(20, 390, 150, 100);
		chessPanel.setLayout(null);
		chessPanel.add(connect6);
		chessPanel.add(Gobang);
		chessPanel.add(go);
		chessPanel.add(chineseChess);

		listModel = new DefaultListModel();
		playerList = new JList(listModel);
		playersScroll = new JScrollPane(playerList);
		playersScroll.setBorder(new TitledBorder("�����б�"));

		JPanel jp = new JPanel();
		JTabbedPane tabbedPane = new JTabbedPane();

		// -----��������--------------
	    bt = new Vector();
		bt.add("GAMEID");
		bt.add("BLACK");
		bt.add("WHITE");
		bt.add("STEP");
		bt.add("WINER");
		bt.add("DATE");
		// -----��������--------------
		this.games = games;
		datas = new Vector();
		for (GameInfo game : games) {
			Vector data = new Vector();
			data.add(game.getGameId());
			data.add(game.getBlackName());
			data.add(game.getWhiteName());
			data.add(game.getStep());
			data.add(game.getWinerName());
			data.add(game.getDate());
			datas.add(data);
		}
		// -----���ģʽ--------------

		// ��ʵ���ӵ���� ����ʹ��Ԫ���ܱ��༭
		model = new DefaultTableModel(datas, bt) {
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;// ����true��ʾ�ܱ༭��false��ʾ���ܱ༭
			}
		};

		table = new JTable(model);
		// ���ݾ��ж���
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ��Ԫ����Ⱦ��
		tcr.setHorizontalAlignment(JLabel.CENTER);// ������ʾ
		table.setDefaultRenderer(Object.class, tcr);// ������Ⱦ��

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // һ��ֻ��ѡ��һ���б�����
		TableColumn idC = table.getColumnModel().getColumn(0);
		idC.setPreferredWidth(6);
		TableColumn stepC = table.getColumnModel().getColumn(3);
		stepC.setPreferredWidth(6);

		rightScroll = new JScrollPane(table);
		rightScroll.setRowHeaderView(new RowHeaderTable(table, 40));
		// rightScroll = new JScrollPane(textArea);
		rightScroll.setBorder(new TitledBorder("Games"));

		tabbedPane.addTab("��ս��Ϣ", rightScroll);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		JComponent panel2 = makeTextPanel("��������");
		tabbedPane.addTab("��������", panel2);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		JComponent panel3 = makeTextPanel("����");
		tabbedPane.addTab("����", panel3);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		jp.add(tabbedPane);

		frame = new JFrame("���Ŀ���ƽ̨");
		// ����JFrame��ͼ�꣺
		// frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("qq.jpg")));
		frame.setLayout(null);
		frame.setJMenuBar(mb);
		chessPanel.setBounds(0, 30, 180, 500);
		jp.setBounds(180, 30, 620, 500);
		// jp.setBackground(new Color(150));
		playersScroll.setBounds(810, 30, 150, 500);

		frame.add(northPanel);
		frame.add(chessPanel);
		frame.add(playersScroll);
		frame.add(jp);
		frame.setSize(1000, 600);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);

		// ��ȡ����������
		this.socket = socket;
		try {
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MessageThread mt = new MessageThread(reader);
		messageThread = new Thread(mt);
		messageThread.start();
		
		HeartThread ht = new HeartThread();
		heartThread = new Thread(ht);
		heartThread.start();
		

		isConnected = true;// �Ѿ���������
		for (String playerName : allPlayer) {
			listModel.addElement(playerName);
		}


		
		
		// ���������ּ��ս
		serverBattle.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				new ServerBattle(allPlayer, socket, mt);
			}
		});
		// ��������б�
		updateGames.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				sendMessage("ALLGAMES@");
			}
		});
		//�ϴ�jar��
		uploadjar.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				new UploadJar();
				sendMessage("UPLOAD");
			}
		});

		// ����б����¼�
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					int colummCount = table.getModel().getColumnCount();// ����
					System.out.println(
							"ѡ�е�gameId:" + table.getModel().getValueAt(table.getSelectedRow(), 0).toString() + " ");
					String gameId = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
					String whiteName = table.getModel().getValueAt(table.getSelectedRow(), 1).toString();
					String blackName = table.getModel().getValueAt(table.getSelectedRow(), 2).toString();
					writer.println("GAMEINFO@" + gameId);
					writer.flush();

					// �½�����
					// SQLDelegate clientPlayer = new SQLDelegate(whiteName,
					// socket,"w");
					// SQLDelegate serverPlayer = new SQLDelegate(blackName,
					// socket,"b");
					SocketPlayer clientPlayer = new SocketPlayer(whiteName, socket, "black", "GameResult");
					SocketPlayer serverPlayer = new SocketPlayer(blackName, socket, "white", "GameResult");
					mt.addObserver(serverPlayer);
					mt.addObserver(clientPlayer);

					// ����������������
					int timeLimit = 30000;
					Game game = new Game(clientPlayer, serverPlayer);
					game.start();
				}
			}
		});
		// �û��б����¼�
		playerList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							// �ҵ��������֣��½�game
							String player = user.getPlayerName();
							try {
								Class clazz = Class.forName(player);
								Player clientPlayer = (Player) clazz.newInstance();

								serverPlayer = new SocketDelegate((String) playerList.getSelectedValue(), socket);
								mt.addObserver(serverPlayer);
								// �����˷��ź� ��ʼ����
								writer.println("PLAYCHESS@" + clientPlayer.name() + "@"
										+ (String) playerList.getSelectedValue() + "@" + isFirst);
								writer.flush();
								// ����������������

								int timeLimit = 30000;
								Game game = null;
								if (isFirst) {
									game = new Game(clientPlayer, serverPlayer);
								} else {
									game = new Game(serverPlayer, clientPlayer);
								}
								game.start();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}).start();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		

		
		
		
		// �رմ���ʱ�¼�
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing   " + isConnected);
				if (isConnected) {
					closeConnection();// �ر�����
				}
				System.exit(0);// �˳�����
			}
		});
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setFont(new Font("����", 1, 50));
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Demo34_JTabbedPane.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}


	/**
	 * ��������������
	 */
	class HeartThread implements Runnable{		
		private Socket socket;	
		@Override
		public void run() {
			try {
				System.out.println("�������߳��Ѿ�����");
				while(true) {
					Thread.sleep(3000000);
					// ����������
					writer.println("biubiubiu");
					writer.flush();
					//socket.sendUrgentData(0xFF); 
				}
			}catch(Exception e) {
				e.printStackTrace();
			}			
		}		
	}
	
	/**
	 * �ͻ��������ر�����
	 */
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// ���ͶϿ����������������
			messageThread.stop();// ֹͣ������Ϣ�߳�
			heartThread.stop();//ֹͣ�������߳�
			// �ͷ���Դ
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			isConnected = true;
			return false;
		}
	}
	/**
	 * �ͻ��˷�����Ϣ����
	 */
	public void sendMessage(String message) {
		System.out.println("sendMessage_" + message);
		writer.println(message);
		writer.flush();
	}

	// ���Ͻ�����Ϣ���߳�
	class MessageThread extends Observable implements Runnable {
		private BufferedReader reader;

		// ������Ϣ�̵߳Ĺ��췽��
		public MessageThread(BufferedReader reader) {
			this.reader = reader;
		}

		// �����Ĺر�����
		public synchronized void closeCon() throws Exception {
			// ����û��б�
			listModel.removeAllElements();
			// �����Ĺر������ͷ���Դ
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// �޸�״̬Ϊ�Ͽ�
		}

		public void run() {
			String message = "";
			while (true) {
				try {
					message = reader.readLine();
					System.out.println(message);
					StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
					String command = stringTokenizer.nextToken();// ����
					if (command.equals("CLOSE"))// �������ѹر�����
					{
						closeCon();// �����Ĺر�����
						JOptionPane.showMessageDialog(frame, "�������رգ�", "����", JOptionPane.ERROR_MESSAGE);
						return;// �����߳�
					} else if (command.equals("ADD")) {// ���û����߸��������б�
						String username = "";
						String userIp = "";
						if ((username = stringTokenizer.nextToken()) != null
								&& (userIp = stringTokenizer.nextToken()) != null) {
							User user = new User(username, userIp);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("DELETE")) {// ���û����߸��������б�
						String username = stringTokenizer.nextToken();
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
					} else if (command.equals("USERLIST")) {// ���������û��б�
						int size = Integer.parseInt(stringTokenizer.nextToken());
						String username = null;
						String userIp = null;
						for (int i = 0; i < size; i++) {
							username = stringTokenizer.nextToken();
							userIp = stringTokenizer.nextToken();
							User user = new User(username, userIp);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("MAX")) {// �����Ѵ�����
						closeCon();// �����Ĺر�����
						JOptionPane.showMessageDialog(frame, "������������������", "����", JOptionPane.ERROR_MESSAGE);
						return;// �����߳�
					} else if (command.equals("SERVERGAME")) {// ���������ս
						System.out.println("SERVERGAME=======================");
					} else if (command.equals("GameInfo")) {// ˢ�¶Ծ��б�
						int count = 0;
						int gameID = Integer.parseInt(stringTokenizer.nextToken());
						for (GameInfo game : games) {
							if (game.getGameId() == gameID) {
								count = 1;
								break;
							}
						}
						System.out.println(gameID);
						if (count == 0) {
							GameInfo gameInfo = new GameInfo();
							gameInfo.setGameId(gameID);
							gameInfo.setBlackName(stringTokenizer.nextToken());
							gameInfo.setWhiteName(stringTokenizer.nextToken());
							gameInfo.setWinerName(stringTokenizer.nextToken());
							gameInfo.setStep(Integer.parseInt(stringTokenizer.nextToken()));
							gameInfo.setDate(stringTokenizer.nextToken());
							gameInfo.setReason(stringTokenizer.nextToken());
							games.add(gameInfo);
							datas = new Vector();
							for (GameInfo game : games) {
								Vector data = new Vector();
								data.add(game.getGameId());
								data.add(game.getBlackName());
								data.add(game.getWhiteName());
								data.add(game.getStep());
								data.add(game.getWinerName());
								data.add(game.getDate());
								datas.add(data);
							}
							model = new DefaultTableModel(datas, bt) {
								public boolean isCellEditable(int row, int column) {
									// TODO Auto-generated method stub
									return false;// ����true��ʾ�ܱ༭��false��ʾ���ܱ༭
								}
							};
							table.setModel(model);
							System.out.println(datas.size());
							frame.repaint();
						}
						
					} else {
						System.out.println("���������");
						System.out.println(message);
						setChanged();
						notifyObservers(message);
					}
					playersScroll.repaint();
				} catch (Exception e) {
					e.printStackTrace();
					//���⣺��ν����̵߳�ͬʱ�رյ�ǰ����
					System.out.println("�����������쳣�����жϷ���");
					JOptionPane.showMessageDialog(frame, "�����������쳣��ֹ����", "����", JOptionPane.ERROR_MESSAGE);
					if(JOptionPane.ERROR_MESSAGE == 0) {
							System.exit(0);// �˳�����
							closeConnection();// �ر�����
							}																
				}
			}
		}
	}
}
