package root;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.xdevapi.JsonArray;

public class Perso extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Connection connection = SingletonConnexion.getConnexion();
	

//	Object de requete pr?par?e
	 static PreparedStatement preparedStatement = null;

	// Enregistrement du USER
	private static final String SQL_RECORD_MESSAGE = "INSERT INTO message(message,email,timestamp)VALUES(?,?,?)";
	private static final String SQL_READ_MESSAGE = "SELECT * FROM message WHERE email LIKE ?";
	private static final String SQL_DEL_MESSAGE = "DELETE FROM message WHERE id=?";
	
	@Override
	public void init() throws ServletException {
		  
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = (String) request.getSession(false).getAttribute("email");
		  
		String civilite = (String) request.getSession(false).getAttribute("prenom")+" "+(String) request.getSession(false).getAttribute("nom");
		request.setAttribute("civilite", civilite );
		request.getRequestDispatcher("/perso.jsp").forward(request, response);
		 
	};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String decisionParam = request.getParameter("purpose");
		String email = (String) request.getSession(false).getAttribute("email");
		ArrayList<String> messages = new ArrayList<String>( );
		Map<Integer,String> tt = new HashMap<>();
		if(decisionParam.equals("ajoutmessage")){ 
			 
			String message = request.getParameter("message");
			 try {
				preparedStatement = connection.prepareStatement(SQL_RECORD_MESSAGE, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, message);
				preparedStatement.setString(2, email);
				preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				preparedStatement.executeUpdate();
				
				ResultSet rs = preparedStatement.getGeneratedKeys();
				 while ( rs.next() ) {
					 System.out.println(rs.getInt(1));
				 }
				 rs.close();
				 preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String tot= "OK";
			String json = new Gson().toJson(tot);
			 PrintWriter out = response.getWriter();
			 response.setContentType("application/json; charset=UTF-8;");
			 out.print(json);
		 }else if(decisionParam.equals("liremessage")) {
			 
			 try {
				 preparedStatement = connection.prepareStatement(SQL_READ_MESSAGE, Statement.RETURN_GENERATED_KEYS);
				 preparedStatement.setString(1, email);
				 
				 ResultSet rs = preparedStatement.executeQuery();
				 while ( rs.next() ) {
					 int v =rs.getInt("id");
					 messages.add(  String.valueOf(v));
					 messages.add(rs.getString("message"));
					 tt.put(rs.getInt("id"), rs.getString("message"));
				 }
				 rs.close();
				 preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			 if(tt.size() >0) {
				 String json = new Gson().toJson(tt);
				 PrintWriter out = response.getWriter();
				 response.setContentType("application/json; charset=UTF-8;");
				 out.print(json);  
			 }
			 
		 }else if (decisionParam.equals("delmessage")) {
			 
			 String message = request.getParameter("id");
			 int id = Integer.parseInt(message);
			 System.out.println(id);
			  try {
				  preparedStatement = connection.prepareStatement(SQL_DEL_MESSAGE, Statement.RETURN_GENERATED_KEYS);
					 preparedStatement.setInt(1, id);
					 int row = preparedStatement.executeUpdate();
					  
					 preparedStatement.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			 
		 }
	 };

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
}
