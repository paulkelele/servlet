package root;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Connexion extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Connection connection = SingletonConnexion.getConnexion();

//	Object de requete préparée
	 static PreparedStatement preparedStatement = null;

	// Enregistrement du USER
	private static final String SQL_RECORD_USER = "SELECT * FROM user WHERE email LIKE ?";

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		request.getRequestDispatcher("/connexion.jsp").forward(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		try {
			preparedStatement = connection.prepareStatement(SQL_RECORD_USER, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			 while ( rs.next() )
			    {
				  String pw = rs.getString("password") ;
			      
			      if (BCrypt.checkpw(password, pw)) {
			    	  HttpSession session=request.getSession();
			    	  session.setAttribute("id", rs.getInt("id"));
			          session.setAttribute("nom",rs.getString("nom"));
			          session.setAttribute("prenom",rs.getString("prenom"));
			          session.setAttribute("email",rs.getString("email"));
			          
			          response.sendRedirect(request.getContextPath() + "/perso");
			          return;
			    	    
			       }
			      else {
			    		request.setAttribute("resultat", "NOT OK");
			    	}
 			    }
			 rs.close();
			 preparedStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		doGet(request, response);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
