// URL Shortener Application JavaScript

let currentShortUrl = '';

/**
 * Shorten a URL by calling the backend API
 */
async function shortenUrl() {
    const urlInput = document.getElementById('urlInput');
    const url = urlInput.value.trim();
    const resultDiv = document.getElementById('result');
    const errorDiv = document.getElementById('error');
    const shortenBtn = document.getElementById('shortenBtn');
    
    if (!url) {
        showError('Please enter a URL');
        return;
    }

    if (!isValidUrl(url)) {
        showError('Please enter a valid URL');
        return;
    }

    setLoadingState(shortenBtn, true);
    hideError();

    try {
        const response = await fetch('/api/shorten', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ url: url })
        });

        if (!response.ok) {
            throw new Error('Failed to shorten URL');
        }

        const data = await response.json();
        currentShortUrl = data.shortUrl;
        
        displayShortUrl(currentShortUrl);
        urlInput.value = '';
        loadStats();
        
    } catch (error) {
        showError('Failed to shorten URL. Please try again.');
    } finally {
        setLoadingState(shortenBtn, false);
    }
}

/**
 * Load and display statistics
 */
async function loadStats() {
    try {
        const response = await fetch('/api/urls');
        const urls = await response.json();
        
        updateStatistics(urls);
        displayRecentUrls(urls);
        
    } catch (error) {
        console.error('Failed to load stats:', error);
    }
}

/**
 * Update statistics display
 */
function updateStatistics(urls) {
    const totalUrls = urls.length;
    const totalClicks = urls.reduce((sum, url) => sum + url.clickCount, 0);
    
    document.getElementById('totalUrls').textContent = totalUrls;
    document.getElementById('totalClicks').textContent = totalClicks;
}

/**
 * Display recent URLs in the list
 */
function displayRecentUrls(urls) {
    const urlListDiv = document.getElementById('urlList');
    urlListDiv.innerHTML = '';
    
    // Show last 5 URLs, most recent first
    urls.slice(-5).reverse().forEach(url => {
        const urlItem = createUrlItem(url);
        urlListDiv.appendChild(urlItem);
    });
}

/**
 * Create a URL item element
 */
function createUrlItem(url) {
    const urlItem = document.createElement('div');
    urlItem.className = 'url-item';
    urlItem.innerHTML = `
        <h4>${url.originalUrl}</h4>
        <p>Short: ${url.shortUrl}</p>
        <p>Clicks: ${url.clickCount}</p>
    `;
    return urlItem;
}

/**
 * Display the shortened URL result
 */
function displayShortUrl(shortUrl) {
    document.getElementById('shortUrl').textContent = shortUrl;
    const resultDiv = document.getElementById('result');
    resultDiv.classList.add('show');
}

/**
 * Copy short URL to clipboard
 */
function copyToClipboard() {
    if (!currentShortUrl) return;
    
    navigator.clipboard.writeText(currentShortUrl).then(() => {
        showCopySuccess();
    }).catch(err => {
        showError('Failed to copy to clipboard');
    });
}

/**
 * Show copy success feedback
 */
function showCopySuccess() {
    const copyBtn = document.querySelector('.copy-btn');
    const originalText = copyBtn.textContent;
    copyBtn.textContent = 'Copied!';
    copyBtn.style.background = '#218838';
    
    setTimeout(() => {
        copyBtn.textContent = originalText;
        copyBtn.style.background = '#28a745';
    }, 2000);
}

/**
 * Validate URL format
 */
function isValidUrl(string) {
    try {
        new URL(string);
        return true;
    } catch (_) {
        return false;
    }
}

/**
 * Show error message
 */
function showError(message) {
    const errorDiv = document.getElementById('error');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
}

/**
 * Hide error message
 */
function hideError() {
    const errorDiv = document.getElementById('error');
    errorDiv.style.display = 'none';
}

/**
 * Set loading state for button
 */
function setLoadingState(button, isLoading) {
    if (isLoading) {
        button.disabled = true;
        button.textContent = 'Shortening...';
    } else {
        button.disabled = false;
        button.textContent = 'Shorten';
    }
}

/**
 * Initialize event listeners
 */
function initializeEventListeners() {
    // Enter key to submit
    document.getElementById('urlInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            shortenUrl();
        }
    });

    // Load stats on page load
    window.addEventListener('load', loadStats);
}

// Initialize the application when DOM is ready
document.addEventListener('DOMContentLoaded', initializeEventListeners);
