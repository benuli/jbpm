package com.sample;

import java.util.List;

import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

/**
 * This is a sample file to launch a process.
 */
public class ProcessTest2 extends JbpmJUnitBaseTestCase {
	
	@Test
	public void testProcessProcessInstance() {			 
		RuntimeManager manager = createRuntimeManager(Strategy.PROCESS_INSTANCE, "justaname", "com/sample/MultiInstanceProcess.bpmn2");		
		RuntimeEngine engine = manager.getRuntimeEngine(ProcessInstanceIdContext.get());		
		KieSession ksession = engine.getKieSession();	
		
		assertNotNull(ksession);       
		long ksession1Id = ksession.getIdentifier();
//        assertTrue(ksession1Id == 1);
		
		
		ProcessInstance processInstance = ksession.startProcess("defaultPackage.MultiInstanceProcess");
		assertProcessInstanceActive(processInstance.getId(), ksession);
//		
//		try {
//			Thread.sleep(30000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}

	//@Test
	public void testProcess() {			 
		/*default runtime strategy singleton*/
		RuntimeManager manager = createRuntimeManager("com/sample/MultiInstanceProcess.bpmn2");
		RuntimeEngine engine = manager.getRuntimeEngine(null);
		
		KieSession ksession = engine.getKieSession();
		ProcessInstance processInstance = ksession.startProcess("defaultPackage.MultiInstanceProcess");		
		TaskService taskService = engine.getTaskService();
		

		assertProcessInstanceActive(processInstance.getId(), ksession);
		//assertNodeTriggered(processInstance.getId(), "Task 1");
//		
//		// let john execute Task
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
		for(TaskSummary task : list){
			
			System.out.println("John is executing task " + task.getName());
			taskService.start(task.getId(), "john");
			taskService.complete(task.getId(), "john", null);
		}


		//assertProcessInstanceCompleted(processInstance.getId(), ksession);
		
		manager.disposeRuntimeEngine(engine);
		manager.close();
	}
	
	public ProcessTest2() {
		super(true, true);
	}
	
}