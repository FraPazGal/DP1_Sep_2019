<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<ul id="jSubMenu">
		<security:authorize access="hasRole('ADMIN')">
		
			<li><a href="submission/list.do?catalog=underR"><spring:message
						code="submission.submission.list.underreview" /></a></li>
						
			<li><a href="submission/list.do?catalog=accepted"><spring:message
						code="submission.submission.list.accepted" /></a></li>
						
			<li><a href="submission/list.do?catalog=rejected"><spring:message
						code="submission.submission.list.rejected" /></a></li>
							
		</security:authorize>
	</ul>
</div>
