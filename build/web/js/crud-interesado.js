
const crudinteresado = async () => {  
    
    const option = 3
    document.getElementById('result').innerHTML = ""
    document.getElementById('mensaje').innerHTML= "Digita algún campo"
    try
    {
        const resultDiv = document.getElementById('formulario')
        let stringHTML = 
        `
        <div class="col-6"> 
            <h6>Nombre</h6> 
            <input id="nombreinteresado" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Tipo influencia</h6> 
            <input id="tipoinfluencia" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Cargo</h6> 
            <input id="cargo" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Rol Proyecto</h6> 
            <input id="rolproyecto" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6">
            <h6>Equipo Proyecto</h6>
            <input id="equipoproyecto" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Teléfono</h6> 
            <input id="telefono" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>E-mail</h6>
            <input id="email" type="text" class="form-control mb-3"> 
        </div> 
        <div class="col-12">
            <button class="btn btn-primary 
                    btn-block"
                    onclick="buscandointeresado()">Buscar Interesado
            </button>
        </div>`
        resultDiv.innerHTML = stringHTML
    }
    catch(err)
    {
        console.log(err);         
    }   
} 

const buscandointeresado = async () => {
    document.getElementById('mensaje').innerHTML= "Interesados encontrados"
    const option = 3
    const group = document.getElementById('nombreinteresado').value
    const comparative = document.getElementById('tipoinfluencia').value
    const assignment = document.getElementById('cargo').value
    const gender = document.getElementById('rolproyecto').value
    const age = document.getElementById('equipoproyecto').value
    const grade = document.getElementById('telefono').value
    const queryParams = `?option=${option}&group=${group}&comparative=${comparative}&assignment=${assignment}&gender=${gender}&age=${age}&grade =${grade}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const resultDiv = document.getElementById('result')
        let stringHtml = '<div class="alert alert-primary" role="alert">Sin resultados</div>'
        if (data.length > 0) 
        {  
        const items = data.map(item =>  
        `<tr>
            <td>${item.nombreinteresado}</td>
            <td>${item.tipoinfluencia}</td>
            <td>${item.cargo}</td>
            <td>${item.rolproyecto}</td>
            <td>${item.equipoproyecto}</td>
            <td>${item.telefono}</td>
            <td>${item.email}</td>
        </tr>`
        ).join('')
        stringHtml =
        `<table class="table">
            <thead>
                <tr>
                    <th class= "col-1" scope="col">Nombre Interesado</th>
                    <th class= "col-1" scope="col">Tipo Influencia</th>
                    <th class= "col-1" scope="col">Cargo</th>
                    <th class= "col-1" scope="col">Rol Proyecto</th>
                    <th class= "col-1" scope="col">Equipo Proyecto</th>
                    <th class= "col-1" scope="col">Teléfono</th>
                    <th class= "col-1" scope="col">E-mail</th>
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>`
        } console.log("Entro a crud-interesado",data)
    resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}