package org.digijava.module.fundingpledges.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class AmpCollections {
	
	/**
	 * returns a map of all the input values
	 * keys appear in map in input order; on each level values appear in input order
	 * @param values
	 * @param mapper
	 * @return
	 */
	public static<K, V> Map<K, List<V>> distribute(Collection<V> values, Function<V, K> mapper)
	{
		Map<K, List<V>> res = new TreeMap<K, List<V>>();
		for(V v:values)
		{
			K key = mapper.apply(v);
			if (!res.containsKey(key))
				res.put(key, new ArrayList<V>());
			res.get(key).add(v);
		}
		return res;
	}
	
	/**
	 * 
	 * merges N lists
	 * @param lists
	 * @return
	 */
	@SafeVarargs
	public static<V> List<V> mergeLists(Iterable<V>... lists)
	{
		return Lists.newArrayList(Iterables.concat(lists));
	}
	
}
