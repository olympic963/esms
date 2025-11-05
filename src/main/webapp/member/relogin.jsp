<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Session expired - Please re-login</title>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <style>
        html, body { height: 100%; margin: 0; }
        body {
            background: rgba(0,0,0,0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .modal {
            background: #fff;
            border-radius: 12px;
            max-width: 560px;
            width: 92%;
            padding: 24px;
            text-align: center;
            box-shadow: 0 20px 40px rgba(0,0,0,0.25);
        }
        .title { font-size: 1.4rem; margin-bottom: 8px; color: #222; }
        .subtitle { color: #555; margin-bottom: 14px; }
        .countdown { font-weight: 700; color: #5a67d8; }
        .note { font-size: 0.9rem; color: #666; margin-top: 6px; }
        .spinner {
            margin: 12px auto 0; width: 40px; height: 40px;
            border: 4px solid #e9ecef; border-top-color: #5a67d8; border-radius: 50%;
            animation: spin 0.9s linear infinite;
        }
        @keyframes spin { to { transform: rotate(360deg); } }
    </style>
    <script>
        (function(){
            // Prevent back navigation
            history.pushState(null, document.title, location.href);
            window.addEventListener('popstate', function () {
                history.pushState(null, document.title, location.href);
            });
            // Countdown and redirect
            var seconds = Number('<c:out value="${reloginSeconds}" default="5"/>') || 5;
            function tick(){
                var el = document.getElementById('seconds');
                if (el) el.textContent = seconds;
                seconds--;
                if (seconds < 0) {
                    window.location.replace('${pageContext.request.contextPath}/auth/showLogin');
                } else {
                    setTimeout(tick, 1000);
                }
            }
            window.addEventListener('load', tick);
        })();
    </script>
</head>
<body>
    <div class="modal" role="alertdialog" aria-modal="true">
        <div class="title">Security notice</div>
        <div class="subtitle">
            <c:out value='${reloginMessage}' default='Your session has changed. For security reasons, please sign in again.'/>
        </div>
        <div>Redirecting to the login page in <span id="seconds" class="countdown"></span> seconds…</div>
        <div class="note">This dialog cannot be closed. Please wait.</div>
        <div class="spinner" aria-hidden="true"></div>
    </div>
</body>
</html> 