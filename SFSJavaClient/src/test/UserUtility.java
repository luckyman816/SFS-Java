package test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author madhusmitap
 *
 */
public class UserUtility {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
        	SimpleUserMove worker = new SimpleUserMove();
            //executor.execute(worker);
            worker.start();
          }
	}

}
