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

	<h1><spring:message	code="category.title.edit" /></h1>
	<form:form action="category/edit.do"
		modelAttribute="category" id="form">

		<form:hidden path="id" />

		<p>
			<spring:message code="category.title.es" />
		</p>
		
		<input type="text" name="nameES" id="nameES"
			value="${category.title.get('Español')}"
			placeholder="<spring:message code='category.edit.placeholder.es' />"
			style="width: 25%">
		<br />
		<br />

		<p>
			<spring:message code="category.title.en" />
		</p>
		<input type="text" name="nameEN" id="nameEN"
			value="${category.title.get('English')}"
			placeholder="<spring:message code='category.edit.placeholder.en' />"
			style="width: 25%">
			<br><br>
		<form:errors cssClass="error" path="title" />
		<br />
		<br />
		<jstl:if test="${category.id == 0 }">
			<jstl:choose>
				<jstl:when test="${pageContext.response.locale.language == 'es'}">
					<form:label path="parentCategory"><spring:message code="category.parentCategory" /></form:label><br>
					<form:select path="parentCategory" style="width:200px;">
						<jstl:forEach var="parentCategory" items="${categories}">
							<form:option value="${parentCategory}" label="${parentCategory.title.get('Español')}" />
						</jstl:forEach>
					</form:select><br>
				</jstl:when>
				<jstl:otherwise>
					<form:label path="parentCategory"><spring:message code="category.parentCategory" /></form:label><br>
					<form:select path="parentCategory" style="width:200px;">
					<jstl:set var="lol" value="<spring:message code='category.no.parent' />"/>
						<jstl:forEach var="parentCategory" items="${categories}">
							<form:option value="${parentCategory}" label="${parentCategory.title.get('English')}" />
						</jstl:forEach>
					</form:select><br>
				</jstl:otherwise>
			</jstl:choose>
		</jstl:if>
		<br>
		<acme:submit code="mp.save" name="save"/>&nbsp;

		<acme:cancel code="mp.cancel" url="category/list.do"/><br/><br/>
	</form:form>
</security:authorize>