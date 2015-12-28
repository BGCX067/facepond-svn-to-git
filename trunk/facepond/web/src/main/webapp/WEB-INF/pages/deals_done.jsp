<%@ include file="/common/taglibs.jsp"%>


Deals Update Executed

<form action="/facepond/getUserDeals" method="POST">
			<div id="container_Content" role="main" aria-live="assertive">
				<input type="hidden" name="userId" value="${userId}">
				<input type=submit id="deals" value="Get My Deals">
			</div>
</form>