<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasAnyRole('SPONSOR')">
<jstl:choose>
<jstl:when test="${errMsg ne null}">
		<p>
			<jstl:out value="${errMsg}" />
		</p>
</jstl:when>
<jstl:when test="${isPrincipal }">


<h1><spring:message code="sponsorship.title.display" /></h1>
<table class="displayStyle">
	
	<tr>
		<td><strong> <spring:message code="sponsorship.banner" /> : </strong></td>
		<td><jstl:out value="${sponsorship.banner}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="sponsorship.targetPage" /> : </strong></td>
		<td><jstl:out value="${sponsorship.targetPage}"></jstl:out></td>
	</tr>
	
	
</table>

<h2><spring:message code="sponsorship.title.creditcard.display" /></h2>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="sponsorship.holder" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.holder}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.make" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.make}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.number" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.number}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.expirationMonth" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.expirationMonth}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.expirationYear" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.expirationYear}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="sponsorship.CVV" /> : </strong></td>
		<td><jstl:out value="${sponsorship.creditCard.CVV}"></jstl:out></td>
	</tr>
	
</table>

<h2><spring:message code="sponsorship.display.conferences" /></h2>
<display:table class="displaytag" name="${sponsorship.conferences}" pagesize="5" 
		requestURI="sponsorship/display.do" id="conference">

	<display:column titleKey="sponsorship.conference.title" sortable="true">
		<jstl:out value="${conference.title}" />
	</display:column>
	
	<display:column titleKey="sponsorship.conference.acronym" sortable="true">
		<jstl:out value="${conference.acronym}" />
	</display:column>
	
	<display:column >
		<a href="conference/display.do?conferenceId=${conference.id}"> <spring:message
				code="sponsorship.display" />
		</a>
	</display:column>
</display:table>

	<input type="button" name="back"
		value="<spring:message code="sponsorship.back" />"
		onclick="window.history.back()" />

</jstl:when>
<jstl:otherwise>
<p>
	<spring:message	code="sponsorship.not.allowed" /><br>
</p>

</jstl:otherwise>
</jstl:choose>

</security:authorize>