<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.konnect.model.User" %>
<%@ page import="com.konnect.model.Message" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<jsp:include page="/common/header.jsp">
    <jsp:param name="title" value="Chat"/>
</jsp:include>

<h2>Chat</h2>

<%
    User currentUser = (User) session.getAttribute("user");
    List<User> chatPartners = (List<User>) request.getAttribute("chatPartners");
    List<Message> conversation = (List<Message>) request.getAttribute("conversation");
    User currentPartner = (User) request.getAttribute("currentPartner"); // The user being chatted with
%>

<div class="chat-container">
    <%-- Sidebar with conversation partners --%>
    <div class="chat-sidebar">
        <div class="chat-sidebar-header">Conversations</div>
        <ul class="chat-partner-list">
            <% if (chatPartners == null || chatPartners.isEmpty()) { %>
                <li class="p-3 text-muted">No conversations yet.</li>
            <% } else { %>
                <% for (User partner : chatPartners) { %>
                    <li>
                        <a href="<%= request.getContextPath() %>/creator/chat?with=<%= partner.getUserId() %>"
                           class="<%= (currentPartner != null && currentPartner.getUserId() == partner.getUserId()) ? "active" : "" %>">
                            <%= partner.getUsername() %>
                        </a>
                    </li>
                <% } %>
            <% } %>
        </ul>
    </div>

    <%-- Main chat area --%>
    <div class="chat-main">
        <% if (currentPartner != null) { %>
            <div class="chat-header">
                Chatting with: <%= currentPartner.getUsername() %>
            </div>
            <div class="chat-messages">
                <% if (conversation == null || conversation.isEmpty()) { %>
                <p class="text-muted text-center mt-3">No messages in this conversation yet. Start chatting!</p>
                <% } else { %>
                     <% DateTimeFormatter msgFormatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm"); %>
                    <% for (Message msg : conversation) { %>
                        <% boolean isSent = msg.getSenderUserId() == currentUser.getUserId(); %>
                        <div class="message <%= isSent ? "sent" : "received" %>">
                            <%= msg.getContent() %>
                            <div class="message-meta">
                                <%= msg.getSentAt().toLocalDateTime().format(msgFormatter) %>
                            </div>
                        </div>
                    <% } %>
                <% } %>
            </div>
            <div class="chat-input">
                <form action="<%= request.getContextPath() %>/creator/chat" method="POST" style="display: flex; width: 100%;">
                    <input type="hidden" name="receiverId" value="<%= currentPartner.getUserId() %>">
                    <textarea name="content" placeholder="Type your message..." required rows="1"></textarea>
                    <button type="submit" class="btn">Send</button>
                </form>
            </div>
        <% } else { %>
            <%-- Display when no chat partner is selected --%>
            <div class="chat-header">Select a Conversation</div>
            <div class="chat-messages no-chat-selected">
                <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                </svg>
                <p>Select a business from the list on the left to start chatting.</p>
            </div>
            <div class="chat-input">
                <textarea placeholder="Select a conversation first..." disabled rows="1"></textarea>
                <button type="submit" class="btn" disabled>Send</button>
            </div>
        <% } %>
    </div>
</div>

<jsp:include page="/common/footer.jsp" />
