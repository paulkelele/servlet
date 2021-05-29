package root;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnexion {

//	Object de connexion à la bdd
	static Connection connection = null;
	
	static{
//		Url de connexion à la bdd
		String url = "jdbc:mysql://localhost:3306/servlet?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Paris";

//		Nom de l'admin à la bdd
		String root = "root";
		
//		MOt de passe admin bdd
		String password = "cerise";
		
		try {

//			Charge la class du driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, root, password);
			
		} catch (ClassNotFoundException| SQLException e) {
			e.printStackTrace();
		}
		
	};//Fin du static
	
	public static Connection getConnexion() {
		 
		return connection;
		
	}
}