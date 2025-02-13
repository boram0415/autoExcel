package io.security.excelauto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Excel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mania"; // MySQL 포트는 기본적으로 3306
    private static final String DB_USER = "root"; // 사용자명
    private static final String DB_PASSWORD = "jupjup1234"; // 비밀번호

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("일일 업무 보고");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // 4행 1열로 수정하여 버튼도 추가

        // JTextArea 생성 및 스타일링
        JTextArea userName = new JTextArea("이름을 입력하세요.");
        JTextArea todayArea = new JTextArea("오늘의 업무를 입력하세요.");
        JTextArea nextArea = new JTextArea("내일의 업무를 입력하세요.");
        JButton saveButton = new JButton("저장");

        // 텍스트 필드 스타일링
        userName.setFont(new Font("Arial", Font.PLAIN, 14));
        todayArea.setFont(new Font("Arial", Font.PLAIN, 14));
        nextArea.setFont(new Font("Arial", Font.PLAIN, 14));

        userName.setForeground(Color.GRAY);
        todayArea.setForeground(Color.GRAY);
        nextArea.setForeground(Color.GRAY);

        userName.setBackground(Color.LIGHT_GRAY);
        todayArea.setBackground(Color.LIGHT_GRAY);
        nextArea.setBackground(Color.LIGHT_GRAY);

        // 텍스트 클릭 시 기존 텍스트를 지우는 MouseListener 추가
        userName.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userName.getText().equals("이름을 입력하세요.")) {
                    userName.setText("");
                    userName.setForeground(Color.BLACK);
                }
            }
        });

        todayArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (todayArea.getText().equals("오늘의 업무를 입력하세요.")) {
                    todayArea.setText("");
                    todayArea.setForeground(Color.BLACK);
                }
            }
        });

        nextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nextArea.getText().equals("내일의 업무를 입력하세요.")) {
                    nextArea.setText("");
                    nextArea.setForeground(Color.BLACK);
                }
            }
        });

        // 각 텍스트 영역을 스크롤패널로 감싸기
        panel.add(new JScrollPane(userName));
        panel.add(new JScrollPane(todayArea));
        panel.add(new JScrollPane(nextArea));
        panel.add(saveButton);

        // 저장 버튼 클릭 시 데이터베이스 저장
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToDatabase(userName.getText(), todayArea.getText(), nextArea.getText());
                System.exit(0);
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    // 데이터베이스에 저장하는 메서드 (예시)
    private static void saveToDatabase(String userName, String today, String next) {
        String sql = "INSERT INTO daily_reports (user_name, today_task, next_task) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, today);
            pstmt.setString(3, next);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "DB 저장 완료!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "DB 저장 중 오류 발생!");
        }
    }
}