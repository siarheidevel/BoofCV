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

package gecv.abst.filter;

import gecv.core.image.GeneralizedImageOps;
import gecv.struct.image.ImageBase;


/**
 * Applies a sequence of filters. After the first filter each filter will have the same input
 * and output image type.
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class FilterSequence<Input extends ImageBase, Output extends ImageBase>
		implements FilterImageInterface<Input,Output> {

	FilterImageInterface<Input,Output> firstFilter;
	FilterImageInterface<Output,Output> sequence[];

	int borderHorizontal = 0;
	int borderVertical = 0;

	public FilterSequence( FilterImageInterface<Input,Output> first,
						   FilterImageInterface<Output,Output>... sequence )
	{
		this.firstFilter = first;
		this.sequence = sequence;

		if( first.getHorizontalBorder() > borderHorizontal)
			borderHorizontal = first.getHorizontalBorder();
		if( first.getVerticalBorder() > borderVertical)
			borderVertical = first.getVerticalBorder();

		for( FilterImageInterface<Output,Output> f : sequence ) {
			if( f.getHorizontalBorder() > borderHorizontal )
				borderHorizontal = f.getHorizontalBorder();
			if( f.getVerticalBorder() > borderVertical )
				borderVertical = f.getVerticalBorder();
		}
	}

	@Override
	public void process(Input input, Output output) {
		Output temp1 = (Output)output._createNew( output.width , output.height );
		Output temp2 = (Output)output._createNew( output.width , output.height );

		firstFilter.process(input,temp1);

		for( FilterImageInterface<Output,Output> f : sequence ) {
			f.process(temp1,temp2);
			Output swap = temp1;
			temp1 = temp2;
			temp2 = swap;
			GeneralizedImageOps.setAll(temp2,0);
		}

		output.setTo(temp1);
	}

	@Override
	public int getHorizontalBorder() {
		return borderHorizontal;
	}

	@Override
	public int getVerticalBorder() {
		return borderVertical;
	}
}
