package ecozar;
import java.sql.*;

public class GestorCarteles
{
	static final int PUBLICADO=1;
	static final int RECHAZADO=-1;
	static final int PENDIENTE=0;
	static final int PENDIENTE_DE_CARTEL=2;
	static final int CORRECTA=1;
	static final int INCORRECTA = -1;
	
	private GestorDB gdb = new GestorDB();
	private GestorNoticias gn = new GestorNoticias();
	private GestorRetos gr = new GestorRetos();
	private GestorPreguntas gp = new GestorPreguntas();
	
	
	/* Añade el cartel del usuario con idUsuario y los datos pasados como parámetros*/
	@SuppressWarnings("finally")
	public boolean subirCartel(String urlNoticia, String imagenNoticia, String textoNoticia, String titularNoticia, String[] palabras_clave, 
			String pregunta, String correcta, String[] incorrectas, String esloganReto, String imagenReto, String textoReto, 
			String titulo, String idUsuario, String tipo, String urlCartel) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			int idNoticia=-1, idReto=-1, idPregunta=-1;
			idPregunta = gp.subirPregunta (idUsuario, PENDIENTE_DE_CARTEL, pregunta, correcta, incorrectas);
			idNoticia = gn.subirNoticia(urlNoticia, PENDIENTE_DE_CARTEL, imagenNoticia, textoNoticia, titularNoticia, idUsuario, palabras_clave);
			idReto = gr.subirReto(esloganReto, PENDIENTE_DE_CARTEL, imagenReto, textoReto, titulo, idUsuario);
			String query = "INSERT INTO Cartel (idNoticia, idPregunta, idReto, tipo, url) VALUES (?,?,?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, Integer.toString(idNoticia));
			pstmt.setString(2, Integer.toString(idPregunta));
			pstmt.setString(3, Integer.toString(idReto));
			pstmt.setString(4, tipo);
			pstmt.setString(5, urlCartel);
			updated = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return updated != 0;
		}
	}
	
	@SuppressWarnings("finally")
	public boolean puntuarCartel(int id, int nota, int publicado) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			if (publicado==PUBLICADO) {
				String query = "UPDATE Cartel SET nota = ? WHERE idNoticia = ?\n"
						+ "UPDATE Noticia SET publicado = ? WHERE idNoticia = ?\n"
						+ "UPDATE Reto SET publicado = ? WHERE idReto = (SELECT idReto FROM Cartel"
						+ " WHERE idNoticia = ?\nUPDATE Pregunta SET publicado = ? WHERE idPregunta"
						+ " = (SELECT idPregunta FROM Cartel WHERE idNoticia = ?)";

				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, Integer.toString(nota));
				pstmt.setString(2, Integer.toString(id));
				pstmt.setString(3, Integer.toString(PUBLICADO));
				pstmt.setString(4, Integer.toString(id));
				pstmt.setString(5, Integer.toString(PUBLICADO));
				pstmt.setString(6, Integer.toString(id));
				pstmt.setString(7, Integer.toString(PUBLICADO));
				pstmt.setString(8, Integer.toString(id));
				updated = pstmt.executeUpdate();
			}
			else {

				String query = ("SELECT * FROM Cartel WHERE idNoticia = " + Integer.toString(id));
				pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery(query);
				String reto = rs.getString("idReto");
				String pregunta = rs.getString("idPreg");
				pstmt.close();
				query = "DELETE FROM Cartel WHERE idNoticia = " + id + "\nDELETE FROM Noticia WHERE idNoticia = " + id +"\n"
						+ "DELETE FROM Reto WHERE idReto = " + reto + "\nDELETE FROM Pregunta WHERE idPreg = " + pregunta;
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
	 * Devuelve la puntuación del cartel con id idNoticia
	 */
	public int verPuntuacionCartel (int idNoticia) throws SQLException
	{
		return idNoticia;	
	}
	
	
	public boolean verCarteles (int tipo_reto, int[] preguntas, int[] noticias, int[] retos, String[] imagenes) throws SQLException
	{
		return false;
	}
	
	
	public boolean obtenerCarteles (String idUsuario, int[] preguntas, int[] noticias, int[] retos, String[] imagenes) throws SQLException
	{
		return false;
	}
}