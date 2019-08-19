<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<security:authorize access="hasAnyRole('REVIEWER')">

	<h1>
		<spring:message code="review.list" />
	</h1>

	<display:table class="displaytag" name="reports" pagesize="5"
		requestURI="report/reviewer/myreports.do" id="report">

		<display:column titleKey="review.submission" sortable="true">
			<a href="submission/display.do?submissionId=${report.submission.id}"><jstl:out
					value="${report.submission.ticker}" /></a>
		</display:column>

		<display:column titleKey="review.originality" sortable="true">
			<jstl:choose>
				<jstl:when test="${not empty report.originalityScore}">
					<jstl:out value="${report.originalityScore}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="N/A" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column titleKey="review.quality" sortable="true">
			<jstl:choose>
				<jstl:when test="${not empty report.qualityScore}">
					<jstl:out value="${report.qualityScore}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="N/A" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column titleKey="review.readability" sortable="true">
			<jstl:choose>
				<jstl:when test="${not empty report.readabilityScore}">
					<jstl:out value="${report.readabilityScore}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="N/A" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column titleKey="review.status" sortable="true">
			<jstl:choose>
				<jstl:when test="${not empty report.status}">
					<jstl:out value="${report.status}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="N/A" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column titleKey="review.comments" sortable="true">
			<jstl:choose>
				<jstl:when test="${not empty report.comments}">
					<jstl:out value="${report.comments}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="N/A" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

		<display:column>
			<jstl:choose>
				<jstl:when test="${!report.isWritten}">
					<a href="review/reviewer/edit.do?reviewid=${report.id}"><spring:message
							code="review.write" /></a>
				</jstl:when>
				<jstl:otherwise>
					<spring:message code="review.written" />
				</jstl:otherwise>
			</jstl:choose>
		</display:column>

	</display:table>

</security:authorize>