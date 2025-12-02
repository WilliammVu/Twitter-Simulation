// API Base URL
const API_BASE = '/api';

// Current user data
let currentUser = null;
let profileUser = null;

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
const profileUsername = document.getElementById('profileUsername');
const profileTweetCount = document.getElementById('profileTweetCount');
const profileAvatar = document.getElementById('profileAvatar');
const profileName = document.getElementById('profileName');
const profileHandle = document.getElementById('profileHandle');
const followingCount = document.getElementById('followingCount');
const followersCount = document.getElementById('followersCount');
const followBtn = document.getElementById('followBtn');
const unfollowBtn = document.getElementById('unfollowBtn');
const profileTweets = document.getElementById('profileTweets');
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('searchInput');
const searchResults = document.getElementById('searchResults');

// Get username from URL
const urlParams = new URLSearchParams(window.location.search);
const username = urlParams.get('username');

if (!username) {
    window.location.href = 'index.html';
}

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

    // Load profile
    loadProfile();
}

// Load profile
async function loadProfile() {
    try {
        const response = await fetch(`${API_BASE}/users/${username}`);
        const data = await response.json();

        if (!data.success) {
            profileTweets.innerHTML = '<div class="empty-state"><h3>User not found</h3><p>This user does not exist.</p></div>';
            return;
        }

        profileUser = data.data;
        displayProfile(profileUser);
        loadUserTweets();
    } catch (error) {
        profileTweets.innerHTML = '<div class="empty-state"><h3>Error loading profile</h3><p>Please try again later.</p></div>';
    }
}

// Display profile
function displayProfile(user) {
    const avatar = user.username.charAt(0).toUpperCase();

    profileUsername.textContent = user.username;
    profileTweetCount.textContent = `${user.tweetsCount} Tweets`;
    profileAvatar.textContent = avatar;
    profileName.textContent = user.username;
    profileHandle.textContent = `@${user.username}`;
    followingCount.textContent = user.followingCount;
    followersCount.textContent = user.followersCount;

    // Show follow/unfollow button if not own profile
    if (user.username !== currentUser.username) {
        if (user.isFollowing) {
            followBtn.style.display = 'none';
            unfollowBtn.style.display = 'block';
        } else {
            followBtn.style.display = 'block';
            unfollowBtn.style.display = 'none';
        }

        // Show composer button only on own profile
        composeTweetBtn.style.display = 'none';
    }
}

// Load user tweets
async function loadUserTweets() {
    try {
        const response = await fetch(`${API_BASE}/users/${username}/tweets`);
        const data = await response.json();

        if (data.success) {
            displayTweets(data.data);
        } else {
            profileTweets.innerHTML = '<div class="empty-state"><h3>No tweets yet</h3><p>This user hasn\'t posted any tweets.</p></div>';
        }
    } catch (error) {
        profileTweets.innerHTML = '<div class="empty-state"><h3>Error loading tweets</h3><p>Please try again later.</p></div>';
    }
}

// Display tweets
function displayTweets(tweets) {
    if (tweets.length === 0) {
        profileTweets.innerHTML = '<div class="empty-state"><h3>No tweets yet</h3><p>This user hasn\'t posted any tweets.</p></div>';
        return;
    }

    profileTweets.innerHTML = tweets.map(tweet => createTweetElement(tweet)).join('');
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

// Follow button
followBtn.addEventListener('click', async () => {
    try {
        const response = await fetch(`${API_BASE}/users/${username}/follow`, {
            method: 'POST',
        });

        const data = await response.json();

        if (data.success) {
            followBtn.style.display = 'none';
            unfollowBtn.style.display = 'block';
            loadProfile();
        }
    } catch (error) {
        console.error('Error following user:', error);
    }
});

// Unfollow button
unfollowBtn.addEventListener('click', async () => {
    try {
        const response = await fetch(`${API_BASE}/users/${username}/follow`, {
            method: 'DELETE',
        });

        const data = await response.json();

        if (data.success) {
            unfollowBtn.style.display = 'none';
            followBtn.style.display = 'block';
            loadProfile();
        }
    } catch (error) {
        console.error('Error unfollowing user:', error);
    }
});

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
            loadUserTweets();
            loadProfile();
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
