<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>

<html lang="zh-CN"><head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>登录页</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="../css/signin.css" rel="stylesheet">
</head>

<body>

<div class="container">

    <form class="form-signin">

        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" class="form-control" name="username" placeholder="Email address" required="" autofocus="">
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required="">
        <div class="checkbox">
            <label>
                <input type="checkbox" name="remember-me" id="remember-me-cb" > Remember me
            </label>
        </div>
        <div class="alert alert-info alert-dismissible" hidden role="alert" id="processing-alert">
            <strong>提交中。。。</strong>
        </div>
        <div class="alert alert-danger alert-dismissible" hidden role="alert" id="error-alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <strong>Warning!</strong> 用户名密码错误
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="button" id="login-btn">Sign in</button>
    </form>

</div> <!-- /container -->


<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script>
    $(function(){
        $("#login-btn").on("click", function (){
            $.ajax({
                type:"POST",
                contentType: 'application/json',
                url:"../ajax_login",
//                data: $('.form-signin').serialize(),
                data: JSON.stringify({username: $("#inputEmail").val(), password: $("#inputPassword").val(), "remember-me" : true}),
                beforeSend:function(){$("#processing-alert").show();$("#error-alert").hide();},
                success:function(data){
                    window.location.href = "${pageContext.request.contextPath}/";
                    console.log(data)
                }   ,
                error: function(){
                    $("#error-alert").show();$("#processing-alert").hide();
                }
            });
        })
    });


</script>
</body></html>