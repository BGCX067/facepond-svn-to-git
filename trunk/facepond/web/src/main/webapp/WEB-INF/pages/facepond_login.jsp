<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Facepond</title>
<script language="javascript" src="scripts/jquery-1.7.2.js"  type="text/javascript"></script>
<script  type="text/javascript">
		var extWin; 
        $(document).ready(function() {
        	alert('Login facepond page');
        	$("#loginFB").click(function(event) {
				event.preventDefault();
				var finalUrl = (document.location.href) + "/facebooklogin.jsp";
				console.log(finalUrl);
				var returnToUrl = "http://facepond.couponpond.com:8080/facepond/loginWithSSO?finalUrl=" + escape(finalUrl);
				var url = "https://www.facebook.com/dialog/oauth?client_id=446309825388322&scope=user_likes&redirect_uri="+escape(returnToUrl);
				location.href=url;
				//alert("fbURL: "+url);
				//extWin = window.open(url, "Authenticate","height=500,width=750,location=no,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no ,modal=yes");
				
			});
        });
        
        function gotoDashboard() {
        	alert("take the user to dashboard");
        	extWin.close();
        	//document.location= "/facepond/"
        }

</script>


</head>
	<body>
		<div class="mainContainer">
		<div id="topContainer"></div>
		<div id="leftContainer"></div>
		<div id="middleContainer3"></div>
		<div id="rightContainer"></div>
		</div>
		<input type="button" id="loginFB" value="Login with Facebook">
	
	
		<script type="text/javascript">
			
		</script>
	</body>	
</html>