<%--
 * textbox.tag
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty"%>

<%-- Taglibs --%>


<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<%-- Attributes --%>

<%@ attribute name="path" required="true"%>
<%@ attribute name="code" required="true"%>

<%@ attribute name="codeErr" required="false"%>
<%@ attribute name="size" required="false"%>
<%@ attribute name="placeholder" required="false"%>
<%@ attribute name="readonly" required="false"%>
<%@ attribute name="password" required="false"%>
<%@ attribute name="required" required="false"%>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false" />
</jstl:if>

<%-- Definition --%>

<div>
	<jstl:if test="${not empty placeholder }">
		<spring:message code="${placeholder}" var="place" />
	</jstl:if>

	<form:label path="${path}">
		<spring:message code="${code}" />
	</form:label>
	<br>
	<jstl:if test="${password eq null }">
		<form:input path="${path}" readonly="${readonly}" size="${size }"
			required="${required}" placeholder="${place}" />
	</jstl:if>
	<jstl:if test="${password eq true }">
		<form:password path="${path}" size="${size }" required="${required}"
			placeholder="${place}" />
	</jstl:if>
	<br>
	<form:errors path="${path}" cssClass="error" />
</div>
