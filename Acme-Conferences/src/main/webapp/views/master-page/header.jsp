<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<a href="#"><img src="${banner}" alt="Acme-Conferences Co., Inc."
		style="margin-bottom: 0.5em;" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		<security:authorize access="hasRole('ADMIN')">

			<li><a class="fNiv"
				href="administrator/administrator/register.do"><spring:message
						code="master.page.register.admin" /></a>
		</security:authorize>

		<security:authorize access="permitAll">
			<li><a class="fNiv"><spring:message
						code="master.page.conference" /></a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="!hasRole('ADMIN')">
						<li><a href="conference/list.do?catalog=future"><spring:message
									code="master.page.conference.list" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="conference/list.do?catalog=unpublished"><spring:message
									code="master.page.conference.list" /></a></li>
						<li><a href="conference/create.do"><spring:message
									code="master.page.conference.new" /></a></li>
					</security:authorize>
					<security:authorize access="isAuthenticated()">
						<li><a href="finder/search.do"><spring:message
									code="master.page.conference.finder" /></a></li>
					</security:authorize>
					<security:authorize access="!isAuthenticated()">
						<li><a href="finder/anon/search.do"><spring:message
									code="master.page.conference.finder" /></a></li>
					</security:authorize>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('ADMIN')">

			<li><a class="fNiv"><spring:message
						code="master.page.categories" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="category/list.do"><spring:message
								code="master.page.categories.list" /></a></li>
					<li><a href="category/create.do"><spring:message
								code="master.page.categories.new" /></a></li>
				</ul></li>

		</security:authorize>

		<security:authorize access="hasRole('SPONSOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.sponsorship" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="sponsorship/list.do"><spring:message
								code="master.page.sponsorship.list" /></a></li>
					<li><a href="sponsorship/create.do"><spring:message
								code="master.page.sponsorship.new" /></a></li>
				</ul></li>


		</security:authorize>

		<security:authorize access="hasRole('AUTHOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.registration" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="registration/list.do"><spring:message
								code="master.page.registration.list" /></a></li>
				</ul></li>


		</security:authorize>

		<security:authorize access="hasAnyRole('AUTHOR', 'REVIEWER')">
			<li><a class="fNiv"><spring:message
						code="master.page.submission" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="submission/list.do"><spring:message
								code="master.page.submission.list" /></a></li>
				</ul></li>

		</security:authorize>

		<security:authorize access="hasRole('REVIEWER')">
			<li><a class="fNiv" href="review/reviewer/myreports.do"><spring:message
						code="master.page.reviews" /></a>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>

				<ul>
					<li class="arrow"></li>
					<li><a href="author/register.do"><spring:message
								code="master.page.register.author" /></a></li>

					<li><a href="sponsor/register.do"><spring:message
								code="master.page.register.sponsor" /></a></li>

					<li><a href="reviewer/register.do"><spring:message
								code="master.page.register.reviewer" /></a></li>

				</ul></li>

		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.message" />
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="message/list.do"><spring:message
								code="master.page.message.list" /> </a></li>
					<li><a href="message/create.do"><spring:message
								code="master.page.message.create" /> </a></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="message/createbroadcast.do?type=aut"><spring:message
									code="master.page.message.broadcast.authors" /> </a></li>
						<li><a href="message/createbroadcast.do?type=aut"><spring:message
									code="master.page.message.broadcast.all" /> </a></li>
					</security:authorize>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('ADMIN')">

			<li><a href="statistics/display.do"><spring:message
						code="master.page.administrator.statistics" /></a></li>

			<li><a href="config/admin/display.do"><spring:message
						code="master.page.configuration" /></a>

				<ul>
					<li class="arrow"></li>
					<li><a href="config/admin/display.do"><spring:message
								code="master.page.configuration.display" /> </a></li>
					<li><a href="config/admin/edit.do"><spring:message
								code="master.page.configuration.edit" /> </a></li>
				</ul></li>

		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/display.do"><spring:message
									code="master.page.profile" /> </a></li>
						<li><a href="administrator/administrator/edit.do"><spring:message
									code="master.page.profile.edit" /> </a></li>
					</security:authorize>

					<security:authorize access="hasRole('AUTHOR')">
						<li><a href="author/display.do"><spring:message
									code="master.page.profile" /> </a></li>
						<li><a href="author/author/edit.do"><spring:message
									code="master.page.profile.edit" /> </a></li>
					</security:authorize>

					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="sponsor/display.do"><spring:message
									code="master.page.profile" /> </a></li>
						<li><a href="sponsor/sponsor/edit.do"><spring:message
									code="master.page.profile.edit" /> </a></li>
					</security:authorize>

					<security:authorize access="hasRole('REVIEWER')">
						<li><a href="reviewer/display.do"><spring:message
									code="master.page.profile" /> </a></li>
						<li><a href="reviewer/reviewer/edit.do"><spring:message
									code="master.page.profile.edit" /> </a></li>
					</security:authorize>
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div style="float: right;">

	<a href="?language=en"><img style="width: 20px; height: 15px"
		src="https://upload.wikimedia.org/wikipedia/en/thumb/a/ae/Flag_of_the_United_Kingdom.svg/1280px-Flag_of_the_United_Kingdom.svg.png"
		alt="EN"></a> <span>|</span> <a href="?language=es"><img
		style="width: 20px; height: 15px;"
		src="http://www.ahb.es/m/100150RES.jpg" alt="ES"></a>
</div>

