<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>


<h1><spring:message	code="review.title.list" /><jstl:out value="${submission.ticker}" /></h1>
<display:table class="displaytag" name="reviews" pagesize="5" 
	requestURI="review/list.do" id="review" style="width:80%">
	
	<display:column titleKey="review.reviewer" sortable="true">
		<jstl:out value="${review.reviewer.name} ${review.reviewer.surname}" />
	</display:column>
	
	<jstl:choose>
		<jstl:when test="${review.status eq 'BORDER-LINE'}">
			<spring:message var="status" code='review.status.borderline' />
		</jstl:when>
		<jstl:when test="${review.status eq 'ACCEPTED'}">
			<spring:message var="status" code='review.status.accepted' />
		</jstl:when>
		<jstl:when test="${review.status eq 'REJECTED'}">
			<spring:message var="status" code='review.status.rejected' />
		</jstl:when>
	</jstl:choose>
	
	<display:column titleKey="review.status" sortable="true">
		<jstl:out value="${status}" />
	</display:column>
	
	<display:column titleKey="review.originality" sortable="true">
		<jstl:out value="${review.originalityScore}" />
	</display:column>
	
	<display:column titleKey="review.quality" sortable="true">
		<jstl:out value="${review.qualityScore}" />
	</display:column>
	
	<display:column titleKey="review.readability" sortable="true">
		<jstl:out value="${review.readabilityScore}" />
	</display:column>
	
	<display:column titleKey="review.comments" sortable="true">
		<jstl:if test="${not empty review.comments }">
			<jstl:out value="${review.comments}" />
		</jstl:if>
	</display:column>
	
	<%-- <display:column>
		<a href="review/display.do?id=${review.id}"> <spring:message
				code="mp.display" />
		</a>
	</display:column> --%>
</display:table>			