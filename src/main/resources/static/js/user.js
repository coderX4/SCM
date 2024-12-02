console.log("User.js")
const baseURL = "http://localhost:8081";

//delete user
async function deleteUser(id){
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/profile/delete/${id}`;
            window.location.replace(url);
            Swal.fire({
                title: "Deleted!",
                text: "Your file has been deleted.",
                icon: "success"
            });
        }
    });
}

//update user
async function updateUser(id){
    console.log(id);
    const url = `${baseURL}/user/profile/update-view/${id}`;
    window.location.replace(url);
}

