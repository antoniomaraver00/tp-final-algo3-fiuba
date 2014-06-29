package fiuba.algo3.tpfinal.test;


	
	import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;







import modelo.Coordenadas;
import modelo.Edificio;
import modelo.Pais;
import modelo.Trayectoria;
import modelo.excepcion.ErrorNoHayMasPaisesParaAvanzar;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.mockito.Mockito.*;

	
	@SuppressWarnings("unused")
	public class TrayectoriaTest {
	
		private Edificio biblioteca = new Edificio("biblioteca","biblioteca pista");
		private Edificio puerto = new Edificio("puerto","puerto pista");
		private Edificio fiuba = new Edificio("fiuba", "fiuba pista");
		private Edificio[] edificios = {biblioteca,puerto,fiuba};
		private Pais Argentina = new Pais("Argentina",edificios,new Coordenadas(1, 1));
		private Pais Brasil = new Pais("Brasil",edificios,new Coordenadas(1, 3));
		
		@Test(expected = ErrorNoHayMasPaisesParaAvanzar.class)
		public void TrayectoriaPruebaAvanzarExcepcion() throws ErrorNoHayMasPaisesParaAvanzar{
			ArrayList<Pais> ListaPaises = new ArrayList<Pais>();
			ListaPaises.add(Argentina);
			Trayectoria UnaTrayectoria = new Trayectoria(ListaPaises);
			
			assertEquals(UnaTrayectoria.avanzar(),ErrorNoHayMasPaisesParaAvanzar.class);
			
		}
			
		@Test
		public void TrayectoriaAvanzar() throws ErrorNoHayMasPaisesParaAvanzar{
			ArrayList<Pais> ListaPaises = new ArrayList<Pais>();
			ListaPaises.add(Argentina);
			ListaPaises.add(Brasil);
			Trayectoria UnaTrayectoria = new Trayectoria(ListaPaises);
			
			assertEquals(UnaTrayectoria.avanzar(),Brasil);
		}
		
		@Test
		public void TrayectoPaisActualYFinal() throws ErrorNoHayMasPaisesParaAvanzar{
			ArrayList<Pais> ListaPaises = new ArrayList<Pais>();
			ListaPaises.add(Argentina);
			Trayectoria UnaTrayectoria = new Trayectoria(ListaPaises);
			assertEquals(Argentina, UnaTrayectoria.paisActual());
			assertEquals(Argentina, UnaTrayectoria.paisFinal());
		}
		@Test
		public void testSerializarYcargarTrayectoria() throws ParserConfigurationException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.newDocument();
			HashMap<String, Integer> distancias = new HashMap<>();
			distancias.put("Chile", 2);
			distancias.put("Paraguay",4);
			Argentina.setDistancias(distancias);
			Brasil.setDistancias(distancias);
			ArrayList<Pais> ListaPaises = new ArrayList<Pais>();
			ListaPaises.add(Argentina);
			ListaPaises.add(Brasil);
			Trayectoria unaTrayectoria = new Trayectoria(ListaPaises);
			Element elementoTrayectoria = unaTrayectoria.serializar(doc);
			doc.appendChild(elementoTrayectoria);
			Trayectoria trayectoriaCargada = Trayectoria.hidratar(doc.getFirstChild());
			Assert.assertTrue(trayectoriaCargada.equals(unaTrayectoria));
		}
	}
	