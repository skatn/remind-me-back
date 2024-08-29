package skatn.remindmeback.common.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithRestMockUserSecurityContextFactory.class)
public @interface WithRestMockUser {
    long id() default 1L;

    String username() default "member1";

    String name() default "member 1";

}
