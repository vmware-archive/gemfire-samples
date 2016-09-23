package io.pivotal.pde.sample;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

public class QueryEvents {

	Region<String, Object> eventRegion;
	
	public void setEventRegion(Region<String, Object> r){
		this.eventRegion = r;
	}
	
	public int query(String type, String state){
		String []args = {type,state};
		ResultCollector rc = new SummingResultCollector();
		Execution exec = FunctionService.onRegion(eventRegion).withArgs(args).withCollector(rc);
		
		ResultCollector result = exec.execute("QueryEventsFunction");
		return (Integer) result.getResult();
	}
	
	
	/**
	 * Expects 2 arguments: "type" and "state"  
	 * 
	 * Returns the aggregate count of all entries across the cluster with 
	 * the given type and state.
	 * 
	 */
	public static void main(String []args){
		try {
			if (args.length != 2){
				System.err.println("Please provide 2 arguments: type and state");
				System.exit(1);
			}
			String type = args[0];
			String state = args[1];
			
			
			/**
			 * there are many ways to do this - just instantiating the context
			 * directly in this case
			 */
			ApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
			QueryEvents qe = ctx.getBean(QueryEvents.class);
			
			int count = qe.query(type, state);
			
			System.out.println("" + count);
		} catch(Exception x){
			x.printStackTrace(System.err);
		}
	}

}
