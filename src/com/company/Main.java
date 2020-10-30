package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public enum ESTADO{
        primero,letra,cadena,entero,doble,operador
    }
    public static void main(String[] args) {
        ESTADO estado = ESTADO.primero;
        Scanner s = new Scanner( System.in );
        System.out.println("digite el nombre del archivo");
        String archivo = s.nextLine();
        FileReader f;
        BufferedReader b;
        Map<String,String> operators = new HashMap<>();
        operators.put("{", "token_llave_izq");
        operators.put("}", "token_llave_der");
        operators.put("$", "token_dollar");
        operators.put(";", "token_pyc");
        operators.put("[", "token_cor_izq");
        operators.put("]", "token_cor_der");
        operators.put("(", "token_par_izq");
        operators.put(")", "token_par_der");
        operators.put(">", "token_mayor");
        operators.put("<", "token_menor");
        operators.put(">=", "token_mayor_igual");
        operators.put("<=", "token_menor_igual");
        operators.put("==", "token_igual_num");
        operators.put("!=", "token_diff_num");
        operators.put("&&", "token_and");
        operators.put("||", "token_or");
        operators.put("!", "token_not");
        operators.put("+", "token_mas");
        operators.put("-", "token_menos");
        operators.put("*", "token_mul");
        operators.put("/", "token_div");
        operators.put("%", "token_mod");
        operators.put("**", "token_pot");
        Map<String,String> words = new HashMap<>();
        words.put("ne", "token_diff_str");
        words.put("eq", "token_igual_str");
        words.put("set", "set");
        words.put("gets", "gets");
        words.put("puts", "puts");
        words.put("if", "if");
        words.put("then", "then");
        words.put("elseif", "elseif");
        words.put("else", "else");
        words.put("switch", "switch");
        words.put("default", "default");
        words.put("while", "while");
        words.put("break", "break");
        words.put("continue", "continue");
        words.put("expr", "expr");
        words.put("for", "for");
        words.put("incr", "incr");
        words.put("array", "array");
        words.put("size", "size");
        words.put("exists", "exists");
        words.put("return", "return");
        words.put("proc", "proc");

        try {
            f = new FileReader(archivo);
            b = new BufferedReader(f);
            String line;
            boolean noerror = true;
            int numline = 0, numcol = 1;
            while((line = b.readLine())!= null && noerror){
                numline++;
                numcol = 1;
                int currentchar = 0;
                while(!line.isEmpty() && noerror) {
                    if(currentchar +1 >= line.length())
                        line += " ";
                    String currentString = line.substring(0,currentchar+1);
                    switch (estado) {
                        case primero:
                            if(currentString.equals("#")){
                              line= "";
                            } else {
                                if (currentString.matches("[\\s ]")) {
                                    estado = ESTADO.primero;
                                    line = line.substring(currentchar + 1);
                                    numcol += currentchar + 1;
                                    currentchar = -1;
                                } else {
                                    if (currentString.matches("[A-Za-z]")) {
                                        estado = ESTADO.letra;
                                    } else {
                                        if (currentString.matches("[0-9]")) {
                                            estado = ESTADO.entero;
                                        } else if (currentString.equals("\"")) {
                                            line = line.substring(currentchar + 1);
                                            estado = ESTADO.cadena;
                                        } else
                                            estado = ESTADO.operador;
                                    }
                                }
                            }
                            break;
                        case letra:
                            if(currentString.matches("[A-Za-z]+[0-9]*")) {
                                estado = ESTADO.letra;
                            }else{
                                currentString = line.substring(0,currentchar);
                                if(words.get(currentString)!=null) {
                                    System.out.println(new Token(words.get(currentString),numline,numcol));
                                    estado = ESTADO.primero;
                                    line = line.substring(currentchar);
                                    numcol += currentchar;
                                    currentchar = -1;
                                } else {
                                    if (currentString.matches("[A-Za-z]+[0-9]*")) {
                                        estado = ESTADO.primero;
                                        System.out.println(new Token("id", currentString, numline, numcol));
                                        line = line.substring(currentchar);
                                        numcol += currentchar;
                                        currentchar = -1;
                                    } else {
                                        System.out.println(">>> Error lexico (linea: " + numline + ", posicion: " + numcol + ")");
                                    }
                                }
                            }
                            break;
                        case entero:
                            if(currentString.matches("[0-9]+")) {
                                estado = ESTADO.entero;
                            }else{
                                if(currentString.matches("[0-9]+\\.")) {
                                    estado = ESTADO.doble;
                                } else {
                                    currentString = line.substring(0,currentchar);
                                    if (currentString.matches("[0-9]+")) {
                                        estado = ESTADO.primero;
                                        System.out.println(new Token("token_integer", currentString, numline, numcol));
                                        line = line.substring(currentchar);
                                        numcol += currentchar;
                                        currentchar = -1;
                                    } else {
                                        System.out.println(">>> Error lexico (linea: " + numline + ", posicion: " + numcol + ")" +currentString);
                                    }
                                }
                            }
                            break;
                        case doble:
                            if(currentString.matches("[0-9]+\\.[0-9]+")) {
                                estado = ESTADO.doble;
                            }else{
                                currentString = line.substring(0,currentchar);
                                if(currentString.matches("[0-9]+\\.[0-9]+")) {
                                    estado = ESTADO.primero;
                                    System.out.println(new Token("token_double", currentString, numline, numcol));
                                    line = line.substring(currentchar);
                                    numcol += currentchar;
                                    currentchar = -1;
                                } else {
                                        System.out.println(">>> Error lexico (linea: " + numline + ", posicion: " + numcol + ")" +currentString);
                                }
                            }
                            break;
                        case cadena:
                            if(currentString.matches("[^\"]*")) {
                                estado = ESTADO.cadena;
                            }else{
                                if(currentString.matches(".*\"")) {
                                    estado = ESTADO.primero;
                                    System.out.println(new Token("token_string", currentString.substring(0,currentchar), numline, numcol));
                                    line = line.substring(currentchar+1);
                                    numcol += currentchar+1;
                                    currentchar = -1;
                                } else {
                                    System.out.println(">>> Error lexico (linea: " + numline + ", posicion: " + numcol + ")" +currentString);
                                }
                            }
                            break;
                        case operador:
                            if(operators.get(currentString)!=null) {
                                System.out.println(new Token(operators.get(currentString),numline,numcol));
                                estado = ESTADO.primero;
                                line = line.substring(currentchar+1);
                                numcol += currentchar+1;
                                currentchar = -1;
                            }else{
                                currentString = line.substring(0,currentchar);
                                if(operators.get(currentString)!=null) {
                                    estado = ESTADO.primero;
                                    System.out.println(new Token(operators.get(currentString),numline,numcol));
                                    line = line.substring(currentchar);
                                    numcol += currentchar;
                                    currentchar = -1;
                                } else {
                                    noerror = false;
                                    System.out.println(">>> Error lexico (linea: " + numline + ", posicion: " + numcol + ")" +currentString);
                                }
                            }
                            break;
                    }
                    currentchar++;
                    if(line.matches("[\\s ]*"))
                        line = "";
                }
            }
        }catch (IOException e){
            System.out.println(archivo+ " no disponible");
            e.printStackTrace();
        }

    }
}
