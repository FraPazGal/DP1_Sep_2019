<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('ADMIN')">

	<jstl:choose>

		<jstl:when test="${not empty reviewers}">
			<fieldset>
				<legend>
					<spring:message code="review.reviewer.selection" />
				</legend>

				<spring:message code="no.more" var="advertencia" />
				<form action="review/admin/assign.do"
					onsubmit="return window.confirm('${advertencia}');">

					<input type="hidden" name="submissionid" value="${submissionid}">

					<label><spring:message code="review.available" /></label> <br />
					<br /> <select multiple="multiple" name="reviewerid">

						<jstl:forEach var="reviewer" items="${reviewers}">
							<option value="${reviewer.id}">
								<jstl:out value="${reviewer.userAccount.username}" />
							</option>
						</jstl:forEach>

					</select> <br /> <br /> <input type="submit"
						value="<spring:message code="review.assign" />" formmethod="post">

				</form>

			</fieldset>
			
			<jstl:if test="${not empty errormesage}">
				<p class="error">
					<spring:message code="${errormesage}" />
				</p>
			</jstl:if>
			
		</jstl:when>
		<jstl:otherwise>
			<h2>
				<spring:message code="no.reviewers" />
			</h2>

			<input type="button" onclick="window.history.back()"
				value="<spring:message code="go.back" />" />

		</jstl:otherwise>

	</jstl:choose>

</security:authorize>