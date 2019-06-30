<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('ADMIN')">

	<jstl:choose>
		<jstl:when test="${isPrincipal}">
		<h1><spring:message	code="conference.title.edit" /></h1>
		<form:form modelAttribute="conference" action="conference/edit.do"
			id="form">
	
			<form:hidden path="id" />
			
			<jstl:if test="${conference.id == 0 || !conference.isFinal}">
			
				<acme:textbox code="conference.title" path="title" size="100px" /><br/> <br/>
				<acme:textbox code="conference.acronym" path="acronym" size="100px" /><br/> <br/>
				<acme:textarea code="conference.summary" path="summary" cols="80px" rows="4"/><br/> <br/>
				<acme:textbox code="conference.venue" path="venue" size="100px" /><br/> <br/>
				<acme:textbox code="conference.entryFee" path="entryFee" size="100px" /><br/> <br/>
				
				<spring:message code="conference.category" />
				<jstl:if test="${!conference.isFinal || conference.id == 0}">
					<jstl:choose>
						<jstl:when test="${pageContext.response.locale.language == 'es'}">
							<form:select path="category" name="categoryArray" style="width:200px;">
								<jstl:forEach var="category" items="${categories}">
									<option value="${category.id}" >
										<jstl:out value="${category.title.get('Español')}" />
									</option>
								</jstl:forEach>
							</form:select><br><br>
						</jstl:when>
						<jstl:otherwise>
							<form:select path="category" name="categoryArray" style="width:200px;">
								<jstl:forEach var="category" items="${categories}">
									<option value="${category.id}" >
										<jstl:out value="${category.title.get('English')}" />
									</option>
								</jstl:forEach>
							</form:select><br><br>
						</jstl:otherwise>
					</jstl:choose>
				</jstl:if>
				<br>
				<acme:textbox code="conference.submissionDeadline" path="submissionDeadline" size="100px" /><br/> <br/>
				<acme:textbox code="conference.notificationDeadline" path="notificationDeadline" size="100px" /><br/> <br/>
				<acme:textbox code="conference.cameraReadyDeadline" path="cameraReadyDeadline" size="100px" /><br/> <br/>
				<acme:textbox code="conference.startDate" path="startDate" size="100px" /><br/> <br/>
				<acme:textbox code="conference.endDate" path="endDate" size="100px" /><br/> <br/>

			</jstl:if>
			
			<acme:submit code="conference.save.draft" name="save" />&nbsp;
			<jstl:if test="${!conference.isFinal}">
				<acme:submit code="conference.saveFinal" name="saveFinal" />&nbsp;
			</jstl:if>
			<acme:cancel url="conference/list.do?catalog=unpublished" code="conference.cancel" />
			<br />
	
		</form:form>
		</jstl:when>		
	<jstl:otherwise>
		<p>
			<spring:message	code="conference.not.allowed" /><br>
		</p>
	</jstl:otherwise>
	</jstl:choose>
</security:authorize>
