
const crudproyecto = async () => {  
    //const option = document.getElementById('option').value
    /*try 
    {
        const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const resultDiv = document.getElementById('result')
        let stringHtml = '<div class="alert alert-primary" role="alert">Sin resultados</div>'
        if (data.length > 0) 
        {
        const items = data.map(item =>  
        `<tr>
            <td>${item.nombre}</td>
            
        </tr>`
        ).join('')
        stringHtml =
        `<table class="table">
            <thead>
                <tr>
                    <th scope="col">Nombre</th>
                    
                    
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>`
        }
    resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    } */
    const option = 2
    /*const group = document.getElementById('group').value
    const comparative = document.getElementById('comparative').value
    const assignment = document.getElementById('assignment').value
    const gender = document.getElementById('gender').value
    const age = document.getElementById('age').value
    const grade = document.getElementById('grade').value
    const queryParams = `?option=${option}&group=${group}&comparative=${comparative}&assignment=${assignment}&gender=${gender}&age=${age}&grade =${grade}`
    */
    
    try
    {
        //const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
        document.getElementById('mensaje').innerHTML= "Digita algún campo"
        const resultDiv = document.getElementById('formulario')
        let stringHTML = 
        `
        <div class="col-6"> 
            <h6>Título</h6> 
            <input id="option" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6"> 
            <h6>Objetivo</h6> 
            <input id="group" type="text" class="form-control mb-3">
        </div> 
        <div class="col-6">
            <h6>Descripción</h6>
            <input id="comparative" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Presupuesto</h6> 
            <input id="assignment" type="text" class="form-control mb-3">
        </div>
        <div class="col-6">
            <h6>Fecha Inicio</h6>
            <input id="gender" type="text" class="form-control mb-3"> 
        </div> 
        <div class="col-6"> 
            <h6>Fecha Fin</h6> 
            <input id="age" type="text" class="form-control mb-3">
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
    document.getElementById('mensaje').innerHTML= "En buscando proyecto"
    const option = 2
    const group = document.getElementById('group').value
    const comparative = document.getElementById('comparative').value
    const assignment = document.getElementById('assignment').value
    const gender = document.getElementById('gender').value
    const age = document.getElementById('age').value
    const grade = "0"
    const queryParams = `?option=${option}&group=${group}&comparative=${comparative}&assignment=${assignment}&gender=${gender}&age=${age}&grade =${grade}`
    try 
    {
        const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
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
                    <th scope="col">Título</th>
                    <th scope="col">Objetivo</th>
                    <th scope="col">Descripción</th>
                    <th scope="col">Fecha Inicio</th>
                    <th scope="col">Fecha Fin</th>
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>`
        } console.log("ENtro",data)
    resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}