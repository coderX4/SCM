console.log("Contacts.js")
const baseURL = "http://localhost:8081";

const viewContactModal= document.getElementById('view_contact_modal');
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses: 'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    }
};

const instanceOptions = {
    id:'view_contact_modal',
    override:true
}

const contactModal = new Modal(viewContactModal, options,instanceOptions);

function openContactModal(){
    contactModal.show();
}
function closeContactModal(){
    contactModal.hide();
}

async function loadContactdata(id){
    //function call to load data
    try{
        console.log(id);
        let data = await (await fetch(`${baseURL}/api/contact/${id}`)).json();
        console.log(data);
        document.querySelector("#contact_name").innerHTML = data.name;
        document.querySelector("#contact_email").innerHTML = data.email;
        document.querySelector("#contact_address").innerHTML = data.address;
        document.querySelector("#contact_about").innerHTML = data.description;
        document.querySelector("#contact_phone").innerHTML = data.phoneNumber;
        document.querySelector("#contact_image").src = data.picture;
        document.querySelector("#contact_instagram").href = data.instagramLink;
        document.querySelector("#contact_linkedIn").href = data.linkedinLink;
        // Update favorite status
        const contactFavoriteIndicator = document.querySelector("#contact_favorite_indicator");
        const contactFavoriteText = document.querySelector("#contact_favorite_text");

        if (data.favorite) {
            contactFavoriteIndicator.classList.remove('bg-red-500');
            contactFavoriteIndicator.classList.add('bg-green-500');
            contactFavoriteText.textContent = "Favourite";
        } else {
            contactFavoriteIndicator.classList.remove('bg-green-500');
            contactFavoriteIndicator.classList.add('bg-red-500');
            contactFavoriteText.textContent = "Not Favourite";
        }
        openContactModal()
    } catch (error){
        console.log("Error: ",error);
    }
}

//delete contact
async function deleteContact(id){
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
            const url = `${baseURL}/user/contact/delete/`+id;
            window.location.replace(url);
            Swal.fire({
                title: "Deleted!",
                text: "Your file has been deleted.",
                icon: "success"
            });
        }
    });
}

//delete contact on search
async function deleteContactOnSearch(id,field,value){
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!",
    }).then((result) => {
        if (result.isConfirmed) {
            const url = `${baseURL}/user/contact/s-delete/${id}/${field}/${value}`;
            window.location.replace(url);
            Swal.fire({
                title: "Deleted!",
                text: "Your file has been deleted.",
                icon: "success"
            });
        }
    });
}

//update contact on search
async function updateContactOnSearch(id,field,value){
    const url = `${baseURL}/user/contact/search/view/${id}/${field}/${value}`;
    window.location.replace(url);
}
