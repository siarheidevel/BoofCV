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

package boofcv.abst.geo.pose;

import boofcv.abst.geo.RefinePnP;
import boofcv.abst.geo.optimization.ResidualsPoseMatrix;
import boofcv.alg.geo.pose.PnPJacobianRodrigues;
import boofcv.alg.geo.pose.PnPResidualReprojection;
import boofcv.alg.geo.pose.PnPRodriguesCodec;
import boofcv.numerics.fitting.modelset.ModelCodec;
import boofcv.numerics.optimization.FactoryOptimization;
import boofcv.numerics.optimization.UnconstrainedLeastSquares;
import boofcv.struct.geo.Point2D3D;
import georegression.struct.se.Se3_F64;

import java.util.List;

/**
 * Minimizes the projection residual error in a calibrated camera for a pose estimate.
 * Rotation is encoded using rodrigues coordinates.
 *
 * @author Peter Abeles
 */
public class PnPRefineRodrigues implements RefinePnP {

	ModelCodec<Se3_F64> paramModel = new PnPRodriguesCodec();
	ResidualsPoseMatrix func;
	PnPJacobianRodrigues jacobian = new PnPJacobianRodrigues();

	double param[];
	UnconstrainedLeastSquares minimizer;
	int maxIterations;
	double convergenceTol;

	public PnPRefineRodrigues(double convergenceTol, int maxIterations )
	{
		this.maxIterations = maxIterations;
		this.convergenceTol = convergenceTol;
		this.minimizer = FactoryOptimization.leastSquareLevenberg(1e-3);

		func = new ResidualsPoseMatrix(paramModel,new PnPResidualReprojection());

		param = new double[paramModel.getParamLength()];
	}

	@Override
	public boolean process(Se3_F64 worldToCamera, List<Point2D3D> obs, Se3_F64 refinedWorldToCamera ) {

		paramModel.encode(worldToCamera, param);

		func.setObservations(obs);
		jacobian.setObservations(obs);

		minimizer.setFunction(func,jacobian);

		minimizer.initialize(param,0,convergenceTol*obs.size());

//		System.out.println("  error before "+minimizer.getFunctionValue());
		for( int i = 0; i < maxIterations; i++ ) {
			if( minimizer.iterate() )
				break;
		}
//		System.out.println("  error after  "+minimizer.getFunctionValue());

		paramModel.decode(minimizer.getParameters(), refinedWorldToCamera);

		return true;
	}
}
