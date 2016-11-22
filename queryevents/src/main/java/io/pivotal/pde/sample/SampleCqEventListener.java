package io.pivotal.pde.sample;

import com.gemstone.gemfire.cache.query.CqEvent;
import com.gemstone.gemfire.pdx.PdxInstance;

public class SampleCqEventListener  {

	public void handleEvent(CqEvent event) {		
		if (event.getQueryOperation().isCreate()){
			// a new entry was created or an entry that previously did not match the query does so now
			System.out.println("new event: key = " + event.getKey() + " value = " + ((PdxInstance) event.getNewValue()).getObject());
		} else if (event.getQueryOperation().isUpdate()){
			// an entry that matches the query was updated but still matches the query
			System.out.println("updated event: key = " + event.getKey() + " value = " + ((PdxInstance) event.getNewValue()).getObject());
		} else if (event.getQueryOperation().isDestroy()){
			// an entry that matches the query was removed or updated as to no longer match
			System.out.println("removed event: key = " + event.getKey());
		}
	}

}
