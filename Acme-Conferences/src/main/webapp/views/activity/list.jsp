
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

<display:table class="displaytag" name="activities" pagesize="5"
	requestURI="${requestUri}" id="activity">

	<display:column>
		<input type="button"
			onclick="redirect: location.href = 'activity/display.do?activityid=${activity.id}';"
			value="<spring:message code='activity.display' />" />
	</display:column>

	<display:column titleKey="activity.title" sortable="true">
		<jstl:out value="${activity.title}" />
	</display:column>

	<display:column titleKey="activity.title" sortable="true">
		<jstl:out value="${activity.type}" />
	</display:column>

	<display:column titleKey="activity.summary" sortable="true">
		<jstl:out value="${activity.summary}" />
	</display:column>

	<display:column titleKey="activity.usedRoom" sortable="true">
		<jstl:out value="${activity.usedRoom}" />
	</display:column>

	<display:column titleKey="activity.startMoment" sortable="true">
		<jstl:out value="${activity.startMoment}" />
	</display:column>

	<display:column titleKey="activity.duration" sortable="true">
		<fmt:formatNumber type="number" maxFractionDigits="0"
			value="${activity.duration}" />
		<spring:message code="minutes" />
	</display:column>

	<display:column titleKey="activity.conference">
		<input type="button"
			onclick="redirect: location.href = 'conference/display.do?conferenceId=${activity.conference.id}';"
			value="<spring:message code='activity.conference.display' />" />
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column titleKey="activity.submission">

			<jstl:choose>
				<jstl:when test="${not empty activity.submission}">
					<input type="button"
						onclick="redirect: location.href = 'submission/display.do?submissionId=${activity.submission.id}';"
						value="<spring:message code='activity.submission.display' />" />
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="no.submission" />
				</jstl:otherwise>
			</jstl:choose>

		</display:column>
	</security:authorize>
</display:table>
