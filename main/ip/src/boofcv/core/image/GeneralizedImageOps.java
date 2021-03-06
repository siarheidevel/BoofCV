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

package boofcv.core.image;

import boofcv.alg.InputSanityCheck;
import boofcv.alg.misc.ImageTestingOps;
import boofcv.alg.misc.PixelMath;
import boofcv.core.image.impl.ImplConvertImage;
import boofcv.struct.image.*;
import boofcv.testing.BoofTesting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

/**
 * <p>
 * Operations that return information about the specific image.  Useful when writing highly abstracted code
 * which is independent of the input image.
 * </p>
 *
 * @author Peter Abeles
 */
// TODO rename to GImageOps ?
public class GeneralizedImageOps {

	/**
	 * Converts an image from one type to another type.  Creates a new image instance if
	 * an output is not provided.
	 *
	 * @param src Input image. Not modified.
	 * @param dst Converted output image. If null a new one will be declared. Modified.
	 * @param typeDst The type of output image.
	 * @return Converted image.
	 */
	public static <T extends ImageSingleBand> T convert( ImageSingleBand<?> src , T dst , Class<T> typeDst  )
	{
		if (dst == null) {
			dst =(T) createSingleBand(typeDst, src.width, src.height);
		} else {
			InputSanityCheck.checkSameShape(src, dst);
		}
		convert(src,dst);

		return dst;
	}

	/**
	 * Converts an image from one type to another type.
	 *
	 * @param src Input image. Not modified.
	 * @param dst Converted output image. Modified.
	 * @return Converted image.
	 */
	@SuppressWarnings({"unchecked"})
	public static void convert( ImageSingleBand<?> src , ImageSingleBand<?> dst )
	{
		if( src.getClass() == dst.getClass()) {
			((ImageSingleBand)dst).setTo(src);
			return;
		}

		Method m = BoofTesting.findMethod(ImplConvertImage.class,"convert",src.getClass(),dst.getClass());
		try {
			m.invoke(null,src,dst);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public static double sum(ImageSingleBand imgA ) {
		if (imgA.getClass() == ImageUInt8.class ) {
			return PixelMath.sum( (ImageUInt8) imgA );
		} else if (imgA.getClass() == ImageSInt8.class ) {
			return PixelMath.sum( (ImageSInt8) imgA );
		} else if (imgA.getClass() == ImageUInt16.class ) {
			return PixelMath.sum( (ImageUInt16) imgA );
		} else if (imgA.getClass() == ImageSInt16.class ) {
			return PixelMath.sum( (ImageSInt16) imgA );
		} else if (imgA.getClass() == ImageSInt32.class ) {
			return PixelMath.sum( (ImageSInt32) imgA );
//		} else if (imgA.getClass() == ImageSInt64.class ) {
//			return PixelMath.sum( (ImageSInt64) imgA );
		} else if (imgA.getClass() == ImageFloat32.class) {
			return PixelMath.sum( (ImageFloat32) imgA );
		} else if (imgA.getClass() == ImageFloat64.class) {
			return PixelMath.sum( (ImageFloat64) imgA );
		} else {
			throw new RuntimeException("Unknown type: "+imgA.getClass().getSimpleName());
		}
	}

	public static void boundImage(ImageSingleBand imgA , double lower , double upper ) {
		if (imgA.getClass() == ImageUInt8.class ) {
			PixelMath.boundImage( (ImageUInt8) imgA , (int)lower , (int)upper );
		} else if (imgA.getClass() == ImageSInt8.class ) {
			PixelMath.boundImage( (ImageSInt8) imgA , (int)lower , (int)upper );
		} else if (imgA.getClass() == ImageUInt16.class ) {
			PixelMath.boundImage( (ImageUInt16) imgA , (int)lower , (int)upper );
		} else if (imgA.getClass() == ImageSInt16.class ) {
			PixelMath.boundImage( (ImageSInt16) imgA , (int)lower , (int)upper );
		} else if (imgA.getClass() == ImageSInt32.class ) {
			PixelMath.boundImage( (ImageSInt32) imgA , (int)lower , (int)upper );
//		} else if (imgA.getClass() == ImageSInt64.class ) {
//			return PixelMath.sum( (ImageSInt64) imgA );
		} else if (imgA.getClass() == ImageFloat32.class) {
			PixelMath.boundImage( (ImageFloat32) imgA , (float)lower , (float)upper );
		} else if (imgA.getClass() == ImageFloat64.class) {
			PixelMath.boundImage( (ImageFloat64) imgA , lower , upper );
		} else {
			throw new RuntimeException("Unknown type: "+imgA.getClass().getSimpleName());
		}
	}

	public static double computeMeanSquaredError(ImageSingleBand imgA, ImageSingleBand imgB ) {
		if (imgA.getClass() == ImageUInt8.class ) {
			return ImageTestingOps.computeMeanSquaredError((ImageUInt8) imgA, (ImageUInt8) imgB);
		} else if (imgA.getClass() == ImageSInt8.class ) {
			return ImageTestingOps.computeMeanSquaredError((ImageSInt8) imgA, (ImageSInt8) imgB);
		} else if (imgA.getClass() == ImageUInt16.class ) {
			return ImageTestingOps.computeMeanSquaredError((ImageUInt16) imgA, (ImageUInt16) imgB );
		} else if (imgA.getClass() == ImageSInt16.class ) {
			return ImageTestingOps.computeMeanSquaredError((ImageSInt16) imgA, (ImageSInt16) imgB);
		} else if (imgA.getClass() == ImageSInt32.class ) {
			return ImageTestingOps.computeMeanSquaredError((ImageSInt32) imgA, (ImageSInt32) imgB );
		} else if (imgA.getClass() == ImageFloat32.class) {
			return ImageTestingOps.computeMeanSquaredError((ImageFloat32) imgA, (ImageFloat32) imgB );
		} else if (imgA.getClass() == ImageFloat64.class) {
			return ImageTestingOps.computeMeanSquaredError((ImageFloat64) imgA, (ImageFloat64) imgB );
		} else {
			throw new RuntimeException("Unknown type: "+imgA.getClass().getSimpleName());
		}
	}

	public static void fillRectangle(ImageSingleBand img, double value , int x0 , int y0 , int width , int height ) {
		if (img.getClass() == ImageUInt8.class ) {
			ImageTestingOps.fillRectangle((ImageUInt8) img, (int)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageSInt8.class ) {
			ImageTestingOps.fillRectangle((ImageSInt8) img, (int)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageUInt16.class ) {
			ImageTestingOps.fillRectangle((ImageUInt16) img, (int)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageSInt16.class ) {
			ImageTestingOps.fillRectangle((ImageSInt16) img, (int)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageSInt32.class ) {
			ImageTestingOps.fillRectangle((ImageSInt32) img, (int)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageFloat32.class) {
			ImageTestingOps.fillRectangle((ImageFloat32) img, (float)value , x0 , y0 , width , height );
		} else if (img.getClass() == ImageFloat64.class) {
			ImageTestingOps.fillRectangle((ImageFloat64) img, value , x0 , y0 , width , height );
		} else {
			throw new RuntimeException("Unknown type: "+img.getClass().getSimpleName());
		}
	}

	public static void addGaussian(ImageBase img, Random rand, double sigma , double min , double max ) {
		if( img instanceof ImageSingleBand ) {
			if (img.getClass() == ImageUInt8.class ) {
				ImageTestingOps.addGaussian((ImageUInt8) img, rand, sigma , (int)min , (int)max );
			} else if (img.getClass() == ImageSInt8.class ) {
				ImageTestingOps.addGaussian((ImageSInt8) img, rand, sigma , (int)min , (int)max);
			} else if (img.getClass() == ImageUInt16.class ) {
				ImageTestingOps.addGaussian((ImageUInt16) img, rand, sigma , (int)min , (int)max);
			} else if (img.getClass() == ImageSInt16.class ) {
				ImageTestingOps.addGaussian((ImageSInt16) img, rand, sigma , (int)min , (int)max);
			} else if (img.getClass() == ImageSInt32.class ) {
				ImageTestingOps.addGaussian((ImageSInt32) img, rand, sigma , (int)min , (int)max);
			} else if (img.getClass() == ImageFloat32.class) {
				ImageTestingOps.addGaussian((ImageFloat32) img, rand, sigma , (float)min , (float)max);
			} else if (img.getClass() == ImageFloat64.class) {
				ImageTestingOps.addGaussian((ImageFloat64) img, rand, sigma , (double)min , (double)max);
			} else {
				throw new RuntimeException("Unknown type: "+img.getClass().getSimpleName());
			}
		} else if( img instanceof MultiSpectral ) {
			MultiSpectral m = (MultiSpectral)img;
			int N = m.getNumBands();
			for( int i = 0; i < N; i++ ) {
				addGaussian(m.getBand(i), rand, sigma, min, max);
			}
		} else {
			throw new RuntimeException("Unknown type: "+img.getClass().getSimpleName());
		}
	}

	public static void randomize(ImageBase img, Random rand, int min, int max) {
		if( img instanceof ImageSingleBand ) {
			if (img.getClass() == ImageUInt8.class || img.getClass() == ImageSInt8.class ) {
				ImageTestingOps.randomize((ImageInt8) img, rand, min, max);
			} else if (img.getClass() == ImageSInt16.class || img.getClass() == ImageUInt16.class) {
				ImageTestingOps.randomize((ImageInt16) img, rand, min, max);
			} else if (img.getClass() == ImageSInt32.class ) {
				ImageTestingOps.randomize((ImageSInt32) img, rand, min, max);
			} else if (img.getClass() == ImageSInt64.class ) {
				ImageTestingOps.randomize((ImageSInt64) img, rand, min, max);
			} else if (img.getClass() == ImageFloat32.class) {
				ImageTestingOps.randomize((ImageFloat32) img, rand, min, max);
			} else if (img.getClass() == ImageFloat64.class) {
				ImageTestingOps.randomize((ImageFloat64) img, rand, min, max);
			} else {
				throw new RuntimeException("Unknown type: "+img.getClass().getSimpleName());
			}
		} else if( img instanceof MultiSpectral ) {
			MultiSpectral m = (MultiSpectral)img;
			int N = m.getNumBands();
			for( int i = 0; i < N; i++ ) {
				randomize(m.getBand(i),rand,min,max);
			}
		} else {
			throw new RuntimeException("Unknown type: "+img.getClass().getSimpleName());
		}
	}

	public static boolean isFloatingPoint(Class<?> imgType) {
		if( ImageFloat.class.isAssignableFrom(imgType) ) {
			return true;
		} else {
			return false;
		}
	}

	public static double get(ImageSingleBand img, int x, int y) {
		if (img instanceof ImageInt8) {
			return ((ImageInt8) img).get(x, y);
		} else if (img instanceof ImageInt16) {
			return ((ImageInt16) img).get(x, y);
		} else if (img instanceof ImageSInt32) {
			return ((ImageSInt32) img).get(x, y);
		} else if (img instanceof ImageFloat32) {
			return ((ImageFloat32) img).get(x, y);
		} else if (img instanceof ImageFloat64) {
			return ((ImageFloat64) img).get(x, y);
		} else if (img instanceof ImageSInt64) {
			return ((ImageSInt64) img).get(x, y);
		} else {
			throw new IllegalArgumentException("Unknown or incompatible image type: " + img.getClass().getSimpleName());
		}
	}

	public static void fill( ImageBase img, double value) {
		if( img instanceof ImageSingleBand ) {
			if( ImageInt8.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageInt8)img,(int)value);
			} else if( ImageInt16.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageInt16)img,(int)value);
			} else if ( ImageSInt32.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageSInt32)img,(int)value);
			} else if ( ImageSInt64.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageSInt64)img,(int)value);
			} else if (ImageFloat32.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageFloat32)img,(float)value);
			} else if (ImageFloat64.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.fill((ImageFloat64)img,value);
			} else {
				throw new IllegalArgumentException("Unknown or incompatible image type: " + img.getClass().getSimpleName());
			}
		} else if( img instanceof MultiSpectral ) {
			MultiSpectral m = (MultiSpectral)img;
			for( int i = 0; i < m.getNumBands(); i++ )
				fill(m.getBand(i),value);
		} else {
			throw new IllegalArgumentException("Unknown image type: " + img.getClass().getSimpleName());
		}
	}

	public static <T extends ImageSingleBand> T createSingleBand(Class<T> type, int width, int height) {
		type = BoofTesting.convertGenericToSpecificType(type);

		if (type == ImageUInt8.class) {
			return (T)new ImageUInt8(width, height);
		} else if (type == ImageSInt8.class) {
			return (T)new ImageSInt8(width, height);
		} else if (type == ImageSInt16.class) {
			return (T)new ImageSInt16(width, height);
		} else if (type == ImageUInt16.class) {
			return (T)new ImageUInt16(width, height);
		} else if (type == ImageSInt32.class) {
			return (T)new ImageSInt32(width, height);
		} else if (type == ImageSInt64.class) {
			return (T)new ImageSInt64(width, height);
		} else if (type == ImageFloat32.class) {
			return (T)new ImageFloat32(width, height);
		} else if (type == ImageFloat64.class) {
			return (T)new ImageFloat64(width, height);
		} else if (type == ImageInterleavedInt8.class) {
			return (T)new ImageInterleavedInt8(width, height, 1);
		} else if( type == ImageInteger.class ) {
			// ImageInteger is a generic type, so just create something
			return (T)new ImageSInt32(width,height);
		}
		throw new RuntimeException("Unknown type: "+type.getSimpleName());
	}

	public static void set(ImageSingleBand img, int x, int y, double value) {
		if (ImageInteger.class.isAssignableFrom(img.getClass())) {
			((ImageInteger)img).set(x,y,(int)value);
		} else if (img instanceof ImageFloat32) {
			((ImageFloat32) img).set(x, y,(float)value);
		} else if (img instanceof ImageFloat64) {
			((ImageFloat64) img).set(x, y, value);
		} else {
			throw new IllegalArgumentException("Unknown or incompatible image type: " + img.getClass().getSimpleName());
		}
	}

	public static void flipVertical( ImageBase img ) {
		if( img instanceof ImageSingleBand ) {
			if( ImageInt8.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageInt8)img);
			} else if( ImageInt16.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageInt16)img);
			} else if ( ImageSInt32.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageSInt32)img);
			} else if ( ImageSInt64.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageSInt64)img);
			} else if (ImageFloat32.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageFloat32)img);
			} else if (ImageFloat64.class.isAssignableFrom(img.getClass()) ) {
				ImageTestingOps.flipVertical((ImageFloat64)img);
			} else {
				throw new IllegalArgumentException("Unknown or incompatible image type: " + img.getClass().getSimpleName());
			}
		} else if( img instanceof MultiSpectral ) {
			MultiSpectral m = (MultiSpectral)img;
			for( int i = 0; i < m.getNumBands(); i++ )
				flipVertical(m.getBand(i));
		} else {
			throw new IllegalArgumentException("Unknown image type: " + img.getClass().getSimpleName());
		}
	}
}
