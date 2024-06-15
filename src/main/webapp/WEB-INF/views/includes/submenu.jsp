<%@ page import="java.sql.*,java.text.*,java.util.*,org.research.kadda.nuclide.*" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">

<body>
 <table class="menuTable">
	<tr>
		<td><a href="newBiologicalTracer" id="newBiologicalTracer" title="Add a new Biological Tracer">Create new assay</a></td>
		<td><a href="newDaughter" id="newDaughter" title="Add a new Daughter Tracer Tube">Create new daughter tracer tube</a></td>
		<td><a href="newExternal" id="newExternal"title="Add a new External Usage">Define external usage (material leaves the company)</a></td>
		<td><a href="newInVivo" id="newInVivo" title="Add a new In-Vivo Usage">Define in-vivo usage with new sample creation</a></td>
	</tr>
</table>
</body>

<script type="text/javascript">
	$(function () {
		//alert($(location).attr('pathname'));
		var zeUri = $(location).attr('pathname');
		switch (zeUri) {
		case "/nuclideDB/newBiologicalTracer":
			$("#newBiologicalTracer").addClass("selectedA");
			$("#newDaughter").removeClass("selectedA");
			$("#newExternal").removeClass("selectedA");
			$("#newInVivo").removeClass("selectedA");
			break;
		case "/nuclideDB/newDaughter":
			$("#newBiologicalTracer").removeClass("selectedA");
			$("#newDaughter").addClass("selectedA");
			$("#newExternal").removeClass("selectedA");
			$("#newInVivo").removeClass("selectedA");
			break;
		case "/nuclideDB/newExternal":
			$("#newBiologicalTracer").removeClass("selectedA");
			$("#newDaughter").removeClass("selectedA");
			$("#newExternal").addClass("selectedA");
			$("#newInVivo").removeClass("selectedA");			
			break;
		case "/nuclideDB/newInVivo":
			$("#newBiologicalTracer").removeClass("selectedA");
			$("#newDaughter").removeClass("selectedA");
			$("#newExternal").removeClass("selectedA");
			$("#newInVivo").addClass("selectedA");		
			break;
		}
	  });
	</script>
