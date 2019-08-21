<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<SCRIPT>
	function add() {

		var element = document.createElement("input");

		element.setAttribute("type", "text");
		element.setAttribute("name", "newSpeakerText");

		var foo = document.getElementById("newSpeaker");

		foo.appendChild(element);

	}
</SCRIPT>

<security:authorize access="hasRole('ADMIN')">

	<fieldset>
		<legend>
			<spring:message code="activity.legend" />
		</legend>
		<form:form action="activity/edit.do" modelAttribute="activity"
			id="form">

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="conference" />
			<form:hidden path="submission" />

			<acme:textbox code="activity.title" path="title" required="true"
				size="60" />

			<br>

			<acme:textbox code="activity.speakersInvolved"
				path="speakersInvolved" required="true" size="60" />

			<br>

			<acme:textbox code="activity.summary" path="summary" required="true"
				size="60" />

			<br>

			<acme:textbox code="activity.usedRoom" path="usedRoom"
				required="true" size="60" />

			<br>

			<acme:textbox code="activity.attachement" path="attachement"
				size="60" />

			<br>

			<form:label path="type">
				<spring:message code="activity.type" />
			</form:label>
			<br />
			<form:select path="type" required="true" size="4">
				<spring:message code='activity.selected' var="selected" />
				<jstl:choose>
					<jstl:when test="${activity.id == 0}">
						<form:option value="0" label="----" />
					</jstl:when>
					<jstl:otherwise>
						<form:option value="type" label="${selected} ${activity.type}" />
					</jstl:otherwise>
				</jstl:choose>

				<form:option value="PANEL" label="PANEL" />
				<form:option value="TUTORIAL" label="TUTORIAL" />
				<form:option value="PRESENTATION" label="PRESENTATION" />
			</form:select>
			<form:errors path="type" cssClass="error" />

			<br>
			<br>

			<acme:textbox code="activity.startMoment" path="startMoment"
				placeholder="moment.placeholder" required="true" size="60" />

			<br>

			<acme:textbox code="activity.duration" path="duration"
				required="true" size="60" />

			<br>

			<acme:submit code="save" name="save" />&nbsp;

			<jstl:choose>
				<jstl:when test="${activity.id == 0}">
					<acme:cancel code="cancel"
						url="conference/display.do?conferenceId=${activity.conference.id}" />
				</jstl:when>
				<jstl:otherwise>

					<acme:submit code="delete" name="delete" />&nbsp;
				
					<acme:cancel code="cancel"
						url="activity/display.do?activityid=${activity.id}" />

				</jstl:otherwise>
			</jstl:choose>

			<br />
			<form:errors cssClass="error" code="${message}" />
		</form:form>
	</fieldset>

</security:authorize>

<security:authorize access="!hasRole('ADMIN')">
	<p>
		<spring:message code="category.not.allowed" />
		<br>
	</p>
</security:authorize>