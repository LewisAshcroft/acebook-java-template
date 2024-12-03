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
            button.innerHTML = `❤ (${likeCount})`; // Set to "liked" state

        } else {
            button.classList.remove('liked');
            button.innerHTML = `♡ (${likeCount})`; // Set to "unliked" state

        }
    });
});

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
        var element = elements[i];
       if (search == "" || search == i+1){
            element.classList.remove("hide");
       } else {
            element.classList.add("hide");
       }

    }

}


async function likePost(likeButton) {
    const postElement = likeButton.closest('.Post');
    const postId = postElement.getAttribute('data-post-id');
    const isLiked = likeButton.classList.contains('liked');
     if (!postId) {
          console.error('Post ID not found');
          return;
      }

    try {
        const method = isLiked ? 'DELETE' : 'POST';
        const url = isLiked ? `/unlike/${postId}` : `/like/${postId}`;

        const response = await fetch(url + `?userId=${userId}`, {

            method: method,
            headers: { 'Content-Type': 'application/json' },
        });

        if (response.ok) {

            // Get the updated like count from the response (optional)
            const { likeCount } = await response.json();

            // Update the button's appearance
            if (isLiked) {
                likeButton.classList.remove('liked');
                likeButton.innerHTML = `♡ (${likeCount})`; // Unlike
            } else {
                likeButton.classList.add('liked');
                likeButton.innerHTML = `❤️ (${likeCount})`; // Like
            }
        } else {
            console.error(`Failed to update like status for post ${postId}`);
        }
    } catch (error) {
        console.error(`Error updating like status:`, error);
    }
}

