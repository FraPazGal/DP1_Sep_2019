<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<security:authorize access="hasRole('ADMIN')">

<h1><spring:message code="administrator.statistics" /></h1>	
	<table class="displayStyle" style="width:50%; line-height: 30px; font-size: 18px; " >
	
			<tr><td style="text-align: justify; font-size: 22px;font-weight: bold; ">
				<spring:message code="administrator.statsSubmissionsPerConference" />
			</td></tr>
			<jstl:choose>
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td style=""><spring:message code="administrator.statsSubmissionsPerConference.max" /></td>
						<td> ${statsSubmissionsPerConference[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsSubmissionsPerConference.min" /></td>
						<td> ${statsSubmissionsPerConference[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsSubmissionsPerConference.avg" /></td>
						<td> ${statsSubmissionsPerConference[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsSubmissionsPerConference.stddev" /></td>
						<td> ${statsSubmissionsPerConference[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td><spring:message code="administrator.statsRegistrationsPerConference.max" /></td>
						<td> ${statsRegistrationsPerConference[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsRegistrationsPerConference.min" /></td>
						<td> ${statsRegistrationsPerConference[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsRegistrationsPerConference.avg" /></td>
						<td> ${statsRegistrationsPerConference[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsRegistrationsPerConference.stddev" /></td>
						<td> ${statsRegistrationsPerConference[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td><spring:message code="administrator.statsConferenceFees.max" /></td>
						<td> ${statsConferenceFees[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferenceFees.min" /></td>
						<td> ${statsConferenceFees[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferenceFees.avg" /></td>
						<td> ${statsConferenceFees[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferenceFees.stddev" /></td>
						<td> ${statsConferenceFees[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td><spring:message code="administrator.statsDaysPerConference.max" /></td>
						<td> ${statsDaysPerConference[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsDaysPerConference.min" /></td>
						<td> ${statsDaysPerConference[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsDaysPerConference.avg" /></td>
						<td> ${statsDaysPerConference[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsDaysPerConference.stddev" /></td>
						<td> ${statsDaysPerConference[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td style=""><spring:message code="administrator.statsConferencesPerCategory.max" /></td>
						<td> ${statsConferencesPerCategory[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferencesPerCategory.min" /></td>
						<td> ${statsConferencesPerCategory[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferencesPerCategory.avg" /></td>
						<td> ${statsConferencesPerCategory[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsConferencesPerCategory.stddev" /></td>
						<td> ${statsConferencesPerCategory[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td><spring:message code="administrator.statsCommentsPerConference.max" /></td>
						<td> ${statsCommentsPerConference[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerConference.min" /></td>
						<td> ${statsCommentsPerConference[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerConference.avg" /></td>
						<td> ${statsCommentsPerConference[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerConference.stddev" /></td>
						<td> ${statsCommentsPerConference[3]}</td>
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
				<jstl:when test="${statsSubmissionsPerConference[0] ne null}">
					<tr>
						<td><spring:message code="administrator.statsCommentsPerActivity.max" /></td>
						<td> ${statsCommentsPerActivity[0]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerActivity.min" /></td>
						<td> ${statsCommentsPerActivity[1]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerActivity.avg" /></td>
						<td> ${statsCommentsPerActivity[2]}</td>
					</tr>
					
					<tr>
						<td><spring:message code="administrator.statsCommentsPerActivity.stddev" /></td>
						<td> ${statsCommentsPerActivity[3]}</td>
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
</security:authorize>