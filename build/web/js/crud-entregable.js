
const crudentregable = async () => {  
    
    const option = 2
    document.getElementById('result').innerHTML = ""
    document.getElementById('mensaje').innerHTML= "Digita algún campo"
    try
    {
        const resultDiv = document.getElementById('formulario')
        let stringHTML = 
        `
        <div class="col-6"> 
            <h6>Típo Entregable</h6> 
            <input id="entregable" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Descripción Entregable</h6> 
            <input id="descripcionentregable" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6">
            <h6>Versión Entregable</h6>
            <input id="versionentregable" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Responsable</h6> 
            <input id="responsable" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Fecha Entrega</h6>
            <input id="fechaentrega" type="text" class="form-control mb-3"> 
        </div> 
        <div class="col-6"> 
            <h6>Estado</h6> 
            <input id="estado" type="text" class="form-control mb-3">
        </div>
        <div class="col-12">
            <button class="btn btn-primary 
                    btn-block"
                    onclick="buscandoentregable()">Buscar Entregable
            </button>
        </div>`
        resultDiv.innerHTML = stringHTML
    }
    catch(err)
    {
        console.log(err);         
    }   
} 

const buscandoentregable = async () => {
    document.getElementById('mensaje').innerHTML= "Proyectos encontrados"
    const option = 2
    const group = document.getElementById('entregable').value
    const comparative = document.getElementById('descripcionentregable').value
    const assignment = document.getElementById('versionentregable').value
    const gender = document.getElementById('responsable').value
    const age = document.getElementById('fechaentrega').value
    const grade = document.getElementById('estado').value
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
            <td>${item.titulo}</td>
            <td>${item.objetivo}</td>
            <td>${item.descripcion}</td>
            <td>${item.fechainicio}</td>
            <td>${item.fechafin}</td>
        </tr>`
        ).join('')
        stringHtml =
        `<table class="table">
            <thead>
                <tr>
                    <th class= "col-2" scope="col">Título</th>
                    <th class= "col-3" scope="col">Objetivo</th>
                    <th class= "col-3" scope="col">Descripción</th>
                    <th class= "col-2" scope="col">Fecha Inicio</th>
                    <th class= "col-2" scope="col">Fecha Fin</th>
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>`
        } console.log("Entro a crud-proyecto",data)
    resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}