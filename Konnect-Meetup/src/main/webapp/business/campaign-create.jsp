<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Campaign - Konnect</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
    <style>
        .campaign-form-container {
            background-color: var(--white);
            border-radius: 12px;
            box-shadow: var(--shadow);
            padding: 2.5rem;
            margin-bottom: 2rem;
        }

        .campaign-form-header {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #e0e0e0;
        }

        .campaign-form-header h3 {
            color: var(--primary-color);
            font-size: 1.8rem;
            font-weight: 600;
            margin-bottom: 0.5rem;
        }

        .campaign-form-header p {
            color: var(--secondary-color);
            font-size: 1.1rem;
        }

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 1.8rem;
        }

        .form-group {
            margin-bottom: 0.5rem;
        }

        .form-group.full-width {
            grid-column: span 2;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.6rem;
            font-weight: 500;
            color: var(--secondary-color);
            font-size: 1.05rem;
        }

        .form-group input,
        .form-group textarea,
        .form-group select {
            width: 100%;
            padding: 0.9rem 1.2rem;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            font-family: 'Poppins', sans-serif;
            font-size: 1rem;
            transition: all 0.3s;
            background-color: #f9f9f9;
        }

        .form-group input:focus,
        .form-group textarea:focus,
        .form-group select:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.2);
            background-color: #fff;
        }

        .form-hint {
            font-size: 0.85rem;
            color: #666;
            margin-top: 0.5rem;
        }

        .interest-checkbox-group {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            gap: 1rem;
            margin-top: 0.5rem;
            margin-bottom: 0.5rem;
            background-color: #f9f9f9;
            padding: 1.2rem;
            border-radius: 8px;
            border: 1px solid #e0e0e0;
        }

        .interest-checkbox {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            cursor: pointer;
            padding: 0.5rem;
            border-radius: 6px;
            transition: all 0.2s;
        }

        .interest-checkbox:hover {
            background-color: rgba(74, 144, 226, 0.1);
        }

        .interest-checkbox input[type="checkbox"] {
            width: 18px;
            height: 18px;
            accent-color: var(--primary-color);
        }

        .checkbox-label {
            font-weight: 500;
        }

        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 1rem;
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 1px solid #e0e0e0;
        }

        .form-actions .btn {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.9rem 1.8rem;
            font-weight: 600;
            border-radius: 8px;
            transition: all 0.3s;
        }

        .form-actions .btn-primary {
            background-color: var(--primary-color);
            color: var(--white);
            border: none;
        }

        .form-actions .btn-secondary {
            background-color: #f0f0f0;
            color: var(--secondary-color);
            border: 1px solid #e0e0e0;
        }

        .form-actions .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .form-section-divider {
            grid-column: span 2;
            margin: 1rem 0;
            padding-top: 1rem;
            border-top: 1px solid #e0e0e0;
        }

        .form-section-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: var(--secondary-color);
            margin-bottom: 1.2rem;
        }

        .input-with-icon {
            position: relative;
        }

        .input-with-icon svg {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--primary-color);
        }

        .input-with-icon input {
            padding-left: 2.8rem;
        }

        @media (max-width: 768px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .form-group.full-width {
                grid-column: span 1;
            }

            .campaign-form-container {
                padding: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <header>
        <div class="header-row">
            <a href="<%= request.getContextPath() %>/" class="header-logo">Konnect</a>
            <nav>
                <ul class="nav-list">
                    <li><a href="<%= request.getContextPath() %>/business/dashboard">Dashboard</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/profile">Profile</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/campaigns" class="active">My Campaigns</a></li>
                    <li><a href="<%= request.getContextPath() %>/business/creators">Browse Creators</a></li>
                    <li><a href="<%= request.getContextPath() %>/messages">Messages</a></li>
                    <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
        <div class="header-subtitle">Create Campaign</div>
    </header>

    <main>
        <section class="about-section">
            <div class="about-card">
                <div class="section-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
                    <div>
                        <h2>Create a New Campaign</h2>
                        <p>Connect with creators and launch your marketing campaign</p>
                    </div>
                    <a href="<%= request.getContextPath() %>/business/campaigns" class="btn btn-secondary" style="display: inline-flex; align-items: center; gap: 0.5rem; margin: 0;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"></line><polyline points="12 19 5 12 12 5"></polyline></svg>
                        Back to Campaigns
                    </a>
                </div>

                <% if(request.getAttribute("error") != null) { %>
                    <div class="error-message">
                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <div class="campaign-form-container">
                    <div class="campaign-form-header">
                        <h3>Campaign Details</h3>
                        <p>Fill out the information below to create your campaign</p>
                    </div>

                    <form action="<%= request.getContextPath() %>/business/campaign-create" method="post">
                        <div class="form-grid">
                            <div class="form-group full-width">
                                <label for="title">Campaign Title</label>
                                <div class="input-with-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                                    <input type="text" id="title" name="title" placeholder="Enter a descriptive title" required>
                                </div>
                                <p class="form-hint">A clear, concise title for your campaign.</p>
                            </div>

                            <div class="form-group full-width">
                                <label for="description">Campaign Description</label>
                                <textarea id="description" name="description" rows="4" placeholder="Describe what you're looking for in this campaign" required></textarea>
                                <p class="form-hint">Detailed description of your campaign, including what you're looking for.</p>
                            </div>

                            <div class="form-section-divider">
                                <h4 class="form-section-title">Campaign Parameters</h4>
                            </div>

                            <div class="form-group">
                                <label for="budget">Budget ($)</label>
                                <div class="input-with-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"></line><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path></svg>
                                    <input type="number" id="budget" name="budget" min="0" step="0.01" placeholder="Enter campaign budget" required>
                                </div>
                                <p class="form-hint">Your budget for this campaign.</p>
                            </div>

                            <div class="form-group">
                                <label for="minFollowers">Minimum Followers</label>
                                <div class="input-with-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87"></path><path d="M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
                                    <input type="number" id="minFollowers" name="minFollowers" min="0" placeholder="Optional minimum followers">
                                </div>
                                <p class="form-hint">Minimum followers required (optional).</p>
                            </div>

                            <div class="form-group">
                                <label for="startDate">Start Date</label>
                                <div class="input-with-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                                    <input type="date" id="startDate" name="startDate" required>
                                </div>
                                <p class="form-hint">When the campaign will begin.</p>
                            </div>

                            <div class="form-group">
                                <label for="endDate">End Date</label>
                                <div class="input-with-icon">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                                    <input type="date" id="endDate" name="endDate" required>
                                </div>
                                <p class="form-hint">When the campaign will end.</p>
                            </div>

                            <div class="form-section-divider">
                                <h4 class="form-section-title">Additional Information</h4>
                            </div>

                            <div class="form-group full-width">
                                <label for="requirements">Requirements</label>
                                <textarea id="requirements" name="requirements" rows="3" placeholder="Any specific requirements for creators (optional)"></textarea>
                                <p class="form-hint">Specific requirements for creators (optional).</p>
                            </div>

                            <div class="form-group full-width">
                                <label>Target Interests</label>
                                <div class="interest-checkbox-group">
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Technology">
                                        <span class="checkbox-label">Technology</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Beauty">
                                        <span class="checkbox-label">Beauty</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Fashion">
                                        <span class="checkbox-label">Fashion</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Fitness">
                                        <span class="checkbox-label">Fitness</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Food">
                                        <span class="checkbox-label">Food</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Travel">
                                        <span class="checkbox-label">Travel</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Gaming">
                                        <span class="checkbox-label">Gaming</span>
                                    </label>
                                    <label class="interest-checkbox">
                                        <input type="checkbox" name="targetInterests" value="Lifestyle">
                                        <span class="checkbox-label">Lifestyle</span>
                                    </label>
                                </div>
                                <p class="form-hint">Select categories that match your target audience.</p>
                            </div>
                        </div>

                        <div class="form-actions">
                            <a href="<%= request.getContextPath() %>/business/campaigns" class="btn btn-secondary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
                                Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path><polyline points="17 21 17 13 7 13 7 21"></polyline><polyline points="7 3 7 8 15 8"></polyline></svg>
                                Create Campaign
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </main>

    <footer>
        <p>&copy; 2025 Konnect. All rights reserved. <span class="admin-footer-note">Business Portal</span></p>
    </footer>
</body>
</html>
