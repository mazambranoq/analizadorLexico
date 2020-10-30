package com.company;

import java.util.HashMap;
import java.util.Map;

public class Token {

    String id;
    String lexema = "";
    int fila;
    int columna;

    Token(String id, String lexema, int fila, int columna){
        this.id = id;
        this.lexema = lexema;
        this.fila = fila;
        this.columna = columna;
    }
    Token(String id, int fila, int columna){
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }
    @Override
    public String toString(){
        if(lexema.isEmpty()){
            return "<"+id+","+fila+","+columna+">";
        }else{
            return "<"+id+","+lexema+","+fila+","+columna+">";
        }
    }
    public static void main(String[] args){
        Map<String,String> map = new HashMap<>();
        map.put("hola","chupemelo");
        map.put("chupemelo","");
        if(map.get("chupemelo")== null)
            System.out.println("nel perro");
        String expression = "\n";
        System.out.println(expression.matches("\\s"));
        //System.out.println(expression.substring(0,3).length());
        System.out.println(expression);
    }
}
