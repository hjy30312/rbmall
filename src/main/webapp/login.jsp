<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="common/tag.jsp"%>
<%String path = request.getContextPath();%>
<html>
<head>
    <jsp:include page="common/head.jsp"/>
    <title>登陆</title>
    <style>
        .header {
            text-align: center;
        }
        .header h1 {
            font-size: 200%;
            color: #333;
            margin-top: 30px;
        }
        .header p {
            font-size: 14px;
        }
    </style>
</head>

<body>


<div class="header">
    <div class="am-g">
        <h1>Web ide</h1>
    </div>
    <hr />
</div>
<div class="am-g">
    <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
        <h3>登录</h3>
        <hr>
        <form class="am-form">
            <br>
            <label for="username">账号:</label>
            <input type="text" name="username" id="username"/>
            <br>
            <label for="password">密码:</label>
            <input type="password" name="password" id="password"/>

            <div class="am-cf">
                <button type="button" id="submit" class="btn btn-default">登录</button>
            </div>
        </form>
        <form action="<%=path%>/customer/to_forget_answer" class="am-form">
            <input type="submit"value ="忘记密码?点击找回">
        </form>
        <form action="<%=path%>/customer/to_register"  class="am-form">
            <input type="submit" value ="没有帐号？点击注册">
        </form>
        <hr>
        <p>© 2014 AllMobilize, Inc. Licensed under MIT license.</p>
    </div>
</div>

</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $("#submit").click(function() {
        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
            type: "post",
            url: "<%=path%>/customer/login.do",
            data: {
                loginName: username,
                password: password
            },
            success: function(data) {
                //登录成功
                if(data.status === 0) {
                    window.location.href="<%=path%>/customer/baseInfo/list.do";
//                    window.location.href="跳转的url,后边也可以拼接点data中数据作为参数";
                } else {
                    alert("用户名或密码错误");
                }
            }
        });
    });
</script>
</html>
