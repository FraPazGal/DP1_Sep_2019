<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<h1><spring:message	code="sponsorship.title.list" />
<jstl:out value="${(film.title)}" /></h1>

<display:table class="displaytag" name="sponsorships" pagesize="5" 
	requestURI="sponsorship/list.do" id="sponsorship">

	<display:column titleKey="sponsorship.banner" sortable="true">
		<jstl:out value="${sponsorship.banner}" />
	</display:column>
	
	<display:column titleKey="sponsorship.targetPage" sortable="true">
		<jstl:out value="${sponsorship.targetPage}" />
	</display:column>
	
	<display:column>
		<a href="sponsorship/display.do?sponsorshipId=${sponsorship.id}"> <spring:message
				code="mp.display" />
		</a>
	</display:column>
	
	<display:column>
		<a href="sponsorship/edit.do?sponsorshipId=${sponsorship.id}"> <spring:message
				code="mp.edit" />
		</a>
	</display:column>
	
	<display:column>
		<a href="sponsorship/delete.do?sponsorshipId=${sponsorship.id}"> <spring:message
				code="mp.delete" />
		</a>
	</display:column>
</display:table>

<input type="button"
	onclick="redirect: location.href = 'sponsorship/create.do';"
	value="<spring:message code='sponsorship.create' />" />
