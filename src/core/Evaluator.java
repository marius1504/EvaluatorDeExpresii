package core;
import java.util.Deque;
import java.util.*;
public class Evaluator {
	
	private static TreeMap<String, Integer> operators = new TreeMap<>();

    static void Operanzi() {
        operators.put("-", 0);
        operators.put("+", 0);
        operators.put("/", 1);
        operators.put("*", 1);
        operators.put("^", 2);
        operators.put("(", 3);
        operators.put(")", 3);
    }

   static String[] toStringArray(String expresie) {

        // expresie = expresie.replaceAll("\\s", "");

        String[] tokens = expresie.split("(?<=[\\s-+*/\\^\\(\\)])|(?=[\\s-+*/\\^\\(\\)])");

        return tokens;
    }

    static StringBuilder postfix(String[] input) {
        //   StringBuilder input = new StringBuilder(expresie);
        StringBuilder output = new StringBuilder();

        Deque<String> stivaOperatori = new LinkedList<>();


        //1 cat timp exista o entitate de citit:
        //    1.1 citeste entitatea (i.e. operand sau operator)
        for (int i = 0; i < input.length; i++) {

            String index = input[i];

            // verific daca e space;
            if (index == " ") continue;

            //1.2. daca entitatea este un operand (i.e. un numar), atunci:
            //1.2.1 adauga la forma postfixata
            if (!operators.containsKey(index)) {
                output.append(input[i] + " ");
                continue;
            }

            //  1.3 daca entitatea este un operator (fie el O1), atunci:
            // si nu este mai sunt oparatori in coada il adaugam in coada;
            else if (!(index.equals("(")) && !(index.equals(")"))) {
                //  1.3.1 cat timp (exista un operator in varful stivei (fie el O2)) SI (O2 este diferit de paranteza-stanga) SI (precedenta(O1) < precedenta(O2) SAU
                //   precedenta(O1) = precedenta(O2) SI O2 are asociativitate-stanga)
                while ((!stivaOperatori.isEmpty())
                        && (!(stivaOperatori.getLast().equals("(")))
                        && ((operators.get(index) < operators.get(stivaOperatori.getLast()))
                        || ((operators.get(index) == operators.get(stivaOperatori.getLast())
                        && (!(stivaOperatori.getLast().equals("^"))))))) {
                    // 1.3.1.1 extrage O2 si adauga-l la forma postfixata
                    output.append(stivaOperatori.pollLast() + " ");
                }
                // 1.3.2 adauga O1 in stiva
                stivaOperatori.add(index);
                continue;
            }

            //1.4 daca entitatea este o paranteza-stanga, atunci:
            //1.4.1 adauga paranteza in stiva
            if ((index.equals("("))) {
                stivaOperatori.add(index);
                continue;
            }

            //1.5 daca entitatea este o paranteza-dreapta, atunci:
            if (index.equals(")")) {
                boolean ver = false;

                //1.5.1 cat timp operatorul din varful stivei (fie el O) nu este paranteza-stanga:
                //1.5.1.1 extrage O si adauga-l la forma postfixata
                while (!stivaOperatori.isEmpty() && !(stivaOperatori.getLast().equals("("))) {
                    output.append(stivaOperatori.pollLast() + " ");

                }
                if (!stivaOperatori.isEmpty() && stivaOperatori.getLast().equals("(")) {
                    ver = true;
                }

                //1.5.2 daca stiva s-a golit (si nu s-a gasit o paranteza-stanga)
                //1.5.2.1 returneaza eroare (i.e. expresia avea paranteze gresite)
                if ((stivaOperatori.isEmpty()) && (!ver)) {
                    System.out.println("Eroare - expresia are paranteze gresite, lipsesc paranteza/e \"(\"");
                    output.replace(0, output.length(), "Incercati o alta expresie");
                    break;
                }

                // 1.5.3 extrage paranteza-stanga din varful stivei
                else if (stivaOperatori.getLast().equals("(")) {
                    stivaOperatori.pollLast();
                }
            }
        }

        //2 cat timp exista operator in stiva (fie el O)
        while (!stivaOperatori.isEmpty()) {
            //2.1 extrage O si adauga-l la forma postfixata
            if (!(stivaOperatori.getLast().equals("(")))
                output.append(stivaOperatori.pollLast() + " ");

                //2.2 daca O este o paranteza-stanga, atunci:
                //2.2.1 returneaza eroare (i.e. expresia avea paranteze gresite)
            else {
                System.out.println("Eroare - expresia are paranteze gresite,  lipsesc paranteza/e \")\"");
                output.replace(0, output.length(), "Incercati o alta expresie");
                break;
            }
        }
        //3 afiseaza / returneaza forma postfixata
        return output;
    }

    static int execution(String[] postfix) {
        Deque<Integer> stiva = new LinkedList<>();
        int op1 = 0, op2 = 0, result = 0;
        for (int i = 0; i < postfix.length; i++) {

            String index = postfix[i];
            //1.2. daca entitatea este un operand (i.e. un numar), atunci:
            // 1.2.1 adauga entitatea in stiva
            if (index.equals(" ")) {
                continue;
            }
            if (!operators.containsKey(index)) {
                stiva.addFirst(Integer.parseInt(index));
                continue;
            }

            //1.3 daca entitatea este un operator (fie el O), atunci:
            else {
                //1.3.1 extrage un operand din stiva (fie el op1)
                // 1.3.2 extrage un operand din stiva (fie el op2)

                if (stiva.size() >= 1) {
                    //System.out.println("marime stiva " + stiva.size());
                    op1 = stiva.pollFirst();
                    //System.out.println("op1: " + op1);
                    op2 = stiva.pollFirst();
                   // System.out.println("op2: " + op2);
                    result = 0;

                    //1.3.4 rezultat = op2 O op1
                    switch (index) {
                        case "+":
                            result = op2 + op1;
                            System.out.println("Rezultatul intre o1 + o2: " + result);
                            break;
                        case "-":
                            result = op2 - op1;
                            System.out.println("Rezultatul intre o1 - o2: " + result);
                            break;
                        case "/":
                            result = op2 / op1;
                            System.out.println("Rezultatul intre o1 / o2: " + result);
                            break;
                        case "*":
                            result = op1 * op2;
                            System.out.println("Rezultatul intre o1 * o2: " + result);
                            break;
                        case "^":
                            result = op2;
                            for (int j = 1; j < op1; j++) {
                                result *= op2;
                            }
                            System.out.println("Rezultatul intre o2 la puterea o1: " + result);
                            break;
                        default:
                            System.out.println("operator neidentificat");
                            break;
                    }

                    //1.3.5 adauga rezultat in stiva
                    stiva.addFirst(result);

                    //1.3.3 daca nu exista cei 2 operanzi (i.e. op1 si op2), atunci:
                    // 1.3.3.1 returneaza eroare (i.e. expresia postfixata este gresita)
                } else {
                    System.out.println("Expresia postfixata este gresita");
                }
            }
        }

        // System.out.println(result);
        // 2 rezultat = extrage un operator din stiva
        int finRez = stiva.poll();

        //3 daca stiva nu este goala, atunci:
        //3.1. returneaza eroare (i.e. expresia postfixata este gresita)
        if (!stiva.isEmpty()) {
            System.out.println("Expresia postfixata este gresita");
        }

        //4 afiseaza / returneaza rezultat
        return finRez;
    }
    
    public static void main(String[] args) {

        Operanzi();

        Scanner scan = new Scanner(System.in);
        System.out.println("Evaluator de expresii\n");

        System.out.println("Scrieti expresia matematica");
        String expresie = scan.nextLine();

        StringBuilder postfix = postfix(toStringArray(expresie));

        int calcul = execution(toStringArray(postfix.toString()));
        System.out.println("Input: " + expresie + "\nForma postfix este: " + postfix +
                "\nRezultatul final este: "+ calcul );
        scan.close();
    }
}
