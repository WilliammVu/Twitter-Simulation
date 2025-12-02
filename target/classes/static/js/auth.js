// API Base URL
const API_BASE = '/api';

// DOM Elements
const loginForm = document.getElementById('loginForm');
const signupForm = document.getElementById('signupForm');
const loginFormElement = document.getElementById('loginFormElement');
const signupFormElement = document.getElementById('signupFormElement');
const showSignupLink = document.getElementById('showSignup');
const showLoginLink = document.getElementById('showLogin');
const loginError = document.getElementById('loginError');
const signupError = document.getElementById('signupError');

// Switch between login and signup forms
showSignupLink.addEventListener('click', (e) => {
    e.preventDefault();
    loginForm.style.display = 'none';
    signupForm.style.display = 'block';
    loginError.classList.remove('show');
    loginFormElement.reset();
});

showLoginLink.addEventListener('click', (e) => {
    e.preventDefault();
    signupForm.style.display = 'none';
    loginForm.style.display = 'block';
    signupError.classList.remove('show');
    signupFormElement.reset();
});

// Handle login
loginFormElement.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        const data = await response.json();

        if (data.success) {
            // Store user data in sessionStorage
            sessionStorage.setItem('currentUser', JSON.stringify(data.data));
            // Redirect to home
            window.location.href = 'home.html';
        } else {
            showError(loginError, data.message);
        }
    } catch (error) {
        showError(loginError, 'An error occurred. Please try again.');
    }
});

// Handle signup
signupFormElement.addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('signupUsername').value;
    const password = document.getElementById('signupPassword').value;

    try {
        const response = await fetch(`${API_BASE}/auth/signup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        const data = await response.json();

        if (data.success) {
            // Store user data in sessionStorage
            sessionStorage.setItem('currentUser', JSON.stringify(data.data));
            // Redirect to home
            window.location.href = 'home.html';
        } else {
            showError(signupError, data.message);
        }
    } catch (error) {
        showError(signupError, 'An error occurred. Please try again.');
    }
});

// Show error message
function showError(element, message) {
    element.textContent = message;
    element.classList.add('show');
}

// Check if already logged in
async function checkAuth() {
    try {
        const response = await fetch(`${API_BASE}/auth/me`);
        const data = await response.json();

        if (data.success) {
            // User is already logged in, redirect to home
            sessionStorage.setItem('currentUser', JSON.stringify(data.data));
            window.location.href = 'home.html';
        }
    } catch (error) {
        // User is not logged in, stay on login page
    }
}

// Check auth on page load
checkAuth();
