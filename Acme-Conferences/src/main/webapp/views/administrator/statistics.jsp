<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<h1><spring:message code="administrator.statistics" /></h1>	
<table class="displayStyle" style="width:50%; line-height: 30px; font-size: 18px; " >

	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsSubmissionsPerConference" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsSubmissionsPerConference.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsSubmissionsPerConference[0]}"/> </td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsSubmissionsPerConference.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsSubmissionsPerConference[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsSubmissionsPerConference.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsSubmissionsPerConference[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsSubmissionsPerConference.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsSubmissionsPerConference[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>

	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsRegistrationsPerConference" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsRegistrationsPerConference[0] ne null}">
			<tr>
				<td><spring:message code="administrator.statsRegistrationsPerConference.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsRegistrationsPerConference[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsRegistrationsPerConference.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsRegistrationsPerConference[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsRegistrationsPerConference.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsRegistrationsPerConference[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsRegistrationsPerConference.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsRegistrationsPerConference[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	
	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsConferenceFees" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsConferenceFees[0] ne null}">
			<tr>
				<td><spring:message code="administrator.statsConferenceFees.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferenceFees[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferenceFees.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferenceFees[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferenceFees.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferenceFees[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferenceFees.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferenceFees[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>

	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsDaysPerConference" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsDaysPerConference[0] ne null}">
			<tr>
				<td><spring:message code="administrator.statsDaysPerConference.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsDaysPerConference[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsDaysPerConference.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsDaysPerConference[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsDaysPerConference.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsDaysPerConference[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsDaysPerConference.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsDaysPerConference[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	<tr><td><br></td></tr>
	
	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsConferencesPerCategory" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsConferencesPerCategory[0] ne null}">
			<tr>
				<td style=""><spring:message code="administrator.statsConferencesPerCategory.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferencesPerCategory[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferencesPerCategory.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferencesPerCategory[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferencesPerCategory.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferencesPerCategory[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsConferencesPerCategory.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsConferencesPerCategory[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
	<tr><td><br></td></tr>

	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsCommentsPerConference" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsCommentsPerConference[0] ne null}">
			<tr>
				<td><spring:message code="administrator.statsCommentsPerConference.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerConference[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerConference.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerConference[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerConference.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerConference[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerConference.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerConference[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>

	<tr><td><br></td></tr>

	<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
		<spring:message code="administrator.statsCommentsPerActivity" />
	</td></tr>
	<jstl:choose>
		<jstl:when test="${statsCommentsPerActivity[0] ne null}">
			<tr>
				<td><spring:message code="administrator.statsCommentsPerActivity.max" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerActivity[0]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerActivity.min" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerActivity[1]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerActivity.avg" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerActivity[2]}"/></td>
			</tr>
			
			<tr>
				<td><spring:message code="administrator.statsCommentsPerActivity.stddev" /></td>
				<td><fmt:formatNumber type="number" minFractionDigits="1" maxFractionDigits="2" value="${statsCommentsPerActivity[3]}"/></td>
			</tr>
		</jstl:when>
		<jstl:otherwise>
		<tr><td><br></td></tr>
			<tr>
				<td><spring:message code="administrator.statistics.noData" /></td>
			</tr>
		</jstl:otherwise>
	</jstl:choose>
</table>
<acme:cancel url="welcome/index.do" code="mp.back" /><br><br>
