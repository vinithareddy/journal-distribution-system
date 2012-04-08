<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (request.getRemoteUser() != null) {
        response.sendRedirect(request.getContextPath() + "/home");
    }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <%@include file="../templates/style.jsp"%>
        <title>Login</title>
        <script type="text/javascript">
            $(function() {
                $( "#loginDiv" ).tabs();
            });
        </script>
        <style>
            #bodyContainer{
                margin-top: 68px;
                width: 100%;
                overflow: auto;
                float: left;
            }


            #loginDiv{
                margin: 15% auto 0 auto;
                width: 40%;
                text-align: center;
                height: 200px;
            }

            #loginErrorMsg{

                text-align: center;
                color: red;
            }

            #resetpwdDiv{

                margin: 10% 10%;
            }

            .authField{

                width: 100%;
                margin: 10px auto;
                float: left;
            }

            .authLabel{
                width: 25%;
                float: left;
                margin-left: 10%;
                text-align: right;
            }

            .authInput{
                float: right;
                width: 50%;
                margin-right: 10%;
            }

            .authAction{

                margin-left: 45%;
                margin-top: 10px;
                float: left;
            }

        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp"></jsp:include>
        <%--<jsp:include page="../templates/loginsidebar.jsp"></jsp:include>--%>
        <div id="bodyContainer">
            <form action="j_security_check" method="post" name="frmlogin" onsubmit="return validate('loginFieldId','passwordField')">
                <div id="loginDiv">
                    <ul>
                        <li><a href="#login">Login</a></li>
                        <li><a href="#resetpwd">Forgot Password</a></li>
                    </ul>
                    <div id="login">
                        <div class="authField">
                            <span class="authLabel">User Name</span>
                            <span class="authInput"><input type="text" style="width:160px" class="IASTextBox" name="j_username" value="jds@ias.com" id="loginFieldId" onblur="validateEmail(this.id)"/></span>
                        </div>
                        <div class="authField">
                            <span class="authLabel">Password</span>
                            <span class="authInput"><input type="password" maxlength="16" style="width:160px" class="IASTextBox" value="jds" name="j_password" id="passwordField"/></span>
                        </div>
                        <div class="authAction">
                            <input type="submit" value="Login"/>
                            <input type="reset" value="Reset"/>
                        </div>

                    </div>

                    <div id="resetpwd">
                        <div class="authField">
                            <span class="authLabel">Email ID:</span>
                            <span class="authInput"><input type="text" style="width:160px" class="IASTextBox" name="j_user" id="ResetloginFieldId"/></span>
                        </div>
                        <div class="authAction">
                            <input type="submit" value="Reset Password"/>
                        </div>
                    </div>

                </div>
                <div id="loginErrorMsg">

                </div>

            </form>
        </div>
    </body>
</html>
