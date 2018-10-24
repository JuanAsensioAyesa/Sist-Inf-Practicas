package ecozar;
import java.sql.*;
import java.util.LinkedList;

public class GestorNoticias
{
	static final int PUBLICADO=1;
	static final int RECHAZADO=-1;
	static final int PENDIENTE=0;
	static final int PENDIENTE_DE_CARTEL=2;
	static final int CORRECTA=1;
	static final int INCORRECTA = -1;
	
	private GestorDB gdb = new GestorDB();
	
	/* A�ade la noticia del usuario con idUsuario y los datos pasados como par�metros, y devuelve en idNoticia el id de la noticia*/
	@SuppressWarnings("finally")
	public int subirNoticia(String url, int publicar, String imagen, String texto, String titular, String idUsuario, String[] palabras_clave) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int idNoticia = -1;
		try {
			String query = "INSERT INTO Noticia (url, publicado, imagen, texto, titular, idUsuario) VALUES (?, ?, ?,?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, url);
			pstmt.setString(2, Integer.toString(publicar));
			pstmt.setString(3, imagen);
			pstmt.setString(4, texto);
			pstmt.setString(5, titular);
			pstmt.setString(6, idUsuario);
			pstmt.executeUpdate();
			pstmt.close();
			query = "SELECT LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(query);
             // execute the delete statement
			ResultSet rs = pstmt.executeQuery(query);
			rs.first();
			String id = rs.getString(1);
			for (String i:palabras_clave) {
				pstmt.close();
				query = "INSERT INTO Palabra_clave (palabra) VALUES (?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, i);
				pstmt.executeUpdate();
				pstmt.close();
				query = "INSERT INTO Palabra_noticia (palabra, idNoticia) VALUES (?,?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, i);
				pstmt.setString(2, id);
				pstmt.executeUpdate();
			}
			idNoticia=Integer.parseInt(id);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return idNoticia;
		}
	}
	
	
	/* Si publicado == PUBLICADO marca la noticia como publicada. En caso contrario la borra */
	@SuppressWarnings("finally")
	public boolean evaluarNoticia(int id, int publicado) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			if (publicado==PUBLICADO) {
				String query = "UPDATE Noticia SET publicado = ? WHERE idNoticia = ?";

				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, Integer.toString(PUBLICADO));
				pstmt.setString(2, Integer.toString(id));
				updated = pstmt.executeUpdate();
			}
			else {
				String query = "DELETE FROM Noticia WHERE idNoticia = " + id;
				pstmt = conn.prepareStatement(query);
	             // execute the delete statement
	            updated = pstmt.executeUpdate();
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return updated != 0;
		}
	}
	
	
		/*
		Pre: n > 0 AND pedidas=numero de noticias pedidas anteriormente
		Post: Devuelve las n noticias mas recientes
		Devuelve el id de cada noticia para evitar sacar noticias repetidas posteriormente
	*/
	@SuppressWarnings("finally")
	public int  getNoticiaReciente(LinkedList<Integer> ids,LinkedList<String> URL,LinkedList<String> imagen,LinkedList<String> texto,
			LinkedList<String> titular,int n)
			 throws SQLException
	{
		
		int i = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = gdb.connect();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM NOTICIA WHERE PUBLICADO=1 ORDER BY fecha");
			
			while(rs.next()&&i<n) {
				ids.add(rs.getInt("idNoticia"));
				URL.add(rs.getString("URL"));
				imagen.add(rs.getString("imagen"));
				texto.add(rs.getString("texto"));
				titular.add(rs.getString("titular"));
				i++;
				
		
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
		
			conn.close();
			return i;
		}
	}

	/*
		Pre: n>0 AND n<N , N es el número de noticias en la base de datos
		Post:Devuelve la noticia con id=n
	*/
	public void getNoticiaId(LinkedList<String> URL,LinkedList<String> imagen,LinkedList<String> texto,LinkedList<String> titular,int id)
			throws SQLException
	{
		
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = gdb.connect();
			stmt = conn.createStatement();
			String query="SELECT * FROM NOTICIA WHERE PUBLICADO=1 AND idNoticia="+Integer.toString(id);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				URL.add(rs.getString("url"));
				imagen.add(rs.getString("imagen"));
				texto.add(rs.getString("texto"));
				titular.add(rs.getString("titular"));
				
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
		
			conn.close();
			
		}
	}
}