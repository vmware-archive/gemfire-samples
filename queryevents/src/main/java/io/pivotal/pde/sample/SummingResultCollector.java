package io.pivotal.pde.sample;

import java.util.concurrent.TimeUnit;

import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.distributed.DistributedMember;

public class SummingResultCollector implements ResultCollector<Integer, Integer> {

	int sum = 0;
	
	@Override
	public void addResult(DistributedMember member, Integer result) {
		sum += result.intValue();
	}

	@Override
	public void clearResults() {
		sum = 0;
	}

	@Override
	public void endResults() {
	}

	@Override
	public Integer getResult() throws FunctionException {
		return sum;
	}

	/**
	 * Doesn't seem like it should be necessary for the user to implement this.  Check to see
	 * if there is an adapter that does it. 
	 */
	@Override
	public Integer getResult(long t, TimeUnit tunit) throws FunctionException, InterruptedException {
			WaitThread wt = new WaitThread();
			wt.run();
			return wt.getResult(t, tunit);
	}
	
	public  class WaitThread extends Thread {

		int result = 0;
		
		@Override
		public void run() {
			
		}
		
		Integer getResult(long t, TimeUnit tunit){
			try {
				this.join(tunit.toMillis(t));
			} catch(InterruptedException x){
				//ok
			}
			
			if (this.isAlive())
				throw new RuntimeException("Result not returned in the expected amount of time");
			
			return result;
		}
	}

}
