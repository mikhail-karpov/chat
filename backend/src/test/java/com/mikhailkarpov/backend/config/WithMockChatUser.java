package com.mikhailkarpov.backend.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithMockSecurityFactory.class)
public @interface WithMockChatUser {

  String TEST_USER_ID = "test-user-id";
  String TEST_USERNAME = "test-username";

  String id() default TEST_USER_ID;

  String username() default TEST_USERNAME;

}
