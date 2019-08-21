<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script>
	function checkPhone(msg) {
		var phone = document.getElementById("phoneNumber").value;
		var pattern = /^(((([+][1-9]{1}[0-9]{0,2}[\s]){0,1}([(][1-9]{1}[0-9]{0,2}[)][\s]){0,1})){0,1}([0-9]{4}){1}([0-9]{0,}))$/;
		var pat = pattern.test(phone);
		if (pat) {
			return true;
		} else {
			return confirm(msg);
		}
	}
</script>

<spring:message code="phone.confirmation" var="confirmTelephone" />
<security:authorize access="hasRole('AUTHOR')">
	<form:form modelAttribute="editionFormObject"
		action="author/author/edit.do"
		onsubmit="javascript: return checkPhone('${confirmTelephone}');">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<!-- Actor Attributes -->
		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>

			<acme:textbox path="name" code="actor.name" size="60" required="true" />
			<jstl:if test="${not empty binding.getFieldError('name')}">
				<p class="error">
					<spring:message code="name.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="middleName" code="actor.middleName" size="60" />

			<br />

			<acme:textbox path="surname" code="actor.surname" size="60"
				required="true" />
			<jstl:if test="${not empty binding.getFieldError('surname')}">
				<p class="error">
					<spring:message code="surname.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="photo" code="actor.photo" size="60" />
			<jstl:if test="${not empty binding.getFieldError('photo')}">
				<p class="error">
					<spring:message code="photo.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="email" code="actor.email"
				placeholder="email.placeholder" size="60" required="true" />
			<jstl:if test="${not empty binding.getFieldError('email')}">
				<p class="error">
					<spring:message code="email.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="phoneNumber" code="actor.phone"
				placeholder="phone.placeholder" size="60" />
			<jstl:if test="${not empty binding.getFieldError('phone')}">
				<p class="error">
					<spring:message code="email.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="address" code="actor.address" size="60" />
		</fieldset>
		<br />

		<!-- Buttons -->
		<input type="submit" name="save"
			value="<spring:message code="form.save" />" />

		<button type="button"
			onclick="javascript: relativeRedir('/author/display.do')">
			<spring:message code="form.cancel" />
		</button>

	</form:form>
</security:authorize>