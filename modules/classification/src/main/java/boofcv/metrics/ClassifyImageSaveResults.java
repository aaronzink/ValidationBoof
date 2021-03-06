package boofcv.metrics;

import boofcv.abst.scene.ImageClassifier;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageBase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Classifies images and saves the results.  Can be used to detect changes.
 *
 * @author Peter Abeles
 */
public class ClassifyImageSaveResults<T extends ImageBase<T>> {

	List<File> images = new ArrayList<>();
	PrintStream out;

	public ClassifyImageSaveResults() {
		images.add( new File("data/recognition/image_classification/outdoors01.jpg"));
	}

	public void process(ImageClassifier<T> alg) {
		for( File f : images ) {
			out.println("Processing "+f.getName());
			BufferedImage buffered = UtilImageIO.loadImage(f.getPath());
			if( buffered == null ) {
				out.println("    Failed to load.");
				out.println();
				continue;
			}

			T input = alg.getInputType().createImage(buffered.getWidth(),buffered.getHeight());
			ConvertBufferedImage.convertFrom(buffered,input,true);

			alg.classify(input);

			List<String> categories = alg.getCategories();
			out.println("   best = "+categories.get(alg.getBestResult()));
			List<ImageClassifier.Score> scores = alg.getAllResults();
			out.println("   total scores "+scores.size());
			int N = Math.min(10,scores.size());
			for (int i = 0; i < N; i++) {
				ImageClassifier.Score s = scores.get(i);
				out.println("      "+s.score+"  "+s.category);
			}
			out.println();
		}

	}

	public void setOutput(PrintStream out) {
		this.out = out;
	}
}
