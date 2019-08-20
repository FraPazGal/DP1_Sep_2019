<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<security:authorize access="hasRole('ADMIN')">
	<table class="displayStyle">

		<tr>
			<td><strong> <spring:message code="config.sysname" />
					:
			</strong></td>
			<td><jstl:out value="${config.systemName}" /></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.banner" /> :
			</strong></td>
			<td><a href="<jstl:out
						value='${config.banner}' />">
					<jstl:out value='${config.banner}' />
			</a></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.countryCode" />
					:
			</strong></td>
			<td><jstl:out value="${config.countryCode}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.makes" /> :
			</strong></td>
			<td><jstl:out value="${config.makes}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.cache" /> :
			</strong></td>
			<td><jstl:out value="${config.timeResultsCached}"></jstl:out></td>
		</tr>

		<tr>
			<td><strong> <spring:message code="config.maxResults" />
					:
			</strong></td>
			<td><jstl:out value="${config.maxResults}"></jstl:out></td>
		</tr>

	</table>

	<jstl:if test="${not empty welcome}">
		<table>

			<tr>
				<th><spring:message code="welcome.es" />
				<td><jstl:out value="${welcome.get('Español')}" /></td>
			</tr>

			<tr>
				<th><spring:message code="welcome.en" />
				<td><jstl:out value="${welcome.get('English')}" /></td>
			</tr>

		</table>
	</jstl:if>

	<jstl:if test="${not empty voidEN and not empty voidES}">
		<table>

			<tr>
				<th><spring:message code="void.es" />
				<td><jstl:out value="${fn:replace(voidES, ',', ', ')}" /></td>
			</tr>

			<tr>
				<th><spring:message code="void.en" />
				<td><jstl:out value="${fn:replace(voidEN, ',', ', ')}" /></td>
			</tr>

		</table>
	</jstl:if>


	<jstl:if test="${not empty topics}">
		<table>

			<tr>
				<th><spring:message code="topics.es" />
				<td><jstl:out value="${topics.get('Español')}" /></td>
			</tr>

			<tr>
				<th><spring:message code="topics.en" />
				<td><jstl:out value="${topics.get('English')}" /></td>
			</tr>

		</table>
	</jstl:if>


	<input type="button"
		onclick="redirect: location.href = 'config/admin/edit.do';"
		value="<spring:message code='sysconfig.edit' />" />

	<hr>
</security:authorize>