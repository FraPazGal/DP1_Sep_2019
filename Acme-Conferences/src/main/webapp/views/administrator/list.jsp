
<%--
 * action-1.jsp
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasRole('ADMIN')">
	<jstl:choose>
		<jstl:when test="${found}">
			<display:table class="displaytag" name="authors" pagesize="5"
				requestURI="${requestUri}" id="author">

				<display:column>
					<p>
						<input type="button"
							onclick="redirect: location.href = 'author/display.do?id=${author.id}';"
							value="<spring:message code='author.display' />" />
				</display:column>

				<display:column titleKey="actor.username" sortable="true">
					<p>
						<jstl:out value="${author.userAccount.username}" />
				</display:column>

				<display:column titleKey="actor.surname" sortable="true">
					<p>
						<jstl:out value="${author.surname}" />
				</display:column>

				<display:column titleKey="actor.name" sortable="true">
					<p>
						<jstl:out value="${author.name}" />
				</display:column>

				<display:column titleKey="author.score" sortable="true">
					<jstl:choose>
						<jstl:when test="${not empty author.score}">
							<p>
								<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2"
									value="${author.score }" />
						</jstl:when>
						<jstl:otherwise>
							<p class="error">N/A
						</jstl:otherwise>
					</jstl:choose>
				</display:column>

			</display:table>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<spring:message code="such.empty" />
		</jstl:otherwise>
	</jstl:choose>

</security:authorize>