<html>
	<body>
		<script type="text/javascript">
			//get the current URL
			var url = (window.location.toString()).match(/\?([^#]+)(#.*)?$/)
				//get the parameters
				, params = RegExp.$1
				// split up the query string and store in an
				// associative array
				, params = params.split("&")
				, queryStringList = {}
				, wo = window.opener
				, msg
			;
			for(var i=0;i<params.length;i++) {
				var tmp = params[i].split("=");
				queryStringList[tmp[0]] = unescape(tmp[1]);
			}
			
			wo.gotoDashboard();			
			window.close();
						
			/* if (queryStringList['status'] === 'success') {
				if(queryStringList['userState'] === "pending_validation"){
					wo.login.setRelyingpartyUserID(queryStringList['userID']);
					wo.updateView("signupconfirm", "", "signupconfirm");
					window.close();
				} else {
					wo.gotoDashboard();					
					window.close();
				}
			} else {
        // msg = (queryStringList['msg']).replace('+','-');
				msg = queryStringList['msg'];
				alert(decodeURIComponent((msg+'').replace(/\+/g, '%20')));
        
        // alert(wo.$.i18n.prop(msg));
				//wo.alert(wo.$.i18n.prop(msg));
				window.close();
			} */
			
			//for(var i in queryStringList) {
			 //	document.write(i+" = "+queryStringList[i]+"<br/>");
			//}
		</script>
	</body>
</html>