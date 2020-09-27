<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:forEach var="item" items="${data}">
    <tr>
        <td>${item.dictType}</td>
        <td>${item.dictName}</td>
        <td>${item.dictValue}</td>
        <td>${item.dictDesc}</td>
        <td>${item.orderBy}</td>
        <td>${item.useable}
            <c:if test="${item.useable == 1}>">可用</c:if>
            <c:if test="${item.useable == 0}>">不可用</c:if>
        </td>
        <td>
            <button type="button" class="btn btn-default" onclick="dictionary_delete(${item.id})">删除</button>
            <button type="button" class="btn btn-default" onclick="dictionary_edit(${item.id})">修改</button>
        </td>
    </tr>
</c:forEach>
