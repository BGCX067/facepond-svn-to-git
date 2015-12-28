<%@ include file="/common/taglibs.jsp"%>

<c:forEach items="${deals}" var="deal">
	<p>${deal.deals}<p>
</c:forEach>