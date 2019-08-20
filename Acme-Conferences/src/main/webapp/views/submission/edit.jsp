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

<jstl:choose>
	<jstl:when test="${!cameraReady}">
		<h1><spring:message	code="submission.title.paper" /></h1>
		<form:form modelAttribute="submissionForm" action="submission/edit.do"
			id="form">
			
			<form:hidden path="conference" />
			
			<acme:textbox code="submission.paper.title" path="titleP" size="70px" />
			<form:errors path="titleP" id="title" dir="title" element="title" cssClass="error" /><br>
			<acme:textarea code="submission.paper.authors" path="authorsP" cols="40px" rows="3"/><br>
			<acme:textarea code="submission.paper.summary" path="summaryP" cols="80px" rows="4" /><br>
			<acme:textarea code="submission.paper.paperDocument" path="paperDocumentP" cols="80px" rows="8" /><br>
			
			<acme:submit code="mp.save" name="save" />&nbsp;
			<acme:cancel url="submission/list.do" code="mp.cancel" />
			<br />
	
		</form:form>
	</jstl:when>
	
	<jstl:when test="${cameraReady}">
		<h1><spring:message	code="submission.title.paper.cameraReady" /><jstl:out value="${submissionForm.conference.acronym }"/></h1>
		<form:form modelAttribute="submissionForm" action="submission/edit.do"
			id="form">
			
			<form:hidden path="id" />
			
			<acme:textbox code="submission.paper.title" path="titlePCR" size="70px" /><br> 
			<acme:textarea code="submission.paper.authors" path="authorsPCR" cols="40px" rows="3"/><br> 
			<acme:textarea code="submission.paper.summary" path="summaryPCR" cols="80px" rows="4" /><br> 
			<acme:textarea code="submission.paper.paperDocument" path="paperDocumentPCR" cols="80px" rows="8" /><br>
			
			<acme:submit code="mp.save" name="save" />&nbsp;
			<acme:cancel url="submission/list.do" code="mp.cancel" />
			<br />
	
		</form:form>
	</jstl:when>

<jstl:otherwise>
	<p>
		<spring:message	code="submission.not.allowed" /><br>
	</p>
</jstl:otherwise>
</jstl:choose>
