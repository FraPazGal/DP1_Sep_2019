<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>


<jstl:choose>
	<jstl:when test="${catalog eq null and category eq null}">
		<h1><spring:message code="conference.title.list" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == 'future' }">
		<h1><spring:message	code="conference.conference.list.future" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == 'past' }">
		<h1><spring:message	code="conference.conference.list.past" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == 'running' }">
		<h1><spring:message	code="conference.conference.list.running" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == 'unpublished' }">
		<h1><spring:message	code="conference.conference.list.unpublished" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == '5sub' }">
		<h1><spring:message	code="conference.conference.list.5submission" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == '5noti' }">
		<h1><spring:message	code="conference.conference.list.5notification" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == '5cam' }">
		<h1><spring:message	code="conference.conference.list.5camera" /></h1>
	</jstl:when>
	<jstl:when test="${catalog == '5org' }">
		<h1><spring:message	code="conference.conference.list.5organised" /></h1>
	</jstl:when>
	<jstl:when test="${category ne null}">
		<h1>
			<spring:message	code="conference.conference.list.category" />
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<jstl:out value="${category.title.get('Español')}" />
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${category.title.get('English')}" />
				</jstl:otherwise>
			</jstl:choose>
		</h1>
	</jstl:when>
</jstl:choose>

<jsp:useBean id="now" class="java.util.Date" />

<spring:message code="date.dateFormat" var="format" /> 

<jstl:choose>
	<jstl:when test="${isPrincipal eq 'ADMIN'}">
	
		<display:table class="displaytag" name="conferences" pagesize="5" 
			requestURI="conference/list.do" id="conference" style="width:90%">

			<display:column titleKey="conference.title" sortable="true">
				<jstl:out value="${conference.title}" />
			</display:column>
			
			<display:column titleKey="conference.venue" sortable="true">
				<jstl:out value="${conference.venue}" />
			</display:column>
			
			<display:column titleKey="conference.submissionDeadline" sortable="true">
				<span><fmt:formatDate pattern="${format}" value="${conference.submissionDeadline}" /></span>
			</display:column>
			
			<display:column titleKey="conference.notificationDeadline" sortable="true">
				<span><fmt:formatDate pattern="${format}" value="${conference.notificationDeadline}" /></span>
			</display:column>
			
			<display:column titleKey="conference.cameraReadyDeadline" sortable="true">
				<span><fmt:formatDate pattern="${format}" value="${conference.cameraReadyDeadline}" /></span>
			</display:column>
			
			<display:column titleKey="conference.startDate" sortable="true">
				<span><fmt:formatDate pattern="${format}" value="${conference.startDate}" /></span>
			</display:column>
			
			<display:column titleKey="conference.endDate" sortable="true">
				<span><fmt:formatDate pattern="${format}" value="${conference.endDate}" /></span>
			</display:column>
			
			<display:column titleKey="conference.entryFee" sortable="true">
				<jstl:out value="${conference.entryFee}" />
			</display:column>
			
			<jstl:if test="${conference.status eq 'FINAL'}">
				<spring:message var="status" code='conference.status.final' />
			</jstl:if>
			<jstl:if test="${conference.status eq 'DRAFT'}">
				<spring:message var="status" code='conference.status.draft' />
			</jstl:if>
			<jstl:if test="${conference.status eq 'DECISION-MADE'}">
				<spring:message var="status" code='conference.status.decisionmade' />
			</jstl:if>
			<jstl:if test="${conference.status eq 'NOTIFIED'}">
				<spring:message var="status" code='conference.status.notified' />
			</jstl:if>
			
			<display:column titleKey="conference.status" sortable="true">
				<jstl:out value="${status}" />
			</display:column>
			
			<display:column>
				<a href="conference/display.do?conferenceId=${conference.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<jstl:if test="${catalog eq 'unpublished'}">
				<display:column>
					<a href="conference/edit.do?conferenceId=${conference.id}"> <spring:message
							code="mp.edit" />
					</a>
				</display:column>
					
				<display:column>
					<a href="conference/delete.do?conferenceId=${conference.id}"> <spring:message
							code="mp.delete" />
					</a>
				</display:column>
			</jstl:if>
			
			<jstl:if test="${catalog eq '5noti'}">
				<display:column>
					<jstl:if test="${conference.status eq 'FINAL' and now gt conference.submissionDeadline}">
						<a href="conference/review.do?conferenceId=${conference.id}" 
							onclick="return confirm('<spring:message code="conference.confirm.review"/>')" > <spring:message
								code="conference.review" />
						</a>
					</jstl:if>
				</display:column>
			
				<display:column>
					<jstl:if test="${conference.status eq 'DECISION-MADE' and catalog eq '5noti'}">
						<a href="conference/notify.do?conferenceId=${conference.id}" 
							onclick="return confirm('<spring:message code="conference.confirm.notification"/>')" > <spring:message
								code="conference.notify" />
						</a>
					</jstl:if>
				</display:column>
			</jstl:if>
			
		</display:table>
		<jstl:if test="${ catalog eq 'unpublished'}">
		<input type="button"
			onclick="redirect: location.href = 'conference/create.do';"
			value="<spring:message code='conference.create' />" />
		</jstl:if>
			
	</jstl:when>
	<jstl:when test="${isPrincipal eq 'AUTHOR'}">
		<display:table class="displaytag" name="conferences" pagesize="5" 
			requestURI="conference/list.do" id="conference">

			<display:column titleKey="conference.title" sortable="true">
				<jstl:out value="${conference.title}" />
			</display:column>

			<display:column titleKey="conference.submissionDeadline" sortable="true">
				<span><fmt:formatDate pattern="${format }" value="${conference.submissionDeadline}" /></span>
			</display:column>
							
			<display:column titleKey="conference.startDate" sortable="true">
				<span><fmt:formatDate pattern="${format }" value="${conference.startDate}" /></span>
			</display:column>
			
			<display:column titleKey="conference.endDate" sortable="true">
				<span><fmt:formatDate pattern="${format }" value="${conference.endDate}" /></span>
			</display:column>
			
			<display:column>
				<a href="conference/display.do?conferenceId=${conference.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
			
			<jstl:if test="${catalog eq 'future'}">
				<display:column>
					<jstl:set var="contains" value="false" />
					<jstl:forEach var="conf" items="${conferencesRegisteredTo}">
					  <jstl:if test="${conf eq conference}">
					    <jstl:set var="contains" value="true" />
					  </jstl:if>
					</jstl:forEach>
					<jstl:if test="${!contains}">
						<a href="registration/create.do?conferenceId=${conference.id}"> <spring:message
								code="conference.registration" />
						</a>
					</jstl:if>
				</display:column>
				<display:column>
					<jstl:set var="contains" value="false" />
					<jstl:forEach var="conf" items="${conferencesSubmittedTo}">
					  <jstl:if test="${conf eq conference}">
					    <jstl:set var="contains" value="true" />
					  </jstl:if>
					</jstl:forEach>
					<jstl:if test="${!contains && now lt conference.submissionDeadline}">
						<a href="submission/create.do?conferenceId=${conference.id}"> <spring:message
								code="conference.submission" />
						</a>
					</jstl:if>
				</display:column>
			</jstl:if>
		</display:table>
	</jstl:when>
	<jstl:otherwise>
		<display:table class="displaytag" name="conferences" pagesize="5" 
			requestURI="conference/list.do" id="conference">

			<display:column titleKey="conference.title" sortable="true">
				<jstl:out value="${conference.title}" />
			</display:column>
			
			<jstl:if test="${catalog ne 'past' and catalog ne 'running'}">
				<display:column titleKey="conference.submissionDeadline" sortable="true">
					<span><fmt:formatDate pattern="${format }" value="${conference.submissionDeadline}" /></span>
				</display:column>
			</jstl:if>
							
			<display:column titleKey="conference.startDate" sortable="true">
				<span><fmt:formatDate pattern="${format }" value="${conference.startDate}" /></span>
			</display:column>
			
			<display:column titleKey="conference.endDate" sortable="true">
				<span><fmt:formatDate pattern="${format }" value="${conference.endDate}" /></span>
			</display:column>
			
			<display:column>
				<a href="conference/display.do?conferenceId=${conference.id}"> <spring:message
						code="mp.display" />
				</a>
			</display:column>
		</display:table>
	</jstl:otherwise>
</jstl:choose>
