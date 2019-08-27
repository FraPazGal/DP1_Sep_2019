<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
					<jstl:out value="${author.name}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.middleName" />: </strong>
					<jstl:out value="${author.middleName}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.surname" />: </strong>
					<jstl:out value="${author.surname}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.email" />: </strong>
					<jstl:out value="${author.email}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.phone" />: </strong>
					<jstl:out value="${author.phoneNumber}" />
				</div>

				<br />

				<div>
					<strong><spring:message code="actor.address" />: </strong>
					<jstl:out value="${author.address}" />
				</div>

				<jstl:if test="${not empty author.score}">
					<br />
					<div>
						<strong><spring:message code="actor.score" />: </strong>
						<fmt:formatNumber minFractionDigits="2" maxFractionDigits="2"
							value="${author.score}" />
					</div>
				</jstl:if>

			</div>

			<div style="float: right;">
				<img style="width: 200px; height: 200px" src="${author.photo}"
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