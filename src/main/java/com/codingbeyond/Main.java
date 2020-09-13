package com.codingbeyond;

import com.codingbeyond.machinelearning.LinearRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Uncomment this block and comment rest of code to train model
        /*ArrayList<Double> openList = new ArrayList<>();
        ArrayList<Double> closeList = new ArrayList<>();

        try{

            BufferedReader csvReader = new BufferedReader(new FileReader("Resources/apple_stock.csv"));
            String row = csvReader.readLine();
            while ((row = csvReader.readLine()) != null){
                String data[] = row.split(",");
                openList.add(Double.parseDouble(data[2]));
                closeList.add(Double.parseDouble(data[5]));
            }

        }catch (FileNotFoundException e){

            System.out.println("Error: No such file found.");

        }catch (IOException e){

            System.out.println("Error: Something went wrong while reading data.");

        }*/

        //LinearRegression regressionModel = new LinearRegression(openList, closeList);
        LinearRegression regressionModel = LinearRegression.loadModel("apple_close");

        Scanner sc = new Scanner(System.in);

        System.out.print("How much shares do you hold of Apple? ");

        int numShares = Integer.parseInt(sc.nextLine());

        System.out.print("What was today's open price? ");

        double open = Double.parseDouble(sc.nextLine());

        sc.close();

        System.out.println("Open Price: $" + open + ", Predicted Close Price: $" + (Math.round(regressionModel.estimateValue(open) * 100.0) / 100.0) + ", Model correlation coefficient: " + regressionModel.getCorrelationCoef() + ", SSE: " + regressionModel.getSSE());
        System.out.println("Estimated Gain/Loss for the day: $" + ((Math.round((regressionModel.estimateValue(open) - open) * numShares) * 100.0) / 100.0));
        regressionModel.saveModel("apple_close");

        XYSeries series = new XYSeries("Open and Close Prices");

        ArrayList<Double> trainingInputs = regressionModel.getTrainingInputs();
        ArrayList<Double> trainingOutputs = regressionModel.getTrainingOutputs();

        for(int i = 0; i < trainingInputs.size(); i++){
            series.add(trainingInputs.get(i), trainingOutputs.get(i));
        }

        XYSeries prediction = new XYSeries("Prediction");
        prediction.add(open, (Math.round(regressionModel.estimateValue(open) * 100.0) / 100.0));

        XYSeries bestFit = new XYSeries("Best Fit Line");
        for(int i = 0; i < 500; i++){
            bestFit.add(i, (Math.round(regressionModel.estimateValue(i) * 100.0) / 100.0));
        }

        XYSeriesCollection datasetScatter = new XYSeriesCollection();
        datasetScatter.addSeries(series);
        datasetScatter.addSeries(prediction);
        datasetScatter.addSeries(bestFit);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Close Price Prediction",
                "Open Price",
                "Close Price",
                datasetScatter);

        XYPlot plot = (XYPlot)chart.getPlot();

        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(1, Color.green);
        renderer.setSeriesPaint(2, Color.blue);
        renderer.setSeriesStroke(2, new BasicStroke( 1.0f ) );

        double size = 5.0;
        double delta = size / 2.0;
        Shape shape = new Ellipse2D.Double(-delta, -delta, size, size);
        renderer.setSeriesShape(0, shape);

        double size1 = 10.0;
        double delta1 = size1 / 2.0;
        Shape shape1 = new Rectangle2D.Double(-delta1, -delta1, size1, size1);
        renderer.setSeriesShape(1, shape1);

        double size2 = 1.0;
        double delta2 = size2 / 2.0;
        Shape shape2 = new Ellipse2D.Double(-delta2, -delta2, size2, size2);
        renderer.setSeriesShape(2, shape2);

        renderer.setSeriesShapesVisible(2, false);
        renderer.setDefaultShape(shape);
        renderer.setDefaultShapesFilled(true);
        renderer.setDefaultShapesFilled(true);
        renderer.setSeriesLinesVisible(2, true);

        ChartPanel chartPanel = new ChartPanel(chart);

        JFrame frame = new JFrame("APPL Open vs Close");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

}
