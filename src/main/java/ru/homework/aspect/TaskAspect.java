package ru.homework.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.homework.exception.TaskException;

@Aspect
@Component
public class TaskAspect {

    private final Logger logger = LoggerFactory.getLogger(TaskAspect.class);

    @Before("execution(* ru.homework.service.TaskService.*(..))")
    public void beforeTaskServiceMethods(JoinPoint joinPoint) {
        logger.info("Начало выполнения метода '{}'", joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* ru.homework.service.TaskService.createNewTask(..))", returning = "result")
    public void saveTaskAfterReturning(Object result) {
        logger.info("Задача с индексом '{}' успешно сохранена!", Long.parseLong(result.toString()));
    }

    @AfterThrowing(pointcut = "execution(* ru.homework.controller.TaskController.*(..))", throwing = "taskException")
    public void afterThrowingTaskException(TaskException taskException) {
        logger.error("Выброшено исключение с типом '{}' . Сообщение: {}", taskException.getClass().getName(), taskException.getMessage());
    }

    @Around("@annotation(ru.homework.annotation.LogTimeInterval)")
    public Object checkAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long end = System.currentTimeMillis();

        logger.info("Затраченное время на выполнение метода '{}' равно '{} мс'", joinPoint.getSignature().getName(), end - start);

        return proceed;
    }
}
