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

package boofcv.alg.feature.detect.edge.impl;

import boofcv.core.image.border.FactoryImageBorderAlgs;
import boofcv.core.image.border.ImageBorder_F32;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageInteger;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageSInt32;


/**
 * <p>
 * Implementations of the crude version of non-maximum edge suppression.  If the gradient is positive or negative
 * is used to determine the direction of suppression.
 * </p>
 *
 * <p>
 * DO NOT MODIFY. Generated by {@link GenerateImplEdgeNonMaxSuppressionCrude}.
 * </p>
 *
 * @author Peter Abeles
 */
public class ImplEdgeNonMaxSuppressionCrude {

	/**
	 * Only processes the inner image.  Ignoring the border.
	 */
	static public void inner4( ImageFloat32 intensity , ImageFloat32 derivX , ImageFloat32 derivY, ImageFloat32 output )
	{
		final int w = intensity.width;
		final int h = intensity.height-1;

		for( int y = 1; y < h; y++ ) {
			int indexI = intensity.startIndex + y*intensity.stride+1;
			int indexX = derivX.startIndex + y*derivX.stride+1;
			int indexY = derivY.startIndex + y*derivY.stride+1;
			int indexO = output.startIndex + y*output.stride+1;

			int end = indexI + w - 2;
			for( ; indexI < end; indexI++ , indexX++, indexY++, indexO++ ) {
				int dx,dy;

				if( derivX.data[indexX] > 0 ) dx = 1; else dx = -1;
				if( derivY.data[indexY] > 0 ) dy = 1; else dy = -1;

				float middle = intensity.data[indexI];

				// suppress the value if either of its neighboring values are more than or equal to it
				if( intensity.data[indexI-dx-dy*intensity.stride] > middle || intensity.data[indexI+dx+dy*intensity.stride] > middle ) {
					output.data[indexO] = 0;
				} else {
					output.data[indexO] = middle;
				}
			}
		}
	}

	/**
	 * Only processes the inner image.  Ignoring the border.
	 */
	static public void inner4( ImageFloat32 intensity , ImageSInt16 derivX , ImageSInt16 derivY, ImageFloat32 output )
	{
		final int w = intensity.width;
		final int h = intensity.height-1;

		for( int y = 1; y < h; y++ ) {
			int indexI = intensity.startIndex + y*intensity.stride+1;
			int indexX = derivX.startIndex + y*derivX.stride+1;
			int indexY = derivY.startIndex + y*derivY.stride+1;
			int indexO = output.startIndex + y*output.stride+1;

			int end = indexI + w - 2;
			for( ; indexI < end; indexI++ , indexX++, indexY++, indexO++ ) {
				int dx,dy;

				if( derivX.data[indexX] > 0 ) dx = 1; else dx = -1;
				if( derivY.data[indexY] > 0 ) dy = 1; else dy = -1;

				float middle = intensity.data[indexI];

				// suppress the value if either of its neighboring values are more than or equal to it
				if( intensity.data[indexI-dx-dy*intensity.stride] > middle || intensity.data[indexI+dx+dy*intensity.stride] > middle ) {
					output.data[indexO] = 0;
				} else {
					output.data[indexO] = middle;
				}
			}
		}
	}

	/**
	 * Only processes the inner image.  Ignoring the border.
	 */
	static public void inner4( ImageFloat32 intensity , ImageSInt32 derivX , ImageSInt32 derivY, ImageFloat32 output )
	{
		final int w = intensity.width;
		final int h = intensity.height-1;

		for( int y = 1; y < h; y++ ) {
			int indexI = intensity.startIndex + y*intensity.stride+1;
			int indexX = derivX.startIndex + y*derivX.stride+1;
			int indexY = derivY.startIndex + y*derivY.stride+1;
			int indexO = output.startIndex + y*output.stride+1;

			int end = indexI + w - 2;
			for( ; indexI < end; indexI++ , indexX++, indexY++, indexO++ ) {
				int dx,dy;

				if( derivX.data[indexX] > 0 ) dx = 1; else dx = -1;
				if( derivY.data[indexY] > 0 ) dy = 1; else dy = -1;

				float middle = intensity.data[indexI];

				// suppress the value if either of its neighboring values are more than or equal to it
				if( intensity.data[indexI-dx-dy*intensity.stride] > middle || intensity.data[indexI+dx+dy*intensity.stride] > middle ) {
					output.data[indexO] = 0;
				} else {
					output.data[indexO] = middle;
				}
			}
		}
	}

	/**
	 * Just processes the image border.
	 */
	static public void border4( ImageFloat32 _intensity , ImageFloat32 derivX , ImageFloat32 derivY , ImageFloat32 output )
	{
		int w = _intensity.width;
		int h = _intensity.height-1;

		ImageBorder_F32 intensity = (ImageBorder_F32)FactoryImageBorderAlgs.value(_intensity, 0);

		// top border
		for( int x = 0; x < w; x++ ) {
			int dx,dy;

			if( derivX.get(x,0) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(x,0) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(x-dx,-dy);
			float middle = intensity.get(x,0);
			float right = intensity.get(x+dx,dy);

			if( left > middle || right > middle ) {
				output.set(x,0,0);
			} else {
				output.set(x,0,middle);
			}
		}

		// bottom border
		for( int x = 0; x < w; x++ ) {
			int dx,dy;

			if( derivX.get(x,h) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(x,h) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(x-dx,h-dy);
			float middle = intensity.get(x,h);
			float right = intensity.get(x+dx,h+dy);

			if( left > middle || right > middle ) {
				output.set(x,h,0);
			} else {
				output.set(x,h,middle);
			}
		}

		// left border
		for( int y = 1; y < h; y++ ) {
			int dx,dy;

			if( derivX.get(0,y) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(0,y) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(-dx,y-dy);
			float middle = intensity.get(0,y);
			float right = intensity.get(dx,y+dy);

			if( left > middle || right > middle ) {
				output.set(0,y,0);
			} else {
				output.set(0,y,middle);
			}
		}

		// right border
		w = w - 1;
		for( int y = 1; y < h; y++ ) {
			int dx,dy;

			if( derivX.get(w,y) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(w,y) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(w-dx,y-dy);
			float middle = intensity.get(w,y);
			float right = intensity.get(w+dx,y+dy);

			if( left > middle || right > middle ) {
				output.set(w,y,0);
			} else {
				output.set(w,y,middle);
			}
		}
	}	/**
	 * Just processes the image border.
	 */
	static public void border4( ImageFloat32 _intensity , ImageInteger derivX , ImageInteger derivY , ImageFloat32 output )
	{
		int w = _intensity.width;
		int h = _intensity.height-1;

		ImageBorder_F32 intensity = (ImageBorder_F32)FactoryImageBorderAlgs.value(_intensity, 0);

		// top border
		for( int x = 0; x < w; x++ ) {
			int dx,dy;

			if( derivX.get(x,0) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(x,0) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(x-dx,-dy);
			float middle = intensity.get(x,0);
			float right = intensity.get(x+dx,dy);

			if( left > middle || right > middle ) {
				output.set(x,0,0);
			} else {
				output.set(x,0,middle);
			}
		}

		// bottom border
		for( int x = 0; x < w; x++ ) {
			int dx,dy;

			if( derivX.get(x,h) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(x,h) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(x-dx,h-dy);
			float middle = intensity.get(x,h);
			float right = intensity.get(x+dx,h+dy);

			if( left > middle || right > middle ) {
				output.set(x,h,0);
			} else {
				output.set(x,h,middle);
			}
		}

		// left border
		for( int y = 1; y < h; y++ ) {
			int dx,dy;

			if( derivX.get(0,y) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(0,y) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(-dx,y-dy);
			float middle = intensity.get(0,y);
			float right = intensity.get(dx,y+dy);

			if( left > middle || right > middle ) {
				output.set(0,y,0);
			} else {
				output.set(0,y,middle);
			}
		}

		// right border
		w = w - 1;
		for( int y = 1; y < h; y++ ) {
			int dx,dy;

			if( derivX.get(w,y) > 0 ) dx = 1; else dx = -1;
			if( derivY.get(w,y) > 0 ) dy = 1; else dy = -1;

			float left = intensity.get(w-dx,y-dy);
			float middle = intensity.get(w,y);
			float right = intensity.get(w+dx,y+dy);

			if( left > middle || right > middle ) {
				output.set(w,y,0);
			} else {
				output.set(w,y,middle);
			}
		}
	}
}
