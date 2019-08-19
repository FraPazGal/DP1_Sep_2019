<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:choose>
	<jstl:when test="${found}">
		<!-- Actor Attributes -->
		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>

			<div style="float: left;">
				<div>
					<strong><spring:message code="actor.name" />: </strong>
					<jstl:out value="${reviewer.name}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.middleName" />: </strong>
					<jstl:out value="${reviewer.middleName}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.surname" />: </strong>
					<jstl:out value="${reviewer.surname}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.email" />: </strong>
					<jstl:out value="${reviewer.email}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.phone" />: </strong>
					<jstl:out value="${reviewer.phoneNumber}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.address" />: </strong>
					<jstl:out value="${reviewer.address}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.keywords" />: </strong>
					<jstl:out value="${reviewer.keywords}" />
				</div>

			</div>

			<div style="float: right;">
				<img style="width: 200px; height: 200px" src="${reviewer.photo}"
					alt="User photo">
			</div>

		</fieldset>

	</jstl:when>
	<jstl:otherwise>
		<p class="error">
			<spring:message code="some.error" />
		</p>
	</jstl:otherwise>
</jstl:choose>