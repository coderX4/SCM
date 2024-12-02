console.log("Mails.js")
const justUrl = "http://localhost:8081";

//mails
async function startMails(usermail){
    console.log(usermail);
    const url = `${justUrl}/user/mails/start-mails/${usermail}`;
    window.location.replace(url);
}

async function sendMails(from,to){
    console.log(from);
    console.log(to)
    const url = `${justUrl}/user/mails/contact-send-mails/${from}/${to}`;
    window.location.replace(url);
}