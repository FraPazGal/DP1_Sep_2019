<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<h1>
	<jstl:out value="${conference.title}" />
</h1>

<div>
	<table class="displayStyle">

		<tr>
			<td><strong> <spring:message code="conference.title" />
					:
			</strong></td>
			<td><jstl:out value="${conference.title}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="conference.acronym" />
					:
			</strong></td>
			<td><jstl:out value="${conference.acronym}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="conference.summary" />
					:
			</strong></td>
			<td><jstl:out value="${conference.summary}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="conference.venue" />
					:
			</strong></td>
			<td><jstl:out value="${conference.venue}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="conference.entryFee" />
					:
			</strong></td>
			<td><jstl:out value="${conference.entryFee}"></jstl:out> &#8364;
			</td>
		</tr>

		<jstl:choose>
			<jstl:when test="${pageContext.response.locale.language == 'es'}">
				<tr>
					<td><strong> <spring:message
								code="conference.category" /> :
					</strong></td>
					<td><jstl:out value="${titleCat.get('Español')}" /></td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr>
					<td><strong> <spring:message
								code="conference.category" /> :
					</strong></td>
					<td><jstl:out value="${titleCat.get('English')}" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>

		<tr>
			<td><strong> <spring:message
						code="conference.submissionDeadline" /> :
			</strong></td>
			<td><spring:message code="date.dateFormat" var="format" /> <span><fmt:formatDate
						pattern="${format }" value="${conference.submissionDeadline}" /></span></td>
		</tr>

		<tr>
			<td><strong> <spring:message
						code="conference.notificationDeadline" /> :
			</strong></td>
			<td><span><fmt:formatDate pattern="${format }"
						value="${conference.notificationDeadline}" /></span></td>
		</tr>

		<tr>
			<td><strong> <spring:message
						code="conference.cameraReadyDeadline" /> :
			</strong></td>
			<td><span><fmt:formatDate pattern="${format }"
						value="${conference.cameraReadyDeadline}" /></span></td>
		</tr>

		<tr>
			<td><strong> <spring:message
						code="conference.startDate" /> :
			</strong></td>
			<td><span><fmt:formatDate pattern="${format }"
						value="${conference.startDate}" /></span></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="conference.endDate" />
					:
			</strong></td>
			<td><span><fmt:formatDate pattern="${format }"
						value="${conference.endDate}" /></span></td>
		</tr>

		<jstl:if test="${conference.isFinal eq true}">
			<spring:message var="status" code='conference.isFinal.true' />
		</jstl:if>
		<jstl:if test="${conference.isFinal eq false}">
			<spring:message var="status" code='conference.isFinal.false' />
		</jstl:if>

		<jstl:if test="${isPrincipal }">
			<tr>
				<td><strong> <spring:message code="conference.isFinal" />
						:
				</strong></td>
				<td>${status}</td>
			</tr>

			<tr>
				<td><strong> <spring:message
							code="conference.administrator" /> :
				</strong></td>
				<td><a
					href="administrator/display.do?administratorId=${conference.administrator.id}">
						<jstl:out
							value="${conference.administrator.name} ${conference.administrator.surname}"></jstl:out>
				</a></td>
			</tr>
		</jstl:if>

	</table>

	<input type="button"
		onclick="redirect: location.href = 'activity/list.do?conferenceid=${conference.id}';"
		value="<spring:message code='activity.list' />" />

	<jstl:if test="${isPrincipal}">
		<input type="button"
			onclick="redirect: location.href = 'activity/create.do?conferenceid=${conference.id}';"
			value="<spring:message code='activity.create' />" />
	</jstl:if>

	<jstl:if test="${not empty comments}">
		<h2>
			<strong><spring:message code="comments" /></strong>
		</h2>

		<display:table style="width: 80%" class="displaytag" name="comments"
			pagesize="5"
			requestURI="conference/display.do?conferenceId=${conference.id}"
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
		onclick="redirect: location.href = 'comment/create.do?conferenceid=${conference.id}';"
		value="<spring:message code='comment.create' />" />

</div>

<%-- <h2><spring:message code="conference.saga.persons" /></h2>
<display:table class="displaytag" name="${conference.persons}" pagesize="5" 
		requestURI="conference/display.do" id="person">

	<display:column titleKey="conference.person.name" sortable="true">
		<jstl:out value="${person.name}" />
	</display:column>
	
	<display:column titleKey="conference.person.surname" sortable="true">
		<jstl:out value="${person.surname}" />
	</display:column>
	
	<display:column >
		<a href="person/display.do?personId=${person.id}"> <spring:message
				code="conference.person.display" />
		</a>
	</display:column>
</display:table>

<h2><spring:message code="conference.saga.sagas" /></h2>
<display:table class="displaytag" name="${conference.sagas}" pagesize="5" 
		requestURI="conference/display.do" id="saga">

	<display:column titleKey="conference.saga.title" sortable="true">
		<jstl:out value="${saga.title}" />
	</display:column>
	
	<display:column >
		<a href="saga/display.do?sagaId=${saga.id}"> <spring:message
				code="saga.display" />
		</a>
	</display:column>
</display:table>

 --%>


<jstl:if test="${spoBanner.targetPage ne null}">
	<div>
		<h3>
			<spring:message code="conference.sponsored" />
		</h3>
		<a href="${spoBanner.targetPage }" target="_blank"> <img
			style="height: 100px" src="${spoBanner.banner}" alt="Banner">
		</a><br> <br>
	</div>
</jstl:if>

<input type="button" name="back"
	value="<spring:message code="conference.back" />"
	onclick="window.history.back()" />


