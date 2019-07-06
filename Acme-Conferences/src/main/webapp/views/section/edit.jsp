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
		element.setAttribute("name", "newPictureText");

		var foo = document.getElementById("newPicture");

		foo.appendChild(element);

	}

	function verifyurl(language) {
		var isValid = true;
		var pictures = document.getElementById('picturesInput').value;
		var splittedPictures = pictures.split(',');

		var expression = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-_]*)?\??(?:[\-\+=&;%@\.\w_]*)#?(?:[\.\!\/\\\w]*))?)/;
		var regex = new RegExp(expression);

		for ( var i = 0; i < splittedPictures.length; i++) {
			if (!regex.test(splittedPictures[i])) {
				isValid = false;
			}
		}

		if (!isValid) {
			if (language === 'es')
				alert('¡Ups! Parace que algo en las URL de las imágenes anda mal...');
			else
				alert('Oops! It seems that something in the URL is odd...');
		}

		return isValid;
	}
</SCRIPT>

<security:authorize access="hasRole('ADMIN')">

	<fieldset>
		<legend>
			<spring:message code="section.legend" />
		</legend>
		<form:form action="section/edit.do" modelAttribute="section" id="form">

			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="activity" />

			<acme:textbox code="section.title" path="title" />

			<br>

			<acme:textbox code="section.summary" path="summary" />

			<br>

			<form:label path="summary">
				<spring:message code="section.pictures" />
			</form:label>
			<form:input path="pictures" id="picturesInput" />

			<br>

			<acme:submit code="save" name="save"
				onclick="return verifyurl('${pageContext.response.locale.language}')" />&nbsp;
			
			<jstl:if test="${section.id != 0}">
				<acme:submit code="delete" name="delete" />&nbsp;
			</jstl:if>

			<acme:cancel code="cancel"
				url="activity/display.do?activityid=${section.activity.id}" />

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