package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.JOptionPane;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.protocol.http.documentvalidation.TextContent;
import org.eclipse.jetty.util.StringUtil;
import org.junit.internal.builders.IgnoredBuilder;

import ObjetosDB.*;


public class Factory {
	
	static private Connection con;
	
	public Factory(){
		try {
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
	
		} catch (Exception ex) {
	
			//JOptionPane.showMessageDialog(null, e, "Error en el registro "+e.getMessage(), JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		
		con = null;
		//...
	
		try {
	
		    con = DriverManager.getConnection(
		            "jdbc:mysql://localhost/grupo88?"
		            + "user=llevaYtrae&password=gil&noAccessToProcedureBodies=true");
		    
		    if (con == null)
		    	throw new SQLException("No se pudo abrir la conexion.");
		    //JOptionPane.showMessageDialog(null,"conectado exitoso");
		    
		    
		    
		} catch (SQLException ex) {
	
			//JOptionPane.showMessageDialog(null, ex, "Error al conectar"+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
			//ex.printStackTrace();
		}
		
	}
	
	public Connection getConnection(){
		return con;
	}
	public Boolean loguearUsuario(String user, String password) {
		Boolean resp = false;
		try
		{
			CallableStatement cmd;
			cmd = con.prepareCall("{call SP_Login(?,?,?)}");
			
			cmd.setString(1, user); 
			
			cmd.setString(2, password);
			
			cmd.registerOutParameter(3, Types.BOOLEAN); 
			cmd.execute();
			
			resp = cmd.getBoolean(3);
			
			
		}
		catch(Exception ex){
			
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		return resp;
	}
	
	/*public Usuario cargarDatosBasicosDe(String nombreUsuario){
		ResultSet rs = null;
		Usuario user = new Usuario();
		ResultSet ingRS = null;
		try {
			CallableStatement cmd = con.prepareCall("{call SP_CargarUsuario(?)}");
			
			cmd.setString(1, nombreUsuario);
			//cmd.registerOutParameter(2, Types.REF_CURSOR);
			rs = cmd.executeQuery();
			if(rs.next()){
				user.setNombre(rs.getString("nombre"));
				user.setApellido(rs.getString("apellido"));
				user.setSexo(rs.getString("sexo").charAt(0));
				user.setAltura(rs.getInt("altura"));
				user.setEmail(rs.getString("mail"));
				user.setComplexion(new Complexiones(rs.getInt("idComplexion"), rs.getString("complexion")));
				user.setDieta(new Dietas(rs.getInt("idDieta"), rs.getString("tipoDieta")));
				user.setRutina(new Rutinas(rs.getInt("idRutina"), rs.getString("rutina")));
				
				cmd = con.prepareCall("{call SP_CargarCondPreexUsuario(?)}");
				
				cmd.setString(1, nombreUsuario);
				rs = cmd.executeQuery();
				
				
				
				while(rs.next()){
					
				cmd = con.prepareCall("{call SP_CargarIngNoComestibleCond(?)}");	
				cmd.setInt(1, rs.getInt("idCondicion"));
				ingRS = cmd.executeQuery();
				
				CondicionesPreexistentes condActual = new CondicionesPreexistentes(rs.getInt("idCondicion"), rs.getString("condicion"));
				
				while(ingRS.next()){
					
					condActual.setIngredienteNoComestible(ingRS.getInt("idIngNoComestible"));
				}
				
				user.setCondicion(condActual);
				
				
				}
			}
			
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		return user;
		
	}
	*/
	/*public Ingredientes cargarIngrediente(int idIngrediente){
		ResultSet rs = null;
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_cargarIngrediente(?)}");
			cmd.setInt(1, idIngrediente);
			rs = cmd.executeQuery();
			Ingredientes ingrediente;
			
			rs.next();
				
			ingrediente = new Ingredientes(rs.getInt("idIngrediente"), rs.getString("nombre"), rs.getInt("caloriasPorcion"), rs.getInt("TipoIngrediente"));
			
			cmd = con.prepareCall("{call SP_condPreexIngrediente(?)}");
			cmd.setInt(1, idIngrediente);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				ingrediente.setCondApta(rs.getInt("idCond"));
			}
			return ingrediente;
		}
		catch(SQLException ex){
			return new Ingredientes(0, ex.getMessage(), 0, 0);
		}
		
	
	}*/
	
	
	
	/*public ArrayList<Receta> cargarRecetasUsuario(String usuario)
	{
		ResultSet rs = null;
		ArrayList<Receta> recetas = new ArrayList<Receta>();
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_RecetasUsuario(?)}");
			cmd.setString(1, usuario);
			
			rs = cmd.executeQuery();
			
			
			while (rs.next()){
				
				recetas.add(new Receta(rs.getInt("idReceta"),
										   rs.getString("nombre"), 
										   new Usuario()/rs.getString("creador")/, 
										   new Dificultades(rs.getInt("idDificultad"), rs.getString("dificultad")),
										   new Temporadas(0, ""),
										   new Ingredientes(0, "", 0, 0)
										   ));
				
				
		}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		
		return recetas;
	}
	*/	
	@Deprecated
	public ArrayList<Receta> cargarRecetasBuscadas(itemsABuscar queBuscar){
		
		ResultSet rs = null;
		ArrayList<Receta> recetas = new ArrayList<Receta>();
		Receta receta;
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_BuscarRecetas(?,?,?,?,?,?,?)}");
			
			cmd.setInt(1, (queBuscar.getDificultad()).getIdDificultad());
			cmd.setInt(2, (queBuscar.getTemporada()).getIdTemporada());
			cmd.setInt(3, (queBuscar.getIngredientePrincipal()).getIdIngrediente());
			cmd.setInt(4, (queBuscar.getGrupoAlimenticio()).getIdGrupoAlim());
			cmd.setInt(5, queBuscar.getCalificacion().getValor());
			cmd.setInt(6, queBuscar.getCaloriasMax());
			cmd.setInt(7, queBuscar.getCaloriasMin());

			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				receta = new Receta(rs.getInt("idReceta"),
						rs.getString("nombre"), 
						new Usuario(),//rs.getString("creador"),
						   new Dificultades(rs.getInt("idDificultad"), rs.getString("dificultad")),
						   new Temporadas(0, "hardcore"),
						   new Ingredientes(0, "", 0, 0));
		
				CallableStatement alim;
				ResultSet AlimRS = null;
				alim = con.prepareCall("{call SP_ObtenerIngredientesReceta(?)}");
				alim.setInt(1,rs.getInt("idReceta"));
				AlimRS = alim.executeQuery();
				
				while(AlimRS.next()){
					receta.agregarIngrediente(new Ingredientes(AlimRS.getInt("idIngrediente"), AlimRS.getString("ingrediente"), AlimRS.getInt("caloriasPorcion"), AlimRS.getInt("tipoIngrediente")),1);
				}
				
				alim = con.prepareCall("{call SP_ObtenerCondimentosReceta(?)}");
				alim.setInt(1,rs.getInt("idReceta"));
				AlimRS = alim.executeQuery();
				
				while(AlimRS.next()){
					receta.agregarCondimento(new Condimentos(AlimRS.getInt("idCondimento"),AlimRS.getString("condimento")));
				}
				
				recetas.add(receta);
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		return recetas;
	}
	
	public List<Complexiones> listaComplexiones(){
		
		ResultSet rs = null;
		List<Complexiones> complexiones = new ArrayList<Complexiones>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.complexion");
					
			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				complexiones.add(new Complexiones(rs.getInt("idComplexion"),rs.getString("complexion")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return complexiones;
	}
	
  public List<PreferenciasAlimenticias> listaPreferenciasAlimenticias(){
		
		ResultSet rs = null;
		List<PreferenciasAlimenticias> preferenciasAlimenticias = new ArrayList<PreferenciasAlimenticias>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.preferenciasAlimenticias");
					
			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				preferenciasAlimenticias.add(new PreferenciasAlimenticias(rs.getInt("idPreferencia"),rs.getString("preferencia")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return preferenciasAlimenticias;
	}
	
	
	public ArrayList<CondicionesPreexistentes> listaCondPreexistentes(){
		
		ResultSet rs = null;
		ArrayList<CondicionesPreexistentes> condPreex = new ArrayList<CondicionesPreexistentes>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.condiciones");
					
			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				condPreex.add(new CondicionesPreexistentes(rs.getInt("idCondicion"), rs.getString("condicion")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return condPreex;
	}
	
	public ArrayList<Dietas> listaDietas(){
		
		ResultSet rs = null;
		ArrayList<Dietas> dietas = new ArrayList<Dietas>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.dietas");
					
			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				dietas.add(new Dietas(rs.getInt("idDieta"),rs.getString("tipoDieta")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return dietas;
	}

	public ArrayList<Rutinas> listaRutinas(){
	
		ResultSet rs = null;
		ArrayList<Rutinas> rutinas = new ArrayList<Rutinas>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.rutinas");
					
			rs = cmd.executeQuery();
			
			while (rs.next()){
				
				rutinas.add(new Rutinas(rs.getInt("idRutina"),rs.getString("rutina")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return rutinas;
	}
	
	public ArrayList<Temporadas> listaTemporadas(){
		
		ResultSet rs = null;
		ArrayList<Temporadas> temporadas = new ArrayList<Temporadas>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.Temporadas");
					
			rs = cmd.executeQuery();
			
			//temporadas.add(new Temporadas(-1,"Todas"));
			
			while (rs.next()){
				
				temporadas.add(new Temporadas(rs.getInt("idTemporada"),rs.getString("nombreTemporada")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return temporadas;
	}
	
	public ArrayList<GruposAlimenticios> listaGruposAlim(){
		
		ResultSet rs = null;
		ArrayList<GruposAlimenticios> gruposAlim = new ArrayList<GruposAlimenticios>();
				
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from grupo88.GrupoAlim");
					
			rs = cmd.executeQuery();
			
			//gruposAlim.add(new GruposAlimenticios(-1,"Todos"));
			
			while (rs.next()){
				
				gruposAlim.add(new GruposAlimenticios(rs.getInt("idGrupoAlim"),rs.getString("descripcion")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return gruposAlim;
	}
	
	public ArrayList<Ingredientes> listaIngredientes(){
		
		ResultSet rs = null;
		ArrayList<Ingredientes> ingredientes = new ArrayList<Ingredientes>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from Grupo88.ingredientes order by nombre");
					
			rs = cmd.executeQuery();
			
			//ingredientes.add(new Ingredientes(-1, "Todos", 0,0));
			
			while (rs.next()){
				
				ingredientes.add(new Ingredientes(rs.getInt("idIngrediente"), rs.getString("nombre"), rs.getInt("caloriasPorcion"), rs.getInt("tipoIngrediente")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return ingredientes;
	}
	
	public ArrayList<Condimentos> listaCondimentos(){
		
		ResultSet rs = null;
		ArrayList<Condimentos> condimentos = new ArrayList<Condimentos>();
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("select * from Grupo88.condimento order by nombre");
					
			rs = cmd.executeQuery();
			
			//condimentos.add(new Ingredientes(-1, "Todos", 0,0));
			
			while (rs.next()){
				
				condimentos.add(new Condimentos(rs.getInt("idCondimento"), rs.getString("nombre")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return condimentos;
	}
	
	
	public ArrayList<Dificultades> listaDificultades(){
		
		ResultSet rs = null;
		ArrayList<Dificultades> dificultades = new ArrayList<Dificultades>();
		
		try
		{
		
			CallableStatement cmd = con.prepareCall("select * from Grupo88.dificultad");
					
			rs = cmd.executeQuery();
			
			//dificultades.add(new Dificultades(-1, "Todas"));
			
			while (rs.next()){
				
				dificultades.add(new Dificultades(rs.getInt("idDificultad"), rs.getString("descripcion")));
		
			}
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return dificultades;
	}
	
	
	public String registrarUsuario(Usuario nvoUsuario){
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_RegistrarUsuario(?,?,?,?,?,?,?,?,?,?,?,?)}");
			
			cmd.setString(1, nvoUsuario.getUsername());
			cmd.setString(2, nvoUsuario.getPassword());
			cmd.setString(3, nvoUsuario.getEmail());
			cmd.setString(4, nvoUsuario.getNombre());
			cmd.setString(5, nvoUsuario.getApellido());
			//cmd.setString(6, nvoUsuario.getFechaNacimiento());
			cmd.setString(7, String.valueOf(nvoUsuario.getSexo()));
			cmd.setInt(8, nvoUsuario.getAltura());
			cmd.setInt(9, (nvoUsuario.getComplexion()).getIdComplexion());
			cmd.setInt(10, (nvoUsuario.getDieta()).getIdDietas());
			cmd.setInt(11, (nvoUsuario.getRutina()).getIdRutina());
			cmd.registerOutParameter(12, Types.VARCHAR);
			
			cmd.executeQuery();
			
			
			CallableStatement cmdCondPreex = con.prepareCall("{call SP_RegistrarCondPreexUsuario(?,?)}");
			cmdCondPreex.setString(1, nvoUsuario.getUsername());
				
			for (CondicionesPreexistentes condPreex : nvoUsuario.getCondiciones()) 
			{	
				cmdCondPreex.setInt(2, condPreex.getIdCondPreex());
				cmdCondPreex.executeQuery();
			}	
			
			return cmd.getString(12);


		
		}
		catch(SQLException ex){
			//JOptionPane.showConfirmDialog(null, ex.getMessage());
			return ex.getMessage();
		}
	}
	
	
	public String modificarPerfil(Usuario user){
		try
		{
			CallableStatement cmd=con.prepareCall("{call SP_modificarPerfil(?,?,?,?,?,?,?,?,?,?)}");
			cmd.setString(1, user.getUsername());
			
			cmd.setString(2, user.getNombre());
			cmd.setString(3, user.getApellido());
			cmd.setString(4, user.getEmail());
			//cmd.setString(5, user.getFechaNacimiento());
			cmd.setString(6, String.valueOf(user.getSexo()));
			cmd.setInt(7, user.getAltura());
			cmd.setInt(8, (user.getComplexion()).getIdComplexion());
			cmd.setInt(9, (user.getDieta()).getIdDietas());
			cmd.setInt(10, (user.getRutina()).getIdRutina());
//			cmd.registerOutParameter(12, Types.VARCHAR);
			
			cmd.executeQuery();
			
			CallableStatement cmdCondPreex = con.prepareCall("{call SP_RegistrarCondPreexUsuario(?,?)}");
			cmdCondPreex.setString(1, user.getUsername());
				
			for (CondicionesPreexistentes condPreex : user.getCondiciones()) 
			{	
				cmdCondPreex.setInt(2, condPreex.getIdCondPreex());
				cmdCondPreex.executeQuery();
			}	
			
			//return cmd.getString(12);
		return "Los cambios han sido actualizados";
		}
		catch(SQLException ex){
			return ex.getMessage();
		
		}
		
	};
	
	
	/*public Receta cargarReceta(int idReceta, Usuario user){
		
		ResultSet rs = null;
		Receta receta;
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_ObtenerReceta(?,?)}");
			
			cmd.setInt(1,idReceta);
			cmd.setString(2, user.getUsername());

			rs = cmd.executeQuery();			
			
			if (rs.next()){
				
				receta = new Receta(rs.getInt("idReceta"),
										   rs.getString("nombre"), 
										   rs.getString("creador"), 
										   new Dificultades(rs.getInt("idDificultad"), rs.getString("dificultad")),
										   new Temporadas(rs.getInt("idTemporada"), rs.getString("nombreTemporada")),
										   new Ingredientes(rs.getInt("idIngrediente"), rs.getString("IngPrincipal"),rs.getInt("caloriasPorcion"),rs.getInt("tipoIngrediente")),
										   rs.getString("descripcion"),
										   rs.getInt("calificacion"));
				receta.setFotoPrincipal(rs.getBytes("foto"));
				
			}else{ receta = new Receta(-1, "Error en el if", "", null,null,null,"",0);}
			
			cmd = con.prepareCall("{call SP_ObtenerIngredientesReceta(?)}");
			cmd.setInt(1,idReceta);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				receta.agregarIngrediente(new Ingredientes(rs.getInt("idIngrediente"), rs.getString("ingrediente"), rs.getInt("caloriasPorcion"), rs.getInt("tipoIngrediente")));
			}
			
			cmd = con.prepareCall("{call SP_ObtenerCondimentosReceta(?)}");
			cmd.setInt(1,idReceta);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				receta.agregarCondimento(new Condimentos(rs.getInt("idCondimento"),rs.getString("condimento")));
			}
			
			cmd = con.prepareCall("{call SP_ObtenerPasosReceta(?)}");
			cmd.setInt(1,idReceta);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				Pasos nuevoPaso = new Pasos(rs.getInt("numeroPaso") ,rs.getString("descripcion"));
				nuevoPaso.setImagen(rs.getBytes("foto"));
				receta.agregarPaso(nuevoPaso);
			}
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());	
			receta = new Receta(-1, ex.getMessage(), "", null,null,null,"",0);
		}
		return receta;
	}
	*/
	public boolean agregarAHistorial(int idReceta, Usuario user){
		
		ResultSet rs = null;
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_agregarAHistorial(?,?)}");
			cmd.setInt(1,idReceta);
			cmd.setString(2, user.getUsername());
			
			cmd.executeQuery();
			return true;
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());
			return false;}
		
	}
	
	public boolean calUltimaConfirmacion(int idReceta, Usuario user, int calificacion){
		
		ResultSet rs = null;
		
		try
		{
			
			CallableStatement cmd = con.prepareCall("{call SP_calUltimaConfirmacion(?,?,?)}");
			cmd.setInt(1,idReceta);
			cmd.setString(2, user.getUsername());
			cmd.setInt(3, calificacion);
			
			cmd.executeQuery();
			return true;
		}
		catch(SQLException ex){
			//JOptionPane.showMessageDialog(null, ex.getMessage());
			return false;}
		
	}
	
//	public Set<Grupo> cargarGrupos(String username){
//		
//		ResultSet rs = null;
//		Set<Grupo> grupos = new HashSet<Grupo>();
//		CallableStatement cmd;
//		
//		try
//		{
//			if(username != "")
//			{
//				cmd = con.prepareCall("{call SP_cargarGruposUsuario(?)}");
//				cmd.setString(1,username);
//			}
//			else
//			{
//				cmd = con.prepareCall("{call SP_cargarGrupos()}");
//			}
//			
//			rs = cmd.executeQuery();
//			while(rs.next()){
//				grupos.add(new Grupo(rs.getInt("idGrupo"),rs.getString("nombreGrupo"),rs.getString("creador"),rs.getString("detalle")));
//				
//			}
//			
//		
//		}
//		catch(SQLException ex){
//			JOptionPane.showMessageDialog(null, ex.getMessage());
//			}
//		
//		return grupos;
//	}
	
	public boolean entrarGrupo (String username, int idGrupo){
		
		CallableStatement cmd;
		
		try
		{
				cmd = con.prepareCall("{call SP_entrarGrupo(?,?)}");
				cmd.setString(1,username);
				cmd.setInt(2,idGrupo);
				cmd.executeQuery();
		
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return false;
			}
		
		return true;
	}
	
	public boolean salirGrupo (String username, int idGrupo){
		
		CallableStatement cmd;
		
		try
		{
				cmd = con.prepareCall("{call SP_salirGrupo(?,?)}");
				cmd.setString(1,username);
				cmd.setInt(2,idGrupo);
				cmd.executeQuery();
		
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return false;
			}
		
		return true;
	}

//	public int agregarNuevoGrupo (Grupo grupo){
//		CallableStatement cmd;
//		ResultSet rs = null;
//	
//		try{
//			cmd = con.prepareCall("{call SP_agregarNuevoGrupo(?,?,?)}");
//			cmd.setString(1,grupo.getNombre());
//			cmd.setString(2,grupo.getCreador());
//			cmd.setString(3,grupo.getDetalle());
//			rs = cmd.executeQuery();
//		
//			if(rs.next()){
//				return rs.getInt("idGrupo");
//			}
//		}catch(SQLException ex){
//			//JOptionPane.showMessageDialog(null, ex.getMessage());
//			return -1;
//		}
//		return -1;
//	}
	
	/*public Estadisticas obtenerEstadisticas(){
		
		ResultSet rs = null;
		Estadisticas est = new Estadisticas();
		CallableStatement cmd;
		
		try{
			//Se obtienen top Consultas Hombre Semanal
			cmd = con.prepareCall("{Call SP_TOPRecetas(?,?)}");
			cmd.setString(1, "M");
			cmd.setInt(2, 7);
			rs = cmd.executeQuery();
			
			while (rs.next()){
				est.agregarTopRecetasHombreSemana(rs.getString("nombre"));
			}
			cmd.close();
			//Se obtienen top Consultas Hombre Mes
			cmd = con.prepareCall("{Call SP_TOPRecetas(?,?)}");
			cmd.setString(1, "M");
			cmd.setInt(2, 31);
			rs = cmd.executeQuery();
			
			while (rs.next()){
				est.agregarTopRecetasHombreMes(rs.getString("nombre"));
			}
			
			//Se obtienen top Consultas Mujer Semanal
			cmd = con.prepareCall("{Call SP_TOPRecetas(?,?)}");
			cmd.setString(1, "F");
			cmd.setInt(2, 7);
			rs = cmd.executeQuery();
			
			while (rs.next()){
				est.agregarTopRecetasMujerSemana(rs.getString("nombre"));
			}
			
			//Se obtienen top Consultas Hombre Mes
			cmd = con.prepareCall("{Call SP_TOPRecetas(?,?)}");
			cmd.setString(1, "F");
			cmd.setInt(2, 31);
			rs = cmd.executeQuery();
			
			while (rs.next()){
				est.agregarTopRecetasMujerMes(rs.getString("nombre"));
			}
			
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());	
		}
		
		
		return est;
	}*/
	
	public ArrayList<Consulta> obtenerConsultasSegunDificultad(int dias){
		ArrayList<Consulta> list = new ArrayList<Consulta>();
		
		CallableStatement cmd;
		ResultSet rs = null;
		
		try{
			cmd = con.prepareCall("{call SP_consultasSegunNievelDeDificultad(?)}");
			cmd.setInt(1, dias);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				list.add(new Consulta(rs.getInt("idDificultad"), rs.getString("descripcion"), rs.getInt("cantidad")));
			}
			
		}
		catch (SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		
		return list;
	}
	
	public ArrayList<Consulta> obtenerTopRecetasMasConsultadas(int dias){
		ArrayList<Consulta> list = new ArrayList<Consulta>();
		
		CallableStatement cmd;
		ResultSet rs = null;
		
		try{
			cmd = con.prepareCall("{call SP_recetasMasConsultadas(?)}");
			cmd.setInt(1, dias);
			rs = cmd.executeQuery();
			
			while(rs.next()){
				/*
				list.add(new RecetaU(rs.getInt("idReceta"),
									rs.getString("nombre"),
									rs.getString("creador"),
									new Dificultades(rs.getInt("idDificultad"), rs.getString("Dificultad")),
									new Temporadas(rs.getInt("IdTemporada"), rs.getString("temporada")),
									new Ingredientes(rs.getInt("IdIngredientePrincipal"), rs.getString("IngredientePrincipal"), rs.getInt("CaloriasIngPrincipal"), rs.getInt("TipoIngPrincipal")),
									rs.getString("descripcion"),
									rs.getInt("puntajeTotal")/rs.getInt("vecesCalificada")));*/
				list.add(new Consulta(rs.getInt("idReceta"), rs.getString("nombre"), rs.getInt("cantidad")));
			}
			
		}
		catch (SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		
		return list;
	}
	
	
	

	public boolean grupoTieneReceta(int idGrupo,int idReceta){
	CallableStatement cmd;
	ResultSet rs = null;
	
	try
		{
		cmd = con.prepareCall("{call SP_grupoTieneReceta(?,?)}");
		cmd.setInt(1,idGrupo);
		cmd.setInt(2,idReceta);
		rs = cmd.executeQuery();
		
		if(rs.next()){

			return true;
			}
		return false;
		}
	catch(SQLException ex){
		//JOptionPane.showMessageDialog(null, ex.getMessage());
		return false;
		}
}

	public boolean agregarRecetaGrupo(int idGrupo, int idReceta){
	CallableStatement cmd;
	try{
		cmd=con.prepareCall("{Call SP_agregarRecetaGrupo(?,?)}");
		cmd.setInt(1, idGrupo);
		cmd.setInt(2, idReceta);
		cmd.executeQuery();
		
		return true;
	}catch(SQLException ex){
		return false;
	}
	}

	public boolean agregarHistConsultas(int idReceta, String username){
		CallableStatement cmd;
		try{
			cmd=con.prepareCall("{Call SP_agregarHistorico(?,?)}");
			cmd.setString(1, username);
			cmd.setInt(2, idReceta);
			cmd.executeQuery();
			
			return true;
		}catch(SQLException ex){
			return false;
		}
	}

/*public boolean agregarReceta(Receta receta){
	CallableStatement cmd;
	try
	{
		cmd=con.prepareCall("{Call SP_agregarReceta(?,?,?,?,?,?,?,?,?,?)}");
		cmd.setString(1, receta.getCreador());
		cmd.setString(2, receta.getNombre());
		cmd.setString(3, receta.getDetalle());
		cmd.setInt(4, receta.getDificultad().getIdDificultad());
		cmd.setInt(5, 0);
		cmd.setInt(6, 1);
		cmd.setInt(7, receta.getTemporada().getIdTemporada());
		cmd.setInt(8, receta.getIngredientePrincipal().getIdIngrediente());
		cmd.setBlob(9, new SerialBlob(receta.getFotoPrincipal()));
		cmd.registerOutParameter(10, Types.INTEGER);
		
		cmd.executeQuery();
		
		CallableStatement cmdRegPaso = con.prepareCall("{Call SP_agregarPasoAReceta(?,?,?,?)}");
		cmdRegPaso.setInt(1, cmd.getInt(10));
		
		for (Pasos pasosReceta: receta.getPasos())
		{
			cmdRegPaso.setInt(2, pasosReceta.getNumPaso());
			cmdRegPaso.setString(3, pasosReceta.getDescripcionPaso());
			if(pasosReceta.getImagen() != null)
				cmdRegPaso.setBlob(4, new SerialBlob(pasosReceta.getImagen()));
			else
				cmdRegPaso.setNull(4, Types.BLOB);
			cmdRegPaso.executeQuery();
		}
		
		receta.setIdreceta(cmd.getInt(10));
		return true;
	}
	
	catch(SQLException ex)
	{	JOptionPane.showMessageDialog(null, ex.getMessage());
		return false;
	}
}
*/
//public Grupo obtenerGrupo(int idGrupo){
//	CallableStatement cmd;
//	ResultSet rs;
//	try
//	{
//		cmd=con.prepareCall("{Call SP_obtenerGrupo(?)}");
//		cmd.setInt(1, idGrupo);
//		rs = cmd.executeQuery();
//		
//		rs.next();
//		return new Grupo(rs.getInt("idGrupo"), rs.getString("nombreGrupo"), rs.getString("creador"), rs.getString("detalle"));
//	}
//	
//	catch(SQLException ex)
//	{	//JOptionPane.showMessageDialog(null, ex.getMessage());
//		return new Grupo(0,"error",ex.getMessage(),"");
//	}
//
//}
@Deprecated
public ArrayList<Receta> obtenerRecetasGrupo(int idGrupo){
	CallableStatement cmd;
	ResultSet rs;
	ArrayList<Receta> recetas = new ArrayList<Receta>();
	try
	{
		cmd=con.prepareCall("{Call SP_obtenerRecetasGrupo(?)}");
		cmd.setInt(1, idGrupo);
		rs = cmd.executeQuery();
		
		while(rs.next()){
			recetas.add(new Receta(rs.getInt("idReceta"), 
						rs.getString("nombre"), 
						new Usuario(),//.getString("creador"), 
						new Dificultades( rs.getInt("idDificultad"), rs.getString("dificultad")),
						new Temporadas(0, ""),
						new Ingredientes(rs.getInt("idIngrediente"),  rs.getString("IngPrincipal"), rs.getInt("caloriasPorcion"), rs.getInt("tipoIngrediente"))
						));
		}
			
		return recetas;
		}
	
	catch(SQLException ex)
	{	//JOptionPane.showMessageDialog(null, ex.getMessage());
		return recetas;
	}

}

public ArrayList<Usuario> obtenerUsuariosGrupo(Grupo grupo){
	CallableStatement cmd;
	ResultSet rs;
	ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
	try
	{
		cmd=con.prepareCall("{Call SP_obtenerUsuariosGrupo(?)}");
		cmd.setInt(1, grupo.getIdGrupo());
		rs = cmd.executeQuery();
		
		while(rs.next()){
			Usuario usuarioObtenido;
			usuarios.add(usuarioObtenido = new Usuario());
			usuarioObtenido.setUsername(rs.getString("nombreUsuario"));
		}
			
		return usuarios;
		}
	
	catch(SQLException ex)
	{	JOptionPane.showMessageDialog(null, ex.getMessage());
		return usuarios;
	}

}
@Deprecated
public ArrayList<Receta> cargarHomeRecetas(Usuario user){
	
	ResultSet rs = null;
	ArrayList<Receta> recetas = new ArrayList<Receta>();
	
	try
	{
		
		CallableStatement cmd = con.prepareCall("{call SP_recetasHome(?)}");
		
		cmd.setString(1, user.getUsername());
		rs = cmd.executeQuery();
		
		while (rs.next()){
			
			recetas.add(new Receta(
					rs.getInt("idReceta"),
					rs.getString("nombre"), 
					new Usuario(),//rs.getString("creador"),
					new Dificultades(rs.getInt("idDificultad"), rs.getString("dificultad")),
					new Temporadas(0, ""),
					new Ingredientes(0, "", 0, 0)));
	
		}
	}
	catch(SQLException ex){
		//JOptionPane.showMessageDialog(null, ex.getMessage());	
	}
	
	return recetas;
}

/*	public boolean agregarIngredientesyCondimentos(Receta receta){
		
		ArrayList<Ingredientes> ingredientes = receta.getIngredientes();
		ArrayList<Condimentos> condimentos = receta.getCondimentos();
		
		try
		{
			for(Ingredientes ingrediente : ingredientes){
			CallableStatement cmd = con.prepareCall("{call SP_agregarIngredienteAReceta(?,?)}");
			
			cmd.setInt(1,receta.getIdreceta());
			cmd.setInt(2,ingrediente.getIdIngrediente());
			cmd.executeQuery();
		}
			for(Condimentos condimento : condimentos){
				CallableStatement cmd = con.prepareCall("{call SP_agregarCondimentoAReceta(?,?)}");
				
				cmd.setInt(1,receta.getIdreceta());
				cmd.setInt(2,condimento.getIdCondimento());
				cmd.executeQuery();
			}
			return true;
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null, ex.getMessage());	
			return false;
		}

	}*/
}


