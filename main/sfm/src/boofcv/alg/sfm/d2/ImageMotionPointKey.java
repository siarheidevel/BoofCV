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

package boofcv.alg.sfm.d2;

import boofcv.abst.feature.tracker.ImagePointTracker;
import boofcv.abst.feature.tracker.KeyFramePointTracker;
import boofcv.abst.feature.tracker.KeyFrameTrack;
import boofcv.numerics.fitting.modelset.ModelFitter;
import boofcv.numerics.fitting.modelset.ModelMatcher;
import boofcv.struct.geo.AssociatedPair;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.InvertibleTransform;

import java.util.List;

/**
 * Computes the transform from the first image in a sequence to the current frame. Designed to be
 * useful for stabilization and mosaic creation.
 *
 * @author Peter Abeles
 * @param <I> Input image type
 * @param <T> Motion model data type
 */
@SuppressWarnings("unchecked")
public class ImageMotionPointKey<I extends ImageSingleBand, T extends InvertibleTransform> {

	// number of detected features
	private int totalSpawned;

	// total number of frames processed
	protected int totalProcessed = 0;
	// feature tracker
	protected KeyFramePointTracker<I,KeyFrameTrack> tracker;
	// Fits a model to the tracked features
	protected ModelMatcher<T,AssociatedPair> modelMatcher;
	// Refines the model using the complete inlier set
	protected ModelFitter<T,AssociatedPair> modelRefiner;

	// assumed initial transform from the first image to the world
	protected T worldToInit;

	// transform from the world frame to the key frame
	protected T worldToKey;
	// transform from key frame to current frame
	protected T keyToCurr;
	// transform from world to current frame
	protected T worldToCurr;

	/**
	 * Specify algorithms to use internally.  Each of these classes must work with
	 * compatible data structures.
	 *
	 * @param tracker feature tracker
	 * @param modelMatcher Fits model to track data
	 * @param modelRefiner (Optional) Refines the found model using the entire inlier set. Can be null.
	 * @param model Motion model data structure
	 */
	public ImageMotionPointKey(ImagePointTracker<I> tracker,
							   ModelMatcher<T, AssociatedPair> modelMatcher,
							   ModelFitter<T,AssociatedPair> modelRefiner,
							   T model)
	{
		this.tracker = new KeyFramePointTracker<I,KeyFrameTrack>(tracker);
		this.modelMatcher = modelMatcher;
		this.modelRefiner = modelRefiner;
		
		worldToInit = (T)model.createInstance();
		worldToKey = (T)model.createInstance();
		keyToCurr = (T)model.createInstance();
		worldToCurr = (T)model.createInstance();
	}

	/**
	 * Specifies the initially assumed transform from the world frame
	 * to the first image.
	 *
	 * @param worldToInit The transform.
	 */
	public void setInitialTransform( T worldToInit) {
		this.worldToInit.set(worldToInit);
		this.keyToCurr.set(worldToInit);
		this.worldToCurr.set(worldToInit);
	}

	/**
	 * Makes the current frame the first frame and discards its past history
	 */
	public void reset() {
		worldToKey.set(worldToInit);
		keyToCurr.set(worldToInit);
		worldToCurr.set(worldToInit);
		tracker.reset();
		tracker.spawnTracks(null);
		tracker.setKeyFrame();
		totalProcessed = 0;
	}

	/**
	 * Transforms the world frame into another coordinate system.
	 *
	 * @param oldWorldToNewWorld Transform from the old world frame to the new world frame
	 */
	public void changeWorld(T oldWorldToNewWorld) {

		T worldToKey = (T) this.worldToKey.invert(null);
		worldToInit.concat(worldToKey, oldWorldToNewWorld);

		this.worldToKey.set(worldToInit);
		this.worldToKey.concat(keyToCurr, worldToCurr);
	}

	/**
	 * Processes the next frame in the sequence.
	 *
	 * @param frame Next frame in the video sequence
	 * @return true if motion was estimated and false if no motion was estimated
	 */
	public boolean process( I frame ) {
		// update the feature tracker
		tracker.process(frame);
		totalProcessed++;

		List<KeyFrameTrack> pairs = tracker.getActivePairs(null);

		if( pairs.size() == 0 )
			return false;

		// fit the motion model to the feature tracks
		if( !modelMatcher.process((List)pairs) ) {
			return false;
		}

		// refine the motion estimate
		if( modelRefiner == null ||
				!modelRefiner.fitModel(modelMatcher.getMatchSet(),modelMatcher.getModel(),keyToCurr))
		{
			keyToCurr.set(modelMatcher.getModel());
		}

		// Update the motion
		worldToKey.concat(keyToCurr, worldToCurr);

		return true;
	}

	/**
	 * Make the current frame the first frame in the sequence
	 */
	public void changeKeyFrame() {
		tracker.spawnTracks(null);
		tracker.setKeyFrame();

		totalSpawned = tracker.getActiveTracks(null).size();
		worldToKey.set(worldToCurr);
	}

	public T getWorldToCurr() {
		return worldToCurr;
	}

	public T getWorldToKey() {
		return worldToKey;
	}

	public T getKeyToCurr() {
		return keyToCurr;
	}

	public ImagePointTracker<I> getTracker() {
		return tracker.getTracker();
	}

	public ModelMatcher<T, AssociatedPair> getModelMatcher() {
		return modelMatcher;
	}

	public int getTotalProcessed() {
		return totalProcessed;
	}

	public int getTotalSpawned() {
		return totalSpawned;
	}
}
