<%-- 
    Document   : pregledMeteoPodataka
    Created on : Jun 12, 2013, 1:14:32 PM
    Author     : Kruno
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<?xml version='1.0' encoding='UTF-8' ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <style>
            a:link {color:#78D2D2;}    /* unvisited link */
            a:visited {color:#78D2D2;} /* visited link */
            a:hover {color:#78D2D2;}   /* mouse over link */
            a:active {color:#0000FF;}  /* selected link */
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>kgrcevic_aplikacija_1:Pregled prikupljenih meteo podataka</title>        
            <link rel="stylesheet" type="text/css" href="../css/displaytag.css">
                <link href="${pageContext.servletContext.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
                </head>
                <body style="background-color:#78D2D2">
                    <div class="container" style="background-color:#8686BA; -webkit-border-radius: 36px; margin-top: 10px; margin-bottom: 10px; padding: 30px; ">
                        <center>
                            <form method="GET" name="meteo" >
                                <div class="row-fluid">
                                    <table style="width: 750px;">
                                        <th colspan="2" style="text-decoration:underline">Pregled prikupljenih meteo podataka</th>
                                        <br/>
                                        <tr><td>Vrsta pretraživanja</td>
                                            <td>

                                                <br/>
                                                <input type="radio" id="radio1" name="vrsta" value="1">
                                                    <label for="radio1" style="opacity:0.5">Zip kod</label>


                                                    <input type="radio" id="radio2" name="vrsta" value="2">
                                                        <label for="radio2" style="opacity:0.5">Vremenski interval</label>

                                                        <input type="radio" id="radio3" name="vrsta" value="3">
                                                            <label for="radio3" style="opacity:0.5">Svi prikupljeni podaci</label> 
                                                            <br/>
                                                            <br/>

                                                            </td>
                                                            </tr>
                                                            <tr>
                                                                <td>Zip kod</td>
                                                                <td><input class="input-block-level" placeholder="Unos zip koda" type="text" name="zip" size="5" maxlength="5" value="<c:out value="${param.zip}"/>"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Datum od </td>
                                                                <td><input class="input-block-level" placeholder="dd.MM.yyyy hh.mm.ss" type="text" name="datumOD" value="<c:out value="${param.datumOD}"/>"/></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Datum do </td>
                                                                <td><input  class="input-block-level" placeholder="dd.MM.yyyy hh.mm.ss" type="text" name="datumDO" value="<c:out value="${param.datumDO}"/>"/></td>
                                                            </tr>
                                                            <tr><td>Broj podataka po stranici </td><td>
                                                                    <select name="redaka">
                                                                        <option value="5">5</option>
                                                                        <option value="10">10</option>
                                                                        <option value="20">20</option>
                                                                        <option value="50">50</option>
                                                                        <option value="100">100</option>
                                                                        <option value="0">Svi</option>
                                                                    </select>
                                                                </td></tr>
                                                            <tr><td colspan="2"><center><input type="submit" value="Prikazi" class="btn btn-large btn-block" style="opacity:0.7"/></center></td></tr>
                                                            </table>
                                                            </div>
                                                            </form>

                                                            <a href="${pageContext.servletContext.contextPath}/zasticeno/index.jsp">Idi na Početnu stranica</a><br/>
                                                            <br/>
                                                            <sql:setDataSource var="con"
                                                                               driver="${applicationScope.BP_Konfiguracija.driverDatabase}"
                                                                               url="${applicationScope.BP_Konfiguracija.serverDB}${applicationScope.BP_Konfiguracija.userDatabase}"
                                                                               user="${applicationScope.BP_Konfiguracija.userUsername}"
                                                                               password="${applicationScope.BP_Konfiguracija.userPassword}"/>

                                                            <c:if test="${pageContext.request.method=='GET'}">
                                                                <c:if test="${param.zip!=null}">
                                                                    <c:choose>
                                                                        <c:when test="${param.vrsta=='1'}">
                                                                            <sql:query dataSource="${con}" var="meteo">SELECT * from kgrcevic_meteopodaci where zipzahtjevani='<c:out value="${param.zip}"/>' ORDER BY vrijeme_preuzimanja ASC</sql:query>
                                                                        </c:when>
                                                                        <c:when test="${param.vrsta=='2'}">
                                                                            <sql:query dataSource="${con}" var="meteo">SELECT * from kgrcevic_meteopodaci where vrijeme_preuzimanja BETWEEN concat(substring('<c:out value="${param.datumOD}"/>',1,13),':',substring('<c:out value="${param.datumOD}"/>',15,2),':',substring('<c:out value="${param.datumOD}"/>',18,2)) AND concat(substring('<c:out value="${param.datumDO}"/>',1,13),':',substring('<c:out value="${param.datumDO}"/>',15,2),':',substring('<c:out value="${param.datumDO}"/>',18,2)) ORDER BY vrijeme_preuzimanja ASC</sql:query>
                                                                        </c:when>
                                                                        <c:when test="${param.vrsta=='3'}">
                                                                            <sql:query dataSource="${con}" var="meteo">SELECT * from kgrcevic_meteopodaci ORDER BY vrijeme_preuzimanja ASC</sql:query>
                                                                        </c:when>
                                                                    </c:choose>

                                                                    <display:table name="${meteo.rows}" pagesize="${param.redaka}" style="border:1px solid #666; text-align:center" >   
                                                                        <display:column property="zipzahtjevani" title="ZIP (zahtjevani)" style="border:1px solid #666"/>
                                                                        <display:column property="zipvraceni" title="ZIP (vraceni)" style="border:1px solid #666"/>
                                                                        <display:column property="grad" title="Grad" style="border:1px solid #666"/>
                                                                        <display:column property="temperatura" title="Temperatura" style="border:1px solid #666; width:130px"/>
                                                                        <display:column property="vlaga" title="Vlaga" style="border:1px solid #666; width:70px" />
                                                                        <display:column property="tlak" title="Tlak" style="border:1px solid #666"/>
                                                                        <display:column property="vjetar_brzina" title="Brzina vjetra" style="border:1px solid #666; width:150px"/>
                                                                        <display:column property="vjetar_smjer" title="Smjer vjetra" style="border:1px solid #666; width:150px"/>
                                                                        <display:column property="kisa_stopa" title="Stopa kiše(mm)" style="border:1px solid #666; width:150px"/>
                                                                        <display:column property="vremenska_zona" title="Vremenska zona" style="border:1px solid #666; width:150px"/>
                                                                        <display:column property="vrijeme_preuzimanja" title="Vrijeme preuzimanja podataka" style="border:1px solid #666; width:250px"/>
                                                                    </display:table>       
                                                                </c:if>     
                                                            </c:if>

                                                            </center>
                                                            </div>
                                                            </body>
                                                            </html>
