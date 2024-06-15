<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:if test="${not empty sessionScope.userPicture}">
    <style>
        #user-initials{
            background-image: url(${userPicture});
            color: transparent;
            height: 34px;
            width: 34px;
        }
    </style>
</c:if>

    <%--<input id="user-initials" type="submit" value="?" title="Unknown user"/>--%>

<c:if test="${not empty sessionScope.user}" >

    <jsp:useBean id="user" scope="session" type="com.actelion.research.security.entity.User"/>

    <a id="user-div" class="nav-link btn-icon">
    <input id="user-initials" type="submit" value="${user.getInitials()}"
           class='${user.superAdmin ? "super-admin": ""}'
           data-bs-toggle="tooltip" data-bs-placement="bottom" title="Show user info"
           onclick="jQuery('#user-info').fadeToggle(fadeTimeout)" />
    </a>

    <div id="user-info" class="hide-on-click-outside border-rounded" style="display: none">
        <div id="fullname">${user.getFullName()}</div>
        <div id="username">${user.username}</div>

        <c:if test="${user.superAdmin}">
            <div class="super-admin">(SuperAdmin)</div>
        </c:if>

        <div id="email">${user.email}</div>
        <div id="logout-div">
            <a href="logout">
                <i class="fas fa-sign-out-alt"></i>
                Logout
            </a>
        </div>
    </div>
</c:if>