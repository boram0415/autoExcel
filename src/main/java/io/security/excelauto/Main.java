package io.security.excelauto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mania"; // MySQL 포트는 기본적으로 3306
    private static final String DB_USER = "root"; // 사용자명
    private static final String DB_PASSWORD = "jupjup1234"; // 비밀번호
    private static final String USER_NAME = "김보람"; // 사용자 이름 고정

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("✅  DB 연결 성공!");

            String query = "SELECT ? FROM DUAL"; // 더미 쿼리
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, USER_NAME);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("사용자 이름: " + rs.getString(1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌  DB 연결 실패");
        }
    }
}