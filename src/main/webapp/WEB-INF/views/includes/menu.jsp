<%@page import="org.research.kadda.nuclide.Constants"%>

<nav class="navbar navbar-light navbar-expand-lg">
	<div class="container-fluid">
		<div class="navbar-header">
			<div class="row flex-nowrap">
				<a class="navbar-brand row" href="./">
					<img id="brand-logo" src="resources/img/nuclideDB-logo.svg">
					<div class="brand-name-2">Nuclide DB</div>
				</a>
				<a id="logo-link" href="https://sharepoint.com/sites/research-hub" target="_blank">
					<img id="logo"/>
				</a>
			</div>
			<button type="button" class="navbar-toggler"
					data-bs-target="#navbarCollapseMenuTop" aria-controls="navbarCollapseMenuTop"
					data-bs-toggle="collapse" aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
		</div>
		<div class="collapse navbar-collapse" id="navbarCollapseMenuTop">

			<ul class="navbar-nav me-auto mb-2 mb-lg-0" id="main-menu">
				<li class="nav-item">
					<a href="newTracer" id="newTracer" class="nav-link"><i class="fas fa-plus"></i> New Tracer Tube</a>
				</li>
				<li class="nav-item">
					<a href="newWaste" id="newWaste" class="nav-link"><i class="fas fa-plus"></i> New Waste Container</a>
				</li>
				<li class="nav-item">
					<a href="newUsage" id="newUsage" class="nav-link"><i class="fas fa-chart-pie"></i> Define Usage</a>
				</li>
				<li class="nav-item">
					<a href="showTracers" id="showTracers" class="nav-link"><i class="fas fa-search-location"></i> Tracers</a>
				</li>
				<li class="nav-item">
					<a href="showWaste" id="showWaste" class="nav-link"><i class="fas fa-dumpster"></i> Wastes</a>
				</li>
				<li class="nav-item">
					<a href="showUsers" id="showUsers" class="nav-link"><i class="fas fa-users"></i> Users</a>
				</li>
				<li class="nav-item">
					<a href="listLabs" id="listLabs" class="nav-link"><i class="fas fa-map-marker-alt"></i> Locations</a>
				</li>

			</ul>

			<ul class="navbar-nav mb-2 mb-lg-1" id="secondary-menu">
				<% if(Constants.isTestEnvironment()){%>
				<li class="nav-item">
					<span class="nav-link red" title="You are in test environment">TEST</span>
				</li>
				<% }%>

				<li class="nav-item">
					<a href="https://snprod.service-now.com/sp" class="nav-link btn-icon" target="_blank" title="Bug Report"><i class="fas fa-bug"></i></a>
				</li>

				<li class="nav-item">
					<!-- <a href="https://sharepoint.com/sites/microsite-SciCompDD/SitePages/NuclideDB.aspx" class="nav-link btn-icon" target="_blank" title="User Manual"><i class="fas fa-question"></i></a> -->
					<a href="userManual" class="nav-link btn-icon" title="User Manual"><i class="fas fa-question"></i></a>
				</li>

				<li class="nav-item">
					<jsp:include page="/WEB-INF/views/includes/themer.jsp" />
				</li>

				<li class="nav-item">
					<jsp:include page="/WEB-INF/views/includes/ddResearchAppMenuIcon.jsp" />
				</li>

				<li class="nav-item">
					<jsp:include page="/WEB-INF/views/includes/userInfo.jsp" />
				</li>
			</ul>
		</div>
	</div>
</nav>
