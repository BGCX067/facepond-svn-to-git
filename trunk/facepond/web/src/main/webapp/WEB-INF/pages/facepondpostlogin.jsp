<%@ include file="/common/taglibs.jsp"%>


<html>
<head>
<title>Facepond</title>
<script language="javascript" src="/scripts/jquery-1.4.2.js"  type="text/javascript"></script>
<script language="javascript" src="/scripts/jquery.tmpl.js"  type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/styles/style.css">	

<script  type="text/javascript">
		var userId = ${result.userId};
		

        $(document).ready(function() {
        	
       		$.ajax({
       			url: '/scripts/dealstemplate.tmpl.js',
       			async: true,
       			type: "GET",
       			cache: true,
       			success: function (template) {
       				$('#body_content').append($(template));
       			},
       			error: function (data, textStatus, errorThrown) {
       				alert('addTemplateResourceToPageDOM: Error retrieving app details. data= ' + '; textStatus= '  + textStatus + '; errorThrown= '  + errorThrown);
       			},
       			dataType: 'script'
       			
       		});        		        	
        	
        	//alert('Login facepond page');
        	$("#getdeals").click(function(event) {
        		$("#msgDiv").empty();
        		$.ajax({
            		type: 'GET',
            		data: {'userId': userId},
            		url: '/facepond/getUserDeals',
            		dataType: 'json',
            		success: function(data) {
            			console.log('v4 call template');
            			applyDealsTemplate(data);            			
            		},
        			error: function(xhrObj, textStatus, errorThrown) {
        					$("#dealsContent").html("Error Occurred. "+xhrObj.responseText+ 'errorThrown='+errorThrown);
        			}
            	});
				
			});
        
        	$("#deals").click(function(event) {
        		$("#msgDiv").empty();
        		$.ajax({
            		type: 'POST',
            		data: {'userId': userId},
            		url: '/facepond/updateUserDeals',
            		success: function(data) {
            			$("#dealsContent").empty();
            			if ( data && data == 'failed') {
            				$("#dealsContent").html("<strong><red>Refresh failed. The division is not supported by group on.</red></strong>");
            			}
            			
            			$("#dealsContent").html("Refresh successful. Please access Get My Deals button to see the deals.");
            		},
        			error: function(xhrObj, textStatus, errorThrown) {
        				if (xhrObj.responseText && xhrObj.responseText.indexOf('Division not supported') != -1) {
        					$("#dealsContent").html("<strong>Refresh failed. The division is not supported by group on.</strong>");
        				} else {
        					$("#dealsContent").html("<strong>Refresh failed. The division is not supported by group on.</strong> "+xhrObj.responseText);
        				}
        			}
            	});
				
			});
        	
        	$(".viewDetailLink").live('click', function(e) {
        		e.preventDefault();
    			var url =$(this).attr("id");
    			window.open(url, "groupon");
        	});
        	
        });
        
        function applyDealsTemplate(data) {
        	$("#dealsContent").empty();
        	if (data && data.deals && data.deals.length > 0) {
        		var dealsTemplate = $.template(null, $("#CouponDisplayTemplate"));
        		for (var i=0; i < data.deals.length; i++) {
	        		var deals = data.deals[i].deals;
	    			jsonObj = JSON.parse(deals);
	    			console.log(jsonObj.type);
	    			//$("#CouponDisplayTemplate").tmpl(jsonObj).appendTo("#dealsContent");
	    			if (jsonObj.options) {
	    				jsonObj.actualprice = jsonObj.options[0].value.formattedAmount;
	    				jsonObj.discountPercent = jsonObj.options[0].discountPercent;
	    				jsonObj.disprice = jsonObj.options[0].price.formattedAmount;
	    			}
	    			
	    			$.tmpl(dealsTemplate, jsonObj).appendTo("#dealsContent");
        		}
        	} else {
        		$("#dealsContent").html("No deals returned for your likes. Please try refreshing the deals.");
        	}
			
        	
        }
        
        //alert(userId);
</script>
</head>
<body id="body_content">

	<div id="msgDiv">
		<h1>${result.status}</h1>
		<c:choose>
			<c:when  test="${result.userState == 'new'}">
			<p>Welcome ${result.userName}. Your account Account has been created.</p>
			</c:when>
			<c:when  test="${result.userState == 'failure'}">
			<p>Error occurred during login.</p>
			</c:when>
			<c:otherwise>
			<p>Welcome ${result.userName}. Your account has been updated.</p>
			</c:otherwise>
		</c:choose>
	</div>
	<div id="container_Content" role="main" aria-live="assertive">
		<input type="hidden" name="userId" value="${result.userId}">
		<input type="button" id="deals" value="Update/refresh Deals">
		<input type="button" id="getdeals" value="Get My Deals">
	</div>
	<div id="dealsContent">
	</div>

</body>
</html>