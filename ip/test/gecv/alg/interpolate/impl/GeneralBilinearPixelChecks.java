/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.interpolate.impl;

import gecv.alg.interpolate.FactoryInterpolation;
import gecv.alg.interpolate.InterpolatePixel;
import gecv.core.image.FactorySingleBandImage;
import gecv.core.image.SingleBandImage;
import gecv.core.image.border.BorderIndex1D_Extend;
import gecv.core.image.border.FactoryImageBorder;
import gecv.core.image.border.ImageBorder;
import gecv.struct.image.ImageBase;


/**
 * Generic class for testing bilinear interpolation
 *
 * @author Peter Abeles
 */
public abstract class GeneralBilinearPixelChecks<T extends ImageBase> extends GeneralInterpolationPixelChecks<T> {

	@Override
	protected InterpolatePixel<T> wrap(T image) {
		return FactoryInterpolation.bilinearPixel(image);
	}

	@Override
	protected float compute(T _img, float x, float y) {
		ImageBorder<?> imgB = FactoryImageBorder.general(_img, BorderIndex1D_Extend.class);
		SingleBandImage img = FactorySingleBandImage.wrap(imgB);


		int gX = (int) x;
		int gY = (int) y;

		float v0 = img.get(gX, gY).floatValue();
		float v1 = img.get(gX + 1, gY).floatValue();
		float v2 = img.get(gX, gY + 1).floatValue();
		float v3 = img.get(gX + 1, gY + 1).floatValue();

		x %= 1f;
		y %= 1f;

		float a = 1f - x;
		float b = 1f - y;

		return a * b * v0 + x * b * v1 + a * y * v2 + x * y * v3;
	}
}
