package org.dgfoundation.amp.testutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.digijava.module.fundingpledges.action.DisableableKeyValue;

import junit.framework.TestCase;

/**
 * intermediary class for holding various utility methods for testcases
 * @author Dolghier Constantin
 *
 */
public abstract class AmpTestCase extends TestCase
{
	public AmpTestCase(String name) {
		super(name);
	}
	
	public void shouldFail(AmpRunnable runnable){
		try{
			runnable.run();
			throw new RuntimeException("did not throw exception");
		}
		catch(Throwable thr){
			// it is ok, we want an exception
		}
	}
	
	public void testDisableableKeyValues(Collection<DisableableKeyValue> res, DisableableKeyValue... cor){
		List<DisableableKeyValue> corList = new ArrayList<>(Arrays.asList(cor));
		List<DisableableKeyValue> resList = new ArrayList<>(res);
		Collections.sort(corList);
		Collections.sort(resList);
		assertEquals(corList.size(), resList.size());
		for(int i = 0; i < corList.size(); i++){
			assertEquals(corList.get(i), resList.get(i));
		}
	}

}
