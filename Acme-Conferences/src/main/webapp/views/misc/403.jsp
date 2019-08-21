<%--
 * 403.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<div style=" margin-top: 2%; margin-bottom: 2%; text-align: center;">
	<img alt="Error" src="https://image.flaticon.com/icons/svg/61/61875.svg" height="150px" />
</div>
<p style=" text-align: center; font-size: 30"><spring:message code="welcome.oops" /></p>

<p style="text-align: center; font-size: 18">
	<a href="<spring:url value='/' />"><spring:message code="welcome.return" /></a>
<p>

