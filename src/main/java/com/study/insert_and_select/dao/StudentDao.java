package com.study.insert_and_select.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.study.insert_and_select.config.DBConfig;
import com.study.insert_and_select.entity.Student;

public class StudentDao {
	private static StudentDao instance;
	
	private StudentDao() {}// StudentDao를 싱글톤으로 만들어줌

	public static StudentDao getInstance() {
		if(instance == null) {
			instance = new StudentDao();
		}
		return instance;
	}
	
	
	public Student findStudentByName(String name) { // select 
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null; // select 이기 때문에 result가 있어야함
		Student student = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD); 
			String sql = "select * from student_tb where student_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery(); // 조회
			
			if(rs.next()) { // 결과가 있으면
				student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2)) // 2 = 컬럼 번호
						.age(rs.getInt(3))
						.build();
				}	
			
			} catch (Exception e) {
				e.printStackTrace();
			}  finally { 
				try {
					if(pstmt != null) {
						pstmt.close();
					}
					if(con != null) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	
		return student;
	}
	
	public int saveStudent(Student student) { // insert
		Connection con = null;
		PreparedStatement pstmt = null;
		int successCount = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");		// 데이터베이스 커넥터 드라이브 클래스 이름
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD); 
			String sql = "insert into student_tb(student_name, student_age) values(?,?)"; // ? = 어떤값이 들어올 지 모르는 변수
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getName());
			pstmt.setInt(2, student.getAge());
			successCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally { 
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
		
		return successCount;
	}
	
	public List<Student> getStudentListAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Student> students = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD); 
			String sql = "select * from student_tb"; // 전체조회
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 조회
			
			while(rs.next()) { // next가 false가 될때까지 반복
				Student student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2)) // 2 = 컬럼 번호
						.age(rs.getInt(3))
						.build();
				
				students.add(student); // student 객체를 만들었으니 students에 넣어주기
				}	
			
			} catch (Exception e) {
				e.printStackTrace();
			}  finally { 
				try {
					if(pstmt != null) {
						pstmt.close();
					}
					if(con != null) {
						con.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	
		
		return students; 
	}
}
