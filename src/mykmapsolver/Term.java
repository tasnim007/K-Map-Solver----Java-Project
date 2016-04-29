package mykmapsolver;

import javax.swing.JOptionPane;

class Term
{
	private int value, bitCount, dcCount;
	private static int nBits;
	private boolean checked, bits[], dontCare[];
	private String sop, pos;

	// creates a term from a decimal value
	Term (int val)
	{
		value = val;
		checked = false;
		bits = new boolean [nBits];
		dontCare = new boolean [nBits];

		for (int i = 0; i < nBits; i++)
		{
			bits[i] = ((val & (1 << i)) == (1 << i)) ? true : false;
			dontCare[i] = false;
		}

		dcCount = countDC();
		bitCount = countBits(val);
		sop = stringize();
		pos = conv(sop);
	}

	// creates a term from two existing terms with a 1-bit difference
	Term (Term a, Term b)
	{
		bits = new boolean [nBits];
		dontCare = new boolean [nBits];
		checked = false;

		for (int i = 0; i < nBits; i++)
		{
			if(a.dontCare[i]) dontCare[i] = true;
			else dontCare[i] = a.bits[i] ^ b.bits[i];

			if(dontCare[i] == false) bits[i] = a.bits[i];
			else bits[i] = false;
		}

		a.check();
		b.check();
		dcCount = countDC();
		value = findVal();
		sop = stringize();
		pos = conv(sop);
	}

	// creates a term from a literal expression
	Term (String s)
	{
		bits = new boolean [nBits];
		dontCare = new boolean [nBits];
		checked = false;

		for (int i = 0; i < s.length(); i++)
		{
			dontCare[s.charAt(i)-(Character.isUpperCase(s.charAt(i)) ? 'A' : 'a')] = true;
			bits[s.charAt(i)-(Character.isUpperCase(s.charAt(i)) ? 'A' : 'a')]
				= Character.isUpperCase(s.charAt(i)) ? true : false;
		}

		for (int i = 0; i < nBits; i++) dontCare[i] = !dontCare[i];

		dcCount = countDC();
		value = findVal();
		sop = stringize();
		pos = conv(sop);
	}

	char toggleCase(char c)
	{
		return (char)(c^32);
	}

	String conv(String s)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("(");

		for(int i = 0; i < s.length(); i++)
			sb.append(toggleCase(s.charAt(i))).append('+');

		sb.deleteCharAt(sb.length()-1).append(")");

		return sb.toString();
	}

	// specify the number of bits/variables
	static void set_nBits(int n)
	{
		nBits = n;
		//JOptionPane.showMessageDialog(null, n);
	}

	static int get_nBits()
	{
		return nBits;
	}

	// returns the string representing a term
	String get_sop()
	{
		return sop;
	}

	String get_pos()
	{
		return pos;
	}

	// creates a literal expression of a term
	String stringize()
	{
		StringBuffer s = new StringBuffer();

		for (int i = nBits-1; i >= 0; i--)
		if(!dontCare[i]) s.append((char)((bits[i] ? 'A' : 'a')+nBits-i-1));

		return s.toString();
	}

	// returns the decimal value of a term
	int findVal()
	{
		int x = 0;

		for (int i = 0; i < nBits; i++)
		if(!dontCare[i]) x |= (bits[i] ? 1 : 0) << i;

		return x;
	}

	// checks if two terms can be combined into one
	boolean matchWith(Term t)
	{
		for (int i = 0; i < nBits; i++)
			if(dontCare[i] != t.dontCare[i]) return false;

		if(countBits(value^t.value) == 1) return true;

		return false;
	}

	// counts the number of 1's in a term
	int countBits(int n)
	{
		int i;
		for (i = 0; n != 0; n &= n-1, i++);
		return i;
	}

	// counts the number of don't care bits of a term
	int countDC()
	{
		int x = 0;

		for (int i = 0; i < nBits; i++)
			if(dontCare[i]) x++;

		return x;
	}

	// marks a term which is no longer needed
	void check()
	{
		checked = true;
	}

	boolean isChecked()
	{
		return checked;
	}

/*	void print()
	{
		System.out.print(sop+ "\t");

		for(int i = nBits-1; i >= 0; i--)
		System.out.print(dontCare[i] ? "-" : (bits[i] ? "1" : "0"));

		System.out.println();
	}*/

	boolean cover(int n)
	{
		int mask = 0;

		for(int i = 0; i < nBits; i++)
			mask |= (((dontCare[i]) ? 0 : 1) << i);

		return ((mask & n) == (mask & value));
	}

	boolean isConst()
	{
		for(int i = 0; i < nBits; i++)
			if(dontCare[i] == false) return false;
		return true;
	}
}