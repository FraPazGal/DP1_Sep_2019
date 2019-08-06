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
		<security:authorize access="permitAll">
			<security:authorize access="hasRole('ADMIN')">
				<li><a href="conference/list.do?catalog=unpublished"><spring:message
								code="conference.conference.list.unpublished" /></a></li>
			</security:authorize>
			<li><a href="conference/list.do?catalog=future"><spring:message
						code="conference.conference.list.future" /></a></li>
						
			<li><a href="conference/list.do?catalog=running"><spring:message
						code="conference.conference.list.running" /></a></li>
						
			<li><a href="conference/list.do?catalog=past"><spring:message
						code="conference.conference.list.past" /></a></li>
						
			<security:authorize access="hasRole('ADMIN')">
			
				<li><a class="fNiv"><spring:message
						code="conference.conference.list.admin" /></a>
				<ul>
					<li><a href="conference/list.do?catalog=5sub"><spring:message
								code="conference.conference.list.5submission" /></a></li>
								
					<li><a href="conference/list.do?catalog=5not"><spring:message
								code="conference.conference.list.5notification" /></a></li>
								
					<li><a href="conference/list.do?catalog=5cam"><spring:message
								code="conference.conference.list.5camera" /></a></li>
								
					<li><a href="conference/list.do?catalog=5org"><spring:message
								code="conference.conference.list.5organised" /></a></li>
				</ul></li>
				
			</security:authorize>

		</security:authorize>
	</ul>
</div>
