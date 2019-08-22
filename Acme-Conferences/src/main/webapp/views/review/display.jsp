<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


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

<table class="displayStyle">

		<tr>
			<td><strong> <spring:message code="review.submission" />: </strong></td>
			<td><jstl:out value="${review.submission.ticker}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="review.status" />: </strong></td>
			<td><jstl:out value="${status}"></jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="review.originality" />: </strong></td>
			<td><jstl:out value="${review.originalityScore}"></jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="review.quality" />: </strong></td>
			<td><jstl:out value="${review.qualityScore}"></jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="review.readability" />: </strong></td>
			<td><jstl:out value="${review.readabilityScore}"></jstl:out></td>
		</tr>
		
		<tr>
			<td><strong> <spring:message code="review.reviewer" />: </strong></td>
			<td><jstl:out value="${review.reviewer.userAccount.username}"></jstl:out></td>
		</tr>
		<jstl:if test="${not empty review.comments }">
			<tr>
				<td><strong> <spring:message code="review.comments" />: </strong></td>
				<td><jstl:out value="${review.comments}"></jstl:out></td>
			</tr>
		</jstl:if>
</table>

<input type="button" name="back"
	value="<spring:message code="mp.back" />"
	onclick="redirect: location.href = 'review/list.do?submissionId=${review.submission.id}';" />
	