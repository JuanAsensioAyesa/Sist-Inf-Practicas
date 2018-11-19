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
	
	public static class Pregunta{
		
		public Pregunta() {
			
		}
		public Pregunta(int idPregunta, String idUsuario, int publicada, LinkedList<String> respuestasIncorrectas,
				String correcta, String texto) {
			super();
			this.idPregunta = idPregunta;
			this.idUsuario = idUsuario;
			this.publicada = publicada;
			this.respuestasIncorrectas = respuestasIncorrectas;
			this.correcta = correcta;
			this.texto = texto;
		}
		private int idPregunta;
		private String idUsuario;
		private int publicada;
		private LinkedList<String> respuestasIncorrectas;
		private String correcta;
		private String texto;
		public int getIDPregunta() {
			return idPregunta;
		}
		public void setIDPregunta(int idPregunta) {
			this.idPregunta = idPregunta;
		}
		public String getIDUsuario() {
			return idUsuario;
		}
		public void setIDUsuario(String  idUsuario) {
			this.idUsuario = idUsuario;
		}
		public int getPublicada() {
			return publicada;
		}
		public void setPublicada(int publicada) {
			this.publicada = publicada;
		}
		
		public String getTexto() {
			return texto;
		}
		public void setTexto(String texto) {
			this.texto = texto;
		}
		public LinkedList<String> getRespuestasIncorrectas() {
			return respuestasIncorrectas;
		}
		public void setRespuestasIncorrectas(LinkedList<String> respuestasIncorrectas) {
			this.respuestasIncorrectas = respuestasIncorrectas;
		}
		public String getCorrecta() {
			return correcta;
		}
		public void setCorrecta(String correcta) {
			this.correcta = correcta;
		}
	}
	public static  class PreguntaRespondida{
		private int idPregunta;
		private String elegida;
		private String correcta;
		private String pregunta;
		public int getIdPregunta() {
			return idPregunta;
		}
		public void setIdPregunta(int idPregunta) {
			this.idPregunta = idPregunta;
		}
		public String getElegida() {
			return elegida;
		}
		public void setElegida(String elegida) {
			this.elegida = elegida;
		}
		public String getCorrecta() {
			return correcta;
		}
		public void setCorrecta(String correcta) {
			this.correcta = correcta;
		}
		public PreguntaRespondida(int idPregunta, String elegida, String correcta,String pregunta) {
			//super();
			this.idPregunta = idPregunta;
			this.elegida = elegida;
			this.correcta = correcta;
			this.pregunta= pregunta;
		}
		public String getPregunta() {
			return pregunta;
		}
		public void setPregunta(String pregunta) {
			this.pregunta = pregunta;
		}
		
	}
	
	/* Añade la pregunta del usuario con idUsuario y los datos pasados como parámetros, y devuelve en idPregunta el id de la pregunta*/
	@SuppressWarnings("finally")
	public int subirPregunta(Alumno user, Pregunta P) throws SQLException
	{
		String idUsuario=user.getIDUsuario();
		int publicar=P.getPublicada();
		String pregunta=P.getTexto();
		String correcta=P.getCorrecta();
		LinkedList<String> incorrectas=P.getRespuestasIncorrectas();
		//usar el iterador para acceder a las respuestas incorrectas
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
	public boolean evaluarPregunta(Pregunta P) throws SQLException
	{
		int id=P.getIDPregunta();
		int publicado=P.getPublicada();
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
	public int getPreguntasRespondidas(Alumno user ,LinkedList<PreguntaRespondida> L)throws SQLException
	{
		
		int id=user.getID();
		
		
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
				PreguntaRespondida aux= new  PreguntaRespondida(idPregunta,texto,correcta,pregunta);
				L.add(aux);
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
	public int  getPreguntasPendientes(String id ,LinkedList<Pregunta> LP) throws SQLException
	{
		
		
		int n = 0;
		LinkedList<String > L = new LinkedList<String>();
		String query="Select idPregunta WHERE publicado="+PUBLICADO+ "from Pregunta MINUS "
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
				String query2="Select texto from pregunta where idPregunta="+idPregunta;
				stmt2 = conn.prepareStatement(query2);
				rs2=stmt2.executeQuery(query2);
				String pregunta=rs2.getString("texto");
				String query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(INCORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				while(rs3.next()) 
				{
					L.add(rs3.getString("texto"));
				}
				query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(CORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				String correcta= rs3.getString("texto");
				Pregunta aux = new Pregunta(Integer.parseInt(idPregunta),"",PUBLICADO,L,correcta,pregunta);
				L.clear();
				LP.add(aux);
				n++;
				
			}
			
		}catch(SQLException e) {
			
			System.out.println(e.getMessage());
		}finally {
			conn.close();
			return n;
		}
		
		
	}
	@SuppressWarnings("finally")
	public boolean obtenerPregunta(int idPregunta,Pregunta resul) throws SQLException
	{
		LinkedList<String > L = new LinkedList<String>();
		boolean exist=false;
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2=null;
		Statement stmt3=null;
		ResultSet rs = null;
		ResultSet rs2=null;
		ResultSet rs3=null;
		try {
			conn=gdb.connect();
			String query="SELECT * from pregunta where idPregunta="+idPregunta;
			stmt=conn.prepareStatement(query);
			rs=stmt.executeQuery(query);
			if(rs.next()) {
				exist=true;
				
				String pregunta=rs.getString("texto");
				String query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(INCORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				while(rs3.next()) 
				{
					L.add(rs3.getString("texto"));
				}
				query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(CORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				String correcta= rs3.getString("texto");
				resul = new Pregunta(idPregunta,rs.getString("idUsuario"),rs.getInt("publicado"),L,correcta,pregunta);
				L.clear();
			}
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			conn.close();
			return exist;
		}
	}

	@SuppressWarnings("finally")
	public boolean evaluarPregunta(int id, int publicado) throws SQLException{
		int  updated=0;
		Connection conn = null;
		PreparedStatement stmt = null;
		//ResultSet rs=null;
		String query="";
		try {
			conn=gdb.connect();
			if(publicado==PUBLICADO) {
				query="UPDATE Pregunta SET publicado="+PUBLICADO+" WHERE idPRegunta="+id;
				stmt=conn.prepareStatement(query);
			}else if(publicado==RECHAZADO) {
				query="DELETE FROM Pregunta WHERE idPregunta="+id;
				
			}
			stmt=conn.prepareStatement(query);
			updated=stmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			
			
		}finally {
			conn.close();
			stmt.close();
			return updated!=0;
		}
		
	}
	
	@SuppressWarnings("finally")
	public boolean subirPregunta(String idUsuario,Pregunta P) throws SQLException {
		int  updated=0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2=null;
		String query="INSERT INTO PREGUNTA (idPregunta,publicado,texto,idUsuario) VALUES(?,?,?,?)";
		String query2="INSERT INTO PREGUNTA_RESPUESTA (idPregunta,texto,correcta) VALUES(?,?,?)";
		try {
			conn=gdb.connect();
			stmt=conn.prepareStatement(query);
			stmt2=conn.prepareStatement(query2);
			
			stmt.setInt(1, P.getIDPregunta());
			stmt.setInt(2, P.getPublicada());
			stmt.setString(3, P.getTexto());
			stmt.setString(4, idUsuario);
			updated=stmt.executeUpdate();
			
			LinkedList<String> aux=P.getRespuestasIncorrectas();
			
			stmt2.setInt(1, P.getIDPregunta());
			stmt2.setInt(3, INCORRECTA);
			for(String i : aux) {
				stmt2.setString(2,i );
				stmt2.executeUpdate();
			}
			
			stmt2.setInt(3, CORRECTA);
			stmt2.setString(2, P.getCorrecta());
			stmt2.executeUpdate();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			stmt.close();
			conn.close();
			return updated!=0;
		}
		
	}
		
	@SuppressWarnings("finally")
	public boolean responderPregunta(String idUsuario, int idPregunta, String texto) throws SQLException {
		int updated=0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn=gdb.connect();
			String query="INSERT INTO RESPONDE_USUARIO(idPregunta,texto,idUsuario) VALUES(?,?;?)";
			stmt=conn.prepareStatement(query);
			stmt.setString(1,idUsuario);
			stmt.setInt(2, idPregunta);
			stmt.setString(3, texto);
			updated=stmt.executeUpdate();
			
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			conn.close();
			stmt.close();
			return updated!=0;
		}
	}
	
	public boolean  getPreguntaPendiente(String id ,Pregunta P ) throws SQLException
	{
		
		
		int n = 0;
		LinkedList<String > L = new LinkedList<String>();
		String query="Select\n" + 
				"  idPregunta\n" + 
				"from\n" + 
				"  Pregunta P\n" + 
				"  Left join responde_usuario R ON P.publicado = 'Publicado'\n" + 
				"where\n" + 
				"  P.id IS NULL\n" + 
				"LIMIT\n" + 
				"  1";
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
			if(rs.next()) {
				String idPregunta=rs.getString("idPregunta");
				String query2="Select texto from pregunta where idPregunta="+idPregunta;
				stmt2 = conn.prepareStatement(query2);
				rs2=stmt2.executeQuery(query2);
				String pregunta=rs2.getString("texto");
				String query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(INCORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				while(rs3.next()) 
				{
					L.add(rs3.getString("texto"));
				}
				query3="Select texto from pregunta_respuesta where idPregunta="+idPregunta +"correcta=" + Integer.toString(CORRECTA) ;
				stmt3 = conn.prepareStatement(query3);
				rs3=stmt3.executeQuery(query3);
				String correcta= rs3.getString("texto");
				P = new Pregunta(Integer.parseInt(idPregunta),"",PUBLICADO,L,correcta,pregunta);
				
				
				n++;
				
			}
			
		}catch(SQLException e) {
			
			System.out.println(e.getMessage());
		}finally {
			conn.close();
			return n!=0;
		}
		
		
	}

}