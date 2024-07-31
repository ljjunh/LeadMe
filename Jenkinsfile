spring:
  profiles:
    default : local

server:
  port: 8090

---
spring:

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        format_sql: true
        default_batch_fetch_size: 500
    database: mysql
    show-sql: true
    open-in-view: true


  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://i11c109.p.ssafy.io:3306/leadme?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
#    url: jdbc:mysql://localhost:3306/leadme?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8&allowPublicKeyRetrieval=true
    username: ssafy
    password: ssafy

  # 파일 전송 시 20MB를 초과할 수 없으며 모든 데이터가 25MB를 초과할 수 없다.
  servlet:
    multipart:
      max-file-size: 20MB
      max-request-size: 25MB

  data:
    mongodb:
      uri: mongodb://i11c109.p.ssafy.io:27017/local

#  data:
#    mongodb:
#      uri: mongodb://localhost:27017/local

    redis:
      host: i11c109.p.ssafy.io
      port: 6379

  config:
    activate:
      on-profile: local
    import: optional:application-secret.properties

logging:
  level:
    org.hibernate.type: trace
    com.ssafy.withme: debug

jwt:
  issuer: secretkey@gmail.com
  secret_key: leadme

youtube:
  api:
    keys: AIzaSyCu0oBuaDUV8ygWfz3bQca0jhcj_1AR7MQ,AIzaSyAQ3pFZNGvYRfQdXArQFsTOW7IZRZYJl4M,AIzaSyChYqKKvvbN266NScADZSkuX6hBHHgnLqc,AIzaSyD2nOKRgfaakgVN0AeM8eLJ2z4AAuQZP3Q,AIzaSyASk6YJlMuLa3HdUywUHEawON3DuyJx_CE,AIzaSyDTJ60OEpf-Py9_Z_PTgCKlU5iuJtmyKAk,AIzaSyCu0oBuaDUV8ygWfz3bQca0jhcj_1AR7MQ,AIzaSyCyfz2EdcJOPQ2ymzHdEwpoFvLbfAaqZAk,AIzaSyDHBUPg9QWDHmuUVCru0eBDqGYRG03KF5Q,AIzaSyAvzeYNTgi3rxLbfdcPJ13DQ5TvF7E0xVc,AIzaSyBM45MzHUgO-RmK5pKPjqv7zLTElSkRuyo,AIzaSyBcPUSKi45t98gNLbw-LYgWzqemIEHgC8A,AIzaSyBleMRtO0sCEp0DIkLSTYiPuCai7azafTs,AIzaSyDYFmmfbl68r6RkUQ5hpsYXFT2lvaEjzRA,AIzaSyDvAoM3iDqVfeiE9rMAPpVmn23Gc5FguQ0,AIzaSyCAeAXrl8MO5D_9b59v1dr0C9O7sYMzUX4,AIzaSyD33a0wftIaRh30O58gK-yCVDONOE1hzfE,AIzaSyA7akjqLy7E1lf9A5ie_Oz4lvDMSSkYsU0,AIzaSyAhmAOcy47IeJZLtLdTJANICnThjx1jAGc

python-server:
  url: http://i11c109.p.ssafy.io:4567
  temp-directory: home/ubuntu/python/video/temporary
  permanent-directory: home/ubuntu/python/video/user


---
spring:
  config:
    activate:
      on-profile: test

  jpa:
    hibernate:
      ddl-auto: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  sql:
    init:
      mode: never
