package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.study.insert_and_select.dao.StudentDao;
import com.study.insert_and_select.entity.Student;

@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public DataInsertServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder(); // 할당된 기존 메모리 공간에 붙이기 때문에 새로운 데이터 할당이 되지 않고 한번에 붙여 보내기 때문에 StringBuilder 사용
		
		String readData = null;
		
		BufferedReader reader = request.getReader(); // request = 클라이언트->서버, Buffered = 데이터가 들어왔을 때 쌓아서 한번에 처리 

		/*
		 *  "{ // 한번 호출될때 마다 한줄씩
		 *  	"name":"김준일",
		 *  	"age":31
		 *  }"
		 *  
		 */
		
		while((readData = reader.readLine()) != null) { // 한줄씩 Buffered에 담아서 
			builder.append(readData);
		}
		
		System.out.println(builder.toString());
		
//		Student student = Student.builder().name("김준일").age(31).build();
//		
//		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		
//		String studentJson = gson.toJson(student);
//		
//		System.out.println(studentJson);
		
		// 1. Json -> Map : key값을 다 외워야하기 때문에 잘쓰지않음
		Gson gson = new Gson();
		
		Map<String, Object> map = gson.fromJson(builder.toString(), Map.class); // Map의 value는 object타입으로 한다 -> string,int 타입 다 받아야하기 때문
		
		System.out.println(map);
		System.out.println(map.get("name")); // get 메소드 사용
		System.out.println(map.get("age"));
		
		// 2 .Json -> Emtity 객체 : 사용 권장 -> 자료형(Student)이 정해져 있기 때문 (데이터베이스 객체)
		Student student = gson.fromJson(builder.toString(), Student.class); // fromJson = gson을 통해 json을 class형태로 변환
		System.out.println(student);
		System.out.println(student.getName());
		System.out.println(student.getAge());
		
		StudentDao studentDao = StudentDao.getInstance(); // new StudentDao(); = 요청이 들어올때마다 생성되기 때문에 메모리 할당 => StudentDao에서 싱글톤으로 만들어줌
		
		Student findStudent = studentDao.findStudentByName(student.getName()); // 
		
		// config => DBConfig로 빼줌
//		// 같이 써주기 위해서 빼줌
//		// 직접 넣으면 길어져서 변수로 뺌
//		String url = "jdbc:mysql://mysql-db.cfo64mwoijhv.ap-northeast-2.rds.amazonaws.com/db_study"; // DB에 연결
//		String username = "aws";
//		String password = "1q2w3e!!";
		
		// dao로 빼줌
//		// ↓ DB에 넣기 -> pom.xml에 mysql dependency 넣기 필수
//		Connection con = null;				// 데이터베이스 연결
//		String sql = null;					// SQL 쿼리문 작성
//		PreparedStatement pstmt = null;		// SQL 쿼리문 입력
//		int successCount = 0;				// SQL insert, update, delete 실행 결과(성공 횟수)
//		ResultSet rs = null;			
//		Student findStudent = null;
//		
//		
//		try { // 이름 중복 확인
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			
//			con = DriverManager.getConnection(url, username, password); 
//			sql = "select * from student_tb where student_name = ?";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, student.getName());
//			rs = pstmt.executeQuery(); // 조회
//			
//			if(rs.next()) {
//				findStudent = Student.builder()
//						.name(rs.getString(2)) // 2 = 컬럼 번호
//						.age(rs.getInt(3))
//						.build();
//				}	
//			
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally { 
//				try {
//					if(pstmt != null) {
//						pstmt.close();
//					}
//					if(con != null) {
//						con.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//		}
//		
		if(findStudent != null) { // 데이터 조회가 되었을 때
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("errorMessage", "이미 등록된 이름입니다.");
			
			response.setStatus(400);
			response.setContentType("application/json");
			response.getWriter().println(gson.toJson(errorMap));
			return; // 밑에꺼가 실행되면 안되기 때문에 return
			}
		
		int successCount = studentDao.saveStudent(student);
		
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");		// 데이터베이스 커넥터 드라이브 클래스 이름
//			
//			con = DriverManager.getConnection(url, username, password); 
//			sql = "insert into student_tb(student_name, student_age) values(?,?)"; // ? = 어떤값이 들어올 지 모르는 변수
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, student.getName());
//			pstmt.setInt(2, student.getAge());
//			successCount = pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally { 
//			try {
//				if(pstmt != null) {
//					pstmt.close();
//				}
//				if(con != null) {
//					con.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		
		Map<String, Object> responsMap = new HashMap<>();
		responsMap.put("status", 201);
		responsMap.put("data", "응답데이터");
		responsMap.put("successCount", successCount);
		
		response.setStatus(201);
		response.setContentType("application/json");// json인걸 인지시키기
		
		PrintWriter writer = response.getWriter(); // 서버->클리이언트 응답
		writer.println(gson.toJson(responsMap));
	}

}
