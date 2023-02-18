package johncervantes.springproject.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {

	// Setup logger
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	// Setup pointcut declarations
	@Pointcut("execution(* johncervantes.springproject.controller.*.*(..))")
	private void forControllerPackage() {}
	
	// Service
	@Pointcut("execution(* johncervantes.springproject.service.*.*(..))")
	private void forServicePackage() {}
	
	// DAO
	@Pointcut("execution(* johncervantes.springproject.dao.*.*(..))")
	private void forDaoPackage() {}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
	private void forAppFlow() {}
	
	// add @Before advice
	@Before("forAppFlow()")
	public void before(JoinPoint theJoinPoint) {
		// display method we are calling
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====>> in @Before: calling method: " + theMethod);
		
		// display the arguments to the method
	}
	
	// add @AfterReturning advice
	
	
}
