
var firebase; 
var date = new Date();
var anio = date.getFullYear();
var mes  = date.getMonth()+1
var dia  = date.getDate()
var hora = date.getHours()
var minuto = date.getMinutes()
var segundo = date.getSeconds()
var fecha = ""

var iniciarfire = async () => {
    //console.log("Iniciando firebase");
    var firebaseConfig = {
    apiKey: "AIzaSyC7SFAE_PMtP05n7nRVCV1kcy6GbCB97yg",
    authDomain: "ontoblogp.firebaseapp.com",
    projectId: "ontoblogp",
    storageBucket: "ontoblogp.appspot.com",
    messagingSenderId: "914521866441",
    appId: "1:914521866441:web:0db9e847663467cde94d18"
    };
    firebase.initializeApp(firebaseConfig);
  
}

function cargararchivo(){
    console.log("En cargar archivo")
    var targetDiv = document.getElementById("div-alert");
    targetDiv.innerHTML = 
    `
    <div class="progress" id="success-alert" style="display: none">
        <div id="uploader" class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" style="width: 10%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
        </div>
        <small id="numprogreso" class="justify-content-center d-flex position-absolute w-100" style="font-weight: bolder"></small>
    </div>
    `
    $("#success-alert").fadeIn(1000);
    const nombrearchivo = document.getElementById('fileButton').value
    //console.log("nombre archivo ",nombrearchivo.length)
    if (nombrearchivo.length>0) {
        console.log("Cargando archivo")
        var completada= false;
        var uploader = document.getElementById('uploader');
        var progresimetro = document.getElementById('numprogreso');
        var fileButton = document.getElementById('fileButton').files[0];
        date = new Date();
        anio = date.getFullYear();
        mes  = date.getMonth()+1
        dia  = date.getDate()
        hora = date.getHours()
        minuto = date.getMinutes()
        segundo = date.getSeconds()
        fecha = anio+"-"+mes+"-"+dia+" "+hora+":"+minuto+":"+segundo+" "
        var storageRef= firebase.storage().ref('Files/'+fecha+fileButton.name);
        
        var task=  storageRef.put(fileButton);
        
        task.on('state_changed',
            function progress(snapshot){
                var percentage = (snapshot.bytesTransferred/snapshot.totalBytes)*100;
                //uploader.value = percentage;
                //console.log("% ",percentage)
                uploader.setAttribute("style","width:"+percentage+"%")
                progresimetro.innerHTML = ""
                progresimetro.append(percentage.toFixed(2)+"%")
                if(percentage===100)
                {
                    //console.log("Se completo")
                    setTimeout(function(){ publicarenlace(); }, 3000);
                    $("#success-alert").fadeOut(3000);
                }
            }
        )
        $('#modal-archivo').modal('hide')
    
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
    var nombre="";
    var token="";
    if (nombrearchivo.length>0) {
    var fileButton = document.getElementById('fileButton').files[0];
    //var storageRef= firebase.storage().ref('Files/'+fecha+fileButton.name);
    console.log("fecha ",'Files/'+fecha+fileButton.name)
    var storageRef= firebase.storage().ref('Files/'+fecha+fileButton.name);
    var archivoRef = storageRef;
    archivoRef.getDownloadURL().then(function(url) {
            link = url;
            //nombre = nombrearchivo.split("fakepath\\")[1] 
            nombre = fecha+fileButton.name
            token  = (link.split("&token=")[1])
            //token = token[1]
            console.log("Nombrearchi ",nombre);
            //console.log("url2 ",url);
            mandarenlace(nombre,token)
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
    
    document.getElementById('fileButton').value= ""
    
    }
}

const mandarenlace = async (nombre,token) =>{
    //console.log("enlace ",link)
    //console.log("token ",token)
    const option = 26
    const id = document.getElementById('idcarga').value;
    const queryParams = `?option=${option}&atributo1=${nombre}&atributo2=${token}&atributo7=${id}`
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

const resetprogreso = async () =>{
    //console.log("reset barra progreso")
    //document.getElementById("uploader").value= 0
    
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

 