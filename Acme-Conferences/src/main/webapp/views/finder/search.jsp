
<%--
 * login.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<h1><spring:message code="finder.title" /></h1>

<jstl:choose>
	<jstl:when test="${errMsg ne null}">
		<p>
			<jstl:out value="${errMsg}"/>
		</p>
	</jstl:when>
	<jstl:otherwise>
		<security:authorize access="isAuthenticated()">
		
			<form:form action="finder/search.do" modelAttribute="finder" id="form">
		
				<form:hidden path="id" />
				
				<acme:textbox code="finder.keyWord" path="keyWord" size="50px" /><br/> <br/>
				<acme:textbox code="finder.maximumFee" path="maximumFee" size="50px" placeholder="price.placeholder"/><br/> <br/>
				<acme:textbox code="finder.minimumDate" path="minimumDate" size="50px" placeholder="date.placeholder"/><br/> <br/>
				<acme:textbox code="finder.maximumDate" path="maximumDate" size="50px" placeholder="date.placeholder"/><br/> <br/>
				<spring:message code="conference.category" />
				<jstl:if test="${!conference.isFinal || conference.id == 0}">
					<jstl:choose>
						<jstl:when test="${pageContext.response.locale.language == 'es'}">
							<form:select path="category" name="categoryArray" style="width:200px;">
								<option value=${null } >
										<spring:message code="finder.categories.all"/>
								</option>
								<jstl:forEach var="category" items="${categories}">
									<option value="${category.id}" >
										<jstl:out value="${category.title.get('Español')}" />
									</option>
								</jstl:forEach>
							</form:select><br><br>
						</jstl:when>
						<jstl:otherwise>
							<form:select path="category" name="categoryArray" style="width:200px;">
								<option value=${null } >
										<spring:message code="finder.categories.all"/>
								</option>
								<jstl:forEach var="category" items="${categories}">
									<option value="${category.id}" >
										<jstl:out value="${category.title.get('English')}" />
									</option>
								</jstl:forEach>
							</form:select><br><br>
						</jstl:otherwise>
					</jstl:choose>
				</jstl:if>
				<br>
		
				<input type="submit" name="save" id="save"
					value="<spring:message code="finder.showResults" />" />
				
			&#160;
				<jstl:if test="${finder.id!=0}">
					<input type="submit" name="delete" id="delete"
						value='<spring:message code="finder.delete"/>' />
				</jstl:if>
		
			</form:form>
		
			<jstl:if test="${not empty conferences}">
				<h2><spring:message code="finder.results" /></h2>
				<display:table name="conferences" id="row"
					requestURI="${requestURI}" pagesize="10" class="displaytag">
		
					<!-- Attributes-->
		
					<display:column titleKey="conference.title" sortable="true">
						<jstl:out value="${row.title}" />
					</display:column>
					<display:column titleKey="conference.acronym" sortable="true">
						<jstl:out value="${row.acronym}" />
					</display:column>
					<display:column titleKey="conference.entryFee" sortable="true">
						<fmt:formatNumber maxFractionDigits="2" value="${row.entryFee }" />
					</display:column>
					<display:column titleKey="conference.submissionDeadline" sortable="true">
						<jstl:out value="${row.submissionDeadline}" />
					</display:column>
					<display:column titleKey="conference.startDate" sortable="true">
						<jstl:out value="${row.startDate}" />
					</display:column>
					<display:column titleKey="conference.endDate" sortable="true">
						<jstl:out value="${row.endDate}" />
					</display:column>
		
					<!-- Action links -->
		
					<display:column>
						<a href="conference/display.do?conferenceId=${row.id}"> <spring:message
								code="conference.display" />
						</a>
					</display:column>
				</display:table>
			</jstl:if>
		</security:authorize>
		
		<security:authorize access="!isAuthenticated()">
		
			<form>
				<b><spring:message code="finder.enterKey"/>&#160;</b> <input id="test"
					type="text" name="keyWord" size="20" />
		
				<script>
					var keyWord = "";
					document.getElementById("test").value = keyWord;
				</script>
		
				<input type="submit" value="Search" name="submit" />
		
			</form>
		
			<br>
			<br>
			<jstl:if test="${not empty conferences}">
				<display:table name="conferences" id="row"
					requestURI="finder/list.do" pagesize="10" class="displaytag">
		
					<!-- Attributes-->
		
					<display:column titleKey="conference.title" sortable="true">
						<jstl:out value="${row.title}" />
					</display:column>
					<display:column titleKey="conference.acronym" sortable="true">
						<jstl:out value="${row.acronym}" />
					</display:column>
					<display:column titleKey="conference.entryFee" sortable="true">
						<fmt:formatNumber maxFractionDigits="2" value="${row.entryFee }" />
					</display:column>
					<display:column titleKey="conference.submissionDeadline" sortable="true">
						<jstl:out value="${row.submissionDeadline}" />
					</display:column>
					<display:column titleKey="conference.startDate" sortable="true">
						<jstl:out value="${row.startDate}" />
					</display:column>
					<display:column titleKey="conference.endDate" sortable="true">
						<jstl:out value="${row.endDate}" />
					</display:column>
		
					<!-- Action links -->
		
					<display:column>
						<a href="conference/display.do?conferenceId=${row.id}"> <spring:message
								code="conference.display" />
						</a>
					</display:column>
				</display:table>
			</jstl:if>
		</security:authorize>
		</jstl:otherwise>
</jstl:choose>