<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('AUTHOR')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
			<h1><spring:message	code="registration.title.create" /><i><jstl:out value="${registrationForm.conference.title }"></jstl:out></i></h1>
			<h3><spring:message	code="registration.price" /><jstl:out value="${registrationForm.conference.entryFee }"></jstl:out>&#8364; </h3><br>
			<form:form modelAttribute="registrationForm" action="registration/edit.do"
				id="form">
				
				<form:hidden path="conference" />
				
				<acme:textbox code="registration.holder" path="holder" size="100px" /><br> <br>
				<acme:textbox code="registration.make" path="make" size="100px" /><br> <br>
				<acme:textbox code="registration.number" path="number" size="100px" /><br> <br>
				<acme:textbox code="registration.expirationMonth" path="expirationMonth" size="100px" /><br> <br>
				<acme:textbox code="registration.expirationYear" path="expirationYear" size="100px" /><br> <br>
				<acme:textbox code="registration.CVV" path="CVV" size="100px" /><br> <br>
				
<%-- 				<jstl:if test="${message }">
					<spring:message	code="${message }" cssClass="error"/>
				</jstl:if> --%>
				
				<acme:submit code="registration.save" name="save" />&nbsp;
				<acme:cancel url="registration/list.do" code="registration.cancel" />
				<br />
		
			</form:form>
		</jstl:when>
	
	<jstl:otherwise>
		<p>
			<spring:message	code="registration.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>

<security:authorize access="!hasRole('AUTHOR')">
		<p>
			<spring:message	code="registration.not.allowed" /><br>
		</p>
</security:authorize>