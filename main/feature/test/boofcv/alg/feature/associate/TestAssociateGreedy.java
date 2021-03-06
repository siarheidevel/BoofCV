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

package boofcv.alg.feature.associate;

import boofcv.abst.feature.associate.ScoreAssociateEuclidean_F64;
import boofcv.abst.feature.associate.ScoreAssociation;
import boofcv.struct.FastQueue;
import boofcv.struct.feature.TupleDescQueue;
import boofcv.struct.feature.TupleDesc_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * @author Peter Abeles
 */
public class TestAssociateGreedy {

	ScoreAssociation<TupleDesc_F64> score = new ScoreAssociateEuclidean_F64();

	@Test
	public void basic() {
		FastQueue<TupleDesc_F64> a = createData(1,2,3,4);
		FastQueue<TupleDesc_F64> b = createData(3,4,1,40);

		AssociateGreedy<TupleDesc_F64> alg = new AssociateGreedy<TupleDesc_F64>(score,0.5,false);

		alg.associate(a,b);

		int pairs[] = alg.getPairs();

		assertEquals(2,pairs[0]);
		assertEquals(-1,pairs[1]);
		assertEquals(0,pairs[2]);
		assertEquals(1,pairs[3]);

		double fitScore[] = alg.getFitQuality();

		assertEquals(0,fitScore[0],1e-5);
		assertEquals(0,fitScore[2],1e-5);
		assertEquals(0,fitScore[3],1e-5);
	}

	@Test
	public void maxError() {
		FastQueue<TupleDesc_F64> a = createData(1,2,3,4);
		FastQueue<TupleDesc_F64> b = createData(3,4,1.1,40);

		// large margin for error
		AssociateGreedy<TupleDesc_F64> alg = new AssociateGreedy<TupleDesc_F64>(score,10,false);

		alg.associate(a,b);
		assertEquals(2,alg.getPairs()[1]);

		// small margin for error, no association
		alg = new AssociateGreedy<TupleDesc_F64>(score,0.1,false);
		alg.associate(a,b);
		assertEquals(-1,alg.getPairs()[1]);
	}

	@Test
	public void backwards() {
		FastQueue<TupleDesc_F64> a = createData(1,2,3,8);
		FastQueue<TupleDesc_F64> b = createData(3,4,1,10);

		AssociateGreedy<TupleDesc_F64> alg = new AssociateGreedy<TupleDesc_F64>(score,10,true);

		alg.associate(a,b);

		int pairs[] = alg.getPairs();

		assertEquals(2,pairs[0]);
		assertEquals(-1,pairs[1]);
		assertEquals(0,pairs[2]);
		assertEquals(3,pairs[3]);

		double fitScore[] = alg.getFitQuality();

		assertEquals(0,fitScore[0],1e-5);
		assertEquals(0,fitScore[2],1e-5);
		assertEquals(2,fitScore[3],1e-5);
	}

	private FastQueue<TupleDesc_F64> createData( double ...values )
	{
		FastQueue<TupleDesc_F64> ret = new TupleDescQueue<TupleDesc_F64>(TupleDesc_F64.class,1, true);

		for( int i = 0; i < values.length; i++ ) {
			ret.grow().set(values[i]);
		}

		return ret;
	}
}
