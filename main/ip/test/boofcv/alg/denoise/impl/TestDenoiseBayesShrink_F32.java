/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
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

package boofcv.alg.denoise.impl;

import boofcv.alg.denoise.wavelet.DenoiseBayesShrink_F32;
import boofcv.factory.transform.wavelet.FactoryWaveletDaub;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;
import org.junit.Test;


/**
 * @author Peter Abeles
 */
public class TestDenoiseBayesShrink_F32 extends GenericWaveletDenoiseTests<ImageFloat32> {

	public TestDenoiseBayesShrink_F32() {
		super(ImageFloat32.class, 20, FactoryWaveletDaub.daubJ_F32(4), 3);
	}

	@Test
	public void standardTests() {
		performTest();
	}

	@Override
	public void denoiseWavelet(ImageBase transformedImg, int numLevels ) {
		DenoiseBayesShrink_F32 alg = new DenoiseBayesShrink_F32();
		alg.denoise((ImageFloat32)transformedImg,numLevels);
	}
}