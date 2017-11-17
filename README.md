
# Project Description
This Project use **spring boot** + **spring mvc** + **spring security** + **liquibase** + **h2** technology.  
Functions like below:
- Custom Pre-Auth Filter: When user login into this demo, it will give an Auth token into browser cookies。And when a request come to system, Pre-auth filter will convert token to User authentication info.
- Custom AuthenticationProcessingFilter: you can use AJAX POST {username: 'name', passwoed: 'pwd'} to this filter, and then will login in the system.
- switchUserFilter: A super admin can switch user
- concurrentSessionFilter : One User only can login once.
- sessionManagementFilter : custom sessionManagementFilter
- rememberMeFilter: custom rememberMeFilter
- Force user login function
- dynamic access config

# How to run the project

## download project

````

    git clone https://github.com/moheqionglin/spring-security-demo.git

````
## run project

````

    mvn clean spring-boot:run
````

## open url into browser

````
    # application url
     http://localhost:13103/security-demo/
     # h2 web console url: jdbc:h2:mem:test;DB_CLOSE_DELAY=-1 username: sa pwd: sa
     http://localhost:13103/security-demo/console
     
````

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
