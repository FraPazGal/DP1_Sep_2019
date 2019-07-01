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

	function unify() {
		var string = document.getElementById('newSpeakerText').value;

		console.log(string);
	}
</SCRIPT>

<security:authorize access="hasRole('ADMIN')">

	<fieldset>
		<legend>
			<spring:message code="activity.legend" />
		</legend>
		<form:form action="activity/edit.do" modelAttribute="activity"
			id="form" onsubmit="unify()">

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="conference" />
			<form:hidden path="submission" />

			<acme:textbox code="activity.title" path="title" />

			<br>

			<acme:textbox code="activity.speakersInvolved"
				path="speakersInvolved" />

			<br>

			<span id="newSpeaker"></span>

			<input type="button" value="<spring:message code='textarea.add'/>"
				onclick="add()" />

			<br>
			<br>

			<acme:textbox code="activity.summary" path="summary" />

			<br>

			<acme:textbox code="activity.usedRoom" path="usedRoom" />

			<br>

			<acme:textbox code="activity.attachement" path="attachement" />

			<br>

			<form:label path="type">
				<spring:message code="activity.type" />
			</form:label>
			<form:select path="type">
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
			<form:errors path="${type}" cssClass="error" />

			<br>
			<br>

			<acme:textbox code="activity.startMoment" path="startMoment" />

			<br>

			<acme:textbox code="activity.duration" path="duration" />

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