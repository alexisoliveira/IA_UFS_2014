import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Naive_Bayes {

  public static void main(String args[]) throws Exception {

	Instances dados = DataSource.read("arq_bayes.arff");
	  
	dados.setClassIndex(dados.numAttributes() - 1);
    
    NaiveBayes nb = new NaiveBayes();
    nb.buildClassifier(dados);   
    
    Evaluation eval = new Evaluation(dados);
    eval.crossValidateModel(nb, dados, 10, new Random(1));
    
    System.out.println(" Número de atributos: " + dados.numAttributes());
    System.out.println("Número de instâncias: " + eval.numInstances());
    System.out.println(" Instâncias corretas: " + eval.correct());
    System.out.println("            Acurácia: " + (1-eval.errorRate()));
    System.out.println("  Matriz de confusão: " + eval.toMatrixString().substring(25));
    System.out.println("            Precisão: " + "a = "+eval.precision(0) + "; b = "+eval.precision(1) + "; c = "+eval.precision(2));
    System.out.println("       Sensibilidade: " + "a = "+eval.recall(0) + "; b = "+eval.recall(1) + "; c = "+eval.recall(2));
    System.out.println("            Medida-F: " + "a = "+eval.fMeasure(0) + "; b = "+eval.fMeasure(1) + "; c = "+eval.fMeasure(2));;
    System.out.println("Área sob curva ROC: " + eval.areaUnderROC(0));
    
    //instanciando novo arquivo
    String novabase = "arq_bayes_teste.arff";
    Instances novas = new Instances(
            new BufferedReader(
                new FileReader(novabase)));
	novas.setClassIndex(novas.numAttributes() - 1);
	
	Instances novas_com_classe = new Instances(novas);
	for (int i = 0; i < novas.numInstances(); i++) {
		double clsLabel = nb.classifyInstance(novas.instance(i));
		novas_com_classe.instance(i).setClassValue(clsLabel);
		
		//imprimindo resultado da classificação
		System.out.println("CLASSIFICACAO -----------------------");
		System.out.println(novas_com_classe.instance(i).toString());
	}    
  }
}

