import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
class Transiciones{
	Iterator<JSONArray> transiciones;
	public Transiciones(JSONArray t){
		transiciones = t.iterator();
	}
	public JSONArray verificarTransicion(String cabezaLectora, Character cp){

		JSONArray transicion;
		String cadenaApuntada = String.valueOf(cp);
    	while (transiciones.hasNext()){
			transicion = transiciones.next();

    		if(transicion.get(0).equals(cabezaLectora) && transicion.get(1).equals(cadenaApuntada) ){
    			return transicion;
    		}
      	}
      	return null;
	}
}