
var firebase;  
  
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

function cargararchivo(){
    console.log("En cargar archivo")
//descargararchivo()
        /*var starsRef = storageRef;
         starsRef.getDownloadURL().then(function(url) {
  console.log("url ",url)
}).catch(function(error) {
  switch (error.code) {
    case 'storage/object-not-found':
        console.log("url2 ")
      console.log("Descarga no encontrada")
      break;
    case 'storage/unauthorized':
      // User doesn't have permission to access the object
      console.log("Descarga no autorizada")
      break;
    case 'storage/unknown':
      console.log("Descarga desconocida")
      break;
  }
}); */
    var targetDiv = document.getElementById("div-alert");
    /*targetDiv.innerHTML = 
    `
    <div class="alert" id="success-alert" style="display: none" >
        <div style="width: 300px">
            <label><strong>Progreso de la carga</strong></label>
        </div>
        <div style="width: 300px">
            <progress style="height: 30px" value="0" max="100" id="uploader">0%</progress>
        </div>
    </div>
    `*/
    targetDiv.innerHTML = 
    `
    <div class="progress" id="success-alert" style="display: none">
        <div id="uploader" class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 10%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
    </div>
    `
    $("#success-alert").fadeIn(1000);
    
    
    
    const nombrearchivo = document.getElementById('fileButton').value
    //console.log("nombre archivo ",nombrearchivo.length)
    if (nombrearchivo.length>0) {
        console.log("Cargando archivo")
        var completada= false;
        var uploader = document.getElementById('uploader');
        var fileButton = document.getElementById('fileButton').files[0];
        
        var storageRef= firebase.storage().ref('Files/'+fileButton.name);
        
        var task=  storageRef.put(fileButton);
        
        task.on('state_changed',
            function progress(snapshot){
                var percentage = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
                //uploader.value = percentage;
                //console.log("% ",percentage)
                uploader.setAttribute("style","width:"+percentage+"%"+";"+"text:"+percentage+"%"+";"+"color: #FFFFFF")
                //uploader.setAttribute("")
                if(percentage===100)
                {
                    //ompletada= true
                    //console.log("Se completo")
                    //publicarenlace()
                    setTimeout(function(){ publicarenlace(); }, 3000);
                    $("#success-alert").fadeOut(3000);
                }
            }
        )
        $('#modal-archivo').modal('hide')
        //timeFunction()
        //setTimeout(function(){ publicarenlace(); }, 3000);
        //document.getElementById("uploader").value= 0
        //document.getElementById("fileButton").value= ""
        
        




   
    
    }
    else 
    {
        alert('Por favor seleccione un archivo para subir')
    }
    
}



const publicarenlace = async () => {
    //console.log("En publicar enlace")
    const nombrearchivo = document.getElementById('fileButton').value
    var link="";
    var token="";
    const id = document.getElementById('idcarga').value;
    const option = 26
    
    if (nombrearchivo.length>0) {
    var fileButton = document.getElementById('fileButton').files[0];
    var storageRef= firebase.storage().ref('Files/'+fileButton.name);
    var archivoRef = storageRef;
    
    archivoRef.getDownloadURL().then(function(url) {
        //console.log("url2 ",url);
            link = url;
        //console.log("linkcito ",link);
            //token = link.split("&token=")
            mandarenlace(link,token)
        }).catch(function(error) {
                switch (error.code) {
                    case 'storage/object-not-found':
                        console.log("url3 ")
                      console.log("Descarga no encontrada")
                      break;
                    case 'storage/unauthorized':
                      // User doesn't have permission to access the object
                      console.log("Descarga no autorizada")
                      break;
                    case 'storage/unknown':
                      console.log("Descarga desconocida")
                      break;
                }
    });
    
    

}

const mandarenlace = async (link,token) =>{
    //console.log("enlace ",link)
    //console.log("token ",token)
    
    const queryParams = `?option=${option}&atributo1=${link}&atributo2=${token}&atributo7=${id}`
    //const queryParams = `?option=${option}&atributo1=${link}&atributo7=${id}`
    try 
    {   
        
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        buscandoentregable()
    }
    catch (err) 
    { 
        console.log(err)
    }
    
    }
}

const resetprogreso = async () =>{
    //console.log("reset barra progreso")
    document.getElementById("uploader").value= 0
    
}
 
function timeFunction() {
    console.log("En timefuncion")
            setTimeout(function(){ publicarenlace(); }, 1000);
        }
        
const iniciardescarga = async(link) =>{
    console.log("en iniciadno descarga",link)
    //link = "https://firebasestorage.googleapis.com/v0/b/ontoblogp.appspot.com/o/Recursos%2Fcrud-interesado.js?alt=media&token=b866d65b-9024-4c00-8335-37e8db042fe7"
    if(link.length>0){
    window.open(link)
    }
    else
    {
        alert('¡No existe archivo asociado a este entregable!')
    }

}

 