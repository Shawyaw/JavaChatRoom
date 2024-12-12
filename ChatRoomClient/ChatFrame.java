package cn.shawyaw.chatroom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private static ChatFrame instance;

    public static ChatFrame getInstance() {
        if (instance == null) {
            instance = new ChatFrame();
        }
        return instance;
    }

    private ChatFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("聊天室 - " + MainApplication.getInstance().self.name);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // 左侧聊天区域
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // 聊天记录区域
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane chatScroll = new JScrollPane(chatArea);
        leftPanel.add(chatScroll, BorderLayout.CENTER);
        
        // 输入区域
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputField = new JTextField();
        sendButton = new JButton("发送");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);
        
        // 右侧用户列表
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(BorderFactory.createTitledBorder("在线用户"));
        userScroll.setPreferredSize(new Dimension(150, 0));
        
        // 添加到分割面板
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(userScroll);
        splitPane.setDividerLocation(600);
        
        // 设置主面板
        setContentPane(splitPane);
        
        // 添加发送消息的监听器
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        
        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Connect.getInstance().sendQuitRequest();
                System.exit(0);
            }
        });
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            Connect.getInstance().sendChatMessage(message);
            inputField.setText("");
        }
        inputField.requestFocus();
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            // 添加时间戳
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            chatArea.append("[" + timestamp + "] " + message + "\n");
            // 自动滚动到底部
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

//    public void updateUserList(ArrayList<UserInfo> users) {
//        userListModel.clear();
//        for (UserInfo user : users) {
//            userListModel.addElement(user.name);
//        }
//    }

    public void updateUserList(ArrayList<UserInfo> users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            // 添加当前用户（自己）到列表顶部
            String currentUser = MainApplication.getInstance().self.name + " (我)";
            userListModel.addElement(currentUser);
            
            // 添加其他用户
            for (UserInfo user : users) {
                if (!user.name.equals(MainApplication.getInstance().self.name)) {
                    userListModel.addElement(user.name);
                }
            }
            
            // 更新在线人数显示
            setTitle("聊天室 - " + MainApplication.getInstance().self.name + 
                    " (在线: " + userListModel.getSize() + "人)");
        });
    }
    
    public void showSystemMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            chatArea.append("[" + timestamp + "] 系统消息: " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }
    
    public static void launch() {
        SwingUtilities.invokeLater(() -> {
            System.out.println("正在创建聊天窗口"); // 调试信息
            ChatFrame frame = getInstance();
            frame.setVisible(true);
            System.out.println("聊天窗口已显示"); // 调试信息
        });
    }
}
