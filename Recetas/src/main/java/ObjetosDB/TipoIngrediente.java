package ObjetosDB;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="tipoingrediente")
public class TipoIngrediente implements Serializable{
	
	private static final long serialVersionUID = -6025401808564192712L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idTipoIngrediente")
	private int idTipoIngrediente;
	
	@NotNull(message="La descripción no puese estar vacía")
	@Size(min=1, max=30, message="La descripción debe contener entre 1 y 30 caracteres")
	@Column(name="descripcion")
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "grupoQuePertenece")
	private GruposAlimenticios grupoQuePertenece;
	
	public TipoIngrediente() {}

	public int getIdTipoIngrediente() {
		return idTipoIngrediente;
	}

	public void setIdTipoIngrediente(int idTipoIngrediente) {
		this.idTipoIngrediente = idTipoIngrediente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
