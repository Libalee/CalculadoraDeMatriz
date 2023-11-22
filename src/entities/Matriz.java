package entities;

import excepetions.IllegalOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Matriz {

    private int numLinhas;
    private int numColunas;

    private List<List> matriz;

    // Gera uma matriz com o número de linhas e colunas dados
    public Matriz(int numLinhas, int nunColunas) {
        this.numLinhas = numLinhas;
        this.numColunas = nunColunas;
        this.matriz = new ArrayList<>(nunColunas);
        for (int i = 0; i < numLinhas; i++){
            matriz.add(new LinkedList<Double>(Arrays.asList(new Double[numLinhas])));
        }
        for(int i = 0; i < numLinhas; i++) {
            for(int j = 0; j < nunColunas; j++) {
                matriz.get(j).remove(i);
                matriz.get(j).add(i, 0.0);
            }
        }
    }

    // Adiciona elementos a matriz
    public void addElementoMatriz (int linha, int coluna, Double valor){
        matriz.get(coluna).remove(linha);
        matriz.get(coluna).add(linha, valor);
    }

    // Remove o elemento da matriz
    public void remElementoMatriz(int linha, int coluna) {
        matriz.get(coluna).remove(linha);
        matriz.get(coluna).add(linha, 0.0);
    }

    // Remove uma linha da matriz
    public void removeLinha(int linha) {
        matriz.remove(linha);

        this.numLinhas = this.getnumLinhas() - 1;
    }

    // Remove uma coluna da matriz
    public void removeColuna(int coluna) {
        for (int i = 0; i < this.numColunas; i++) {
                matriz.get(i).remove(coluna);
        }
        this.numColunas = this.getnumColunas() - 1;
    }

    // Retorna uma copia da matriz na qual o metodo foi chamado
    public Matriz copiaMatriz(){
        Matriz matriz1 = new Matriz(this.getnumLinhas(), this.getnumColunas());

        for(int i = 0; i < this.getnumColunas(); i++) {
            for(int j = 0; j < this.getnumLinhas(); j++) {
                matriz1.addElementoMatriz(i, j, this.getElementoMatriz(i, j));
            }
        }
        return matriz1;
    }

    // Soma os elementos da matriz inserida na matriz do qual o metodo foi chamado.
    public void somarMatriz(Matriz matriz2){
        // Verifica se as matrizes tem o mesmo numero de linhas e colunas
        if(!(this.numLinhas == matriz2.getnumLinhas() && this.getnumColunas() == matriz2.getnumColunas())){
            throw new IllegalOperation();
        }
        for(int i = 0; i < this.numColunas; i++){
            for (int j = 0; j < this.numLinhas; j++){
                // Soma os elementos das matrizes e substitui os valores da matriz atual
                Double valor = this.getElementoMatriz(i, j) + matriz2.getElementoMatriz(i, j);
                this.addElementoMatriz(i, j, valor);
            }
        }
    }

    // Multiplica ambas as matrizes e modifica a matriz na qual o metodo foi chamado.
    // Lembrando que as matrizes tem que ter o msm número de colunas e linhas
    // e que a multiplicação de matriz nao é comutativa, então pode acontecer de
    // matriz1.MultiplicaMatriz(matriz2) != matriz2.MultiplicaMatriz(matriz1)
    public void multiplicaMatriz(Matriz matriz1){
        if(!(this.getnumColunas() == matriz1.getnumLinhas())) {
           throw new IllegalOperation();
        }
        Matriz matrizTemp = new Matriz(this.getnumLinhas(), matriz1.getnumColunas());
        Double soma = 0.0;
        // Processo de multiplicação da matriz
        for(int i = 0; i < matrizTemp.getnumColunas(); i++) {
            for(int j = 0; j < matrizTemp.getnumLinhas(); j++){
                for(int n = 0; n < matriz1.getnumLinhas(); n++){
                    soma = soma + this.getElementoMatriz(n , j) * matriz1.getElementoMatriz(i, n);
                }
                matrizTemp.addElementoMatriz(i, j, soma);
                soma = 0.0;
            }
        }
        this.matriz = matrizTemp.getMatriz();
    }

    // Calcula determinante de uma matriz 2 por 2
    private Double determinanteDoisPorDois(){
        if(this.numLinhas != 2 || this.numColunas != 2) {
            throw new IllegalOperation();
        }
        Double det;
        det = (this.getElementoMatriz(0,0) * this.getElementoMatriz(1,1)) -
                (this.getElementoMatriz(0,1) * this.getElementoMatriz(1, 0));
        return det;
    }

    // Determinante calculado utlizando a formula de Laplace
    // Mais informações: https://pt.wikipedia.org/wiki/Determinante
    public Double determinanteMatriz(){
        if(this.getnumLinhas() != this.getnumColunas()) {
            throw new IllegalOperation();
        }
        // Caso de matriz com um elemento
        if(this.getnumColunas() == 1){
            return this.getElementoMatriz(0, 0);
        }
        // Caso de matriz com 2 linhas e colunas
        if(this.getnumColunas() == 2){
            return this.determinanteDoisPorDois();
        }

        // Caso de matriz com n linhas e colunas
        Double det = 0.0;

        List<Matriz> list = new LinkedList<>();
        list.add(this.copiaMatriz());

        // Percorrendo todas as matrizes dentro da lista
        while(!(list.isEmpty())) {
            for(int i = 0; i < list.size(); i++) {

                // Listas auxiliares
                List<Double> doubles = new LinkedList<>();
                List<Matriz> listAux = new LinkedList<>();

                // Pegando os elementos da primeira linha
                for(int j = 0; j < list.get(i).getnumLinhas(); j++) {
                    doubles.add(list.get(i).getLinha(0).get(j));
                }

                // Calculando as matrizes que derivam da matriz original
                for(int j = 0; j < list.get(i).getnumLinhas(); j++) {
                    Matriz matriz1 = list.get(i).copiaMatriz();
                    // Copiando os elementos da matriz original para as matrizes secundarias
                    matriz1.removeColuna(j);
                    matriz1.removeLinha(0);
                    listAux.add(matriz1);
                }

                // Verificando se as matrizes derivam da matriz original tem 2 linhas e colunas
                // Como todas tem o mesmo tamanho podemos fazer apenas a verifiação da primeira
                if(2 == listAux.get(0).getnumLinhas()){
                    for (int j = 0; j < listAux.size(); j++){
                        det += Math.pow(-1, j) * listAux.get(j).determinanteDoisPorDois() * doubles.get(j);
                    }
                    // Removendo a matriz original da list já que seu determinante foi calculado
                    list.remove(i);
                } else {
                    // Removendo a matriz original da list já que calcularemos o determinante de suas matrizes
                    // Derivadas
                    list.remove(i);
                    for(int j = 0; j < listAux.size(); j++) {
                        for(int k = 0; k < listAux.get(0).getnumLinhas(); k++) {
                            // Adicionando o valor que multiplica o determinante dentro dele
                            Double aux = listAux.get(i).getElementoMatriz(0, k) * doubles.get(j);
                            listAux.get(i).addElementoMatriz(0, k, aux);
                        }
                    }
                    // Adicionando as matrizes derivadas da original na lista para que seja calculado o determinante
                    // delas
                    for(int j = 0; j < listAux.size(); j++) {
                        list.add(listAux.get(j));
                    }
                }
            }
        }

        return det;
    }

    public int getnumLinhas() {
        return numLinhas;
    }

    public int getnumColunas() {
        return numColunas;
    }

    // Retorna a linha em forma de lista
    public List<Double> getLinha (int linha){
        return this.getMatriz().get(linha);
    }

    // Retorna a coluna em forma de lista
    public List<Double> getColuna(int coluna) {
        List<Double> list = new LinkedList<>();
        for(int i = 0; i < this.getnumLinhas(); i++){
            Double elemento = this.getLinha(i).get(coluna);
            list.add(elemento);
        }
        return list;
    }

    // Retorna um elemento especifico da matriz
    public Double getElementoMatriz(int linha, int coluna) {
        return (Double) matriz.get(coluna).get(linha);
    }

    // Retorna a matriz em forma de lista com as linhas dentro das colunas
    public List<List> getMatriz() {
        return matriz;
    }
}
