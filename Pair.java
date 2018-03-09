/**
 * Represent a pair of elements.
 */
class Pair<S,T>
        implements Comparable<Pair<S,T>>
{
    /** first element in the pair */
    final S e1;

    /** second element in the pair */
    final T e2;

    /** is the first element an instance of Comparable? */
    final boolean e1Comparable;

    /** is the second element an instance of Comparable? */
    final boolean e2Comparable;


    /**
    * Constructor.
    *
    * @param e1
    *      first element in the pair
    * @param e2
    *      second element in the pair.
    */
    Pair(
            final S e1,
            final T e2)
    {
        this.e1 = e1;
        this.e2 = e2;

        this.e1Comparable = (e1 instanceof Comparable);
        this.e2Comparable = (e2 instanceof Comparable);
    }


    /**
    * Compares this pair of elements to the specified pair.
    * The comparison is performed in element-order, i.e. the first element followed by the second.
    */
    @SuppressWarnings("unchecked")
	public int compareTo(
            Pair<S,T> o)
    {
        if (e1Comparable)
        {
            final int k = ((Comparable<S>) e1).compareTo(o.e1);
            if (k > 0) return 1;
            if (k < 0) return -1;
        }

        if (e2Comparable)
        {
            final int k = ((Comparable<T>) e2).compareTo(o.e2);
            if (k > 0) return 1;
            if (k < 0) return -1;
        }

        return 0;
    }


    /**
    * Check if this pair of elements is equal to the specified object.
    * The comparison is performed on both elements in the pair.
    */
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(
            Object obj)
    {
        if (obj instanceof Pair)
        {
            final Pair<S,T> o = (Pair<S,T>) obj;
            return (e1.equals(o.e1) && e2.equals(o.e2));
        }
        else
        {
            return false;
        }
    }


    /**
    * Hash code for this pair of elements.
    */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 19 * hash + (e1 != null ? e1.hashCode() : 0);
        hash = 19 * hash + (e2 != null ? e2.hashCode() : 0);
        return hash;
    }
}