import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;

public class Maquina {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    JSONParser parser = new JSONParser();


    try {
      //Solicitamos archivo json (Descripcion de la maquina) y la cadena que deseamos verificar.
      System.out.println("Hola porfavor introduce el nombre de tu archivo JSON");
      String mt = in.nextLine();
      System.out.println("Perfecto! \nAhora porfavor introduce la cadena a evaluar");
      String cadena = in.nextLine();
      String cabezaLectora;
      //Lee el archivo json indicado.
      Object obj = parser.parse(new FileReader(mt));

      JSONObject jsonObject = (JSONObject) obj;
      //Posicionamos la cabeza lectora en el estado inicial.
      cabezaLectora = (String) jsonObject.get("Inicial");

      //Marcador que indicara salir del loop de la verificacion
      int iteracion = 0;
      JSONArray t = (JSONArray) jsonObject.get("Transiciones");
      JSONArray finales = (JSONArray) jsonObject.get("Finales");
      char[] cinta = cadena.toCharArray();

      //Segunda cinta inicializada con "_"
      char[] cinta2 = new char[cinta.length];
      for(int k = 0; k<cinta.length; k++){
        cinta2[k] = '_';
      }

      int posicion = 0;

      Character [] elementosCintas = new Character[2];
      elementosCintas[0] = cinta[0];
      elementosCintas[1] = cinta2[0];
      //System.out.println(cabezaLectora + " "+ String.copyValueOf(cinta)+ " apuntador "+ (String.valueOf(cinta[posicion])));

      //Comenzamos con la verificacion
      while(iteracion==0){
        //se inicia la maquina de turing con todas las transicioes posibles
        Transiciones maquinaTuring = new Transiciones(t);
        //Buscamos la trancision para la cabeza lectora que apunta a un caracter
        JSONArray movimiento = maquinaTuring.verificarTransicion(cabezaLectora,elementosCintas);
        //Si no existe dicha transicion y la cabeza lectora no se encuentra en algun estado final, entonces la cadena es rechazada
        if(!estadoFinal(cabezaLectora,finales) && movimiento == null){
          iteracion = 1;
          System.out.println("Cadena rechazada");
         //System.out.println(cabezaLectora + " "+ String.copyValueOf(cinta) );
        }
        //Si la cabeza lectora se encuentra dendro de un estado final, terminamos y la cadena es aceptada
        else if(estadoFinal(cabezaLectora,finales)){
          System.out.println("Cadena aceptada");
          iteracion=1;
        }
        //realizamos cambio en cinta, cabeza lectora y posicion
        else{
          //Editamos estado de cabeza lectora
          cabezaLectora = (String) movimiento.get(3);
          //Editamos cinta
          String c = (String) movimiento.get(4);
          String c2 = (String) movimiento.get(5);
          cinta[posicion] = c.charAt(0);
          cinta2[posicion] = c2.charAt(0);
          //Mover cabeza lectora a la izquierda
          if(movimiento.get(6).equals("L")){
            if(posicion==0){
              cinta = alargarIzquierda(String.copyValueOf(cinta));
              cinta2 = alargarIzquierda(String.copyValueOf(cinta2));
            }else{posicion=posicion-1;}

          //Mover la cabeza lectora a la derecha
        }else if(movimiento.get(6).toString().equals("R")){
            if(posicion==(cinta.length-1)){
              cinta = alargarDerecha(String.copyValueOf(cinta));
              cinta2 = alargarDerecha(String.copyValueOf(cinta2));
              posicion = cinta.length - 1;
            }else{posicion=posicion+1;}
          }
          //System.out.println(cabezaLectora + " "+ String.copyValueOf(cinta) + " apuntador "+ (String.valueOf(cinta[posicion])));
          //dejar la cabeza neutral
        }

      }



    }   catch (FileNotFoundException e) {
      //manejo de error
    } catch (IOException e) {
      //manejo de error
    } catch (ParseException e) {
      //manejo de error
    }

  }
  static char[] alargarIzquierda(String cadena){
    String temp = "_"+cadena;
    return temp.toCharArray();
  }
  static char[] alargarDerecha(String cadena){
    String temp = cadena+"_";
    return temp.toCharArray();
  }
  static Boolean estadoFinal(String cabezaLectora, JSONArray finales){
    Iterator<String> iterator = finales.iterator();
    String temp;
    while (iterator.hasNext()) {
      temp = (String) iterator.next();
      if(temp.equals(cabezaLectora)) return true;
    }
    return false;
  }

}
