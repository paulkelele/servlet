package root;

public class Utilisateur {
	String nom;
	String prenom;

	public Utilisateur() {
	}

	public String getNom() {
		return nom;
	}

	public Utilisateur setNom(String nom) {
		this.nom = nom;
		return this;
	}

	public String getPrenom() {
		return prenom;
	}

	public Utilisateur setPrenom(String prenom) {
		this.prenom = prenom;
		return this;
	}

}
