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

<security:authorize access="permitAll">

<h1><spring:message	code="conference.list" />
<jstl:out value="${conference.conference.title}" /></h1>
	<jstl:choose>
		<jstl:when test="${errMsg ne null}">
			<p>
				<jstl:out value="${errMsg}"/>
			</p>
		</jstl:when>
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
				<spring:message code="date.dateFormat" var="format_submissionDeadline" /> 
				<span><fmt:formatDate pattern="${format_submissionDeadline }" value="${conference.submissionDeadline}" /></span>
				</display:column>
				
				<display:column titleKey="conference.notificationDeadline" sortable="true">
				<spring:message code="date.dateFormat" var="format_notificationDeadline" /> 
				<span><fmt:formatDate pattern="${format_notificationDeadline }" value="${conference.notificationDeadline}" /></span>
				</display:column>
				
				<display:column titleKey="conference.cameraReadyDeadline" sortable="true">
				<spring:message code="date.dateFormat" var="format_cameraReadyDeadline" /> 
				<span><fmt:formatDate pattern="${format_cameraReadyDeadline }" value="${conference.cameraReadyDeadline}" /></span>
				</display:column>
				
				<display:column titleKey="conference.startDate" sortable="true">
				<spring:message code="date.dateFormat" var="format_startDate" /> 
				<span><fmt:formatDate pattern="${format_startDate }" value="${conference.startDate}" /></span>
				</display:column>
				
				<display:column titleKey="conference.endDate" sortable="true">
				<spring:message code="date.dateFormat" var="format_endDate" /> 
				<span><fmt:formatDate pattern="${format_endDate }" value="${conference.endDate}" /></span>
				</display:column>
				
				<display:column titleKey="conference.entryFee" sortable="true">
					<jstl:out value="${conference.entryFee}" />
				</display:column>
				
				<jstl:if test="${conference.isFinal eq true}">
					<spring:message var="status" code='conference.isFinal.true' />
				</jstl:if>
				<jstl:if test="${conference.isFinal eq false}">
					<spring:message var="status" code='conference.isFinal.false' />
				</jstl:if>
				
				<display:column titleKey="conference.isFinal" sortable="true">
					<jstl:out value="${status}" />
				</display:column>
				
				<display:column>
					<a href="conference/display.do?conferenceId=${conference.id}"> <spring:message
							code="conference.display" />
					</a>
				</display:column>
				
				<jstl:if test="${conference.isFinal eq false}">
					<display:column>
						<a href="conference/edit.do?conferenceId=${conference.id}"> <spring:message
								code="conference.edit" />
						</a>
					</display:column>
						
					<display:column>
						<a href="conference/delete.do?conferenceId=${conference.id}"> <spring:message
								code="conference.delete" />
						</a>
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

				<spring:message code="date.dateFormat" var="format" /> 

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
							code="conference.display" />
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
						<jstl:if test="${!contains}">
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

				<spring:message code="date.dateFormat" var="format" /> 

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
							code="conference.display" />
					</a>
				</display:column>
			</display:table>
		</jstl:otherwise>
	</jstl:choose>
	
</security:authorize>