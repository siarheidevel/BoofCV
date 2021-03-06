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

package boofcv.abst.feature.detect.interest;

import boofcv.struct.image.ImageSingleBand;
import georegression.struct.point.Point2D_F64;

/**
 * Interface for automatic interest point detection in an image.  Optional support is
 * provided for scale and orientation.
 *
 * @author Peter Abeles
 */
public interface InterestPointDetector< T extends ImageSingleBand > {

	/**
	 * Detects interest points inside the provided image.
	 *
	 * @param input Input features are detected inside of.
	 */
	void detect( T input );

	/**
	 * Returns the number of interest points found.
	 *
	 * @return Number of interest points.
	 */
	int getNumberOfFeatures();

	/**
	 * <p>
	 * The center location of the feature inside the image.
	 * </p>
	 * <p>
	 * WARNING: Do not save the returned reference since it might get overwritten.
	 * </p>
	 *
	 * @param featureIndex The feature's index.
	 * @return Location of the feature in image pixels.
	 */
	Point2D_F64 getLocation( int featureIndex );

	/**
	 * The scale of the feature relative to the canonical size. To get the size in pixels
	 * multiply the scale by the canonical radius.  If scale is not supported by the detector
	 * then 1 is always returned.
	 *
	 * @see #getCanonicalRadius()
	 *
	 * @param featureIndex Feature whose scale is being requested.
	 * @return Size of the interest point relative to canonical size.
	 */
	double getScale( int featureIndex );

	/**
	 * Returns the features found orientation.   If orientation is not supported by the detector then
	 * 0 is always returned.
	 *
	 * @param featureIndex Feature whose
	 * @return Orientation in radians.
	 */
	double getOrientation( int featureIndex );

	/**
	 * The radius of a feature at a scale of one in pixels.
	 *
	 * TODO See comment in feature_todo.txt  This function might need to be redfined.
	 *
	 * Possible redefintion: radius of the detected blob's contour.  Get rid of scale?
	 *
	 * @return Size of a feature at scale one in pixels.
	 */
	double getCanonicalRadius();

	/**
	 * Does the interest point detector have scale information
	 *
	 * @return true if it has scale information and false otherwise
	 */
	public boolean hasScale();

	/**
	 * If the interest point detector estimates the feature's orientation
	 *
	 * @return true if it estimates the orientation
	 */
	public boolean hasOrientation();
}
