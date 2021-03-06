package boofcv.applications;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.common.misc.PointFileCodec;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.gui.BoofSwingUtil;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;
import georegression.struct.point.Point2D_F64;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Peter Abeles
 */
public class HandSelectPointsApp extends HandSelectBase {



	public HandSelectPointsApp( File file ) {
		super(new SelectPointPanel(),file);

		addDetectQrCodes();
	}

	@Override
	public void process(File file, BufferedImage image) {
		SelectPointPanel gui = (SelectPointPanel)this.imagePanel;

		File outputFile = selectOutputFile(file);

		if( outputFile.exists() ) {
			List<List<Point2D_F64>> sets = PointFileCodec.loadSets(outputFile.getPath());
			if( sets == null ) {
				gui.addPointSet(PointFileCodec.load(outputFile.getPath()));
			} else {
				gui.setSets(sets);
			}
		}
		infoPanel.setImageShape(image.getWidth(),image.getHeight());
		gui.setBufferedImage(image);
	}

	private void addDetectQrCodes() {
		infoPanel.handleSelectShape = ()->{
			QrCodeDetector<GrayU8> detector = FactoryFiducial.qrcode(null,GrayU8.class);
			GrayU8 gray = new GrayU8(1,1);
			ConvertBufferedImage.convertFrom(image,gray);
			detector.process(gray);

			SelectPointPanel gui = (SelectPointPanel)this.imagePanel;

			BoofSwingUtil.invokeNowOrLater(()->{
				gui.clearPoints();
				for(QrCode qr : detector.getDetections() ) {
					List<Point2D_F64> list = new ArrayList<>();
					for (int i = 0; i < qr.bounds.size(); i++) {
						list.add( qr.bounds.get(i));
					}

					gui.addPointSet(list);
				}
				gui.repaint();
				System.out.println("detected "+detector.getDetections().size());
			});
		};
	}

	@Override
	public void save() {
		SelectPointPanel gui = (SelectPointPanel)this.imagePanel;
		File outputFile = selectOutputFile(inputFile);
		List<List<Point2D_F64>> points = gui.getSelectedPoints();

		if( points.size() == 1 ) {
			PointFileCodec.save(outputFile.getPath(), "list of hand selected 2D points", points.get(0));
			System.out.println("Saved to " + outputFile.getPath());
		} else if( points.size() > 1 ){
			PointFileCodec.saveSets(outputFile.getPath(), "list of hand selected 2D points", points);
			System.out.println("Saved to " + outputFile.getPath());
		}
	}

	@Override
	public String getApplicationName() {
		return "Select Point Features";
	}

	@Override
	public void setScale( double scale ) {
		SelectPointPanel gui = (SelectPointPanel)this.imagePanel;
		gui.setScale(scale);
	}

	@Override
	public void clearPoints() {
		SelectPointPanel gui = (SelectPointPanel)this.imagePanel;
		gui.clearPoints();
		gui.repaint();
	}

	public static void main(String[] args) {
		new HandSelectPointsApp(null);
	}
}
