<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jstl:choose>
	<jstl:when test="${not empty activity}">
		<table class="displayStyle">

			<tr>
				<td><strong> <spring:message code="activity.title" />
						:
				</strong></td>
				<td><jstl:out value="${activity.title}" /></td>
			</tr>

			<tr>
				<td><strong> <spring:message
							code="activity.speakersInvolved" /> :
				</strong></td>
				<td><jstl:out
						value="${fn:replace(activity.speakersInvolved, ',', ', ')}"></jstl:out></td>
			</tr>

			<tr>
				<td><strong> <spring:message code="activity.summary" />
						:
				</strong></td>
				<td><jstl:out value="${activity.summary}"></jstl:out></td>
			</tr>

			<tr>
				<td><strong> <spring:message code="activity.usedRoom" />
						:
				</strong></td>
				<td><jstl:out value="${activity.usedRoom}"></jstl:out></td>
			</tr>

			<tr>
				<td><strong> <spring:message
							code="activity.attachement" /> :
				</strong></td>
				<td><a href="${activity.attachement}"><jstl:out
							value="${activity.attachement}" /></a></td>
			</tr>

			<tr>
				<td><strong> <spring:message
							code="activity.startMoment" /> :
				</strong></td>
				<td><fmt:formatDate type="both" dateStyle="short"
						timeStyle="short" value="${activity.startMoment}" /></td>
			</tr>

			<tr>
				<td><strong> <spring:message code="activity.duration" />
						:
				</strong></td>
				<td><jstl:out value="${activity.duration}"></jstl:out></td>
			</tr>

			<tr>
				<td><strong> <spring:message code="activity.type" />
						:
				</strong></td>
				<td><jstl:out value="${activity.type}"></jstl:out></td>
			</tr>

			<tr>
				<td><strong> <spring:message
							code="activity.conference" /> :
				</strong></td>
				<td><a
					href="conference/display.do?conferenceId=${activity.conference.id}">
						<jstl:out value="${activity.conference.title}" />
				</a></td>
			</tr>

			<jstl:if test="${permission}">
				<tr>
					<td><strong> <spring:message
								code="activity.submission" /> :
					</strong></td>
					<td><jstl:choose>
							<jstl:when test="${not empty activity.submission}">
								<a
									href="submission/display.do?submissionid=${activity.submission.id}"><jstl:out
										value="${activity.submission.ticker}" /></a>
							</jstl:when>
							<jstl:otherwise>
								<spring:message code="no.submission" />
							</jstl:otherwise>
						</jstl:choose></td>
				</tr>
			</jstl:if>

		</table>

		<jstl:if test="${permission and conferenceStarted}">
			<input type="button"
				onclick="redirect: location.href = 'activity/edit.do?activityid=${activity.id}';"
				value="<spring:message code='activity.edit' />" />
		</jstl:if>

		<hr>

		<jstl:if test="${not empty sections}">

			<h2>
				<strong><spring:message code="sections" /></strong>
			</h2>
			<display:table style="width: 80%" class="displaytag" name="sections"
				pagesize="5"
				requestURI="activity/display.do?activityid=${activity.id}"
				id="section">

				<display:column titleKey="section.title">
					<jstl:out value="${section.title}" />
				</display:column>

				<display:column titleKey="section.summary">
					<jstl:out value="${section.summary}" />
				</display:column>

				<display:column titleKey="section.pictures">
						${fn:replace(section.pictures, ',', '<br>')}
					</display:column>

				<jstl:if test="${permission}">

					<display:column>
						<input type="button"
							onclick="redirect: location.href = 'section/edit.do?sectionid=${section.id}';"
							value="<spring:message code='section.edit' />" />
					</display:column>

				</jstl:if>
			</display:table>

		</jstl:if>

		<jstl:if test="${permission and activity.type == 'TUTORIAL'}">
			<input type="button"
				onclick="redirect: location.href = 'section/create.do?activityid=${activity.id}';"
				value="<spring:message code='section.create' />" />
		</jstl:if>


		<jstl:if test="${not empty comments}">
			<h2>
				<strong><spring:message code="comments" /></strong>
			</h2>

			<display:table style="width: 80%" class="displaytag" name="comments"
				pagesize="5"
				requestURI="activity/display.do?activityid=${activity.id}"
				id="comment">

				<display:column titleKey="comment.title">
					<jstl:out value="${comment.title}" />
				</display:column>

				<display:column titleKey="comment.body">
					<jstl:out value="${comment.body}" />
				</display:column>

				<display:column titleKey="comment.author">
					<jstl:out value="${comment.author}" />
				</display:column>

				<display:column titleKey="comment.publishedDate">
					<jstl:out value="${comment.publishedDate}" />
				</display:column>

			</display:table>
		</jstl:if>

		<input type="button"
			onclick="redirect: location.href = 'comment/create.do?activityid=${activity.id}';"
			value="<spring:message code='comment.create' />" />

	</jstl:when>
	<jstl:otherwise>
		<p class="error">
			<spring:message code="not.found" />
		</p>
	</jstl:otherwise>
</jstl:choose>

