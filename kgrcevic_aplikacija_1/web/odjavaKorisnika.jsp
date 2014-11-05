<%-- 
    Document   : index
    Created on : Jun 12, 2013, 1:37:11 PM
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Prijava u korisničke stranice</title>        
            <link rel="stylesheet" type="text/css" href="../css/displaytag.css">
                <link href="${pageContext.servletContext.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
                <style>
                    a:link {color:#78D2D2;}    /* unvisited link */
                    a:visited {color:#78D2D2;} /* visited link */
                    a:hover {color:#78D2D2;}   /* mouse over link */
                    a:active {color:#0000FF;}  /* selected link */
                </style>
                </head>
                <body style="background-color:#78D2D2">
                    <div class="container" style=" background-color:#8686BA; -webkit-border-radius: 36px; margin-top: 10%; margin-bottom: 10px; padding: 30px; width: 400px; font-size: 20px;  ">
                        <center>
                           Uspješno ste odjavljeni!<br/><br/>
                            <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">Prijavi se ponovno</a> <br/>
                                
                        </center>
                    </div>
                </body>
                </html>
