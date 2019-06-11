import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;
import java.util.LinkedList;

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
            
      int posicion = 0;
      int posicion_de_la_configuracion=0;

      //Comenzamos con la verificacion
      while(iteracion==0){
        //Mostramos la configuracion inicial.
        System.out.println(mostrarConfiguracion(cinta,cabezaLectora,posicion_de_la_configuracion));
        //se inicia la maquina de turing con todas las transicioes posibles
        Transiciones maquinaTuring = new Transiciones(t);
        //Buscamos la trancision para la cabeza lectora que apunta a un caracter
        JSONArray movimiento = maquinaTuring.verificarTransicion(cabezaLectora,cinta[posicion]);

        //Si no existe dicha transicion y la cabeza lectora no se encuentra en algun estado final, entonces la cadena es rechazada
        if(!estadoFinal(cabezaLectora,finales) && movimiento == null){
          iteracion = 1;
          System.out.println("Cadena rechazada");
        }
        //Si la cabeza lectora se encuentra dendro de un estado final, terminamos y la cadena es aceptada
        else if(estadoFinal(cabezaLectora,finales)){
          System.out.println("Cadena aceptada");
          iteracion=1;
        }
        //realizamos cambio en cinta, cabeza lectora y posicion
        else{
          //Editamos estado de cabeza lectora
          cabezaLectora = (String) movimiento.get(2);
          //Editamos cinta
          String c = (String) movimiento.get(3);
          cinta[posicion] = c.charAt(0);
          //Mover cabeza lectora a la izquierda
          if(movimiento.get(4).equals("L")){
            if(posicion==0){
              cinta = alargarIzquierda(String.copyValueOf(cinta));
            }else{
              posicion=posicion-1;
              posicion_de_la_configuracion= posicion_de_la_configuracion-1;
            }

          //Mover la cabeza lectora a la derecha
          }else if(movimiento.get(4).toString().equals("R")){
            if(posicion==(cinta.length-1)){
              cinta = alargarDerecha(String.copyValueOf(cinta));
              posicion = cinta.length - 1;
              posicion_de_la_configuracion = cinta.length;
            }else{
              posicion=posicion+1;
              posicion_de_la_configuracion = posicion_de_la_configuracion+1;
            }
          }
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
  //Alarga la lista con blancos en direecion izquierda
  static char[] alargarIzquierda(String cadena){
    String temp = "_"+cadena;
    return temp.toCharArray();
  }
  //Alarga la lista con blancos en direecion derecha
  static char[] alargarDerecha(String cadena){
    String temp = cadena+"_";
    return temp.toCharArray();
  }
  //Verifica si un estado es final
  static Boolean estadoFinal(String cabezaLectora, JSONArray finales){
    Iterator<String> iterator = finales.iterator();
    String temp;
    while (iterator.hasNext()) {
      temp = (String) iterator.next();
      if(temp.equals(cabezaLectora)) return true;
    }
    return false;
  }
  //Muestra la configuracion de una cadena
  static String mostrarConfiguracion(char[] cinta, String cabezaLectora, int posicion_de_la_configuracion){
    LinkedList<String> cola = new LinkedList<String>();
    String configuracion = "-> ";
    int temp = 0;
    for(int i=0; i<posicion_de_la_configuracion+1;i++){
      if(i==posicion_de_la_configuracion){
        cola.add("|"+cabezaLectora+"|");

      }else{

        cola.add(String.valueOf(cinta[i]));
      }
    }

    for(int k=posicion_de_la_configuracion; k<cinta.length;k++){
      cola.add(String.valueOf(cinta[k]));

    }

    for (String m : cola) {
      configuracion += m+" ";
    }
    return configuracion;
  }

}
