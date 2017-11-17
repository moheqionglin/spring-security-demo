
# Project Description
This Project use spring boot + spring mvc + spring security technology.  Functions like below:
- Custom Pre-Auth Filter: When user login into this demo, it will give an Auth token into browser cookies。And when a request come to system, Pre-auth filter will convert token to User authentication info.
- Custom AuthenticationProcessingFilter: you can use AJAX POST {username: 'name', passwoed: 'pwd'} to this filter, and then will login in the system.
- switchUserFilter: A super admin can switch user
- concurrentSessionFilter : One User only can login once.
- sessionManagementFilter : custom sessionManagementFilter
- rememberMeFilter: custom rememberMeFilter
- Force user login function

# How to run the project

