package poseidon.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

import poseidon.entity.Corsa;

public class CorsaDAO {
	
	public static void creaCorsa(Corsa c) throws SQLException {
		// PRECONDITION: il parametro di input e' un riferimento ad un oggetto valido della classe Corsa
		// POSTCONDITION: la corsa viene inserita nella tabella CORSA
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		connection = DBManager.getInstance().getConnection();

		try { 
			statement = connection.prepareStatement("INSERT INTO CORSA VALUES (?, ?, ?, ?, ?, ?)");
			
			statement.setInt(1, c.getCodiceCorsa());
			statement.setTime(2, java.sql.Time.valueOf(c.getOrarioPartenza()));
			statement.setTime(3, java.sql.Time.valueOf(c.getOrarioArrivo()));
			statement.setString(4, c.getPortoPartenza());
			statement.setString(5, c.getPortoArrivo());
			statement.setDouble(6, c.getPrezzo());
			
			statement.executeUpdate();
			
		} finally {
			if (statement != null) {
				statement.close();
			}
		}		
		
		DBManager.getInstance().closeConnection();
	}
	
	public static List<Corsa> readallCorsa() throws SQLException {
		// PRECONDITIONS: -
		// POSTCONDITIONS: viene restituita una lista contenente tutte le istanze della tabella CORSA

		List<Corsa> lista_corsa = new ArrayList<Corsa>();
		Corsa corsa = null;
		Connection connection = null;
		Statement s = null;
		ResultSet r = null;

		connection = DBManager.getInstance().getConnection();
		s = connection.createStatement();

		r = s.executeQuery("SELECT * FROM CORSA");

		while (r.next()) {
			int codiceCorsa = r.getInt("codiceCorsa");
			LocalTime orarioPartenza = null;
			if (r.getTime("orarioPartenza") != null)
				orarioPartenza = r.getTime("orarioPartenza").toLocalTime();
			LocalTime orarioArrivo = null;
			if (r.getTime("orarioArrivo") != null)
				orarioArrivo = r.getTime("orarioArrivo").toLocalTime();
			String portoPartenza = r.getString("portoPartenza");
			String portoArrivo = r.getString("portoArrivo");
			double prezzo = r.getDouble("prezzo");
			
			corsa = new Corsa(codiceCorsa, orarioPartenza, orarioArrivo, portoPartenza, portoArrivo, prezzo);
			lista_corsa.add(corsa);
		}

		s.close();
		DBManager.getInstance().closeConnection();

		return lista_corsa;
	}


	public static Corsa readCorsa(int codiceCorsa) throws SQLException {
		// PRECONDITIONS: -
		// POSTCONDITIONS: viene restituita un'instanza specifica della tabella CORSA

		Corsa corsa = null;
		Connection connection = null;
		PreparedStatement s = null;
		ResultSet r = null;

		connection = DBManager.getInstance().getConnection();
		s = connection.prepareStatement("SELECT * FROM CORSA WHERE codicecorsa = ?");
		s.setInt(1, codiceCorsa);

		r = s.executeQuery();

		if (r.next()) {
			LocalTime orarioPartenza = null;
			if (r.getTime("orarioPartenza") != null)
				orarioPartenza = r.getTime("orarioPartenza").toLocalTime();
			LocalTime orarioArrivo = null;
			if (r.getTime("orarioArrivo") != null)
				orarioArrivo = r.getTime("orarioArrivo").toLocalTime();
			String portoPartenza = r.getString("portoPartenza");
			String portoArrivo = r.getString("portoArrivo");
			double prezzo = r.getDouble("prezzo");
			
			corsa = new Corsa(codiceCorsa, orarioPartenza, orarioArrivo, portoPartenza, portoArrivo, prezzo);
		}

		s.close();
		DBManager.getInstance().closeConnection();

		return corsa;
	}
	
	public static void deleteCorsa(int codiceCorsa, LocalTime orarioPartenza, LocalTime orarioArrivo, 
			String portoPartenza, String portoArrivo, double prezzo) throws SQLException {
		// PRECONDITION: e' stata aggiunta almeno una corsa all'interno del database.
		// POSTCONDITION: elimina la corsa dalla tabella CORSA. Se non la trova non modifica il database.
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		connection = DBManager.getInstance().getConnection();
		
		try {
			statement = connection.prepareStatement("DELETE FROM CORSA WHERE " + 
					"codiceCorsa = ? AND orarioPartenza = ? AND orarioArrivo = ? AND portoPartenza = ? AND portoArrivo = ? AND prezzo = ?");
			
			Time time_orarioPartenza = Time.valueOf(orarioPartenza);
			Time time_orarioArrivo = Time.valueOf(orarioArrivo);
			
			statement.setInt(1, codiceCorsa);
			statement.setTime(2, time_orarioPartenza);
			statement.setTime(3, time_orarioArrivo);
			statement.setString(4, portoPartenza);
			statement.setString(5, portoArrivo);
			statement.setDouble(6, prezzo);
			statement.executeUpdate();
			
		} finally {
			if(statement != null) {
				statement.close();
			}
		}
		
		DBManager.getInstance().closeConnection();
		
	}

	public static void deleteallCorsa() throws SQLException {
		// PRECONDITION: - 
		// POSTCONDITION: la tabella CORSA e' stata eliminata.
		
		Connection connection = null;
		Statement statement = null;
				
		connection = DBManager.getInstance().getConnection();

		try { 
			statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM CORSA");
		} finally {
			if (statement != null) {
				statement.close();
			}
		}		
				
		DBManager.getInstance().closeConnection();	
		
	}

}
