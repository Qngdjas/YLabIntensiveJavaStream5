package ru.qngdjas.habitstracker.application.utils.logger.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ExecutionLogger {

    @Pointcut("within(@ru.qngdjas.habitstracker.application.utils.logger.ExecutionLoggable *) && execution(* *(..))")
    public void executionLogging() {
    }

    @Around("executionLogging()")
    public Object doExecutionLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.printf("Вызов метода %s.\n", proceedingJoinPoint.getSignature());
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis() - start;
        System.out.printf("Метод %s завершен. Время выполнения составило %d мс.\n", proceedingJoinPoint.getSignature(), end);
        return result;
    }

}
