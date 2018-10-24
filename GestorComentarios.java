package ecozar;
import java.sql.*;

public class GestorComentarios
{
	static final int PUBLICADO=1;
	static final int RECHAZADO=-1;
	static final int PENDIENTE=0;
	static final int PENDIENTE_DE_CARTEL=2;
	static final int CORRECTA=1;
	static final int INCORRECTA = -1;
	
	private GestorDB gdb = new GestorDB();
	
	/* Añade el comentario del usuario con idUsuario al cartel con y los datos pasados como parámetros. esNoticia indica si es un comentario
	 * a un reto o a una noticia, y tieneNombre si está realizado por un usuario registrado o no registrado.
	 */
	@SuppressWarnings("finally")
	public boolean subirComentario(String texto, boolean esNoticia, int idPublicacion, String idUsuario, boolean tieneNombre, String nombre) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			if (esNoticia && tieneNombre) {
				String query = "INSERT INTO Comentario (texto, idNoticia, idUsuario, nombre) VALUES (?, ?, ?,?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, texto);
				pstmt.setString(2, Integer.toString(idPublicacion));
				pstmt.setString(3, idUsuario);
				pstmt.setString(4, nombre);
				updated = pstmt.executeUpdate();
			}
			else if (esNoticia) {
				String query = "INSERT INTO Comentario (texto, idNoticia, idUsuario) VALUES (?, ?, ?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, texto);
				pstmt.setString(2, Integer.toString(idPublicacion));
				pstmt.setString(3, idUsuario);
				updated = pstmt.executeUpdate();
			}
			else if (tieneNombre) {
				String query = "INSERT INTO Comentario (texto, idReto, idUsuario, nombre) VALUES (?, ?, ?,?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, texto);
				pstmt.setString(2, Integer.toString(idPublicacion));
				pstmt.setString(3, idUsuario);
				pstmt.setString(4, nombre);
				updated = pstmt.executeUpdate();
			}
			else {
				String query = "INSERT INTO Comentario (texto, idReto, idUsuario) VALUES (?, ?, ?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, texto);
				pstmt.setString(2, Integer.toString(idPublicacion));
				pstmt.setString(3, idUsuario);
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
}