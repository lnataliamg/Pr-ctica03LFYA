import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
class Transiciones{
	Iterator<JSONArray> transiciones;
	public Transiciones(JSONArray t){
		transiciones = t.iterator();
	}
	public JSONArray verificarTransicion(String cabezaLectora, Character[] cp){

		JSONArray transicion;
		String [] cadenaApuntada = new String[2];
		cadenaApuntada[0] = String.valueOf(cp[0]);
	 	cadenaApuntada[1] = String.valueOf(cp[1]);
    	while (transiciones.hasNext()){
			transicion = transiciones.next();

    		if(transicion.get(0).equals(cabezaLectora) && transicion.get(1).equals(cadenaApuntada[0]) && transicion.get(2).equals(cadenaApuntada[1]) ){
					System.out.println("La tran es" + transicion);
					return transicion;
    		}
      	}
      	return null;
	}
}
