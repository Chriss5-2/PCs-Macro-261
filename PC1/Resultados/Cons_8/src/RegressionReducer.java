package SalesCountry;

import java.io.*;
import java.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

public class RegressionReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

    private static final Map<String, Integer> MESES = crearMapaMeses();

    private static class PuntoSerie {
        int tiempo;
        double nac;
        double def;

        PuntoSerie(int tiempo, double nac, double def) {
            this.tiempo = tiempo;
            this.nac = nac;
            this.def = def;
        }
    }

    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        List<PuntoSerie> serie = new ArrayList<>();
        double promNacRef = -1;
        double promDefRef = -1;

        while (values.hasNext()) {
            String[] p = values.next().toString().split("\\|");
            if (p.length < 5) continue;

            int tiempo = parseTiempo(p[0]);
            if (tiempo < 0) continue;

            try {
                double nac = Double.parseDouble(p[1]);
                double def = Double.parseDouble(p[2]);

                if (!"N/A".equalsIgnoreCase(p[3])) {
                    promNacRef = Double.parseDouble(p[3]);
                }
                if (!"N/A".equalsIgnoreCase(p[4])) {
                    promDefRef = Double.parseDouble(p[4]);
                }

                serie.add(new PuntoSerie(tiempo, nac, def));
            } catch (NumberFormatException ex) {
                // Ignora registros mal formados
            }
        }

        if (serie.isEmpty()) return;

        Collections.sort(serie, new Comparator<PuntoSerie>() {
            @Override
            public int compare(PuntoSerie a, PuntoSerie b) {
                return Integer.compare(a.tiempo, b.tiempo);
            }
        });

        List<Double> x = new ArrayList<>();
        List<Double> yNac = new ArrayList<>();
        List<Double> yDef = new ArrayList<>();

        for (PuntoSerie p : serie) {
            x.add((double) p.tiempo);
            yNac.add(p.nac);
            yDef.add(p.def);
        }

        if (promNacRef <= 0) promNacRef = promedio(yNac);
        if (promDefRef <= 0) promDefRef = promedio(yDef);

        double[] modelNac = calcularRegresion(x, yNac);
        double[] modelDef = calcularRegresion(x, yDef);

        String[] meses2026 = {"MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};

        for (String mes : meses2026) {
            int xFuturo = toTiempo(2026, mes);
            if (xFuturo < 0) continue;

            double predNac = (modelNac[0] * xFuturo) + modelNac[1];
            double predDef = (modelDef[0] * xFuturo) + modelDef[1];

            predNac = Math.max(0, predNac);
            predDef = Math.max(0, predDef);

            String perfil = "ESTABLE";
            if (predNac < promNacRef && predDef > promDefRef) perfil = "MES_CRITICO";
            else if (predNac > promNacRef) perfil = "ALTA_NATALIDAD";
            else if (predDef > promDefRef) perfil = "ALTA_MORTALIDAD";

            String fecha = "2026-" + mes;
            String reporte = String.format("Nac:%.0f (Prom:%.1f) | Def:%.0f (Prom:%.1f) | PERFIL:%s", 
                                            predNac, promNacRef, predDef, promDefRef, perfil);

            output.collect(new Text(fecha), new Text(reporte));
        }
    }

    private static Map<String, Integer> crearMapaMeses() {
        Map<String, Integer> meses = new HashMap<>();
        meses.put("ENERO", 1);
        meses.put("FEBRERO", 2);
        meses.put("MARZO", 3);
        meses.put("ABRIL", 4);
        meses.put("MAYO", 5);
        meses.put("JUNIO", 6);
        meses.put("JULIO", 7);
        meses.put("AGOSTO", 8);
        meses.put("SEPTIEMBRE", 9);
        meses.put("OCTUBRE", 10);
        meses.put("NOVIEMBRE", 11);
        meses.put("DICIEMBRE", 12);
        return meses;
    }

    private int parseTiempo(String fecha) {
        String[] p = fecha.split("-");
        if (p.length != 2) return -1;
        try {
            int anio = Integer.parseInt(p[0].trim());
            return toTiempo(anio, p[1].trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private int toTiempo(int anio, String mes) {
        Integer nMes = MESES.get(mes.toUpperCase());
        if (nMes == null) return -1;
        return (anio * 12) + nMes;
    }

    private double promedio(List<Double> valores) {
        if (valores.isEmpty()) return 0;
        double suma = 0;
        for (double v : valores) suma += v;
        return suma / valores.size();
    }

    private double[] calcularRegresion(List<Double> xData, List<Double> yData) {
        int n = Math.min(xData.size(), yData.size());
        if (n == 0) return new double[]{0, 0};
        if (n == 1) return new double[]{0, yData.get(0)};

        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
        for (int i = 0; i < n; i++) {
            double x = xData.get(i);
            double y = yData.get(i);
            sumX += x; sumY += y; sumXY += (x * y); sumX2 += (x * x);
        }

        double denominador = (n * sumX2 - sumX * sumX);
        if (denominador == 0) return new double[]{0, sumY / n};

        double m = (n * sumXY - sumX * sumY) / denominador;
        double b = (sumY - m * sumX) / n;
        return new double[]{m, b};
    }
}