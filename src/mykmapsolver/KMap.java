package mykmapsolver;

public class KMap
{
	int nBits, row, col;
	int [] minterm, dontCare, grayRow, grayCol;
	char [][] map;
	String [] rowHead, colHead;

	// creates k-map from 2 arrays of minterms
	KMap(int [] min, int [] dont)
	{
		minterm = min;
		dontCare = dont;
		nBits = Term.get_nBits();
		row = (1 << (nBits/2));
		col = (1 << nBits)/row;
		grayRow = grayCode(nBits/2);
		grayCol = grayCode(nBits-nBits/2);

		char [][] temp = new char [row][col];
		map = new char [row][col];
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				temp[i][j] = '0';

		for(int i = 0; i < minterm.length; i++)
		{
			int m = minterm[i] & (~(~0 << (nBits-nBits/2)));
			int n = (minterm[i] >> (nBits-nBits/2));
			temp[n][m] = '1';
		}

		for(int i = 0; i < dontCare.length; i++)
		{
			int m = dontCare[i] & (~(~0 << (nBits-nBits/2)));
			int n = (dontCare[i] >> (nBits-nBits/2));
			temp[n][m] = 'x';
		}

		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				map[i][j] = temp[grayRow[i]][grayCol[j]];

		makeHead();
	}

	// creates k-map from an array of terms
	KMap(Term [] array)
	{
		nBits = Term.get_nBits();
		row = (1 << (nBits/2));
		col = (1 << nBits)/row;
		grayRow = grayCode(nBits/2);
		grayCol = grayCode(nBits-nBits/2);

		char [][] temp = new char [row][col];
		map = new char [row][col];
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				temp[i][j] = '0';

		for(int k = 0; k < array.length; k++)
			for(int i = 0; i < row; i++)
				for(int j = 0; j < col; j++)
					if(array[k].cover(i*col+j))
						temp[i][j]++;
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				map[i][j] = temp[grayRow[i]][grayCol[j]];
		makeHead();
	}

	// generates gray code having specified number of bits
	int [] grayCode(int bits)
	{
		int [] array = new int [1 << bits];

		for(int i = 0; i < (1 << bits); i++)
			array[i] = i ^ (i >> 1);

		return array;
	}

	// converts a number into a binary string
	String toBinary(int n, int k)
	{
		StringBuffer s = new StringBuffer();

		for(int i = 0; i < k; i++)
		{
			s.append((char)((n % 2) + '0'));
			n /= 2;
		}

		return s.reverse().toString();
	}

	void makeHead()
	{
		rowHead = new String [row];
		colHead = new String [col];
		for(int i = 0; i < row; i++)
			rowHead[i] = toBinary(grayRow[i], nBits/2);
		for(int i = 0; i < col; i++)
			colHead[i] = toBinary(grayCol[i], nBits-nBits/2);
	}

	String [] getRowHead()
	{
		return rowHead;
	}

	String [] getColHead()
	{
		return colHead;
	}

	char [][] getMap()
	{
		return map;
	}
	// prints a k-map
/*	void print()
	{
		System.out.printf("%8c", ' ');
		for(int i = 0; i < col; i++)
			System.out.printf("%8s", colHead[i]);
		System.out.println();
		for(int i = 0; i < row; i++)
		{
			System.out.printf("%8s", rowHead[i]);
			for(int j = 0; j < col; j++)
				System.out.printf("%8c", map[i][j]);
			System.out.println();
		}
	}*/
}
