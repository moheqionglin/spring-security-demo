<%@page pageEncoding="UTF-8"%>
<%@ page import="com.springsecurity.demo.common.AuthDetailsInfo" %>
<%@ page import="com.springsecurity.demo.common.AuthSource" %>
<%
    Authentication authentication1 =
            SecurityContextHolder.getContext().getAuthentication();
    if (authentication1 != null && authentication1.getDetails() instanceof AuthDetailsInfo) {
        AuthDetailsInfo authDetailsInfo = (AuthDetailsInfo) authentication1.getDetails();
        pageContext.setAttribute("authFrom", authDetailsInfo.getAuthFrom() == null ?  "" : authDetailsInfo.getAuthFrom());
    }
%>
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Spring security</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href=".">Spring security</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">管理员功能 <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="${pageContext.request.contextPath}/p/manager-user">用户列表维护</a></li>
                            <sec:authorize access="hasAnyRole('ROLE_SUPER_ADMIN')">
                                <li><a href="javascript:void(0);" data-toggle="modal" data-target="#exampleModal" >切换用户</a></li>
                                <li><a href="${pageContext.request.contextPath}/p/all-login-users" >在线用户管理</a></li>
                            </sec:authorize>

                        </ul>
                    </li>
                </sec:authorize>


                <sec:authorize access="isAuthenticated()">
                    <li ><a href="${pageContext.request.contextPath}/">个人Profile</a></li>
                </sec:authorize>

                <c:if test="${authFrom == 'PRE_AUTH_FILTER'}">
                    <li  ><a href="${pageContext.request.contextPath}/" style="color: #00FF00">从 "Pre auth" 认证</a></li>
                </c:if>
                <c:if test="${authFrom == 'LOGIN_AUTH_FILTER'}">
                    <li ><a href="${pageContext.request.contextPath}/" style="color: #00FF00">从 "Login auth" 认证</a></li>
                </c:if>
                <sec:authorize access="isRememberMe()">
                    <li ><a href="${pageContext.request.contextPath}/" style="color: #00FF00">从 "Remember Me Cookies" 认证</a></li>
                </sec:authorize>

                <sec:authorize access="hasAnyRole('ROLE_PREVIOUS_ADMINISTRATOR')">
                    <li ><a href="${pageContext.request.contextPath}/switch_user_exit" style="color: #00FF00">退出切换用户模式</a></li>
                </sec:authorize>


            </ul>
            <ul class="nav navbar-nav navbar-right">

                <sec:authorize access="isAuthenticated()">
                    <li><a href="${pageContext.request.contextPath}/logout_request">登出</a></li>
                </sec:authorize>
                <c:if test="${empty userEmail}">
                    <li><a href="${pageContext.request.contextPath}/p/login">登录</a></li>
                </c:if>
            </ul>
        </div><!--/.nav-collapse -->
    </div><!--/.container-fluid -->
</nav>