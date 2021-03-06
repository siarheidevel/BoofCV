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

package boofcv.alg.transform.wavelet;

import boofcv.alg.misc.ImageTestingOps;
import boofcv.alg.transform.wavelet.impl.ImplWaveletTransformNaive;
import boofcv.core.image.border.BorderType;
import boofcv.factory.transform.wavelet.FactoryWaveletDaub;
import boofcv.misc.PerformerBase;
import boofcv.misc.ProfileOperation;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.wavelet.WaveletDescription;
import boofcv.struct.wavelet.WlCoef_F32;
import boofcv.struct.wavelet.WlCoef_I32;

import java.util.Random;


/**
 * @author Peter Abeles
 */
public class BenchmarkWaveletInverse {
	static int imgWidth = 640;
	static int imgHeight = 480;
	static long TEST_TIME = 1000;

	static WaveletDescription<WlCoef_F32> desc_F32 = FactoryWaveletDaub.biorthogonal_F32(5,BorderType.REFLECT);
	static WaveletDescription<WlCoef_I32> desc_I32 = FactoryWaveletDaub.biorthogonal_I32(5, BorderType.REFLECT);

	static ImageFloat32 tran_F32 = new ImageFloat32(imgWidth,imgHeight);
	static ImageFloat32 temp1_F32 = new ImageFloat32(imgWidth,imgHeight);
	static ImageFloat32 temp2_F32 = new ImageFloat32(imgWidth,imgHeight);
	static ImageSInt32 tran_I32 = new ImageSInt32(imgWidth,imgHeight);
	static ImageSInt32 temp1_I32 = new ImageSInt32(imgWidth,imgHeight);
	static ImageSInt32 temp2_I32 = new ImageSInt32(imgWidth,imgHeight);

	public static class Naive_F32 extends PerformerBase {

		@Override
		public void process() {
			ImplWaveletTransformNaive.verticalInverse(desc_F32.getBorder(),desc_F32.getInverse(), tran_F32,temp1_F32);
			ImplWaveletTransformNaive.horizontalInverse(desc_F32.getBorder(),desc_F32.getInverse(),temp1_F32,temp2_F32);
		}
	}

	public static class Naive_I32 extends PerformerBase {

		@Override
		public void process() {
			ImplWaveletTransformNaive.verticalInverse(desc_I32.getBorder(),desc_I32.getInverse(), tran_I32,temp1_I32);
			ImplWaveletTransformNaive.horizontalInverse(desc_I32.getBorder(),desc_I32.getInverse(),temp1_I32,temp2_I32);
		}
	}

	public static class Standard_F32 extends PerformerBase {

		@Override
		public void process() {
			WaveletTransformOps.inverse1(desc_F32,tran_F32,temp1_F32,temp1_F32);
		}
	}

	public static class Standard_I32 extends PerformerBase {

		@Override
		public void process() {
			WaveletTransformOps.inverse1(desc_I32,tran_I32,temp1_I32,temp1_I32);
		}
	}


	public static void main(String args[]) {

		Random rand = new Random(234);
		ImageTestingOps.randomize(tran_F32, rand, 0, 100);
		ImageTestingOps.randomize(tran_I32, rand, 0, 100);

		System.out.println("=========  Profile Image Size " + imgWidth + " x " + imgHeight + " ==========");
		System.out.println();

		ProfileOperation.printOpsPerSec(new Naive_F32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Standard_F32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Naive_I32(), TEST_TIME);
		ProfileOperation.printOpsPerSec(new Standard_I32(), TEST_TIME);
	}
}
