package eu.tgx03.multidimensional;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * A class that represents a multidimensional array, basically an array of arrays of arrays of arrays and so on.
 * The depth of nesting is fully flexible (though in the constraints of Java's 32bit-Integers). This also goes for the total number of elements, meaning such an array can never hold more than 2^31 values.
 * It stores all the values in a single array instead of multiple, nested arrays, for better performance.
 */
public class MultidimensionalIntArray extends AbstractMultidimensionalArray implements Cloneable {

	/**
	 * The actual array holding all the values.
	 */
	private final int[] values;

	/**
	 * Creates a new array with a given level of depth.
	 *
	 * @param dimensions What level of the array holds how many values.
	 */
	public MultidimensionalIntArray(int @NotNull ... dimensions) {
		super(true, dimensions);
		int dimension = 1;
		for (int value : dimensions) {
			dimension = dimension * value;
		}
		this.values = new int[dimension];
	}

	/**
	 * This constructor only gets used for cloning. It therefore also does no check on the arguments.
	 *
	 * @param dimensions What level of the array holds how many values.
	 * @param values     The values of the array.
	 */
	private MultidimensionalIntArray(int @NotNull [] dimensions, int @NotNull [] values) {
		super(false, dimensions);
		this.values = values;
	}

	/**
	 * Returns the value at the given point of the array.
	 *
	 * @param coordinates The coordinates of the value to get.
	 * @return The value at the given point.
	 * @throws IllegalArgumentException       Means the number of coordinates doesn't match the dimensions of this array.
	 * @throws ArrayIndexOutOfBoundsException Means that at one stage the index was higher than the actual dimension of the array.
	 */
	@Contract(pure = true)
	public int get(int @NotNull ... coordinates) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		return values[calculateCoordinate(coordinates)];
	}

	/**
	 * Sets the value at a given point of the array.
	 *
	 * @param value       The value to set.
	 * @param coordinates The coordinates of where to set the value.
	 * @throws IllegalArgumentException       Means the number of coordinates doesn't match the dimensions of this array.
	 * @throws ArrayIndexOutOfBoundsException Means that at one stage the index was higher than the actual dimension of the array.
	 */
	public void set(int value, int @NotNull ... coordinates) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
		this.values[calculateCoordinate(coordinates)] = value;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.values);
	}

	@Override
	@NotNull
	public String toString() {

		// Create an array of Strings holding the lowest dimension of the array.
		StringBuilder[] smallestEntity = new StringBuilder[this.values.length / this.dimensions[this.dimensions.length - 1]];
		for (int i = 0; i < smallestEntity.length; i++) smallestEntity[i] = new StringBuilder();
		int smallestDimension = this.dimensions[this.dimensions.length - 1];
		for (int i = 0; i < this.values.length; i++) {
			if (i % smallestDimension != 0) smallestEntity[i / smallestDimension].append("; ");
			smallestEntity[i / smallestDimension].append(this.values[i]);
		}

		return insertBrackets(smallestEntity);
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (o instanceof MultidimensionalIntArray m)
			return Arrays.equals(this.dimensions, m.dimensions) && Arrays.equals(this.values, m.values);
		else return false;
	}

	@Override
	public MultidimensionalIntArray clone() {
		return new MultidimensionalIntArray(this.dimensions, Arrays.copyOf(this.values, this.values.length));
	}
}
