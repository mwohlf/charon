
this directory contains the patched DDL-Scripts from the spring-security-oauth2-authorization-server-1.2.0-SNAPSHOT.jar

- howto about converting to JPA:
https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-jpa.html#define-data-model

we would have 3 Entities:
  RegisteredClientEntity
  OAuth2AuthorizationEntity
  OAuth2AuthorizationConsentEntity

and need to model them in JPA...

