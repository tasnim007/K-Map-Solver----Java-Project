package mykmapsolver;

import java.util.Arrays;

class PITable
{
	private boolean [][] table;
	private int [] minterm, dontCare, cost;
	private boolean [] check;
	private Term [] primarySel, finalSel;
	private Term [][] last;
	private boolean isConst;

	// creates prime implicant table from 2 arrays of minterms
	PITable(int [] num1, int [] num2)
	{
		minterm = num1;
		dontCare = num2;
		primarySel = set_primarySel();
		Arrays.sort(minterm);
		check = new boolean [minterm.length];
		finalSel = new Term[0];

		table = new boolean [primarySel.length][minterm.length];
		//check = new boolean [minterm.length];
		finalSel = new Term[0];

		isConst = (primarySel.length > 0);

		for (int i = 0; i < primarySel.length; i++)
			for(int j = 0; j < minterm.length; j++)
				table[i][j] = primarySel[i].cover(minterm[j]);
	}

	// creates prime implicant table from an array of terms and an array of minterms
	PITable(Term [] term, int [] num)
	{
		minterm = num;
		primarySel = term;
		check = null;
		finalSel = new Term[0];

		table = new boolean [primarySel.length][minterm.length];
		//check = new boolean [minterm.length];

		for (int i = 0; i < primarySel.length; i++)
			for(int j = 0; j < minterm.length; j++)
				table[i][j] = primarySel[i].cover(minterm[j]);
	}

	// creates prime implicants table from an existing table
	PITable(PITable pi)
	{
		this(pi.primarySel, pi.minterm);
		check = new boolean [pi.minterm.length];
		finalSel = new Term[0];
	}

	// combines the terms
	Term [] set_primarySel()
	{
		Term [] p = new Term [0];

		for (int i = 0; i < minterm.length; i++)
			p = TermFunctions.append(p, new Term(minterm[i]));

		for (int i = 0; i < dontCare.length; i++)
			p = TermFunctions.append(p, new Term(dontCare[i]));

		for (int i = 0; i < p.length; i++)
			for (int j = i+1; j < p.length; j++)
				if(p[i].matchWith(p[j]))
					p = TermFunctions.append(p, new Term(p[i], p[j]));

		p = TermFunctions.removeDuplicate(TermFunctions.minimized(p));
		return p;
	}

	// determines if a prime implicant is essential or not
	int isEssential(int col)
	{
		int x = 0, row = 0;

		for(int i = 0; i < primarySel.length; i++)
		{
			if(table[i][col])
			{
				x++;
				if(x == 1) row = i;
			}
		}

		return (x == 1) ? row : -1;
	}

	// marks the columns covered by the essential prime implicants
	void markEssential()
	{
		int row;
		check = new boolean [minterm.length];

		for(int i = 0; i < minterm.length; i++)
		{
			row = isEssential(i);

			if(row != -1)
			{
				finalSel = TermFunctions.removeDuplicate(
						TermFunctions.append(finalSel, primarySel[row]));
				for(int j = 0; j < minterm.length; j++)
					if(table[row][j])
						check[j] = true;
			}
		}
	}

	// removes the essential prime implicants and thir rows from the table
	void removeEssential()
	{
		int size = 0;

		for(int i = 0; i < primarySel.length; i++)
			if(!TermFunctions.exists(finalSel, primarySel[i]))
				size++;

		Term [] newTerm = new Term [size];

		for(int i = 0, j = 0; j < size; i++)
			if(!TermFunctions.exists(finalSel, primarySel[i]))
				newTerm[j++] = primarySel[i];
		primarySel = newTerm;

		size = 0;

		for(int i = 0; i < minterm.length; i++)
			if(!check[i])
				size++;

		int [] newMinterm = new int [size];

		for(int i = 0, j = 0; j < size; i++)
			if(!check[i])
				newMinterm[j++] = minterm[i];
		minterm = newMinterm;

		table = new PITable(newTerm, newMinterm).table;
		check = new boolean [minterm.length];
	}

	// converts a column to a number
	long colToNum(int col)
	{
		long x = 0;

		for(int i = 0; i < primarySel.length; i++)
			x |= ((table[i][col]) ? 1 : 0) << i;

		return x;
	}

	// converts a row to a number
	long rowToNum(int row)
	{
		long x = 0;

		for(int i = 0; i < minterm.length; i++)
			x |= ((table[row][i]) ? 1 : 0) << i;

		return x;
	}

	// removes the columns of minterms which dominates some other row
	void removeDominatingMinterm()
	{
		int size = 0;
		long [] temp = new long[minterm.length];
		boolean [] sel = new boolean [minterm.length];

		for(int i = 0; i < sel.length; i++)
		{
			sel[i] = true;
			temp[i] = colToNum(i);
		}

		for(int i = 0; i < temp.length; i++)
			for(int j = i+1; j < temp.length; j++)
			{
				if((temp[i] & temp[j]) == temp[i])
					sel[j] = false;
				else if((temp[i] & temp[j]) == temp[j])
					sel[i] = false;
			}

		for(int i = 0; i < sel.length; i++)
			if(sel[i]) size++;

		int [] newMinterm = new int [size];

		for(int i = 0, j = 0; i < minterm.length; i++)
			if(sel[i]) newMinterm[j++] = minterm[i];

		minterm = newMinterm;

		table = new PITable(primarySel, minterm).table;
		check = new boolean [minterm.length];
	}

	// removes the rows of terms that are dominated by some other row
	void removeDominatedTerm()
	{
		boolean [] sel = new boolean [primarySel.length];
		long [] temp =  new long [primarySel.length];
		int size = 0;

		for(int i = 0; i < sel.length; i++)
		{
			sel[i] = true;
			temp[i] = rowToNum(i);
		}

		for(int i = 0; i < temp.length; i++)
			for(int j = i+1; j < temp.length; j++)
			{
				if(temp[i] == temp[j])
				{
					if(primarySel[i].get_sop().length() >
						primarySel[j].get_sop().length()) sel[i] = false;
					else sel[j] = false;
				}
				else if((temp[i] & temp[j]) == temp[i])
					sel[i] = false;
				else if((temp[i] & temp[j]) == temp[j])
					sel[j] = false;
			}

		for(int i = 0; i < sel.length; i++)
			if(sel[i]) size++;

		Term [] newTerm = new Term [size];

		for(int i = 0, j = 0; i < primarySel.length; i++)
			if(sel[i]) newTerm[j++] = primarySel[i];

		primarySel = newTerm;

		table = new PITable(primarySel, minterm).table;
		check = new boolean [minterm.length];
	}

	void reduceTable()
	{
		for(int i = 0; true; i++)
		{
			if(i != 0 && new PITable(this).isEqual(this)) break;

			removeDominatingMinterm();
			removeDominatedTerm();
			markEssential();
			removeEssential();
		}
	}

/*	void printTable()
	{
		System.out.printf("%6c", ' ');
		for(int i = 0; i < minterm.length; i++)
			System.out.printf("%3d", minterm[i]);
		System.out.println();

		for(int i = 0; i < primarySel.length; i++)
		{
			System.out.printf("%6s", primarySel[i].get_sop());
			for(int j = 0; j < minterm.length; j++)
				System.out.printf("%3c", (table[i][j] ? 'x' : ' '));
			System.out.println();
		}
		System.out.printf("%6c", ' ');
		for(int i = 0; i < check.length; i++)
			System.out.printf("%3c", (check[i] ? 'o' : ' '));
		System.out.println();
	}*/

	void finish()
	{
		last = new Term [primarySel.length][0];
		cost = new int [primarySel.length];
		Term temp;
		int min, ind = 0;

		for(int i = 0; i < primarySel.length; i++)
		{
			PITable p = new PITable(this);
			temp = p.primarySel[i];

			for(int j = 0; j < p.minterm.length; j++)
				p.check[j] = p.table[i][j];

			p.removeEssential();

			p.reduceTable();

			if(!p.isTableEmpty()) p.finish();

			last[i] = p.finalSel;
			last[i] = TermFunctions.append(last[i], temp);

			for(int j = 0; j < last[i].length; j++)
				cost[i] += last[i][j].get_sop().length();

			min = cost[0];
			ind = 0;

			for(int j = 1; j < cost.length; j++)
				if(cost[j] < min)
				{
					ind = j;
					min = cost[j];
				}

			for(int j = 0; j < last[ind].length; j++)
				finalSel = TermFunctions.append(finalSel, last[ind][j]);

		}

	}

	String [] expr()
	{
		String [] s = new String [2];
		StringBuffer sb;

		if(finalSel.length == 0)
		{
			s[0] = s[1] = "0";
			return s;
		}
		else if(finalSel[0].isConst())
		{
			s[0] = s[1] = "1";
			return s;
		}

		sb = new StringBuffer();
		for(int i = 0; i < finalSel.length; i++)
		{
			sb.append(i == 0 ? "" : " + ");
			for(int j = 0; j < finalSel[i].get_sop().length(); j++)
			{
				char c = finalSel[i].get_sop().charAt(j);
				sb.append(Character.isUpperCase(c) ? c : (Character.toUpperCase(c)+"\'"));
			}
		}
		s[0] = sb.toString();

		sb = new StringBuffer();
		for(int i = 0; i < finalSel.length; i++)
			for(int j = 0; j < finalSel[i].get_pos().length(); j++)
			{
				char c = finalSel[i].get_pos().charAt(j);
				if(Character.isLowerCase(c)) sb.append(Character.toUpperCase(c)).append('\'');
				else sb.append(c);
			}
		s[1] = sb.toString();

		return s;
	}

	Term [] result()
	{
		return finalSel;
	}

	// checks if 2 tables are equal
	boolean isEqual(PITable p)
	{
		return minterm.length == p.minterm.length
				&& primarySel.length == p.primarySel.length;
	}

	// checks if a table is empty or not
	boolean isTableEmpty()
	{
		return table.length == 0 || table[0].length == 0;
	}






	int [] checkAns(){
			int n,arr[],i,j;
			boolean f=false;
			n=1<<Term.get_nBits();
			arr=new int[n];
			for(i=0;i<n;i++){
				for(j=0;j<finalSel.length;j++){
						f=finalSel[j].cover(i);
						if(f==true){
								arr[i]=1;
								break;
							}
					}
					if(f==false)
						arr[i]=0;

			}
			return arr;
		}






}