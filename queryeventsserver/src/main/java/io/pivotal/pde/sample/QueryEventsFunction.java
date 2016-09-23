package io.pivotal.pde.sample;

import java.util.Properties;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.gemstone.gemfire.cache.partition.PartitionRegionHelper;
import com.gemstone.gemfire.pdx.PdxInstance;

public class QueryEventsFunction implements Declarable, Function {

	/**
	 * This Function is meant to be invoked as an "onRegion" or "data aware" 
	 * Function.  The argument is assumed to be an array of 2 entries.  The first
	 * is a String specifying an event type (e.g. "A", "B", etc.) , the second is 
	 * a String specifying a two letter state abbreviation (e.g. "GA").
	 * 
	 *  The Function reads all of the local data and returns the sum of 
	 *  the "count" field for all entries on this node that match both the
	 *  passed type and state. 
	 *  
	 *  This Function is coded using PdxInstance.  It assumes that the server
	 *  pdx configuration "read-serialized" is true.
	 */
	@Override
	public void execute(FunctionContext ctx) {
		RegionFunctionContext rctx = (RegionFunctionContext) ctx;
		
		/*
		 * Uncaught exceptions will be raised on the client side. 
		 */
		String []args = (String []) ctx.getArguments();
		if (args.length != 2)
			throw new RuntimeException("This function requires exactly 2 arguments");
		
		String type = args[0];
		String state = args[1];
		
		int result = 0;
		Region<String, PdxInstance> localData = PartitionRegionHelper.getLocalDataForContext(rctx);
		for(PdxInstance event : localData.values()){
			String t = (String) event.getField("type");
			String s = (String) event.getField("state");
			if (s.equals(state) && t.equals(type)) {
				result += (Byte) event.getField("count");
			}
			/*
			 * Note the cast to Byte above.  This is a side effect of writing
			 * the values through the REST API.  GemFire has to guess at the type.
			 * This does not happen when the objects are added via a typed language. 
			 */
		}
		
		/*
		 * its possible to stream results back, but "lastResult" 
		 * must always be called to send the last result.
		 */
		ctx.getResultSender().lastResult(Integer.valueOf(result));
	}

	@Override
	public String getId() {
		return "QueryEventsFunction";
	}

	
	/**
	 * has result false results in a "fire and forget" invocation pattern
	 */
	@Override
	public boolean hasResult() {
		return true;
	}

	/**
	 * isHA = true means the Function can safely be run again if there is a failure 
	 * this is true if a Function is read-only or is idempotent
	 */
	@Override
	public boolean isHA() {
		return true;
	}

	/**
	 * set to true for Functions that write to enable updates on local data 
	 */
	@Override
	public boolean optimizeForWrite() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Inherited from Declarable.  This allows the Function to be declared in 
	 * cache.xml if needed
	 */
	@Override
	public void init(Properties props) {
	}

}
