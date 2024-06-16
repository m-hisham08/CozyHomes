package com.hisham.HomeCentre.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Custom annotation to inject the currently authenticated user into controller methods.
 *
 * <p>This annotation can be used on method parameters or types to indicate that
 * the annotated element should be bound to the current authenticated user.
 *
 * <pre>
 * Example usage:
 * &#64;GetMapping("/user/me")
 * public UserPrincipal getCurrentUser(&#64;CurrentUser UserPrincipal currentUser) {
 *     return currentUser;
 * }
 * </pre>
 *
 * <p>Annotations used:
 * <ul>
 *   <li>&#64;Target: Specifies the contexts in which the annotation can be used.
 *       <ul>
 *         <li>ElementType.PARAMETER: Indicates that the annotation can be applied to method parameters.</li>
 *         <li>ElementType.TYPE: Indicates that the annotation can be applied to types (classes, interfaces, etc.).</li>
 *       </ul>
 *   </li>
 *   <li>&#64;Retention: Indicates that the annotation is retained at runtime and can be accessed via reflection.
 *       <ul>
 *         <li>RetentionPolicy.RUNTIME: Important for Spring to use the annotation during the execution of the application.</li>
 *       </ul>
 *   </li>
 *   <li>&#64;Documented: Indicates that the annotation should be included in the JavaDoc of the annotated element,
 *       helping to generate documentation that includes the use of this custom annotation.
 *   </li>
 *   <li>&#64;AuthenticationPrincipal: Spring Security annotation to indicate that a method parameter should be bound to the current authenticated user.
 *   </li>
 * </ul>
 */

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
