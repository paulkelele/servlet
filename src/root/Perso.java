package root;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Perso extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Connection connection = SingletonConnexion.getConnexion();
	

//	Object de requete préparée
	 static PreparedStatement preparedStatement = null;

	// Enregistrement du USER
	private static final String SQL_RECORD_USER = "INSERT INTO message(message,email,timestamp)VALUES(?,?,?)";
	
	@Override
	public void init() throws ServletException {
		  
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = (String) request.getSession(false).getAttribute("email");
		  
		String civilite = (String) request.getSession(false).getAttribute("prenom")+" "+(String) request.getSession(false).getAttribute("nom");
		request.setAttribute("civilite", civilite );
		request.getRequestDispatcher("/perso.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String decisionParam = request.getParameter("purpose");
		if(decisionParam.equals("ajoutmessage")){ 
			String message = request.getParameter("message");
			String email = (String) request.getSession(false).getAttribute("email");
			
			try {
				preparedStatement = connection.prepareStatement(SQL_RECORD_USER, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, message);
				preparedStatement.setString(2, email);
				preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				preparedStatement.executeUpdate();

				ResultSet rs = preparedStatement.getGeneratedKeys();
				 while ( rs.next() ) {
					 System.out.println(rs.getInt(1));
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		doGet(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
}
