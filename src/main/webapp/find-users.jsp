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
        <h2>请输入查找用户的email地址</h2>
        <div class="row">
            <div class="col-lg-12">
                <div class="input-group">

                    <input type="text" id="findEmail" class="form-control" placeholder="email" aria-describedby="basic-addon1">
                    <span class="input-group-btn">
                    <button id="findBtn" class="btn btn-primary">查找</button>
                  </span>
                </div><!-- /input-group -->
            </div>
        </div>


        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>#</th>
                    <th>用户名</th>
                    <th>密码</th>
                    <th>姓名</th>
                    <th>性别</th>
                </tr>
                </thead>
                <tbody id="tableData">

                </tbody>
            </table>
        </div>
    </div>

</div> <!-- /container -->
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<%@ include file="modal.jsp" %>
<script>
    $(function () {
        var loadingDom = ' <tr class=\'text-center\'><td  colspan="5">加载中...</td></tr>';

        var generateDom = function(user){
            var dom = '<tr>';
            dom += '<td>' + user.id + '</td>';
            dom += '<td>' + user.email + '</td>';
            dom += '<td>' + user.password + '</td>';
            dom += '<td>' + user.name + '</td>';
            dom += '<td>' + user.sex + '</td>';
            dom += '</tr>';
            return dom;
        }

        $("#findBtn").on("click", function () {
            var email = $("#findEmail").val();
            $('#tableData').html(loadingDom);
            $.ajax({
                type:"GET",
                url: '${pageContext.request.contextPath}/user/findUserByEmail/'+ email,
                success:function(data){
                    $('#tableData').html(generateDom(data));
                }   ,
                error: function(jqXHR, textStatus, errorThrown){
                    $('#tableData').html('<tr class=\'text-center\'><td  colspan="5">' + jqXHR.responseText + '<br>' + errorThrown +'</td></tr>');
                }
            });

        })
    });
</script>
</body></html>