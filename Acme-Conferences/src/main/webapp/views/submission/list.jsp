<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	function submissionAlert(men) {
		console.log(men);
		window.alert(men);
	}
</script>

<jstl:choose>
	<jstl:when test="${catalog eq null }">
		<h1>
			<spring:message code="submission.title.list" />
		</h1>
	</jstl:when>
	<jstl:when test="${catalog == 'underR' }">
		<h1>
			<spring:message code="submission.submission.list.underreview" />
		</h1>
	</jstl:when>
	<jstl:when test="${catalog == 'accepted' }">
		<h1>
			<spring:message code="submission.submission.list.accepted" />
		</h1>
	</jstl:when>
	<jstl:when test="${catalog == 'rejected' }">
		<h1>
			<spring:message code="submission.submission.list.rejected" />
		</h1>
	</jstl:when>
</jstl:choose>

<jsp:useBean id="now" class="java.util.Date" />
<spring:message code="date.dateFormat" var="format" />

<jstl:choose>
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

			<display:column titleKey="submission.paper" sortable="true">
				<jstl:out value="${submission.paper.title}" />
			</display:column>

			<display:column titleKey="submission.conference.submission.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.submissionDeadline}" />
			</display:column>

			<display:column
				titleKey="submission.conference.notification.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.notificationDeadline}" />
			</display:column>

			<display:column titleKey="submission.conference.cameraReady.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.cameraReadyDeadline}" />
			</display:column>

			<display:column>
				<jstl:if
					test="${submission.status == 'ACCEPTED' or submission.status == 'REJECTED'}">
					<a href="review/list.do?submissionId=${submission.id}"> <spring:message
							code="submission.display.reports" />
					</a>
				</jstl:if>
			</display:column>

			<display:column>
				<a href="submission/display.do?submissionId=${submission.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>

			<display:column>
				<jstl:if
					test="${submission.cameraReadyPaper == null and submission.status == 'ACCEPTED' and now lt submission.conference.cameraReadyDeadline}">
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

			<display:column titleKey="submission.conference.submission.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.submissionDeadline}" />
			</display:column>

			<display:column
				titleKey="submission.conference.notification.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.notificationDeadline}" />
			</display:column>

			<display:column titleKey="submission.conference.cameraReady.deadline"
				sortable="true">
				<fmt:formatDate pattern="${format }"
					value="${submission.conference.cameraReadyDeadline}" />
			</display:column>

			<display:column>
				<a href="submission/display.do?submissionId=${submission.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>

		</display:table>
		<jstl:if test="${catalog == 'underR' and not empty submissions}">
			<spring:message var="mensaje"
				code='<%=request.getParameter("mensaje")%>' />
			<jstl:if test="${not empty mensaje}">
				<p class="error">
					<jstl:out value="${mensaje}" />
			</jstl:if>
			<input type="button" value="<spring:message code="assign.all"/>"
				onclick="location.href = 'review/admin/automaticassign.do';"
				formmethod="post">
		</jstl:if>


	</jstl:when>
</jstl:choose>