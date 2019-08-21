<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<style>
<!--
.forms {
	list-style-type: none;
	text-align: center;
	margin: 0;
	padding: 0;
}

.forms li {
	display: inline-block;
	padding: 20px;
}
-->
</style>

<security:authorize access="isAuthenticated()">

	<fieldset>
		<legend>
			<spring:message code="message.orderby" />
		</legend>
		<ul class="forms">
			<li>
				<form action="message/list.do">

					<select name="topic">
						<jstl:choose>
							<jstl:when test="${pageContext.response.locale.language == 'es'}">
								<jstl:forEach var="topic" items="${topics.get('Español')}">
									<option value="${topic}">${topic}</option>
								</jstl:forEach>
							</jstl:when>
							<jstl:otherwise>
								<jstl:forEach var="topic" items="${topics.get('English')}">
									<option value="${topic}">${topic}</option>
								</jstl:forEach>
							</jstl:otherwise>
						</jstl:choose>
					</select> <input type="submit" formmethod="get"
						value="<spring:message code="order"/>">
				</form>
			</li>


			<li><form action="message/list.do">

					<input type="text" name="sender"
						placeholder="<spring:message code="mensaje.sender" />" /> <input
						type="submit" formmethod="get"
						value="<spring:message code="order"/>">

				</form></li>


			<li><form action="message/list.do">

					<input type="text" name="receiver"
						placeholder="<spring:message code="mensaje.reciever" />" /> <input
						type="submit" formmethod="get"
						value="<spring:message code="order"/>">

				</form></li>
		</ul>
	</fieldset>

	<display:table class="displaytag" name="mensajes" pagesize="5"
		requestURI="message/list.do" id="mensaje">

		<display:column titleKey="mensaje.sent" sortable="true">
			<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
				value="${mensaje.sendMoment}" />
		</display:column>

		<display:column titleKey="mensaje.subject" sortable="true">
			<jstl:out value="${mensaje.subject}" />
		</display:column>

		<display:column titleKey="mensaje.body" sortable="true">
			<jstl:out value="${mensaje.body}" />
		</display:column>

		<display:column titleKey="mensaje.topic" sortable="true">
			<jstl:out value="${mensaje.topic}" />
		</display:column>

		<display:column titleKey="mensaje.sender" sortable="true">
			<jstl:out value="${mensaje.sender.userAccount.username}" />
		</display:column>

		<display:column titleKey="mensaje.reciever" sortable="true">
			<jstl:forEach items="${mensaje.reciever}" var="reciever">
				<jstl:out value="${reciever.userAccount.username}" />
				<br>
			</jstl:forEach>
		</display:column>

		<display:column>
			<spring:message var="confirm" code="delete.confirmation" />
			<jstl:if test="${mensaje.sender.id == principal.id}">
				<a onclick="window.confirm(${confirm});"
					href="message/delete.do?messageid=${mensaje.id}"><spring:message
						code="mp.delete" /></a>
			</jstl:if>
		</display:column>

	</display:table>

	<button onclick="location.href='message/create.do';">
		<spring:message code="mensaje.new" />
	</button>

	<security:authorize access="hasRole('ADMIN')">

		<button onclick="location.href='message/createbroadcast.do?type=all';">
			<spring:message code="new.broadcast.all" />
		</button>

		<button onclick="location.href='message/createbroadcast.do?type=aut';">
			<spring:message code="new.broadcast.aut" />
		</button>

	</security:authorize>

</security:authorize>