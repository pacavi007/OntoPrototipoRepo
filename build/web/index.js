const send = async () => {
const option = document.getElementById('option').value
const group = document.getElementById('group').value
const comparative = document.getElementById('comparative').value
const assignment = document.getElementById('assignment').value
const gender = document.getElementById('gender').value
const age = document.getElementById('age').value
const grade = document.getElementById('grade').value
const queryParams =`?option=${option}&group=${group}&comparative=${comparative}&assignment=${assignment}&gender=${gender}&age=${age}&grade =${grade}`
try {
    const { data } = await axios.get(`http://localhost:8080/OntoPrototipoRepo/webresources/generic${queryParams}`)
    const resultDiv = document.getElementById('result')
    let stringHtml = '<div class="alert alert-primary" role="alert">Sin resultados</div>'
    if (data.length > 0) {
    const items = data.map(item =>
    `<tr>
        <td>${item.name}</td>
        <td>${item.last_name}</td>
        <td>${item.age} años</td>
    </tr>`
    ).join('')
    stringHtml =
    `<table class="table">
        <thead>
            <tr>
                <th scope="col">Nombre</th>
                <th scope="col">Apellidos</th>
                <th scope="col">Edad</th>
            </tr>
        </thead>
        <tbody>${items}</tbody>
    </table>`
}
resultDiv.innerHTML = stringHtml
} catch (err) { console.log(err)
} 
}
/*
const crudproyecto = async () => {  
    //const option = document.getElementById('option').value
    const option = 1
    const group = document.getElementById('group').value
    const comparative = document.getElementById('comparative').value
    const assignment = document.getElementById('assignment').value
    const gender = document.getElementById('gender').value
    const age = document.getElementById('age').value
    const grade = document.getElementById('grade').value
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
    }
} */