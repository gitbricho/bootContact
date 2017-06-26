package bootapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private AppData data;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.err.println("Create Test Data");	
		data.createUsers();
	}
}
