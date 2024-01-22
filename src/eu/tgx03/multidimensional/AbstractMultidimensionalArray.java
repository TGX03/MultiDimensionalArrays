package eu.tgx03.multidimensional;

import java.util.Arrays;

abstract class AbstractMultidimensionalArray {

	protected final int[] dimensions;

	protected AbstractMultidimensionalArray(boolean copy, int ...dimensions) {
		if (copy) this.dimensions = Arrays.copyOf(dimensions, dimensions.length);
		else this.dimensions = dimensions;
	}

	/**
	 * Calculates the place of a value in the underlying array from the given values.
	 *
	 * @param coordinates The coordinates of the desired value.
	 * @return The position in the array of the requested coordinates.
	 * @throws IllegalArgumentException       Means the number of coordinates doesn't match the dimensions of this array.
	 * @throws ArrayIndexOutOfBoundsException Means that at one stage the index was higher than the actual dimension of the array.
	 */
	protected int calculateCoordinate(int ...coordinates) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		if (coordinates.length != this.dimensions.length)
			throw new IllegalArgumentException("Dimensions of this array do not match dimensions of provided coordinates");
		int coordinate = 0;
		int multiplier = 1;
		for (int i = this.dimensions.length - 1; i >= 0; i--) {
			if (coordinates[i] >= this.dimensions[i] || coordinates[i] < 0)
				throw new ArrayIndexOutOfBoundsException(coordinates[i] + "is not a valid index for max index " + this.dimensions[i]);
			coordinate = coordinate + coordinates[i] * multiplier;
			multiplier = multiplier * this.dimensions[i];
		}
		return coordinate;
	}

	protected String insertBrackets(StringBuilder[] smallestEntity) {
		// Insert brackets at the correct places to represent the different dimensions.
		int multiplier = 1;
		for (int currentDimension = this.dimensions.length - 2; currentDimension >= 0; currentDimension--) {
			for (int i = 0; i < smallestEntity.length; i++) {
				if (i % multiplier == 0) smallestEntity[i].insert(0, "[");
				if (i % multiplier == multiplier - 1) smallestEntity[i].append("]");
			}
			multiplier = multiplier * this.dimensions[currentDimension];
		}

		// Unite all the substrings into one large String.
		StringBuilder finalBuilder = new StringBuilder();
		for (StringBuilder current : smallestEntity) finalBuilder.append(current);
		return finalBuilder.toString().replaceAll("]\\[", "], [");
	}
}
