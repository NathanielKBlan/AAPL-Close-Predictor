package com.codingbeyond.machinelearning;

import com.codingbeyond.linearalgebra.LinearAlgebra;
import com.codingbeyond.stats.Statistics;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;

public class LinearRegression {

    private ArrayList<Double> xTerms;
    private ArrayList<Double> yTerms;
    private double r;
    private double m;
    private double b;
    private double SSE;

    public LinearRegression(ArrayList<Double> xTerms, ArrayList<Double> yTerms){
        train(xTerms, yTerms);
    }

    private double calculateSSE(ArrayList<Double> xTerms, ArrayList<Double> yTerms){

        double[][] xVec = {{m},{b}};
        double[][] A = new double[xTerms.size()][2];
        double[][] bVec = new double[yTerms.size()][1];

        for(int i = 0; i < xTerms.size(); i++){
            for(int j = 0; j < 2; j++){
                if(j == 0){
                    A[i][j] = xTerms.get(i);
                }else{
                    A[i][j] = 1;
                }
            }
        }

        for(int i = 0; i < yTerms.size(); i++){
            double[] row = new double[1];
            row[0] = yTerms.get(i);
            bVec[i] = row;
        }

        double[][] Ax = LinearAlgebra.multiplyMatrices(A, xVec, A.length, A[0].length, 1);

        double[][] diff = LinearAlgebra.subtractMatrices(Ax, bVec, Ax.length, Ax[0].length);

        double sum = 0;
        for(int i = 0; i < diff.length; i++){
            sum += Math.pow(diff[i][0], 2);
        }

        return sum;
    }

    private double calculateCorrelationCoef(ArrayList<Double> xTerms, ArrayList<Double> yTerms){

        double stdX = Statistics.std(xTerms);
        double avgX = Statistics.mean(xTerms);

        ArrayList<Double> xValues = new ArrayList<>();
        for(double i : xTerms){
            xValues.add((i - avgX) / stdX);
        }

        double stdY = Statistics.std(yTerms);
        double avgY = Statistics.mean(yTerms);

        ArrayList<Double> yValues = new ArrayList<>();
        for(double i : yTerms){
            yValues.add((i - avgY) / stdY);
        }

        ArrayList<Double> prod = new ArrayList<>();
        for(int i = 0; i < xValues.size(); i++){
            prod.add(xValues.get(i) * yValues.get(i));
        }

        return (1.0 / xTerms.size()) * Statistics.sum(prod);

    }

    public void train(ArrayList<Double> xTerms, ArrayList<Double> yTerms){
        this.xTerms = xTerms;
        this.yTerms = yTerms;
        r = calculateCorrelationCoef(xTerms, yTerms);
        m = r * (Statistics.std(yTerms)/Statistics.std(xTerms));
        b = Statistics.mean(yTerms);
        SSE = calculateSSE(xTerms, yTerms);
    }

    public double estimateValue(double xVal){

        double yVal = m * (xVal - Statistics.mean(xTerms)) + b;

        return yVal;
    }

    public void saveModel(String modelName){

        String path = "Models/" + modelName + ".model";

        try {

            Gson gson = new Gson();
            FileWriter writer = new FileWriter(path);
            gson.toJson(this, writer);
            writer.flush();
            writer.close();

        }catch (IOException e){

            System.out.println("Error: File path not available.");

        }


    }

    private double lookUpValue(double x){

        return yTerms.get(xTerms.indexOf(x));

    }

    private boolean exists(double x){

        return xTerms.contains(x);

    }

    public static LinearRegression loadModel(String modelName){

        String path = "Models/" + modelName + ".model";

        try {

            Gson gson = new Gson();
            JsonReader jReader = new JsonReader(new FileReader(path));
            return gson.fromJson(jReader, LinearRegression.class);

        }catch (FileNotFoundException e){

            System.out.println("Error: No such file.");

        }

        return null;
    }

    public double getCorrelationCoef() { return r; }

    public double getSSE(){ return SSE; }

    public ArrayList<Double> getTrainingInputs() { return xTerms; }
    public ArrayList<Double> getTrainingOutputs() { return yTerms; }

}
