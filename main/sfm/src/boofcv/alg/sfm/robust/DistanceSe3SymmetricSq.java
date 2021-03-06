package boofcv.alg.sfm.robust;

import boofcv.abst.geo.TriangulateTwoViewsCalibrated;
import boofcv.alg.geo.DistanceModelStereoPixels;
import boofcv.alg.geo.NormalizedToPixelError;
import boofcv.struct.geo.AssociatedPair;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;

import java.util.List;

/**
 * <p>
 * Computes the error for a given camera motion from two calibrated views.  First a point
 * is triangulated from the two views and the motion.  Then the difference between
 * the observed and projected point is found at each view. Error is normalized pixel difference
 * squared.
 * </p>
 * <p>
 * error = &Delta;x<sub>1</sub><sup>2</sup> + &Delta;y<sub>1</sub><sup>2</sup> +
 * &Delta;x<sub>2</sub><sup>2</sup> + &Delta;y<sub>2</sub><sup>2</sup>
 * </p>
 *
 * <p>
 * Error units can be in either pixels<sup>2</sup> or unit less (normalized pixel coordinates).  To compute
 * the error in pixels pass in the correct intrinsic calibration parameters in the constructor.  Otherwise
 * pass in fx=1.fy=1,skew=0 for normalized.
 * </p>
 *
 * <p>
 * NOTE: If a point does not pass the positive depth constraint then a very large error is returned.
 * </p>
 *
 * <p>
 * NOTE: The provided transform must be from the key frame into the current frame.
 * </p>
 *
 * @author Peter Abeles
 */
public class DistanceSe3SymmetricSq implements DistanceModelStereoPixels<Se3_F64,AssociatedPair> {

	// transform from key frame to current frame
	private Se3_F64 keyToCurr;
	// triangulation algorithm
	private TriangulateTwoViewsCalibrated triangulate;
	// working storage
	private Point3D_F64 p = new Point3D_F64();

	// Used to compute error in pixels
	private NormalizedToPixelError errorKey;
	private NormalizedToPixelError errorCurr;

	/**
	 * Configure distance calculation.
	 *
	 * @param triangulate Triangulates the intersection of two observations
	 */
	public DistanceSe3SymmetricSq(TriangulateTwoViewsCalibrated triangulate ,
								  double key_fx, double key_fy , double key_skew ,
								  double curr_fx, double curr_fy , double curr_skew ) {
		this.triangulate = triangulate;
		setIntrinsic(key_fx,key_fy,key_skew,curr_fx,curr_fy,curr_skew);
	}

	/**
	 * Configure distance calculation.
	 *
	 * @param triangulate Triangulates the intersection of two observations
	 */
	public DistanceSe3SymmetricSq(TriangulateTwoViewsCalibrated triangulate ) {
		this.triangulate = triangulate;
	}

	/**
	 * Specifies intrinsic parameters   See comment above about how to specify error units using
	 * intrinsic parameters.
	 *
	 * @param key_fx intrinsic parameter: focal length x for key camera
	 * @param key_fy intrinsic parameter: focal length y for key camera
	 * @param key_skew intrinsic parameter: skew for key camera (usually zero)
	 * @param curr_fx intrinsic parameter: focal length x for curr camera
	 * @param curr_fy intrinsic parameter: focal length y for curr camera
	 * @param cam2_skew intrinsic parameter: skew for curr camera (usually zero)
	 */
	@Override
	public void setIntrinsic(double key_fx, double key_fy , double key_skew ,
							 double curr_fx, double curr_fy , double cam2_skew) {
		errorKey = new NormalizedToPixelError(key_fx,key_fy,key_skew);
		errorCurr = new NormalizedToPixelError(curr_fx,curr_fy, cam2_skew);
	}

	@Override
	public void setModel(Se3_F64 keyToCurr) {
		this.keyToCurr = keyToCurr;
	}

	/**
	 * Computes the error given the motion model
	 *
	 * @param obs Observation in normalized pixel coordinates
	 * @return observation error
	 */
	@Override
	public double computeDistance(AssociatedPair obs) {

		// triangulate the point in 3D space
		triangulate.triangulate(obs.p1,obs.p2,keyToCurr,p);

		if( p.z < 0 )
			return Double.MAX_VALUE;

		// compute observational error in each view
		double error = errorKey.errorSq(obs.p1.x,obs.p1.y,p.x/p.z,p.y/p.z);

		SePointOps_F64.transform(keyToCurr,p,p);
		if( p.z < 0 )
			return Double.MAX_VALUE;

		error += errorCurr.errorSq(obs.p2.x,obs.p2.y, p.x/p.z , p.y/p.z);

		return error;
	}

	@Override
	public void computeDistance(List<AssociatedPair> associatedPairs, double[] distance) {
		for( int i = 0; i < associatedPairs.size(); i++ ) {
			AssociatedPair obs = associatedPairs.get(i);
			distance[i] = computeDistance(obs);
		}
	}
}
