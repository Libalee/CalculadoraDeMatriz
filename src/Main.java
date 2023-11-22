import entities.Matriz;

public class Main {
    public static void main(String[] args) {

        Matriz matriz = new Matriz(4, 4);
        Matriz matriz1 = new Matriz(2, 2);

        for(int i = 0; i < matriz.getnumLinhas(); i++) {
            for(int j = 0; j < matriz.getnumColunas(); j++) {
                matriz.addElementoMatriz(i, j, (double) i + j + 1);
            }
        }

        matriz1.addElementoMatriz(0,0, 9.0);
        matriz1.addElementoMatriz(1,0, 8.0);
        matriz1.addElementoMatriz(0,1, 7.0);
        matriz1.addElementoMatriz(1,1, 6.0);


        System.out.println(matriz.getMatriz());
        System.out.println(matriz.determinanteMatriz());


    }
}
