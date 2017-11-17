
# 项目描述
关于 spring securiy的深入理解请看博客： [spring security源码深入剖析][11]

本项目采用 **spring boot** + **spring mvc** + **spring security** + **liquibase** + **h2** 实现的，如果你想用mysql作为数据库，那么只要修改下配置文件application.yaml中的spring.profiles.active: msql 即可。 这时候你要修改application-mysql.yaml中的数据库用户名密码， 项目启动的时候会用liquibase自动把mysql表建好，数据也会自动初始化。

项目功能列表：

- Custom Pre-Auth Filter: 在数据库的token上表中会存储登录的Token信息。用户只要携带token请求，就会通过pre-auth filter模块的过滤通过认证。
- Custom AuthenticationProcessingFilter: 你可以使用AJAX POST {username: 'name', passwoed: 'pwd'} 来登录系统.
- switchUserFilter: 使用spring security的 switch user 功能，只要你是超级管理员。你可以随意切换到任意用户视角使用系统。
- concurrentSessionFilter : 这里会控制用户并发限值
- sessionManagementFilter : custom sessionManagementFilter
- rememberMeFilter: custom rememberMeFilter
- 强制用户下线功能

# 如何启动项目

## 下载项目

````

    git clone https://github.com/moheqionglin/spring-security-demo.git

````
## mvn 运行程序
程序会自动初始化数据库和数据。

````

    mvn clean spring-boot:run
````

## 打开浏览器

````
    # 程序url
     http://localhost:13103/security-demo/
     # h2 数据库web控制台路径 jdbc:h2:mem:test;DB_CLOSE_DELAY=-1 用户名: sa 密码: sa
     http://localhost:13103/security-demo/console
     
````
## h2 数据库web 控制台
一共有
 | Tables        | desc           | other  |
 | ------------- |:-------------:| -----:|
 | auth_tokens      | Authentication token table |  |
 | authorization      | access url control config      |    |
 | persistent_logins | remember me token table      |     |
 | roles | role table      |     |
 | users | user table      |     |
 | users_roles | user role mapping table      |     |

![][12]

## home page
![][1]

## login page
There are three user account into database

 - ROLE_SUPPER_ADMIN:  login username: **super_admin@super_admin** pwd: **super_admin**
 - ROLE_ADMIN:  login username: **admin@admin** pwd: **admin**
 - ROLE_USER:  login username: **user@user** PWD: **user**


![][2]

## super_admin login 
![][3]

## super_admin pre-auth AUTH_TOKEN login
![][4]

## super_admin remember-auth login
![][5]

## super_admin manager-user
![][6]

## super_admin online user manager, force login user logout

![][7]

## super_admin switch user
![][8]
![][9]

## find user page
![][10]

[1]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/homepage.png
[2]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/login.png
[3]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/suer-admin.png
[4]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/pre-auth.png
[5]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/remember-auth.png
[6]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/super-manager-user.png
[7]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/super-online-user-manager.png
[8]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/super-switch-user-1.png
[9]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/super-switch-user.png
[10]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/find-user.png
[11]: http://www.moheqionglin.com/site/serialize/02007001001/detail.html
[12]: https://github.com/moheqionglin/spring-security-demo/blob/develop/src/main/resources/images/database.png
