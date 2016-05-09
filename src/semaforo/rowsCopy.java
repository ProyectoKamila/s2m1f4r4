/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semaforo;

/**
 *
 * @author maikolleon
 */
public class rowsCopy {
    
    public static Object copiaTabla[][]=new Object[100][8];
    public static int[] numerosEstado =new int[100];
    
    public rowsCopy(int fila, int columna, int caso){
        switch (caso){
            case 1:
                copiaTabla=new Object[fila][columna];
            break;
            case 2: 
                
            break;
        }
    }
    public Object[][] retornaCopiaTabla(){
        return copiaTabla;
    }
    public int[] retornaNumerosEstado(){
        return numerosEstado;
    }
    public Object retornaValorCopiaTabla(int fila, int col){
        return copiaTabla[fila][col];
    }
    public void cambiarCopiaTabla(int fila, int col, Object valor){
        copiaTabla[fila][col]=valor;
    }
    public void cambiarNumerosEstados(int posicion, int valor){
        numerosEstado[posicion]=valor;
    }
}
