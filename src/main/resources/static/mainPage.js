function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}

function searchFunction() {
    const elements = document.getElementsByClassName("Post");
    let search = document.getElementById("searchbar").value;
    console.log("This is what you searched: " + search);
    for (let i = 0; i < elements.length; i++) {
        elements[i].classList.toggle("hide");
    }
    for (let i = 0; i < elements.length; i++) {
        if (i == search && window.getComputedStyle().display === "none") {
            elements[i].classList.toggle("hide");
        }
    }
    if (search == "") {
        for (let i = 0; i < elements.length; i++) {
            if (window.getComputedStyle().display === "none") {
                elements[i].classList.toggle("hide");
            }
        }
    }
}

async function likePost(likeButton) {
    // Locate the parent post div
    const postElement = likeButton.closest('.Post');
    if (!postElement) {
        console.error('Unable to find the parent post element.');
        return;
    }

    // Extract the post ID from the `data-post-id` attribute
    const postId = postElement.getAttribute('data-post-id');
    if (!postId) {
        console.error('Post ID not found on the parent post element.');
        return;
    }

    // Check if the post is already liked
    const isLiked = likeButton.classList.contains('liked');

    // Assuming userId is available in a global JS variable
    //const userId = 1; // Replace with actual user ID from session or another source

    try {
        // Send request to backend
        const method = isLiked ? 'DELETE' : 'POST';
        const response = await fetch(`/like/${postId}?userId=${userId}`, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            // Toggle the like button's appearance
            if (isLiked) {
                likeButton.classList.remove('liked');
                likeButton.innerHTML = '♡'; // Unlike
            } else {
                likeButton.classList.add('liked');
                likeButton.innerHTML = '❤️'; // Like
            }
        } else {
            console.error(`Failed to update like status for post ${postId}:`, response.statusText);
            alert('Failed to update like status. Please try again.');
        }
    } catch (error) {
        console.error(`Error updating like status for post ${postId}:`, error);
        alert('An error occurred while updating the like status.');
    }
}
