package io.pivotal.pde.sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

public class DurableEventQuery {

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
	 * Expects 1 arguments: "query"  
	 * 
	 */
	public static void main(String []args){
		try {
			
			/**
			 * there are many ways to do this - just instantiating the context
			 * directly in this case
			 */
			ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
			try {
				System.out.println("listening - press enter to stop");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				reader.readLine();
				
			} finally {
				// note: closing the ApplicationContext will remove the Cq -not what we want
				System.out.println("shutting down");
			}
			
		} catch(Exception x){
			x.printStackTrace(System.err);
		}
	}

}
