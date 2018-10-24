package ecozar;
import java.sql.*;

public class GestorRetos
{
	static final int PUBLICADO=1;
	static final int RECHAZADO=-1;
	static final int PENDIENTE=0;
	static final int PENDIENTE_DE_CARTEL=2;
	static final int CORRECTA=1;
	static final int INCORRECTA = -1;
	
	private GestorDB gdb = new GestorDB();
	
	
	/* Añade el reto del usuario con idUsuario y los datos pasados como parámetros, y devuelve en idReto el id del reto*/
	@SuppressWarnings("finally")
	public int subirReto(String eslogan, int publicar, String imagen, String texto, String titulo, String idUsuario) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int idReto = -1;
		try {
			String query = "INSERT INTO Reto (eslogan, publicado, imagen, texto, titulo, idUsuario) VALUES (?, ?, ?,?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, eslogan);
			pstmt.setString(2, Integer.toString(publicar));
			pstmt.setString(3, imagen);
			pstmt.setString(4, texto);
			pstmt.setString(5, titulo);
			pstmt.setString(6, idUsuario);
			pstmt.executeUpdate();
			pstmt.close();
			query = "SELECT LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(query);
             // execute the delete statement
			ResultSet rs = pstmt.executeQuery(query);
			rs.first();
			String id = rs.getString(1);
			idReto=Integer.parseInt(id);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return idReto;
		}
	}
	
	
	/* Si publicado == PUBLICADO marca el reto como publicado. En caso contrario la borra */
	@SuppressWarnings("finally")
	public boolean evaluarReto(int id, int publicado) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			if (publicado==PUBLICADO) {
				String query = "UPDATE Reto SET publicado = ? WHERE idReto = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, Integer.toString(PUBLICADO));
				pstmt.setString(2, Integer.toString(id));
				updated = pstmt.executeUpdate();
			}
			else {
				String query = "DELETE FROM Reto WHERE idReto = " + id;
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
	
	
	@SuppressWarnings("finally")
	public boolean realizarReto (String idUsuario, int estado, int idReto)throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			String query = "UPDATE Registrado_reto SET estado = ? WHERE idReto = ? AND idUsuario = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, Integer.toString(estado));
			pstmt.setString(2, Integer.toString(idReto));
			pstmt.setString(3,idUsuario);
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
	public boolean realizarNuevoReto (String idUsuario, int estado, int idReto)throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			String query = "INSERT INTO Registrado_reto (estado, idReto, idUsuario) VALUES (?,?,?)";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, Integer.toString(estado));
			pstmt.setString(2, Integer.toString(idReto));
			pstmt.setString(3,idUsuario);
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
	public boolean verRetos (String idUsuario, int estado, int[] lista, int max)throws SQLException
	{	
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int consulted = 0;
		try {
			String query = "SELECT idReto FROM Registrado_reto WHERE estado = ? AND idUsuario = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, Integer.toString(estado));
			pstmt.setString(2, idUsuario);
             // execute the delete statement
			ResultSet rs = pstmt.executeQuery(query);
			int i = 0;
			while (rs.next() && i<max) {
				lista[i]= Integer.parseInt(rs.getString(1));
				i++;
			}
			consulted=1;
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return consulted != 0;
		}
	}
}