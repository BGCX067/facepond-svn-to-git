<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Facepond</title>
<script language="javascript" src="/scripts/jquery-1.7.2.js"  type="text/javascript"></script>
<script language="javascript" src="/scripts/jquery.tmpl.js"  type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/styles/style.css">
<script  type="text/javascript">
		var extWin; 
        $(document).ready(function() {
        	//alert('Login facepond page');
        	$("#loginFB").click(function(event) {
				event.preventDefault();
				//var finalUrl = (document.location.href) + "facepondpostlogin.html";
				//console.log(finalUrl);
				//var returnToUrl = "http://facepond.couponpond.com:8080/facepond/loginWithSSO/";"?finalUrl="+finalUrl;
				var url = "/facepond/initiateLogin";//?finalUrl="+escape(finalUrl)
				//location.href=url;
				//alert("fbURL: "+url);
				extWin = window.open(url, "Authenticate","height=500,width=750,location=no,toolbar=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no ,modal=yes");
				
			});
        	
        	/* $.ajax({
        		type: 'GET',
        		url: 'dashboard',
        		success: function(data) {
        			console.log('parse as json'); 
        			var datastr=JSON.stringify(data);
        			var response=JSON.parse(datastr);
        			console.log('parsed: '+response);
        	        if(typeof response =='object') {
        		        alert('JSON Object');
        	    	} else {
        		    	//alert('Not a JSON Object');	    	
        		    	//response = JSON.parse("\""+data+"\"");
        	    	}
        			//$("#container_Content").empty();
        			//$("#container_Content").html(response.deals[0].id);
        		}
        	}); */
        	
        });
        
        function gotoDashboard() {
        	//alert("take the user to dashboard");
        	extWin.close();
        	$.ajax({
        		type: 'GET',
        		url: '/facepond/getUser',
        		dataType: 'json',
        		success: function(data) {
        			$("#container_Content").empty();
        			$("#container_Content").html('dashboard');
        		}
        	});
        }
        
        function gotoPersonalInfoEdit() {
        	extWin.close();
        	$.ajax({
        		type: 'GET',
        		url: '/facepond/getUser',
        		dataType: 'json',
        		success: function(data) {
        			$("#container_Content").empty();
        			$("#container_Content").html('personal info edit');
        		}
        	});
        }

</script>


</head>
	<body>
		<div id="container_Header"></div>
		
		<form action="/facepond/initiateLogin" method="GET">
			<input type="submit" value="Login with Facebook">
		</form>
		<div id="Main" class="container_wrap Main clearthis">
			<div id="container_Left" aria-live="polite"></div>
			<!--  <div id="container_Content" role="main" aria-live="assertive">
				<input type="button" id="loginFB" value="Login with Facebook">
			</div>-->

		</div>
		
		
	</body>	
</html>