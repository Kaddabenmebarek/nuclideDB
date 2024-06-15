$(function () {
    //alert($(location).attr('pathname'));
    var highlighted;
    var zeUri = $(location).attr('pathname');
    if(zeUri == "/nuclideDB/newTracer" || zeUri == "/nuclideDB/addNewTracer" || zeUri == "/nuclideDB/insertTracer" || zeUri.startsWith("/nuclideDB/attachFile")){
        highlighted = "newTracerMenu"
    }
    if(zeUri == "/nuclideDB/newWaste" || zeUri == "/nuclideDB/addNewWaste" || zeUri == "/nuclideDB/insertWaste" ){
        highlighted = "newWasteMenu"
    }
    if(zeUri == "/nuclideDB/newUsage" || zeUri == "/nuclideDB/newBiologicalTracer" || zeUri == "/nuclideDB/newDaughter" || zeUri == "/nuclideDB/newExternal" || zeUri == "/nuclideDB/newInVivo" || zeUri == "/nuclideDB/newUsageMessage"){
        highlighted = "newUsageMenu"
    }
    if(zeUri == "/nuclideDB/showTracers" || zeUri == "/nuclideDB/listTracers" || zeUri.startsWith("/nuclideDB/tracerDetail_") || zeUri.startsWith("/nuclideDB/discardTracer") || zeUri.startsWith("/nuclideDB/relocateTracer") || zeUri.startsWith("/nuclideDB/listAttachedFiles") || zeUri ==  "/nuclideDB/updateTracerLocation"){
        highlighted = "tracersMenu"
    }
    if(zeUri == "/nuclideDB/showWaste" || zeUri  == "/nuclideDB/listWaste" || zeUri.startsWith("/nuclideDB/wasteDetail_") || zeUri.startsWith("/nuclideDB/relocateWaste") || zeUri.startsWith("/nuclideDB/updateWasteLocation") || zeUri.startsWith("/nuclideDB/closeWaste") || zeUri.startsWith("/nuclideDB/disposeWaste") || zeUri.startsWith("/nuclideDB/disposeWasteSuccess") || zeUri ==  "/nuclideDB/updateWasteLocation"){
        highlighted = "wastesMenu"
    }
    if(zeUri == "/nuclideDB/showUsers" || zeUri == "/nuclideDB/listUsers" || zeUri == "/nuclideDB/newUserMessage" || zeUri.startsWith("nuclideDB/showAddUser") || zeUri.startsWith("nuclideDB/showEditUser") || zeUri == "/nuclideDB/editUser"){
        highlighted = "usersMenu"
    }
    if(zeUri == "/nuclideDB/listLabs" || zeUri.startsWith("/nuclideDB/labDetail_")){
        highlighted = "labsMenu"
    }
    switch (highlighted) {
        case "newTracerMenu":
            $("#newTracer").addClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "newWasteMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").addClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "newUsageMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").addClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "tracersMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").addClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "wastesMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").addClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "usersMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").addClass("selected");
            $("#listLabs").removeClass("selected");
            break;
        case "labsMenu":
            $("#newTracer").removeClass("selected");
            $("#newWaste").removeClass("selected");
            $("#newUsage").removeClass("selected");
            $("#showTracers").removeClass("selected");
            $("#showWaste").removeClass("selected");
            $("#showUsers").removeClass("selected");
            $("#listLabs").addClass("selected");
            break;
        default:
            break;
    }

    /*Workaround for Boostrap menu toggler with incompatible jquery*/
    $(".navbar-toggler").click(function () {
        //let isCollapsed = $(this).hasClass("collapsed");
        //let target = $(this).data("bs-target"); //data is not working for this version of jquery
        let target = $($(this).attr("data-bs-target"));
        //target.toggleClass("show", isCollapsed);
        target.toggleClass("show");
    });
});