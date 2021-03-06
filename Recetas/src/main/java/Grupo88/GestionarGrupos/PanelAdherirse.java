package Grupo88.GestionarGrupos;

import master.ErrorPage;
import master.RegisteredPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import Grupo88.Detalles.DetalleGrupo;
import Grupo88.Inicio.Inicio;
import ObjetosDB.Grupo;

public class PanelAdherirse extends RegisteredPage{
	
	private static final long serialVersionUID = 3576530176656083562L;
	private NegocioGrupos negocio;
	private Grupo grupo;
	public PanelAdherirse(NegocioGrupos negocio, Grupo grupo) {
		super();
		this.negocio = negocio;
		this.grupo = grupo;
		add(new FormPanelAdherirse("frmAdherise"));
		
    }
	
	private class FormPanelAdherirse extends Form<Object>{

		private static final long serialVersionUID = -3158424907182082622L;

		public FormPanelAdherirse(String id) {
			super(id);
			add(new Label("msg", "Usted no esta adherido al grupo"));
			
			add(new Link<Object>("adherirse"){

				private static final long serialVersionUID = -5658598422876465115L;

				@Override
				public void onClick() {
					if(!negocio.agregarUsuario(getUsuarioActual(), grupo))
						setResponsePage(ErrorPage.ErrorEnLaDB());
					
					PageParameters pars = new PageParameters();
					pars.add("idGrupo",grupo.getIdGrupo());
					setResponsePage(DetalleGrupo.class,pars);
				}
			});
			
			add(new Link<Object>("volverAGrupos"){

				private static final long serialVersionUID = -3049508001558222346L;

				@Override
				public void onClick() {
					setResponsePage(GestionarGrupos.class);
				}
			});
			add(new Link<Object>("irInicio"){

				private static final long serialVersionUID = -4029130113176359755L;

				@Override
				public void onClick() {
					setResponsePage(Inicio.class);
				}
			});
		}
	}

}
