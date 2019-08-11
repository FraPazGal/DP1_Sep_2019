<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('REVIEWER')">
	<form:form modelAttribute="review" action="review/reviewer/edit.do">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="reviewer" />
		<form:hidden path="isWritten" />
		<form:hidden path="submission" />

		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="review.write" />
			</legend>

			<acme:textbox path="originalityScore" code="review.originality"
				size="60" required="true" />
			<jstl:if
				test="${not empty binding.getFieldError('originalityScore')}">
				<p class="error">
					<spring:message code="originality.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="qualityScore" code="review.quality"
				required="true" size="60" />
			<jstl:if test="${not empty binding.getFieldError('qualityScore')}">
				<p class="error">
					<spring:message code="quality.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textbox path="readabilityScore" code="review.originality"
				size="60" required="true" />
			<jstl:if
				test="${not empty binding.getFieldError('readabilityScore')}">
				<p class="error">
					<spring:message code="readability.error" />
				</p>
			</jstl:if>

			<br />

			<acme:textarea path="comments" code="review.comments" required="true" />
			<jstl:if test="${not empty binding.getFieldError('comments')}">
				<p class="error">
					<spring:message code="comments.error" />
				</p>
			</jstl:if>

			<br />

			<form:select path="status" required="true" titleKey="review.status">
				<form:option value="---------">---------</form:option>
				<form:option value="ACCEPTED">Accepted</form:option>
				<form:option value="BORDER-LINE">Border-line</form:option>
				<form:option value="REJECTED">Rejected</form:option>
			</form:select>
			<jstl:if test="${not empty binding.getFieldError('status')}">
				<p class="error">
					<spring:message code="status.error" />
				</p>
			</jstl:if>

		</fieldset>
		<br />

		<!-- Buttons -->
		<acme:submit name="save" code="form.save" />

		<button type="button"
			onclick="javascript: relativeRedir('review/reviewer/myreports.do')">
			<spring:message code="form.cancel" />
		</button>

	</form:form>
</security:authorize>