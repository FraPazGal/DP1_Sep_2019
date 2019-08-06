
<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">
<h1><spring:message	code="category.title.list" /></h1>
	<jstl:choose>
		<jstl:when test="${errMsg ne null}">
			<p>
				<jstl:out value="${errMsg}" />
			</p>
		</jstl:when>
		<jstl:otherwise>
			
			<display:table style="width: 80%" class="displaytag" name="categories" pagesize="5" 
				requestURI="category/list.do" id="category">

				<jstl:choose>
					<jstl:when test="${pageContext.response.locale.language == 'es'}">
						<display:column titleKey="category.title" sortable="true">
							<jstl:out value="${category.title.get('Español')}" />
						</display:column>
					</jstl:when>
					<jstl:otherwise>
						<display:column titleKey="category.title" sortable="true">
							<jstl:out value="${category.title.get('English')}" />
						</display:column>
					</jstl:otherwise>
				</jstl:choose>
				<display:column titleKey="category.parentCategory" sortable="true">
					<jstl:choose>
						<jstl:when test="${not empty category.parentCategory}">
							<jstl:choose>
								<jstl:when test="${pageContext.response.locale.language == 'es'}">
									<jstl:out value="${category.parentCategory.title.get('Español')}" />
								</jstl:when>
								<jstl:otherwise>
									<jstl:out value="${category.parentCategory.title.get('English')}" />
								</jstl:otherwise>
							</jstl:choose>
						</jstl:when>
						<jstl:otherwise>
							<spring:message	code="category.no.parent" />
						</jstl:otherwise>
					</jstl:choose>
				</display:column>
				
				<display:column titleKey="category.childCategories" sortable="true">
					<jstl:choose>
						<jstl:when test="${not empty category.childCategories}">
							<jstl:choose>
								<jstl:when test="${pageContext.response.locale.language == 'es'}">
									-
									<jstl:forEach var="child" items="${category.childCategories}">
										<jstl:out value="${child.title.get('Español')}" />
										-
									</jstl:forEach>
								</jstl:when>
								<jstl:otherwise>
									-
									<jstl:forEach var="child" items="${category.childCategories}">
										<jstl:out value="${child.title.get('English')}" />
										-
									</jstl:forEach>
								</jstl:otherwise>
							</jstl:choose>
						</jstl:when>
						<jstl:otherwise>
							<spring:message	code="category.no.children" />
						</jstl:otherwise>
					</jstl:choose>
				</display:column>
						
				<display:column style="width: 10%">
					<jstl:if test="${not empty category.parentCategory}">
						<a href="category/edit.do?categoryId=${category.id}"><spring:message
								code="mp.edit" /></a>
					</jstl:if>
				</display:column>
				
				
				<display:column style="width: 10%">
					<jstl:if test="${not empty category.parentCategory}">
						<a href="category/delete.do?categoryId=${category.id}"><spring:message
								code="mp.delete" /></a>
					</jstl:if>
				</display:column>
				
			</display:table>
			<input type="button"
				onclick="redirect: location.href = 'category/create.do';"
				value="<spring:message code='category.create' />" />
		</jstl:otherwise>
	</jstl:choose>

</security:authorize>