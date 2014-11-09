

import java.awt.Color;
import javax.swing.JFrame;
import org.math.plot.*;

public class LinearRegressionGradDescent
{
	
//static double[] x = {5,4,3,7,11,9,6,3,12,7,14,12,10,10,14,9,7,18,6,31,13,20,19,10,4,16,9,6,6,21,15,17,7,13,8,28,23,22,23,7,16,2,6,3,8};
//static double[] y = {41.2,31.7,39.4,57.5,74.8,59.8,54.3,47.9,77.2,56.6,80.9,34.3,53.1,55.4,57.8,62.8,67.3,56.7,37.6,129.9,70.3,104.2,83.6,66.0,52.3,86.9,66.6,40.1,55.7,58.1,74.3,98.1,40.7,66.7,48.0,122.5,92.1,76.0,97.5,33.8,90.5,29.7,28.0,51.6,55.7};
		
static double[] x = {2, 4, 6, 8, 9, 12, 1, 2, 15};
static double[] y = {2, 5, 5, 8, 10,11, 2, 3, 13};

static int linhaprevisao;

// parametros
static double w0;
static double w1;

public static void main(String[] args)
{
   double alfa = 0.01;  // taxa de aprendizado
   double tolerancia = 1e-11;   // toleranciaerancia para convergencia
   int maxiter = 9000;   // # max de iteracoes
   int intervalo = 1;   // intervalo para mostrar resultados durante iteracoes
   double delta0, delta1; //diferenca para atualizacao dos pesos
   int iters = 0;
   // pesos iniciais
   w0 = 0;
   w1 = 0;
   // guardar resultados parciais para plotagem
   double[] w0plot = new double[maxiter+1];
   double[] w1plot = new double[maxiter+1];
   double[] MSEplot = new double[maxiter+1];
   double[] tplot = new double[maxiter+1];
   // cria painel de plotagem
   Plot2DPanel plot = new Plot2DPanel();
   plot.addScatterPlot("X-Y", Color.BLUE, x, y);
   // mostra a linha de tendencia
   addlinhaprevisao(plot, false);
   JFrame frame = new JFrame("Dados X-Y originais");
   frame.setContentPane(plot);
   frame.setSize(1000, 1000);
   frame.setVisible(true);
   
   do {
      // armazena dados para plotagem da convergencia dos pesos
      tplot[iters] = iters;
      w0plot[iters] = w0;
      w1plot[iters] = w1;      
      // armazena dados para plotagem da convergencia do erro
      MSEplot[iters] = MSE();
      
      iters++;
      
      delta1 = alfa * dEdw1();
      delta0 = alfa * dEdw0();

      w1 = w1 - delta1;
      w0 = w0 - delta0;

      // imprime progresso
      if (iters % intervalo == 0) {
         System.out.println("Iteracao " + iters + ": w0=" + w0 + " - " + delta0 + ", w1=" + w1 + " - "+ delta1);
         System.out.println("Erro na iteracao " + iters + "= " + MSE());     
         addlinhaprevisao(plot, false);
      }

      if (iters > maxiter) break;
   } while (Math.abs(delta1) > tolerancia || Math.abs(delta0) > tolerancia);

   // Resultado da convergencia
   System.out.println("\nConvergencia apos " + iters + " iteracoes: w0=" + w0 + ", w1=" + w1);
   addlinhaprevisao(plot, true);

   // Plota a convergencia dos pesos com o tempo
   double[] w0plot2 = new double[iters];
   double[] w1plot2 = new double[iters];
   double[] tplot2 = new double[iters];
   System.arraycopy(w0plot, 0, w0plot2, 0, iters);
   System.arraycopy(w1plot, 0, w1plot2, 0, iters);
   System.arraycopy(tplot, 0, tplot2, 0, iters);
   Plot2DPanel convPlot = new Plot2DPanel();
   convPlot.addLinePlot("w0", tplot2, w0plot2);
   convPlot.addLinePlot("w1", tplot2, w1plot2);
   JFrame frame2 = new JFrame("Convergencia dos pesos com o tempo");
   frame2.setContentPane(convPlot);
   frame2.setSize(1000, 1000);
   frame2.setVisible(true);
   
// Plota a convergencia do erro (MSE) com o tempo
   double[] tplot3 = new double[iters];
   double[] MSEplot3 = new double[iters];
   System.arraycopy(tplot, 0, tplot3, 0, iters);
   System.arraycopy(MSEplot, 0, MSEplot3, 0, iters);
   Plot2DPanel erroPlot = new Plot2DPanel();
   erroPlot.addLinePlot("MSE(w0,w1)", tplot3, MSEplot3);
   JFrame frame3 = new JFrame("Convergencia do erro (MSE) com o tempo");
   frame3.setContentPane(erroPlot);
   frame3.setSize(1000, 1000);
   frame3.setVisible(true);
   
   // System.exit(0);
}

public static double h(double x) {
	   return w0 + w1*x;
}

public static double dEdw1() {
   double sum = 0;
   for (int j=0; j<x.length; j++) {
      sum += (h(x[j]) - y[j]) * x[j];
   }
   return 2 * sum / x.length;
}

public static double dEdw0() {
   double sum = 0;
   for (int j=0; j<x.length; j++) {
      sum += (h(x[j]) - y[j]) * 1;
   }
   return 2 * sum / x.length;
}

public static double MSE() {
	   double sum = 0;
	   for (int j=0; j<x.length; j++) {
	      sum += (h(x[j]) - y[j]) * (h(x[j]) - y[j]);
	   }
	   return sum / x.length;
	}

public static void addlinhaprevisao(Plot2DPanel plot, boolean ultimo)
{
   double[] yEnd = new double[x.length];
   for (int i=0; i<x.length; i++)
	   yEnd[i] = h(x[i]);
   if (ultimo)
	   linhaprevisao = plot.addLinePlot("previsao", Color.RED, x, yEnd);
   else
	   linhaprevisao = plot.addLinePlot("previsao", Color.LIGHT_GRAY, x, yEnd);
}


}
