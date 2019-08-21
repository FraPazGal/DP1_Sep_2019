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


<h1><spring:message	code="registration.title.create" /><i><jstl:out value="${registrationForm.conference.title }"></jstl:out></i></h1>
<h3><spring:message	code="registration.price" /><jstl:out value="${registrationForm.conference.entryFee }"></jstl:out> &#8364; </h3><br>
<form:form modelAttribute="registrationForm" action="registration/edit.do"
	id="form">
	
	<form:hidden path="conference" />
	
	<acme:textbox code="registration.holder" path="holder" size="100px" /><br>
	<acme:selectString items="${makes}" code="registration.make" path="make"/><br/>
	<acme:textbox code="registration.number" path="number" size="100px" placeholder="ccnumber.placeholder"/><br> 
	<acme:textbox code="registration.expirationMonth" path="expirationMonth" size="100px" placeholder="expirationMonth.placeholder"/><br> 
	<acme:textbox code="registration.expirationYear" path="expirationYear" size="100px" placeholder="expirationMonth.placeholder"/><br>
	<acme:textbox code="registration.CVV" path="CVV" size="100px" placeholder="cvv.placeholder"/><br> 
	
	<acme:submit code="mp.save" name="save" />&nbsp;
	<acme:cancel url="registration/list.do" code="mp.cancel" />
	<br/>

</form:form>
