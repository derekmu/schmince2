package thed;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * When you shouldn't create strings repeatedly, but need to show integers in your strings, you can use this class as CharSequence.
 *
 * @author Derek Mulvihill
 */
public class NumCharSequence implements CharSequence {
	private final CharSequence[] strs;
	private final NumContainer[] ints;

	/**
	 * Pass in as many Strings as you want in order they should print, if there should be an integer, pass a null in place of a String.
	 */
	public NumCharSequence(CharSequence... strs) {
		this.strs = Arrays.copyOf(strs, strs.length);
		int cnt = 0;
		for (CharSequence obj : strs) {
			if (obj == null) {
				cnt++;
			}
		}
		ints = new NumContainer[cnt];
		for (int i = 0; i < cnt; i++) {
			ints[i] = new NumContainer();
		}
	}

	/**
	 * 'i' should correspond with a null passed into the constructor.<br>
	 * 'x' is the value that you want at that location.<br>
	 * E.g. if the NumCharSequence was created like:<br>
	 * NumCharSequence ics = new NumCharSequence("(", null, ", ", null, ")");<br>
	 * to get "(10,-13)", you would call:<br>
	 * ics.setInt(0, 10);<br>
	 * ics.setInt(1, 13);
	 */
	public void setNum(int i, long x) {
		ints[i].setVal(x);
	}

	@Override
	public char charAt(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("index " + index);
		}
		int cin = 0;
		int iin = 0;
		for (CharSequence str : strs) {
			if (str == null) {
				NumContainer x = ints[iin++];
				if (x.length < 0) {
					x.fill();
				}
				if (cin + x.length > index) {
					return x.chars[index - cin];
				}
				cin += x.length;
			} else {
				if (cin + str.length() > index) {
					return str.charAt(index - cin);
				}
				cin += str.length();
			}
		}
		throw new IndexOutOfBoundsException("index=" + index);
	}

	@Override
	public int length() {
		int len = 0;
		int iin = 0;
		for (CharSequence str : strs) {
			if (str == null) {
				NumContainer x = ints[iin++];
				if (x.length < 0) {
					x.fill();
				}
				len += x.length;
			} else {
				len += str.length();
			}
		}
		return len;
	}

	@NotNull
	@Override
	public CharSequence subSequence(int start, int end) {
		// I don't care about implementing this...
		return null;
	}

	private static class NumContainer {
		private long val;
		private int length = -1;
		private char[] chars; //cache characters for the current val, and we can reuse the array so there isn't more allocation except when the number of digits increases

		private void fill() {
			long x = val;
			length = 1;
			if (x < 0) {
				x = -x;
				length++;
			}
			while (x >= 10) {
				length++;
				x /= 10;
			}
			if (chars == null || chars.length < length) {
				chars = new char[length];
			}
			int n = 0;
			x = val;
			if (val < 0) {
				x = -x;
				chars[n++] = '-';
			}
			n = length - 1;
			while (x >= 10) {
				chars[n--] = (char) ('0' + (x % 10));
				x /= 10;
			}
			chars[n] = (char) ('0' + x);
		}

		public void setVal(long val) {
			if (this.val != val) {
				this.val = val;
				this.length = -1;
			}
		}
	}
}
