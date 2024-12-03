function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
}

document.addEventListener("DOMContentLoaded", () => {
    // Find all like buttons
    const likeButtons = document.querySelectorAll('.Like');
    likeButtons.forEach(button => {
        // Check the data-is-liked attribute
        const isLiked = button.getAttribute('data-is-liked') === 'true';

        // Update the button's appearance
        if (isLiked) {
            button.classList.add('liked');
            button.innerHTML = '❤️'; // Set to "liked" state
        } else {
            button.classList.remove('liked');
            button.innerHTML = '♡'; // Set to "unliked" state
        }
    });
});

window.onclick = function (event) {
    if (!event.target.matches('.dropbtn')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (let i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
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
        var element = elements[i];
        if (search === "" || search == i + 1) {
            element.classList.remove("hide");
        } else {
            element.classList.add("hide");
        }
    }
}

async function likePost(likeButton) {
    // Locate the post ID from the button's data attribute
    const postId = likeButton.getAttribute('data-post-id');

    if (!postId) {
        console.error('Post ID not found');
        return;
    }

    // Check if the post is already liked
    const isLiked = likeButton.classList.contains('liked');

    try {
        // Determine the request method: POST to like, DELETE to unlike
        const method = isLiked ? 'DELETE' : 'POST';
        const endpoint = isLiked ? `/unlike/${postId}` : `/like/${postId}`;
        const response = await fetch(endpoint, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            // Toggle the button's appearance based on whether the post is liked or unliked
            if (isLiked) {
                likeButton.classList.remove('liked'); // Remove the 'liked' class
                likeButton.innerHTML = '♡'; // Change to 'unlike' symbol
            } else {
                likeButton.classList.add('liked'); // Add the 'liked' class
                likeButton.innerHTML = '❤️'; // Change to 'like' symbol
            }
        } else {
            console.error(`Failed to update like status for post ${postId}`);
        }
    } catch (error) {
        console.error(`Error updating like status:`, error);
    }
}
