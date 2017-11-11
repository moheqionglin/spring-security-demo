<%@ page import="com.springsecurity.demo.common.SecurityUtils" %>
<%@ page import="com.springsecurity.demo.common.UserInfo" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% Authentication authentication =   SecurityContextHolder.getContext().getAuthentication();
    UserInfo user = null;
    try{
        user = (UserInfo) authentication.getPrincipal();
        pageContext.setAttribute("userEmail", user.getEmail());
    }catch(Exception e){}

%>
<!DOCTYPE html>
<html lang="zh-CN"><head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>首页</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href=" ${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet">
</head>

<body>

<div class="container">
    <!-- Static navbar -->
    <%@ include file="nav.jsp" %>
    <div class="alert alert-success" role="alert">
        <strong>温馨提示</strong> <br>
        用户信息：<sec:authentication property="principal"/> <br>
        权限信息：<sec:authentication property="authorities"/><br>
        <hr>
        <%=SecurityContextHolder.getContext().getAuthentication()%><br>
        <hr>
        是否启用切换用户模式：
        <%= SecurityUtils.isImpersonating()%><br>
        <hr>
        SourceAuthentication 信息:
        <%= SecurityUtils.getImpersonator()%><br>
    </div>
    <!-- Main component for a primary marketing message or call to act ion -->
    <sec:authorize access="isAuthenticated()">
        <div class="jumbotron">
            <h1>Session Timeout page</h1>

                <dl class="dl-horizontal">
                    <dt>姓名</dt>
                    <dd><%= user.getName()%></dd>
                    <dt>邮箱</dt>
                    <dd><%= user.getEmail()%></dd>
                    <dt>性别</dt>
                    <dd><%= user.getSex()%></dd>
                    <dt>权限</dt>
                    <dd><%= user.getRoles()%></dd>
                </dl>
        </div>
    </sec:authorize>
</div> <!-- /container -->

<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<%@ include file="modal.jsp" %>
</body></html>