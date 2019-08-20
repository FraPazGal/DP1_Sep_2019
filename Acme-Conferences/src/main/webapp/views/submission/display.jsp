<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h1><spring:message code="submission.title.display" /></h1>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message code="submission.ticker" />
				:
		</strong></td>
		<td><jstl:out value="${submission.ticker}" /></td>
	</tr>

	<spring:message code="submission.under.review" var="message1" />
	<spring:message code="submission.rejected" var="message2" />
	<spring:message code="submission.accepted" var="message3" />

	<tr>
		<td><strong> <spring:message code="submission.status" />
				:
		</strong></td>
		<td><jstl:choose>
				<jstl:when test="${submission.status == 'UNDER-REVIEW'}">
					<jstl:out value='${message1}' />
				</jstl:when>
				<jstl:when test="${submission.status == 'REJECTED'}">
					<jstl:out value="${message2}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${message3}" />
				</jstl:otherwise>
			</jstl:choose></td>
	</tr>

	<tr>
		<spring:message code="date.dateFormat" var="format" />
		<td><strong> <spring:message
					code="submission.submissionMoment" /> :
		</strong></td>
		<td><fmt:formatDate pattern="${format }"
				value="${submission.submissionMoment}" /></td>
	</tr>

	<tr>
		<td><strong> <spring:message
					code="submission.conference" /> :
		</strong></td>
		<td><a
			href="conference/display.do?conferenceId=${submission.conference.id}">
				<jstl:out value="${submission.conference.acronym}" />
		</a></td>
	</tr>

	<tr>
		<td><strong> <spring:message code="submission.author" />
				:
		</strong></td>
		<td><a href="actor/display.do?actorId=${submission.author.id}">
				<jstl:out
					value="${submission.author.name} ${submission.author.surname}" />
		</a></td>
	</tr>

</table>

<h2><spring:message code="submission.title.paper.display" /></h2>
<table class="displayStyle">
	<tr>
		<td><strong> <spring:message
					code="submission.paper.title" /> :
		</strong></td>
		<td><jstl:out value="${submission.paper.title}" /></td>
	</tr>
	<tr>
		<td><strong> <spring:message
					code="submission.paper.authors" /> :
		</strong></td>
		<td><jstl:out value="${submission.paper.authors}" /></td>
	</tr>
	<tr>
		<td><strong> <spring:message
					code="submission.paper.summary" /> :
		</strong></td>
		<td><jstl:out value="${submission.paper.summary}" /></td>
	</tr>
	<tr>
		<td><strong> <spring:message
					code="submission.paper.paperDocument" /> :
		</strong></td>
		<td><jstl:out value="${submission.paper.paperDocument}" /></td>
	</tr>
</table>

<jstl:if test="${submission.cameraReadyPaper != null }">
	<h2><spring:message code="submission.title.paper.cameraReady.display" /></h2>
	<table class="displayStyle">
		<tr>
			<td><strong> <spring:message
						code="submission.paper.title" /> :
			</strong></td>
			<td><jstl:out value="${submission.cameraReadyPaper.title}" /></td>
		</tr>
		<tr>
			<td><strong> <spring:message
						code="submission.paper.authors" /> :
			</strong></td>
			<td><jstl:out value="${submission.cameraReadyPaper.authors}" /></td>
		</tr>
		<tr>
			<td><strong> <spring:message
						code="submission.paper.summary" /> :
			</strong></td>
			<td><jstl:out value="${submission.cameraReadyPaper.summary}" /></td>
		</tr>
		<tr>
			<td><strong> <spring:message
						code="submission.paper.paperDocument" /> :
			</strong></td>
			<td><jstl:out
					value="${submission.cameraReadyPaper.paperDocument}" /></td>
		</tr>
	</table>
</jstl:if>

<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${!isAssigned}">
		<input type="button"
			value="<spring:message code="submission.assign" />"
			onclick="location.href='review/admin/assign.do?submissionid=${submission.id}';" />
	</jstl:if>
</security:authorize>

<input type="button" name="back"
	value="<spring:message code="mp.back" />"
	onclick="window.history.back()"/>