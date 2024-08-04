package ru.mastkey.translater.audit.annotation;


import ru.mastkey.translater.audit.service.AuditSender;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditAnnotation {
    Class<? extends AuditSender>[] senders() default {};
}
