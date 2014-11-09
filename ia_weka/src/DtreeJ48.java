import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;
import weka.classifiers.Evaluation;
import java.util.Random;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFrame;

public class DtreeJ48 {

  public static void main(String args[]) throws Exception {

    // faz leitura da base e especifica atributo de classe
	// o arquivo .arff � auto-explicativo: basta abrir em qualquer editor de texto
	  
    //EXEMPLO 1: base iris com tr�s classes diferentes
	Instances dados = DataSource.read("iris.arff");
	  
	//EXEMPLO 2: base vehicle com quatro classes diferentes
    //Instances dados = DataSource.read("vehicle.arff");
    
    dados.setClassIndex(dados.numAttributes() - 1);
    
    // treina classificador com algoritmo C4.5
    J48 dtree = new J48();
    dtree.buildClassifier(dados);
    
    // desenha �rvore de decis�o em um frame
    TreeVisualizer tv = new TreeVisualizer(null, dtree.graph(), new PlaceNode2());
    JFrame jf = new JFrame("�rvore de Decis�o resultante");
    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    jf.setSize(1024, 768);
    jf.getContentPane().setLayout(new BorderLayout());
    jf.getContentPane().add(tv, BorderLayout.CENTER);
    jf.setVisible(true);
    tv.fitToScreen();
    
    // aplica valida��o cruzada
    Evaluation eval = new Evaluation(dados);
    eval.crossValidateModel(dtree, dados, 10, new Random(1));
    
    // imprime resultados de m�tricas
    System.out.println(" N�mero de atributos: " + dados.numAttributes());
    System.out.println("N�mero de inst�ncias: " + eval.numInstances());
    System.out.println(" Inst�ncias corretas: " + eval.correct());
    System.out.println("            Acur�cia: " + (1-eval.errorRate()));
    System.out.println("  Matriz de confus�o: " + eval.toMatrixString().substring(25));
    System.out.println("            Precis�o: " + "a = "+eval.precision(0) + "; b = "+eval.precision(1) + "; c = "+eval.precision(2));
    System.out.println("       Sensibilidade: " + "a = "+eval.recall(0) + "; b = "+eval.recall(1) + "; c = "+eval.recall(2));
    System.out.println("            Medida-F: " + "a = "+eval.fMeasure(0) + "; b = "+eval.fMeasure(1) + "; c = "+eval.fMeasure(2));;
    System.out.println("  �rea sob curva ROC: " + eval.areaUnderROC(0));
    
    
    // atribui classes a novas inst�ncias ainda n�o classificadas presentes em outro arquivo: novas.arff
    // a p�s atribuir as classes, grava estas inst�ncias no arquivo novas_com_classe.arff
    
    //EXEMPLO 1: flores iris desconhecidas
    String novabase = "iris_novas.arff";
    //EXEMPLO 2: ve�culos desconhecidos
    //String novabase = "vehicle_novos.arff";
    
    Instances novas = new Instances(
                              new BufferedReader(
                                  new FileReader(novabase)));
    novas.setClassIndex(novas.numAttributes() - 1);
    Instances novas_com_classe = new Instances(novas);
    for (int i = 0; i < novas.numInstances(); i++) {
    	double clsLabel = dtree.classifyInstance(novas.instance(i));
    	novas_com_classe.instance(i).setClassValue(clsLabel);
    }
    BufferedWriter writer = new BufferedWriter(
              new FileWriter("novos_com_classe.arff"));
    writer.write(novas_com_classe.toString());
    writer.newLine();
    writer.flush();
    writer.close();
  }
}

