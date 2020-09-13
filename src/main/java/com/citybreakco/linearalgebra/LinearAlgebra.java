package com.citybreakco.linearalgebra;

public class LinearAlgebra {

    public static double[][] multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix, int r1, int c1, int c2) {
        double product[][] = new double[r1][c2];
        for(int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return product;
    }

    public static double[][] subtractMatrices(double[][] firstMatrix, double[][] secondMatrix, int rows, int columns){

        double[][] product = new double[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                product[i][j] = firstMatrix[i][j] - secondMatrix[i][j];
            }
        }
        return product;

    }

    public static double[][] addMatrices(double[][] firstMatrix, double[][] secondMatrix, int rows, int columns){

        double[][] product = new double[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                product[i][j] = firstMatrix[i][j] + secondMatrix[i][j];
            }
        }
        return product;

    }

    public static void printMatrix(double[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.println();
        }
    }
}
