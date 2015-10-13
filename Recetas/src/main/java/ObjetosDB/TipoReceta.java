package ObjetosDB;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tipoReceta")
public class TipoReceta {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idTipoReceta")
	private int idTipoReceta;
	
	@Column(name="descripcion")
	private String descripcion;

	public TipoReceta() {}

	public int getIdTipoIngrediente() {
		return idTipoReceta;
	}

	public void setIdTipoIngrediente(int idTipoIngrediente) {
		this.idTipoReceta = idTipoIngrediente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
