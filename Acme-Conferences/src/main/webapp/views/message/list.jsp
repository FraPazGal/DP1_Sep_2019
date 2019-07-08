<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
