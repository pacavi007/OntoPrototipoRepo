//var bandera= false
  // Your web app's Firebase configurationw
var iniciarfire = async () => {
    //console.log("Iniciando firebase");
    
    //if(bandera==false)
    //{
    //bandera = true
    
    var firebaseConfig = {
    apiKey: "AIzaSyC7SFAE_PMtP05n7nRVCV1kcy6GbCB97yg",
    authDomain: "ontoblogp.firebaseapp.com",
    projectId: "ontoblogp",
    storageBucket: "ontoblogp.appspot.com",
    messagingSenderId: "914521866441",
    appId: "1:914521866441:web:0db9e847663467cde94d18"
    };
    firebase.initializeApp(firebaseConfig);
    //}
  
}

const cargararchivo = async () =>{
    console.log("Cargando archivo")
    var uploader = document.getElementById('uploader');
    var fileButton= document.getElementById('fileButton').files[0];
    
    var storageRef= firebase.storage().ref('Pepe'+fileButton.name);
    var task= storageRef.put(fileButton);
    task.on('state_changed',
        function progress(snapshot){
            var percentage = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
            uploader.value = percentage;
        })
    
    /*fileButton.addEventListener('change',function(e){
        var file = e.target.files[0];
        var storageRef= firebase.storage().ref(''+file.name);
        var task= storageRef.put(file);
        task.on('state_changed', 
            function progress(snapshot){
            var percentage = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
            uploader.value = percentage;
            }, 
            function error(err){

            },
            function complete(){

            }
        );
    });  */
  
  
}
 
 