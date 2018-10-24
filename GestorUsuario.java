package ecozar;
import java.sql.*;


public class GestorUsuario
{
	private GestorDB gdb = new GestorDB();
	
	public class Usuario
	{
		private String userID;
		
		public Usuario(String s) {
			this.userID = s;
		}
		
		public String getID() {
			return this.userID;
		}
	}

	
	@SuppressWarnings("finally")
	public boolean enter_unregistered(String login) throws SQLException
	{
		String query = "INSERT INTO Usuario (idUsuario) VALUES (?)";
		int created = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, login);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return created != 0;
		}
	}
	
	
	@SuppressWarnings("finally")
	public boolean exit_unregistered(String login) throws SQLException
	{
		String query = "DELETE FROM Usuario WHERE idUsuario = ?";
		int deleted = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, login);
			pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return deleted != 0;
		}
	}
	

	//REGISTRARSE
	@SuppressWarnings("finally")
	public boolean sign_inNormalUser(String login, String password, String name, String surname, String email)
	throws SQLException
	{
		String query1 = "INSERT INTO Usuario (idUsuario) VALUES (?)";
		String query2 = "INSERT INTO Registrado (idUsuario,contra,descripcion,nombre,apellidos,email) VALUES (?,?,?,?,?,?)";
		int created = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query1);
			pstmt.setString(1, login);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			pstmt.setString(3, "");
			pstmt.setString(4, name);
			pstmt.setString(5, surname);
			pstmt.setString(6, email);
             // execute the delete statement
            created = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return created != 0;
		}
	}
	
	@SuppressWarnings("finally")
	public boolean sign_inAlumno(String login, String password, String name, String surname, String email)
	throws SQLException
	{
		String query1 = "INSERT INTO Usuario (idUsuario) VALUES (?)";
		String query2 = "INSERT INTO Registrado (idUsuario,contra,descripcion,nombre,apellidos,email) VALUES (?,?,?,?,?,?)";
		String query3 = "INSERT INTO Alumno (idUsuario) VALUES (?)";
		int created = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query1);
			pstmt.setString(1, login);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			pstmt.setString(3, "");
			pstmt.setString(4, name);
			pstmt.setString(5, surname);
			pstmt.setString(6, email);
            created = pstmt.executeUpdate();
            
            pstmt.close();
            pstmt = conn.prepareStatement(query3);
            pstmt.setString(1, login);
            created = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return created != 0;
		}
	}
	
	
	@SuppressWarnings("finally")
	public boolean sign_inProfe(String login, String password, String name, String surname, String email)
	throws SQLException
	{
		String query1 = "INSERT INTO Usuario (idUsuario) VALUES (?)";
		String query2 = "INSERT INTO Registrado (idUsuario,contra,descripcion,nombre,apellidos,email) VALUES (?,?,?,?,?,?)";
		String query3 = "INSERT INTO Profesor (idUsuario) VALUES (?)";
		int created = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query1);
			pstmt.setString(1, login);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, login);
			pstmt.setString(2, password);
			pstmt.setString(3, "");
			pstmt.setString(4, name);
			pstmt.setString(5, surname);
			pstmt.setString(6, email);
            created = pstmt.executeUpdate();
            
            pstmt.close();
            pstmt = conn.prepareStatement(query3);
            pstmt.setString(1, login);
            created = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return created != 0;
		}
	}


	//INICIAR SESION
	@SuppressWarnings("finally")
	public boolean exist_user(String email) throws SQLException 
	{
		Connection con = gdb.connect();
        boolean isAvailable = false;
        String query = "SELECT * FROM Registrado WHERE email = '"+email+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            //resultSet.absolute(int fila) -> Mueve el cursor a la fila [fila] (si no puede devuelve false)
            if(resultSet.absolute(1)){
                isAvailable = true;
            } else {
                isAvailable = false;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return isAvailable;
		}
	}

	@SuppressWarnings("finally")
	public boolean removeUser(String username) throws SQLException 
	{
		String query = "DELETE FROM Usuario WHERE idUsuario = '" + username + "'";
		int deleted = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
             // execute the delete statement
            deleted = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		finally{
			pstmt.close();
			conn.close();
			return deleted != 0;
		}
	}
	

	@SuppressWarnings("finally")
	public boolean updateUserID(String username, String newUserID) throws SQLException
	{
		String query = "UPDATE Usuario SET idUsuario = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newUserID);
			pstmt.setString(2, username);
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
	public boolean updateSurname(String username, String newSurname) throws SQLException
	{
		String query = "UPDATE Registrado SET apellidos = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newSurname);
			pstmt.setString(2, username);
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
	public boolean updateName(String username, String newName) throws SQLException
	{
		String query = "UPDATE Registrado SET nombre = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newName);
			pstmt.setString(2, username);
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
	public boolean updatePassword(String username, String newPassword) throws SQLException
	{
		String query = "UPDATE Registrado SET contra = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newPassword);
			pstmt.setString(2, username);
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
	public boolean updateEmail(String username, String newEmail) throws SQLException
	{
		String query = "UPDATE Registrado SET email = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newEmail);
			pstmt.setString(2, username);
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
	public boolean updateDescription(String username, String newDescription) throws SQLException
	{
		String query = "UPDATE Registrado SET descripcion = ? WHERE idUsuario = ?";
		int updated = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = gdb.connect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, newDescription);
			pstmt.setString(2, username);
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
	public String getName(String userID) throws SQLException {
		Connection con = gdb.connect();
        String query = "SELECT nombre FROM Registrado WHERE idUsuario = '"+userID+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        String name = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            resultSet.first();
            name = resultSet.getString(1);
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return name;
		}
	}
	
	@SuppressWarnings("finally")
	public String getSurname(String userID) throws SQLException {
		Connection con = gdb.connect();
        String query = "SELECT apellidos FROM Registrado WHERE idUsuario = '"+userID+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        String name = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            resultSet.first();
            name = resultSet.getString(1);
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return name;
		}
	}

	@SuppressWarnings("finally")
	public String getPassword(String userID) throws SQLException {
		Connection con = gdb.connect();
        String query = "SELECT contra FROM Registrado WHERE idUsuario = '"+userID+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        String name = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            resultSet.first();
            name = resultSet.getString(1);
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return name;
		}
	}
	
	@SuppressWarnings("finally")
	public String getDescription(String userID) throws SQLException {
		Connection con = gdb.connect();
        String query = "SELECT descripcion FROM Registrado WHERE idUsuario = '"+userID+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        String name = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            resultSet.first();
            name = resultSet.getString(1);
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return name;
		}
	}
	
	@SuppressWarnings("finally")
	public String getEmail(String userID) throws SQLException {
		Connection con = gdb.connect();
        String query = "SELECT email FROM Registrado WHERE idUsuario = '"+userID+"';";
        Statement stmt = null;
        ResultSet resultSet = null;
        String name = null;
        try{
            stmt = con.prepareStatement(query);
            resultSet = stmt.executeQuery(query);
            resultSet.first();
            name = resultSet.getString(1);
        } catch(SQLException e){
            e.printStackTrace();
        }finally {
        	resultSet.close();
        	stmt.close();
        	con.close();
        	return name;
		}
	}
}