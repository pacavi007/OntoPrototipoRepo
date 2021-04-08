
const crudproyecto = async () => {  
    
    const option = 2
    document.getElementById('result').innerHTML = ""
    document.getElementById('mensaje').innerHTML= "Digita algún campo"
    try
    {
        const resultDiv = document.getElementById('formulario')
        let stringHTML = 
        `
        <div class="col-6"> 
            <h6>Título</h6> 
            <input id="titulo" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Objetivo</h6> 
            <input id="objetivo" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6">
            <h6>Descripción</h6>
            <input id="descripcion" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Presupuesto</h6> 
            <input id="presupuesto" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Fecha Inicio</h6>
            <input id="fechainicio" type="text" class="form-control mb-3"> 
        </div> 
        <div class="col-6"> 
            <h6>Fecha Fin</h6> 
            <input id="fechafin" type="text" class="form-control mb-3">
        </div>
        <div class="col-12">
            <button class="btn btn-primary 
                    btn-block"
                    onclick="buscandoproyecto()">Buscar Proyecto
            </button>
        </div>`
        resultDiv.innerHTML = stringHTML
    }
    catch(err)
    {
        console.log(err);         
    }   
} 

const buscandoproyecto = async () => {
    document.getElementById('mensaje').innerHTML= "Proyectos encontrados"
    const option = 2
    const group = document.getElementById('titulo').value
    const comparative = document.getElementById('objetivo').value
    const assignment = document.getElementById('descripcion').value
    const gender = document.getElementById('presupuesto').value
    const age = document.getElementById('fechainicio').value
    const grade = document.getElementById('fechafin').value
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