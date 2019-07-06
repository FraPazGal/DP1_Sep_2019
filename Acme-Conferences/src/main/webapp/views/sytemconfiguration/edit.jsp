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
	function checkEmpty(language) {
		var isValid = true;
		var maps = [ document.getElementById('welcomeES').value,
				document.getElementById('welcomeEN').value,
				document.getElementById('voidES').value,
				document.getElementById('voidEN').value,
				document.getElementById('topicsES').value,
				document.getElementById('topicsEN').value ];

		for ( var int = 0; int < maps.length; int++) {
			if (maps[int] === null || maps[int].trim() === "") {
				isValid = false;
			}
		}

		if (!isValid) {
			if (language === 'es')
				alert('¡Ups! Parace que algo en los mensajes anda mal...');
			else
				alert('Oops! It seems that something in the messages is odd...');
		}

		return isValid;
	}
</SCRIPT>

<security:authorize access="hasRole('ADMIN')">

	<br>

	<fieldset>
		<legend>
			<spring:message code="config.legend" />
		</legend>

		<form:form action="config/admin/edit.do" modelAttribute="config"
			id="form">

			<form:hidden path="id" />
			<form:hidden path="version" />

			<acme:textbox code="config.sysname" path="systemName" />
			<jstl:if
				test="${not empty binding.getFieldError('systemName').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('systemName').getCode()}" />
				</p>
			</jstl:if>

			<br>

			<acme:textbox code="config.banner" path="banner" />
			<jstl:if
				test="${not empty binding.getFieldError('banner').getCode()}">
				<p class="error">
					<spring:message code="${binding.getFieldError('banner').getCode()}" />
				</p>
			</jstl:if>

			<br>

			<acme:textbox code="config.countryCode" path="countryCode" />
			<jstl:if
				test="${not empty binding.getFieldError('countryCode').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('countryCode').getCode()}" />
				</p>
			</jstl:if>

			<br>

			<form:label path="timeResultsCached">
				<spring:message code="config.cache" />
			</form:label>
			<form:input type="number" path="timeResultsCached" />
			<jstl:if
				test="${not empty binding.getFieldError('timeResultsCached').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('timeResultsCached').getCode()}" />
				</p>
			</jstl:if>

			<br>
			<br>

			<form:label path="maxResults">
				<spring:message code="config.maxResults" />
			</form:label>
			<form:input type="number" path="maxResults" />
			<jstl:if
				test="${not empty binding.getFieldError('maxResults').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('maxResults').getCode()}" />
				</p>
			</jstl:if>

			<br>

			<p>
				<spring:message code="welcome.es" />
			</p>

			<input type="text" name="welcomeES" id="welcomeES" size="100%"
				value="${welcome.get('Español')}"
				placeholder="<spring:message code='sysconfig.edit.welcome.message.es' />"
				required>

			<br />

			&nbsp;

			<p>
				<spring:message code="welcome.en" />
			</p>
			<input type="text" name="welcomeEN" id="welcomeEN" size="100%"
				value="${welcome.get('English')}"
				placeholder="<spring:message code='sysconfig.edit.welcome.message.en' />"
				required>
			<jstl:if
				test="${not empty binding.getFieldError('welcomeMessage').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('welcomeMessage').getCode()}" />
				</p>
			</jstl:if>

			<br />
			<br />



			<p>
				<spring:message code="void.es" />
			</p>

			<input type="text" name="voidES" id="voidES" size="100%"
				value="${voidES}"
				placeholder="<spring:message code='sysconfig.edit.void.message.es' />"
				required>

			<br />
			<br />

			<p>
				<spring:message code="void.en" />
			</p>
			<input type="text" name="voidEN" id="voidEN" size="100%"
				value="${voidEN}"
				placeholder="<spring:message code='sysconfig.edit.void.message.en' />"
				required>
			<jstl:if
				test="${not empty binding.getFieldError('voidWords').getCode()}">
				<p class="error">
					<spring:message
						code="${binding.getFieldError('voidWords').getCode()}" />
				</p>
			</jstl:if>

			<br />
			<br />



			<p>
				<spring:message code="topics.es" />
			</p>

			<input type="text" name="topicsES" id="topicsES" size="100%"
				value="${topics.get('Español')}"
				placeholder="<spring:message code='sysconfig.edit.topics.message.es' />"
				required>

			<br />
			<br />

			<p>
				<spring:message code="topics.en" />
			</p>
			<input type="text" name="topicsEN" id="topicsEN" size="100%"
				value="${topics.get('English')}"
				placeholder="<spring:message code='sysconfig.edit.topics.message.en' />"
				required>
			<jstl:if
				test="${not empty binding.getFieldError('topics').getCode()}">
				<p class="error">
					<spring:message code="${binding.getFieldError('topics').getCode()}" />
				</p>
			</jstl:if>

			<br />
			<br />

			<acme:submit code="save" name="save"
				onclick="return checkEmpty('${pageContext.response.locale.language}')" />
			<acme:cancel code="cancel" url="config/admin/display.do" />

			<br />
			<form:errors cssClass="error" code="${message}" />
		</form:form>
	</fieldset>
</security:authorize>
