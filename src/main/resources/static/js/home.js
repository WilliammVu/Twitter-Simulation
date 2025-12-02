// API Base URL
const API_BASE = '/api';

// Current user data
let currentUser = null;

// DOM Elements
const sidebarUsername = document.getElementById('sidebarUsername');
const sidebarAvatar = document.getElementById('sidebarAvatar');
const profileLink = document.getElementById('profileLink');
const logoutBtn = document.getElementById('logoutBtn');
const composeTweetBtn = document.getElementById('composeTweetBtn');
const tweetComposer = document.getElementById('tweetComposer');
const closeComposer = document.getElementById('closeComposer');
const composerForm = document.getElementById('composerForm');
const tweetBody = document.getElementById('tweetBody');
const charCount = document.getElementById('charCount');
const feed = document.getElementById('feed');
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('searchInput');
const searchResults = document.getElementById('searchResults');
const suggestions = document.getElementById('suggestions');

// Check authentication
async function checkAuth() {
    try {
        const response = await fetch(`${API_BASE}/auth/me`);
        const data = await response.json();

        if (!data.success) {
            window.location.href = 'index.html';
            return;
        }

        currentUser = data.data;
        sessionStorage.setItem('currentUser', JSON.stringify(currentUser));
        initializePage();
    } catch (error) {
        window.location.href = 'index.html';
    }
}

// Initialize page
function initializePage() {
    // Set user info
    sidebarUsername.textContent = `@${currentUser.username}`;
    sidebarAvatar.textContent = currentUser.username.charAt(0).toUpperCase();
    profileLink.href = `profile.html?username=${currentUser.username}`;

    // Load feed and suggestions
    loadFeed();
    loadSuggestions();
}

// Load feed
async function loadFeed() {
    try {
        const response = await fetch(`${API_BASE}/tweets/feed`);
        const data = await response.json();

        if (data.success) {
            displayFeed(data.data);
        } else {
            feed.innerHTML = '<div class="empty-state"><h3>Welcome to Twitter!</h3><p>Follow users to see their tweets in your feed.</p></div>';
        }
    } catch (error) {
        feed.innerHTML = '<div class="empty-state"><h3>Error loading feed</h3><p>Please try again later.</p></div>';
    }
}

// Display feed
function displayFeed(tweets) {
    if (tweets.length === 0) {
        feed.innerHTML = '<div class="empty-state"><h3>Your feed is empty</h3><p>Follow users to see their tweets here.</p></div>';
        return;
    }

    feed.innerHTML = tweets.map(tweet => createTweetElement(tweet)).join('');
    attachTweetListeners();
}

// Create tweet element
function createTweetElement(tweet) {
    const timeAgo = getTimeAgo(new Date(tweet.date));
    const avatar = tweet.user.username.charAt(0).toUpperCase();

    return `
        <div class="tweet" data-tweet-id="${tweet.id}">
            <div class="tweet-header">
                <div class="tweet-avatar">${avatar}</div>
                <div class="tweet-content">
                    <div class="tweet-user">
                        <a href="profile.html?username=${tweet.user.username}" class="tweet-username">${tweet.user.username}</a>
                        <span class="tweet-time">· ${timeAgo}</span>
                    </div>
                    <div class="tweet-body">${escapeHtml(tweet.body)}</div>
                    <div class="tweet-actions">
                        <button class="tweet-action like-btn ${tweet.liked ? 'liked' : ''}" data-tweet-id="${tweet.id}">
                            <svg viewBox="0 0 24 24"><path d="M16.697 5.5c-1.222-.06-2.679.51-3.89 2.16l-.805 1.09-.806-1.09C9.984 6.01 8.526 5.44 7.304 5.5c-1.243.07-2.349.78-2.91 1.91-.552 1.12-.633 2.78.479 4.82 1.074 1.97 3.257 4.27 7.129 6.61 3.87-2.34 6.052-4.64 7.126-6.61 1.111-2.04 1.03-3.7.477-4.82-.561-1.13-1.666-1.84-2.908-1.91zm4.187 7.69c-1.351 2.48-4.001 5.12-8.379 7.67l-.503.3-.504-.3c-4.379-2.55-7.029-5.19-8.382-7.67-1.36-2.5-1.41-4.86-.514-6.67.887-1.79 2.647-2.91 4.601-3.01 1.651-.09 3.368.56 4.798 2.01 1.429-1.45 3.146-2.1 4.796-2.01 1.954.1 3.714 1.22 4.601 3.01.896 1.81.846 4.17-.514 6.67z"></path></svg>
                            <span>${tweet.likeCount}</span>
                        </button>
                        <button class="tweet-action retweet-btn ${tweet.retweeted ? 'retweeted' : ''}" data-tweet-id="${tweet.id}">
                            <svg viewBox="0 0 24 24"><path d="M4.5 3.88l4.432 4.14-1.364 1.46L5.5 7.55V16c0 1.1.896 2 2 2H13v2H7.5c-2.209 0-4-1.79-4-4V7.55L1.432 9.48.068 8.02 4.5 3.88zM16.5 6H11V4h5.5c2.209 0 4 1.79 4 4v8.45l2.068-1.93 1.364 1.46-4.432 4.14-4.432-4.14 1.364-1.46 2.068 1.93V8c0-1.1-.896-2-2-2z"></path></svg>
                            <span></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Attach tweet listeners
function attachTweetListeners() {
    // Like buttons
    document.querySelectorAll('.like-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            const tweetId = btn.dataset.tweetId;
            const isLiked = btn.classList.contains('liked');

            try {
                const response = await fetch(`${API_BASE}/tweets/${tweetId}/like`, {
                    method: isLiked ? 'DELETE' : 'POST',
                });

                const data = await response.json();

                if (data.success) {
                    btn.classList.toggle('liked');
                    const countSpan = btn.querySelector('span');
                    countSpan.textContent = data.data.likeCount;
                }
            } catch (error) {
                console.error('Error liking tweet:', error);
            }
        });
    });

    // Retweet buttons
    document.querySelectorAll('.retweet-btn').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.stopPropagation();
            const tweetId = btn.dataset.tweetId;
            const isRetweeted = btn.classList.contains('retweeted');

            try {
                const response = await fetch(`${API_BASE}/tweets/${tweetId}/retweet`, {
                    method: isRetweeted ? 'DELETE' : 'POST',
                });

                const data = await response.json();

                if (data.success) {
                    btn.classList.toggle('retweeted');
                }
            } catch (error) {
                console.error('Error retweeting:', error);
            }
        });
    });
}

// Load suggestions
async function loadSuggestions() {
    try {
        const response = await fetch(`${API_BASE}/users/suggested`);
        const data = await response.json();

        if (data.success && data.data.length > 0) {
            displaySuggestions(data.data);
        } else {
            suggestions.innerHTML = '<div class="empty-state" style="padding: 16px;"><p>No suggestions available</p></div>';
        }
    } catch (error) {
        suggestions.innerHTML = '<div class="empty-state" style="padding: 16px;"><p>Error loading suggestions</p></div>';
    }
}

// Display suggestions
function displaySuggestions(users) {
    suggestions.innerHTML = users.map(user => {
        const avatar = user.username.charAt(0).toUpperCase();
        return `
            <div class="user-card">
                <div class="user-card-avatar">${avatar}</div>
                <div class="user-card-info">
                    <div class="user-card-name">${user.username}</div>
                    <div class="user-card-username">@${user.username}</div>
                </div>
                <button class="btn-primary btn-sm follow-btn" data-username="${user.username}">Follow</button>
            </div>
        `;
    }).join('');

    // Attach follow listeners
    document.querySelectorAll('.follow-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const username = btn.dataset.username;
            await followUser(username);
            loadSuggestions();
            loadFeed();
        });
    });
}

// Follow user
async function followUser(username) {
    try {
        const response = await fetch(`${API_BASE}/users/${username}/follow`, {
            method: 'POST',
        });

        const data = await response.json();
        return data.success;
    } catch (error) {
        console.error('Error following user:', error);
        return false;
    }
}

// Tweet composer
composeTweetBtn.addEventListener('click', () => {
    tweetComposer.style.display = 'block';
    tweetBody.focus();
});

closeComposer.addEventListener('click', () => {
    tweetComposer.style.display = 'none';
    tweetBody.value = '';
    charCount.textContent = '0 / 280';
    charCount.classList.remove('warning', 'error');
});

tweetBody.addEventListener('input', () => {
    const length = tweetBody.value.length;
    charCount.textContent = `${length} / 280`;

    charCount.classList.remove('warning', 'error');
    if (length > 260) {
        charCount.classList.add('warning');
    }
    if (length > 280) {
        charCount.classList.add('error');
    }
});

composerForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const body = tweetBody.value.trim();

    if (!body || body.length > 280) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/tweets`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ body }),
        });

        const data = await response.json();

        if (data.success) {
            tweetComposer.style.display = 'none';
            tweetBody.value = '';
            charCount.textContent = '0 / 280';
            charCount.classList.remove('warning', 'error');
            loadFeed();
        }
    } catch (error) {
        console.error('Error posting tweet:', error);
    }
});

// Search
searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const query = searchInput.value.trim();

    if (!query) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/users/search?query=${encodeURIComponent(query)}`);
        const data = await response.json();

        if (data.success) {
            displaySearchResults([data.data]);
        } else {
            displaySearchResults([]);
        }
    } catch (error) {
        displaySearchResults([]);
    }
});

searchInput.addEventListener('input', () => {
    if (searchInput.value.trim() === '') {
        searchResults.classList.remove('show');
    }
});

// Display search results
function displaySearchResults(users) {
    if (users.length === 0) {
        searchResults.innerHTML = '<div style="padding: 16px; text-align: center; color: var(--text-secondary);">No users found</div>';
    } else {
        searchResults.innerHTML = users.map(user => {
            const avatar = user.username.charAt(0).toUpperCase();
            return `
                <a href="profile.html?username=${user.username}" class="user-card">
                    <div class="user-card-avatar">${avatar}</div>
                    <div class="user-card-info">
                        <div class="user-card-name">${user.username}</div>
                        <div class="user-card-username">@${user.username}</div>
                    </div>
                </a>
            `;
        }).join('');
    }

    searchResults.classList.add('show');
}

// Logout
logoutBtn.addEventListener('click', async () => {
    try {
        await fetch(`${API_BASE}/auth/logout`, { method: 'POST' });
        sessionStorage.removeItem('currentUser');
        window.location.href = 'index.html';
    } catch (error) {
        window.location.href = 'index.html';
    }
});

// Utility functions
function getTimeAgo(date) {
    const seconds = Math.floor((new Date() - date) / 1000);

    if (seconds < 60) return `${seconds}s`;
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}h`;
    return `${Math.floor(seconds / 86400)}d`;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Initialize
checkAuth();
