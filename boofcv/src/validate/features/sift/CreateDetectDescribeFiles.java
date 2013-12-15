/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of the SURF Performance Benchmark
 * (https://github.com/lessthanoptimal/SURFPerformance).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package validate.features.sift;

import boofcv.abst.feature.detdesc.DetectDescribePoint;
import boofcv.struct.feature.SurfFeature;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageType;
import validate.features.homography.CreateDetectDescribeFile;

import java.io.FileNotFoundException;

/**
 * @author Peter Abeles
 */
public class CreateDetectDescribeFiles {
	public static void doStuff(String directory) throws FileNotFoundException {

		ImageType<ImageFloat32> imageType = ImageType.single(ImageFloat32.class);

		DetectDescribePoint<ImageFloat32,SurfFeature> alg =
				FactorySift.detectDescribe();

		CreateDetectDescribeFile<ImageFloat32,SurfFeature> cdf =
				new CreateDetectDescribeFile<ImageFloat32,SurfFeature>(alg,imageType,"BOOFCV_SIFTN");

		cdf.directory(directory,"./");
	}

	public static void main( String args[] ) throws FileNotFoundException {
		doStuff("data/bikes/");
		doStuff("data/boat/");
		doStuff("data/graf/");
		doStuff("data/leuven/");
		doStuff("data/ubc/");
		doStuff("data/trees/");
		doStuff("data/wall/");
		doStuff("data/bark/");
	}
}