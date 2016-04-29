package mykmapsolver;

// appends a term at the end of an array of terms
class TermFunctions
{
	public static Term [] append(Term [] array, Term elem)
	{
		Term[] temp = new Term [array.length+1];

		for (int i = 0; i < array.length; i++)
			temp[i] = array[i];

		temp[array.length] = elem;

		return temp;
	}

	// checks if a term exists in an array of terms
	public static boolean exists(Term [] array, Term elem)
	{
		for(int i = 0; i < array.length; i++)
			if(array[i].get_sop().equals(elem.get_sop()))
				return true;
		return false;
	}

	// append the unchecked terms into an array
	public static Term [] minimized(Term [] array)
	{
		Term[] temp = new Term [0];

		for(int i = 0; i < array.length; i++)
			if(!array[i].isChecked())
				temp = append(temp, array[i]);

		return temp;
	}

	// removes duplicate entries of terms
	public static Term [] removeDuplicate(Term [] array)
	{
		Term [] temp = new Term [0];

		for(int i = 0; i < array.length; i++)
			if(!exists(temp, array[i]))
				temp = append(temp, array[i]);

		return temp;
	}
}