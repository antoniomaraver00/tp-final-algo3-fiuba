package fiuba.algo3.tpfinal.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import modelo.Edificio;
import modelo.Pais;

import org.junit.Test;

public class PaisTest {

	private Edificio biblioteca = new Edificio("biblioteca","biblioteca pista");
	private Edificio puerto = new Edificio("puerto","puerto pista");
	private Edificio fiuba = new Edificio("fiuba", "fiuba pista");
	
	private Edificio[] edificios = {biblioteca,puerto,fiuba};
	private String nombre = "Argentina";
	
	private Pais UnPais = new Pais(nombre,edificios);
	
	@Test
	public void testElNombreDelPaisEsCorrecto() {
		
		assertEquals( UnPais.getNombre(), nombre );
	}	
	
	@Test
	public void testLosNombresDeLosEdificiosSonCorrectos(){
		
		ArrayList<String> NombresDeEdificios = UnPais.getNombresDeEdificios();
		
		Iterator<String> Iterador = NombresDeEdificios.iterator();
		int i = 0;
		while( Iterador.hasNext() ){
			String NombreActual = Iterador.next();
			assertEquals( NombreActual, edificios[i].getNombre() );
			i++;
		}
	}
}
