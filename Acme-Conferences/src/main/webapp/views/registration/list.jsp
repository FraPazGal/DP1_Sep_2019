<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<security:authorize access="hasAnyRole('AUTHOR')">

<h1><spring:message	code="registration.title.list" /></h1>

	<display:table class="displaytag" name="registrations" pagesize="5" 
		requestURI="registration/list.do" id="registration">

		<display:column titleKey="conference.title" sortable="true">
			<jstl:out value="${registration.conference.title}" />
		</display:column>
		
		<display:column titleKey="conference.venue" sortable="true">
			<jstl:out value="${registration.conference.venue}" />
		</display:column>
		
		<display:column titleKey="conference.startDate" sortable="true">
			<jstl:out value="${registration.conference.startDate}" />
		</display:column>
		
		<display:column titleKey="conference.endDate" sortable="true">
			<jstl:out value="${registration.conference.endDate}" />
		</display:column>
		
		<display:column>
			<a href="registration/display.do?registrationId=${registration.id}"> <spring:message
					code="registration.display" />
			</a>
		</display:column>
		
	</display:table>
	
</security:authorize>

<security:authorize access="!hasAnyRole('AUTHOR')">
	<p>
		<spring:message	code="registration.not.allowed" /><br>
	</p>
</security:authorize>