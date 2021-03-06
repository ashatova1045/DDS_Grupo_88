package Grupo88.Detalles;

import master.ErrorPage;
import master.MasterPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;

import Grupo88.Inicio.Inicio;
import ObjetosDB.Condimentos;
import ObjetosDB.Historial;
import ObjetosDB.Pasos;
import ObjetosDB.Receta;
import ObjetosDB.Receta_Ingrediente;

public class DetalleDeReceta extends MasterPage {

	private static final long serialVersionUID = 7898970758684629351L;

	private NegocioRecetas negocio;
	private StringValue idReceta;
	private Receta receta;
	
	public DetalleDeReceta(final PageParameters parameters){
		super();
		iniciar(parameters);
		
		add(new Link<Object>("volver") {

			private static final long serialVersionUID = -7738673708435294081L;

			@Override
			public void onClick() {
				setResponsePage(paginaRetorno());
			}
		});

		}
	
	protected Page paginaRetorno(){
		return new Inicio();
	}
	
	private void iniciar(PageParameters parameters){

		negocio = new NegocioRecetas(getSessionUser());
		receta = null;
				
		if(parameters.getNamedKeys().contains("idReceta")){
			idReceta = parameters.get("idReceta");

			try {
				receta = negocio.getReceta(idReceta.toInt());
			} catch (NullPointerException e){
				setResponsePage(ErrorPage.ErrorIngresoInvalidoDatos());
				return;
			}
			catch (StringValueConversionException e) {
				setResponsePage(ErrorPage.ErrorIngresoInvalidoDatos());
				return;
			}
		
		if(receta == null){
			info(negocio.getError());
			setResponsePage(new ErrorPage("La receta que intenta visitar no existe"));
		}
		
		add(new FrmDetalleDeReceta("FrmDetalleDeReceta"));
		
		negocio.guardarConsulta(new Historial("", receta, getUsuarioActual()),getUsuarioActual());

		if(getSessionUser().estaLogueado()){
			add(new FormCalificarPanel("formCalificar",receta,getUsuarioActual(),negocio));
			add(new FormConfirmarPanel("formConfirmar",getUsuarioActual(),receta,negocio));
			add(new FormCompartirPanel("formCompartir",getUsuarioActual(),receta,negocio));
		}
		else{
			add(new EmptyPanel("formCalificar"));
			add(new EmptyPanel("formConfirmar"));
			add(new EmptyPanel("formCompartir"));
		}
		
	}
		else{
			setResponsePage(ErrorPage.ErrorIngresoInvalidoDatos());
		}
	}
	private class FrmDetalleDeReceta extends Form<Object> {


	private static final long serialVersionUID = -1393638805587049063L;

		public FrmDetalleDeReceta(String id) {
			super(id);

			add(new Label("Nombre",receta.getNombre()));
			add(new Label("dif",receta.getDificultad().getDificultad()));
			add(new Label("tem",receta.getTemporada().getTemporada()));
			add(new Label("cal", receta.getCalorias()));
			add(new Image("imgPrinc", new DynamicImageResource(){
	
				private static final long serialVersionUID = -296773244831553413L;

				@Override
				protected byte[] getImageData(Attributes attributes) {
					return receta.getFotoPrincipal();
				}
			}));

			RepeatingView ingredientes = new RepeatingView("listaIngredientes");

			for (Receta_Ingrediente ingrediente : receta.getRelacionIngredientes()) {
				
				AbstractItem item = new AbstractItem(ingredientes.newChildId());
						
				item.add(new Label("unIngrediente", ingrediente.getIngrediente().getIngrediente()));
				ingredientes.add(item);
				
			}

			add(ingredientes);
			
			RepeatingView condimentos = new RepeatingView("listaCondimentos");
			
			for (Condimentos condimento : receta.getCondimentos()) {
				
				AbstractItem item = new AbstractItem(condimentos.newChildId());
						
				item.add(new Label("unCondimento", condimento.getCondimento()));
				condimentos.add(item);
				
			}
			
			add(condimentos);
			
			RepeatingView pasos = new RepeatingView("pasos");
			
			int i = 0;
			for (final Pasos paso : receta.getPasos()) {
				
				AbstractItem item = new AbstractItem(pasos.newChildId());
				i++;
				item.add(new Label("nro","Paso "+i+":"));		
				item.add(new Label("paso", paso.getDescripcionPaso()));
				item.add(new Image("img", new DynamicImageResource(){
					
					private static final long serialVersionUID = 4335079479260519834L;

					@Override
					protected byte[] getImageData(Attributes attributes) {
						return paso.getImagen();
					}
				}));
				pasos.add(item);
				
			}
			add(pasos);
		}
	}
}