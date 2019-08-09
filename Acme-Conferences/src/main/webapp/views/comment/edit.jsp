<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<fieldset>
	<legend>
		<spring:message code="comment.legend" />
	</legend>
	<form:form action="comment/edit.do" modelAttribute="comment" id="form">

		<form:hidden path="conference" />
		<form:hidden path="activity" />
		<form:hidden path="publishedDate" />
		<form:hidden path="writer" />
		<form:hidden path="author" />

		<acme:textbox code="comment.title" path="title" />
		<jstl:if test="${not empty binding.getFieldError('title')}">
			<p class="error">
				<spring:message code="title.error" />
			</p>
		</jstl:if>

		<br>

		<acme:textbox code="comment.body" path="body" />
		<jstl:if test="${not empty binding.getFieldError('body')}">
			<p class="error">
				<spring:message code="body.error" />
			</p>
		</jstl:if>

		<br>

		<acme:submit code="save" name="save" />&nbsp;

			<jstl:choose>
			<jstl:when test="${not empty comment.conference}">
				<acme:cancel code="cancel"
					url="conference/display.do?conferenceId=${comment.conference.id}" />
			</jstl:when>
			<jstl:otherwise>
				<acme:cancel code="cancel"
					url="activity/display.do?activityid=${comment.activity.id}" />
			</jstl:otherwise>
		</jstl:choose>

		<br />
		<form:errors cssClass="error" code="${message}" />
	</form:form>
</fieldset>
