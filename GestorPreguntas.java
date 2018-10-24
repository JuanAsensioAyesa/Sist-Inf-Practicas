package ecozar;
import java.sql.*;
import java.util.LinkedList;


public class GestorPreguntas
{
	static final int PUBLICADO=1;
	static final int RECHAZADO=-1;
	static final int PENDIENTE=0;
	static final int PENDIENTE_DE_CARTEL=2;
	static final int CORRECTA=1;
	static final int INCORRECTA = -1;
	
	private GestorDB gdb = new GestorDB();
	
	
	/* Añade la pregunta del usuario con idUsuario y los datos pasados como parámetros, y devuelve en idPregunta el id de la pregunta*/
	@SuppressWarnings("finally")
	public int subirPregunta(String idUsuario, int publicar, String pregunta, String correcta, String[] incorrectas) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int idPreg = -1;
		try {
			String query = "INSERT INTO Pregunta (publicado, texto, idUsuario) VALUES (?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, Integer.toString(publicar));
			pstmt.setString(2, pregunta);
			pstmt.setString(3, idUsuario);
			pstmt.executeUpdate();
			pstmt.close();
			query = "SELECT LAST_INSERT_ID()";
			pstmt = conn.prepareStatement(query);
             // execute the delete statement
			ResultSet rs = pstmt.executeQuery(query);
			rs.first();
			String id = rs.getString(1);
			pstmt.close();
			query = "INSERT INTO Respuesta (texto) VALUES (?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, correcta);
			pstmt.executeUpdate();
			pstmt.close();
			query = "INSERT INTO Pregunta_respuesta (idPregunta, texto, correcta) VALUES (?,?,?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, correcta);
			pstmt.setString(3, Integer.toString(CORRECTA));
			pstmt.executeUpdate();
			for (String i:incorrectas) {
				pstmt.close();
				query = "INSERT INTO Respuesta (texto) VALUES (?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, i);
				pstmt.executeUpdate();
				pstmt.close();
				query = "INSERT INTO Pregunta_respuesta (idPregunta, texto, correcta) VALUES (?,?,?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, id);
				pstmt.setString(2, i);
				pstmt.setString(3, Integer.toString(INCORRECTA));
				pstmt.executeUpdate();
			}
			idPreg=Integer.parseInt(id);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return idPreg;
		}
	}
	
	
	/* Si publicado == PUBLICADO marca la pregunta como publicada. En caso contrario la borra */
	@SuppressWarnings("finally")
	public boolean evaluarPregunta(int id, int publicado) throws SQLException
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		conn = gdb.connect();
		int updated = 0;
		try {
			if (publicado==PUBLICADO) {
				String query = "UPDATE Pregunta SET publicado = ? WHERE idPreg = ?";

				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, Integer.toString(PUBLICADO));
				pstmt.setString(2, Integer.toString(id));
				updated = pstmt.executeUpdate();
			}
			else {
				String query = "DELETE FROM Pregunta WHERE idPreg = " + id;
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
	public int getPreguntasRespondidas(String id ,LinkedList<String> Preguntas,LinkedList<String> RespuestasCorrectas,
			LinkedList<String> RespuestasElegidas)throws SQLException
	{
		
		int n = 0;
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2=null;
		ResultSet rs = null;
		ResultSet rs2=null;
		try {
			conn = gdb.connect();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			String query="SELECT *  FROM RESPONDE_USUARIO WHERE  idUsuario="+id;
			rs = stmt.executeQuery(query);
			String query2;
			while(rs.next()) {
				int idPregunta=rs.getInt("idPregunta");
				String texto=rs.getString("texto");//Respuesta elegida por el usuario
				query2="SELECT * FROM pregunta_respuesta WHERE correcto="+ CORRECTA +
						" idPregunta="+idPregunta;
				rs2=stmt2.executeQuery(query2);
				String correcta=rs2.getString("texto");//respuesta correcta
				query2="SELECT * FROM PREGUNTA WHERE idPRegunta="+idPregunta;
				rs2=stmt2.executeQuery(query2);
				String pregunta=rs2.getString("texto");
				Preguntas.add(pregunta);
				RespuestasCorrectas.add(correcta);
				RespuestasElegidas.add(texto);
				n++;
				
			}
			
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
		
			conn.close();
			return n;
		}
	}
	
	/*
		Pre:id = identificador del uusario
		Post:Devuelve las preguntas pendientes de responder por el usuario( y el numero de estas)
	 */	
	@SuppressWarnings("finally")
	public int  getPreguntasPendiente(String id ,LinkedList<String>Preguntas,LinkedList<LinkedList<String>> Respuestas) throws SQLException
	{
		int n = 0;
		LinkedList<String > L = new LinkedList<String>();
		String query="Select idPregunta from Pregunta MINUS "
				+ "SELECT idPregunta FROM Responde_usuario WHERE idUsuario="+ id;
		Connection conn=null;
		Statement stmt=null;
		ResultSet rs=null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		Statement stmt3=null;
		Statement stmt2=null;
		
		try {
			conn=gdb.connect();
			stmt = conn.prepareStatement(query);
			rs=stmt.executeQuery(query);
			while(rs.next()) {
				String idPregunta=rs.getString("idPregunta");
				String query2="Select texto from pregunta where idNoticia="+idPregunta;
				stmt2 = conn.prepareStatement(query2);
				rs2=stmt2.executeQuery(query2);
				String pregunta=rs2.getString("texto");
				String query3="Select texto from pregunta_respuesta where idNoticia="+idPregunta;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				while(rs3.next()) 
				{
					L.add(rs3.getString("texto"));
				}
				Preguntas.add(pregunta);
				Respuestas.add(L);
				L.clear();
				n++;
				
			}
			
		}catch(SQLException e) {
			
			System.out.println(e.getMessage());
		}finally {
			conn.close();
			return n;
		}
		
	}
	
}