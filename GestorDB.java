package ecozar;
import java.sql.*;

public class GestorDB
{
	public Connection connect(){
		String url = "jdbc:mysql://127.0.0.1:3306/sys?user=root&password=sist_inf&serverTimezone=UTC";
		Connection con = null;
		try{   
			con=DriverManager.getConnection(url);    
		} catch(Exception e){ 
			System.out.println(e);
		} 
		return con; 
	}  
	
	
	//CREAR BBDD
	public void create_DB() throws SQLException
	{
		//Sentencia para crear la tabla USUARIO
		String usuario = "CREATE TABLE Usuario (\n" + 
				"	idUsuario		VARCHAR (30)	PRIMARY KEY);";
		
		//Sentencia para crear la tabla PROFESOR
		String profesor = "CREATE TABLE Profesor (\n" + 
				"	idUsuario		VARCHAR (30)	PRIMARY KEY,\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Registrado (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla ALUMNO
		String alumno = "CREATE TABLE Alumno (\n" + 
				"	idUsuario		VARCHAR (30)	PRIMARY KEY,\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Registrado (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla REGISTRADO
		String registrado = "CREATE TABLE Registrado (\n" + 
				"	idUsuario		VARCHAR (30)	PRIMARY KEY,\n" + 
				"	contra 			VARCHAR (30)	NOT NULL,\n" + 
				"	descripcion		VARCHAR (200)	NOT NULL, \n" + 
				"	nombre			VARCHAR (50)	NOT NULL,\n" + 
				"	apellidos		VARCHAR (70)	NOT NULL,\n" + 
				"	email			VARCHAR (50)	NOT NULL,\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla NOTICIA
		String noticia = "CREATE TABLE Noticia (\n" + 
				"	idNoticia		INTEGER		AUTO_INCREMENT,\n" + 
				"	url				VARCHAR (200) 	NOT NULL,\n" + 
				"	publicado		SMALLINT		NOT NULL,\n" + 
				"	imagen			VARCHAR (200)	NOT NULL,\n" + 
				"	texto			VARCHAR (2000)	NOT NULL,\n" + 
				"	titular			VARCHAR (300)	NOT NULL,\n" + 
				"	idUsuario		VARCHAR (30)	NOT NULL,\n" + 
				"	PRIMARY KEY (idNoticia),\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Alumno (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla RETO
		String reto = "CREATE TABLE Reto (\n" + 
				"	idReto		INTEGER		AUTO_INCREMENT,\n" + 
				"	eslogan			VARCHAR (300) 	NOT NULL,\n" + 
				"	publicado		SMALLINT		NOT NULL,\n" + 
				"	imagen			VARCHAR (200)	NOT NULL,\n" + 
				"	texto			VARCHAR (2000)	NOT NULL,\n" + 
				"	titulo			VARCHAR (300)	NOT NULL,\n" + 
				"	idUsuario		VARCHAR (30)	NOT NULL,\n" + 
				"	PRIMARY KEY (idReto),\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Alumno (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla PREGUNTA
		String pregunta = "CREATE TABLE Pregunta (\n" + 
				"	idPregunta		INTEGER		AUTO_INCREMENT,\n" + 
				"	publicado		SMALLINT		NOT NULL,\n" + 
				"	texto			VARCHAR (500)	NOT NULL,\n" + 
				"	idUsuario		VARCHAR (30)	NOT NULL,\n" + 
				"	PRIMARY KEY (idPregunta),\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Alumno (idUsuario)\n" + 
				"							ON DELETE CASCADE ON UPDATE CASCADE);";
		
		//Sentencia para crear la tabla CARTEL
		String cartel = "CREATE TABLE Cartel (\n" + 
				"	idNoticia		INTEGER,\n" + 
				"	idPregunta		INTEGER		NOT NULL,\n" + 
				"	idReto 			INTEGER		NOT NULL,\n" + 
				"	puntuacion		SMALLINT,\n" + 
				"	tipo			VARCHAR(10) NOT NULL,\n" + 
				"	url				VARCHAR(200) NOT NULL,\n" + 
				"	PRIMARY KEY (idNoticia),\n" + 
				"	FOREIGN KEY (idNoticia) REFERENCES Noticia (idNoticia)\n" + 
				"							ON DELETE CASCADE,\n" + 
				"	FOREIGN KEY (idPregunta) REFERENCES Pregunta (idPregunta)\n" + 
				"							ON DELETE CASCADE,\n" + 
				"	FOREIGN KEY (idReto) REFERENCES Reto (idReto)\n" + 
				"							ON DELETE CASCADE);";
		
		//Sentencia para crear la tabla PALABRA_CLAVE
		String keyword = "CREATE TABLE Palabra_clave (\n" + 
				"	palabra		VARCHAR (50)	PRIMARY KEY);";
		
		//Sentencia para crear la tabla PALABRA_NOTICIA
		String new_word = "CREATE TABLE Palabra_noticia (\n" + 
				"	palabra		VARCHAR (50),\n" + 
				"	idNoticia	INTEGER,\n" + 
				"	PRIMARY KEY (palabra, idNoticia),\n" + 
				"	FOREIGN KEY (palabra) REFERENCES Palabra_clave (palabra) ON DELETE CASCADE,\n" + 
				"	FOREIGN KEY (idNoticia) REFERENCES Noticia (idNoticia) ON DELETE CASCADE);";
		
		//Sentencia para crear la tabla RESPUESTA
		String respuesta = "CREATE TABLE Respuesta (\n" + 
				"	texto		VARCHAR (200)	PRIMARY KEY);";
		
		//Sentencia para crear la tabla PREGUNTA_RESPUESTA
		String pregunta_respuesta = "CREATE TABLE Pregunta_respuesta (\n" + 
				"	idPregunta INTEGER,\n" + 
				"	texto 		VARCHAR (200),\n" + 
				"	correcta	SMALLINT 	NOT NULL,\n" + 
				"	PRIMARY KEY (idPregunta, texto),\n" + 
				"	FOREIGN KEY (idPregunta) REFERENCES Pregunta (idPregunta) ON DELETE CASCADE,\n" + 
				"	FOREIGN KEY (texto)		REFERENCES Respuesta (texto) ON DELETE CASCADE);";
		
		//Sentencia para crear la tabla COMENTARIO
		String comentario = "CREATE TABLE Comentario (\n" + 
				"	idComentario INTEGER AUTO_INCREMENT,\n" + 
				"	texto 		VARCHAR (1000)		NOT NULL,\n" + 
				"	idNoticia	INTEGER,\n" + 
				"	idReto		INTEGER,\n" + 
				"	idUsuario 	VARCHAR(30) NOT NULL,\n" + 
				"	nombre		VARCHAR (100),\n" + 
				"	FOREIGN KEY (idNoticia) 	REFERENCES Noticia (idNoticia) ON DELETE CASCADE,\n" + 
				"	FOREIGN KEY (idUsuario)		REFERENCES Usuario (idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,\n" + 
				"	FOREIGN KEY (idReto)		REFERENCES Reto (idReto) ON DELETE CASCADE,\n" + 
				"	PRIMARY KEY (idComentario));";
		
		//Sentencia para crear la tabla REGISTRADO_RETO
		String registrado_reto = "CREATE TABLE Registrado_reto (\n" + 
				"	idUsuario VARCHAR(30),\n" + 
				"	idReto INTEGER,\n" + 
				"	estado SMALLINT NOT NULL,\n" + 
				"	PRIMARY KEY (idUsuario, idReto),\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Registrado (idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,\n" + 
				"	FOREIGN KEY (idReto) REFERENCES Reto (idReto) ON DELETE CASCADE);";
		
		String responde_usuario = "CREATE TABLE Responde_usuario (\n" + 
				"	idPregunta INTEGER,\n" + 
				"	texto VARCHAR(200),\n" + 
				"	idUsuario VARCHAR(30),\n" + 
				"	PRIMARY KEY (idPregunta,texto,idUsuario),\n" + 
				"	FOREIGN KEY (idUsuario) REFERENCES Usuario (idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,\n" + 
				"	FOREIGN KEY (idPregunta) REFERENCES Pregunta_respuesta (idPregunta) ON DELETE CASCADE ON UPDATE CASCADE,\n" + 
				"	FOREIGN KEY (texto) REFERENCES Pregunta_respuesta (texto) ON DELETE CASCADE ON UPDATE CASCADE);";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		String[] queries = {usuario,registrado,profesor,alumno,noticia,reto,pregunta,cartel,keyword,
				new_word,respuesta,pregunta_respuesta,comentario,registrado_reto,responde_usuario};
		try{
			conn = this.connect();
			// crea todas las tablas
			for(int i=0; i < queries.length; i++) {
				pstmt = conn.prepareStatement(queries[i]);
	            pstmt.executeUpdate();
	            pstmt.close();
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			conn.close();
		}
	}
	
	public void delete_DB() throws SQLException
	{
		String query = "DROP TABLE Responde_usuario;\n" + 
				"\n" + 
				"DROP TABLE Registrado_reto;\n" + 
				"\n" + 
				"DROP TABLE Comentario;\n" + 
				"\n" + 
				"DROP TABLE Pregunta_respuesta;\n" + 
				"\n" + 
				"DROP TABLE Respuesta;\n" + 
				"\n" + 
				"DROP TABLE Palabra_noticia;\n" + 
				"\n" + 
				"DROP TABLE Palabra_clave;\n" + 
				"\n" + 
				"DROP TABLE Cartel;\n" + 
				"\n" + 
				"DROP TABLE Pregunta;\n" + 
				"\n" + 
				"DROP TABLE Reto;\n" + 
				"\n" + 
				"DROP TABLE Noticia;\n" + 
				"\n" + 
				"DROP TABLE Alumno;\n" + 
				"\n" + 
				"DROP TABLE Profesor;\n" + 
				"\n" + 
				"DROP TABLE Registrado;\n" + 
				"\n" + 
				"DROP TABLE Usuario;";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = this.connect();
			// crea todas las tablas
			pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
            pstmt.close();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			conn.close();
		}
	}
}