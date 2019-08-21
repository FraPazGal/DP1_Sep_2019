<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<h1><spring:message code="registration.title.display" /></h1>

<h2><spring:message code="registration.display.conference" /></h2>
<table class="displayStyle">

	<tr>
		<td><strong> <spring:message code="conference.title" /> : </strong></td>
		<td><jstl:out value="${registration.conference.title}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="conference.acronym" /> : </strong></td>
		<td><jstl:out value="${registration.conference.acronym}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="conference.venue" /> : </strong></td>
		<td><jstl:out value="${registration.conference.venue}"></jstl:out></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="conference.entryFee" /> : </strong></td>
		<td><jstl:out value="${registration.conference.entryFee}"/> &#8364;</td>
	</tr>
	
	<spring:message code="date.dateFormat" var="format" /> 
	<tr>
		<td><strong> <spring:message code="conference.startDate" /> : </strong></td>
		<td><span><fmt:formatDate pattern="${format }" value="${registration.conference.startDate}" /></span></td>
	</tr>
	
	<tr>
		<td><strong> <spring:message code="conference.endDate" /> : </strong></td>
		<td><span><fmt:formatDate pattern="${format }" value="${registration.conference.endDate}" /></span></td>
	</tr>
	
	<tr>
		<td>
			<br>
			<a href="conference/display.do?conferenceId=${registration.conference.id}"> <spring:message
					code="conference.goto" />
			</a>
		</td>
	</tr>
</table>
		
<h2><spring:message code="registration.title.creditcard.display" /></h2>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="registration.holder" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.holder}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="registration.make" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.make}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="registration.number" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.number}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="registration.expirationMonth" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.expirationMonth}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="registration.expirationYear" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.expirationYear}"></jstl:out></td>
	</tr>
	<tr>
		<td><strong> <spring:message code="registration.CVV" /> : </strong></td>
		<td><jstl:out value="${registration.creditCard.CVV}"></jstl:out></td>
	</tr>
	
</table>

<input type="button" name="back" value="<spring:message code="mp.back" />"
	onclick="window.history.back()" />
