
import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

public class LinearRegressionWeka {
	public static void main(String args[]) throws Exception{
		//ler dados
		Instances data = new Instances(
								new BufferedReader(
										new FileReader("cirrose.arff")));
		data.setClassIndex(data.numAttributes() - 1);
		//treinar modelo
		LinearRegression model = new LinearRegression();
		model.buildClassifier(data);
		System.out.println(model);
		//classificar ultima instancia
		Instance me = data.lastInstance();
		double deathrate = model.classifyInstance(me);
		System.out.println("Death rate ("+me+"): "+deathrate);
		}
}
