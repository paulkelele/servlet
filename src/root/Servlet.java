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

 
public class Servlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Connection connection = SingletonConnexion.getConnexion();

//	Object de requete préparée
	 static PreparedStatement preparedStatement = null;

	// Enregistrement du USER
	private static final String SQL_RECORD_USER = "INSERT INTO user(nom,prenom,email,password,timestamp)VALUES(?,?,?,?,?)";

	@Override
	public void init() throws ServletException {

		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setStatus(response.SC_MOVED_TEMPORARILY);
//		
//		response.setHeader("Location", "https://wallet.bas.esw.esante.gouv.fr/auth?response_type=code&client_id=457888874"
//				+ "&redirect_uri=http://www.testttt.com&scope=openid scope_all&nonce=44444&acr_values=eidas2&state=1222222");
		Utilisateur u = new Utilisateur();
		u.setNom("DUPOND").setPrenom("Pierre");
 
//		 
		request.setAttribute("utilisateur", u);
		request.getRequestDispatcher("/servlet.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	 
		String nom = req.getParameter("nom");
		String prenom = req.getParameter("prenom");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
		
	  
		try {//	Import du Singleton de connexion

			preparedStatement = connection.prepareStatement(SQL_RECORD_USER, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, hashed);
			preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			preparedStatement.executeUpdate();

			ResultSet rs = preparedStatement.getGeneratedKeys();
			while (rs.next()) {
				System.out.println(rs.getInt(1));
			}
			rs.close();
			preparedStatement.close();
		} catch (Exception e) {
			e.getMessage();
		}
		 
		 
		doGet(req, resp);
 	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
}
