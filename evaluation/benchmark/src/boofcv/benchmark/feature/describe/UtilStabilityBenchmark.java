/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
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

package boofcv.benchmark.feature.describe;

import boofcv.benchmark.feature.BenchmarkAlgorithm;
import boofcv.factory.feature.describe.FactoryDescribeRegionPoint;
import boofcv.struct.image.ImageSingleBand;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Peter Abeles
 */
public class UtilStabilityBenchmark {
	public static <T extends ImageSingleBand, D extends ImageSingleBand>
	List<BenchmarkAlgorithm> createAlgorithms(int radius, Class<T> imageType , Class<D> derivType ) {
		List<BenchmarkAlgorithm> ret = new ArrayList<BenchmarkAlgorithm>();

		ret.add( new BenchmarkAlgorithm("SURF", FactoryDescribeRegionPoint.surf(true, imageType)));
		ret.add( new BenchmarkAlgorithm("SURF-M", FactoryDescribeRegionPoint.surfm(true, imageType)));
		ret.add( new BenchmarkAlgorithm("BRIEF", FactoryDescribeRegionPoint.brief(16, 512, -1, 4, true, imageType)));
		ret.add( new BenchmarkAlgorithm("BRIEF-SO", FactoryDescribeRegionPoint.brief(16, 512, -1, 4, false, imageType)));
		ret.add( new BenchmarkAlgorithm("Gaussian 12", FactoryDescribeRegionPoint.gaussian12(20, imageType, derivType)));
		ret.add( new BenchmarkAlgorithm("Steer", FactoryDescribeRegionPoint.steerableGaussian(20, false, imageType, derivType)));
		ret.add( new BenchmarkAlgorithm("Steer Norm", FactoryDescribeRegionPoint.steerableGaussian(20, true, imageType, derivType)));

		return ret;
	}
}
