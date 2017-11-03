<%@ page import="com.springsecurity.demo.common.UserInfo" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% Authentication authentication =   SecurityContextHolder.getContext().getAuthentication();
    try{
        UserInfo user = (UserInfo) authentication.getPrincipal();

        pageContext.setAttribute("userInfo", user);
        pageContext.setAttribute("userEmail", user.getEmail());
    }catch(Exception e){}

%>
<!DOCTYPE html>
<html lang="zh-CN"><head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>用户管理页</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet">
</head>

<body>

<div class="container">
    <!-- Static navbar -->
    <%@ include file="nav.jsp" %>
    <div class="alert alert-success" role="alert">
        <strong>温馨提示</strong>
        <br><sec:authentication property="principal"/>
        <c:out value="${userData}"/>
        <br><sec:authentication property="authorities"/>
    </div>
    <!-- Main component for a primary marketing message or call to action -->
    <div class="jumbotron">
        <h2>管理用户列表</h2>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>#</th>
                    <th>用户名</th>
                    <th>密码</th>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="currentUser" items="${allUsers}" varStatus="status">
                    <tr>
                        <td>${status.index}</td>
                        <td>${currentUser.email}</td>
                        <td>${currentUser.password}</td>
                        <td>${currentUser.name}</td>
                        <td>${currentUser.sex}</td>
                        <td><button type="button" class="btn btn-primary">修改</button><button type="button" class="btn btn-danger">删除</button></td>
                    </tr>
                </c:forEach>

                </tbody>
                <button type="button" class="btn btn-success">新增</button>
            </table>
        </div>
    </div>

</div> <!-- /container -->
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<%@ include file="modal.jsp" %>
</body></html>