
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

    const i18n = [];

    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus","common.confirm"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>

    function i18nInit(context) {
        if (context = "user") {
            i18n["addTitle"] = '<spring:message code="user.add"/>';
            i18n["editTitle"] = '<spring:message code="user.edit"/>';
        }

        if (context = "meal") {
            i18n["addTitle"] = '<spring:message code="meal.add"/>';
            i18n["editTitle"] = '<spring:message code="meal.edit"/>';
        }
    }

</script>
