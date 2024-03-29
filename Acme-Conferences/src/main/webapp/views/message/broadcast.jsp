<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAuthenticated()">

	<jstl:choose>
		<jstl:when test="${empty messageError}">
			<fieldset>
				<legend>
					<spring:message code="mensaje.legend" />
				</legend>
				<form:form action="message/broadcast.do" modelAttribute="mensaje"
					id="form">

					<form:hidden path="id" />
					<form:hidden path="version" />
					<form:hidden path="reciever" />

					<acme:textbox code="mensaje.subject" path="subject" required="true" />

					<br>

					<acme:textarea code="mensaje.body" path="body" required="true" />

					<br>

					<spring:message code="mensaje.topic" />
					<jstl:choose>
						<jstl:when test="${pageContext.response.locale.language == 'es'}">
							<form:select required="true" path="topic" name="topics"
								style="width:200px;">
								<jstl:forEach var="topic" items="${topics.get('Espa�ol')}">
									<option value="${topic}">
										<jstl:out value="${topic}" />
									</option>
								</jstl:forEach>
							</form:select>
							<br>
							<br>
						</jstl:when>
						<jstl:otherwise>
							<form:select required="true" path="topic" name="topics"
								style="width:200px;">
								<jstl:forEach var="topic" items="${topics.get('English')}">
									<option value="${topic}">
										<jstl:out value="${topic}" />
									</option>
								</jstl:forEach>
							</form:select>
							<br>
							<br>
						</jstl:otherwise>
					</jstl:choose>
					<jstl:if test="${not empty binding.getFieldError('topic')}">
						<p class="error">
							<spring:message code="message.topic.error" />
						</p>
					</jstl:if>

					<br>

					<acme:submit code="save" name="save" />&nbsp;

			<acme:cancel code="cancel" url="message/list.do" />

					<br />
				</form:form>
			</fieldset>
		</jstl:when>
		<jstl:otherwise>
			<h3>
				<spring:message code="receiver.error" />
			</h3>
		</jstl:otherwise>
	</jstl:choose>

</security:authorize>
