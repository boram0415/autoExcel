package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:test"; // DB 접속 URL
    private static final String DB_USER = "test"; // DB 사용자명
    private static final String DB_PASSWORD = "test"; // DB 비밀번호

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        try {
            // 맑은 고딕 폰트 설정
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("맑은 고딕", Font.PLAIN, 14);
            UIManager.put("Label.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("TextField.font", font);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("일일 업무 보고");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // 4행 1열로 수정하여 버튼도 추가

        // JTextArea 생성 및 스타일링
        final JTextArea userName = createTextArea("이름을 입력하세요.");
        final JTextArea todayArea = createTextArea("오늘의 업무를 입력하세요.");
        final JTextArea nextArea = createTextArea("내일의 업무를 입력하세요.");
        JButton saveButton = new JButton("저장");

        // 각 텍스트 영역을 스크롤패널로 감싸기
        panel.add(new JScrollPane(userName));
        panel.add(new JScrollPane(todayArea));
        panel.add(new JScrollPane(nextArea));
        panel.add(saveButton);

        // 저장 버튼 클릭 시 데이터베이스 저장
        saveButton.addActionListener(e -> {
            // 텍스트 필드가 비어 있으면 저장하지 않음
            if (isEmpty(userName, "이름을 입력하세요.") || isEmpty(todayArea, "오늘의 업무를 입력하세요.")
                    || isEmpty(nextArea, "내일의 업무를 입력하세요.")) {
                return;
            }

            // 저장 전에 확인
            int confirm = JOptionPane.showConfirmDialog(frame, "저장하시겠습니까?", "저장 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                saveToDatabase(userName.getText(), todayArea.getText(), nextArea.getText());
                System.exit(0); // 프로그램 종료
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JTextArea createTextArea(String placeholder) {
        JTextArea textArea = new JTextArea(placeholder);
        textArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 한글 지원 폰트로 변경
        textArea.setForeground(Color.GRAY);
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setCaretPosition(0); // 초기 커서 위치 설정

        // 클릭 시 텍스트가 지워지도록 MouseListener 추가
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 기본 텍스트가 있을 경우에만 지움
                if (textArea.getText().equals(placeholder)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                }
            }
        });

        return textArea;
    }

    private static boolean isEmpty(JTextArea textArea, String placeholder) {
        // 텍스트가 비어있거나 기본 텍스트와 같을 경우 경고 메시지 표시
        if (textArea.getText().trim().equals("") || textArea.getText().equals(placeholder)) {
            JOptionPane.showMessageDialog(null, placeholder + "을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    // 데이터베이스에 저장하는 메서드 (예시)
    private static void saveToDatabase(String userName, String today, String next) {
    	String sql = "INSERT INTO reports (user_name, today_task, next_task, created_at) VALUES (?, ?, ?, SYSDATE)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            pstmt.setString(2, today);
            pstmt.setString(3, next);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "완료");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "DB 저장 중 오류 발생!");
        }
    }
}
