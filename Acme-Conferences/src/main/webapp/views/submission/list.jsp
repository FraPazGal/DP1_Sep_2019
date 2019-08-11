<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<security:authorize access="hasAnyRole('AUTHOR','REVIEWER','ADMIN')">
	<h1>
		<spring:message code="submission.title.list" />
	</h1>
	<spring:message code="date.dateFormat" var="format" />

	<jstl:choose>
		<jstl:when test="${errMsg ne null}">
			<p>
				<jstl:out value="${errMsg}" />
			</p>
		</jstl:when>
		<jstl:when test="${isPrincipal == 'AUTHOR'}">
			<display:table class="displaytag" name="submissions" pagesize="5"
				requestURI="submission/list.do" id="submission" style="width: 90%;">

				<display:column titleKey="submission.ticker" sortable="true">
					<jstl:out value="${submission.ticker}" />
				</display:column>

				<spring:message code="submission.under.review" var="message1" />
				<spring:message code="submission.rejected" var="message2" />
				<spring:message code="submission.accepted" var="message3" />

				<display:column titleKey="submission.status" sortable="true">
					<jstl:choose>
						<jstl:when test="${submission.status == 'UNDER-REVIEW'}">
							<jstl:out value='${message1}' />
						</jstl:when>
						<jstl:when test="${submission.status == 'REJECTED'}">
							<jstl:out value="${message2}" />
						</jstl:when>
						<jstl:otherwise>
							<jstl:out value="${message3}" />
						</jstl:otherwise>
					</jstl:choose>
				</display:column>

				<display:column titleKey="submission.submissionMoment"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.submissionMoment}" />
				</display:column>

				<display:column titleKey="submission.conference" sortable="true">
					<a
						href="conference/display.do?conferenceId=${submission.conference.id}">
						<jstl:out value="${submission.conference.acronym}" />
					</a>
				</display:column>

				<display:column titleKey="submission.author" sortable="true">
					<jstl:out
						value="${submission.author.name} ${submission.author.surname}" />
				</display:column>

				<display:column titleKey="submission.paper" sortable="true">
					<jstl:out value="${submission.paper.title}" />
				</display:column>

				<display:column
					titleKey="submission.conference.notification.deadline"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.conference.notificationDeadline}" />
				</display:column>

				<display:column
					titleKey="submission.conference.cameraReady.deadline"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.conference.cameraReadyDeadline}" />
				</display:column>

				<display:column>
					<a href="submission/display.do?submissionId=${submission.id}">
						<spring:message code="mp.display" />
					</a>
				</display:column>

				<display:column>
					<jstl:if
						test="${submission.cameraReadyPaper == null and submission.status == 'ACCEPTED'}">
						<a href="submission/edit.do?submissionId=${submission.id}"> <spring:message
								code="submission.submit" />
						</a>
					</jstl:if>
				</display:column>

			</display:table>
		</jstl:when>
		<jstl:when test="${isPrincipal == 'ADMIN'}">
			<display:table class="displaytag" name="submissions" pagesize="5"
				requestURI="submission/list.do" id="submission" style="width: 90%;">

				<display:column titleKey="submission.ticker" sortable="true">
					<jstl:out value="${submission.ticker}" />
				</display:column>

				<spring:message code="submission.under.review" var="message1" />
				<spring:message code="submission.rejected" var="message2" />
				<spring:message code="submission.accepted" var="message3" />

				<display:column titleKey="submission.status" sortable="true">
					<jstl:choose>
						<jstl:when test="${submission.status == 'UNDER-REVIEW'}">
							<jstl:out value='${message1}' />
						</jstl:when>
						<jstl:when test="${submission.status == 'REJECTED'}">
							<jstl:out value="${message2}" />
						</jstl:when>
						<jstl:otherwise>
							<jstl:out value="${message3}" />
						</jstl:otherwise>
					</jstl:choose>
				</display:column>

				<display:column titleKey="submission.submissionMoment"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.submissionMoment}" />
				</display:column>

				<display:column titleKey="submission.conference" sortable="true">
					<a
						href="conference/display.do?conferenceId=${submission.conference.id}">
						<jstl:out value="${submission.conference.acronym}" />
					</a>
				</display:column>

				<display:column titleKey="submission.author" sortable="true">
					<jstl:out
						value="${submission.author.name} ${submission.author.surname}" />
				</display:column>

				<display:column titleKey="submission.paper" sortable="true">
					<jstl:out value="${submission.paper.title}" />
				</display:column>

				<display:column
					titleKey="submission.conference.notification.deadline"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.conference.notificationDeadline}" />
				</display:column>

				<display:column
					titleKey="submission.conference.cameraReady.deadline"
					sortable="true">
					<fmt:formatDate pattern="${format }"
						value="${submission.conference.cameraReadyDeadline}" />
				</display:column>

				<display:column>
					<a href="submission/display.do?submissionId=${submission.id}">
						<spring:message code="mp.display" />
					</a>
				</display:column>

			</display:table>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<spring:message code="submission.not.allowed" />
				<br>
			</p>
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>