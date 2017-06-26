package bootapp;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogRule extends TestWatcher {
	private static final Logger log = LoggerFactory.getLogger(LogRule.class);
    private static final String LOG_TEST_BAR = "&&&&&&&&&&&&&&& {} {}: {}";
    private Description desc;
    
    @Override
	protected void starting(Description description) {
    	desc = description;
	}
    
    protected void testStart(String syubetu, String setumei) {
    	log.info(LOG_TEST_BAR, syubetu, desc.getMethodName(), setumei);
    }
    
	@Override
	protected void finished(Description description) {
    	log.info("&&&---&&&");
	}
}
