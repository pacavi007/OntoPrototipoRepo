
const consultarriesgo = async () => { 
    document.getElementById('linkproyecto').setAttribute("class","navbar-brand")
    document.getElementById('linkproyecto').setAttribute("style","color: #0A2269")
    document.getElementById('linkinteresado').setAttribute("class","navbar-brand")
    document.getElementById('linkinteresado').setAttribute("style","color: #0A2269")
    document.getElementById('linkentregable').setAttribute("class","navbar-brand")
    document.getElementById('linkentregable').setAttribute("style","color: #0A2269")
    document.getElementById('linksupuesto').setAttribute("class","navbar-brand")
    document.getElementById('linksupuesto').setAttribute("style","color: #0A2269")
    document.getElementById('linkincidente').setAttribute("class","navbar-brand")
    document.getElementById('linkincidente').setAttribute("style","color: #0A2269")
    document.getElementById('linkriesgo').setAttribute("class","nav-link navbar-brand active")
    document.getElementById('linkriesgo').setAttribute("style","background: #0A2269; color: #E6E6E6")
    document.getElementById('mensaje').innerHTML= ""
    document.getElementById('nuevoboton').innerHTML= 
    ` 
    <button type="button" 
            data-bs-toggle="modal" 
            data-bs-target="#modal-1" 
            style="background: #0A2269; border-color: #E6E6E6; width:150px" 
            id="nuevoboton" 
            class="col-1 bt btn-primary btn-block"
        >Nuevo Riesgo
    </button> 
    <p></p> 
    `
    document.getElementById('div-modal').innerHTML =
    ` 
    <div class="modal fade" id="modal-1" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"> 
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Formulario Crear Riesgo</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                    </button>
                </div>
                <form onsubmit="agregarriesgo(); return false">
                    <div class="modal-body">
                        <div> 
                            <h6>Nombre Riesgo</h6> 
                            <input id="nombreriesgo" type="text" class="form-control mb-3" placeholder="Nombre Riesgo" rows="2" autocomplete="off" minlength="3" required>
                        </div> 
                        <div> 
                            <h6>Descripción Riesgo</h6> 
                            <textarea id="descripcionriesgo" class="form-control mb-3" placeholder="Descripción Riesgo" rows="4" style="height:100%"></textarea>
                        </div> 
                        <div id="divauxcrearriesgo">

                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                        <button type="submit" class="btn btn-primary"
                                >Guardar
                        </button>
                    </div>
                </form>
            </div>
        </div>    
    </div>
    `
    document.getElementById('div-modal-2').innerHTML =
    ` 
    <div class="modal fade" id="modal-2" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"> 
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="exampleModalLabel">Formulario Editar Riesgo</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                  </button>
                </div>
                <form onsubmit="editarriesgo(); return false">
                    <div class="modal-body">
                        <div> 
                            <h6>Nombre Riesgo</h6> 
                            <input id="nombreriesgo-2" type="text" class="form-control mb-3" autocomplete="off" minlength="3" required>
                        </div> 
                        <div> 
                            <h6>Descripción Riesgo</h6> 
                            <textarea id="descripcionriesgo-2" class="form-control mb-3" rows="4" style="height:100%"></textarea>
                        </div> 
                        <div id="divauxeditarriesgo">

                        </div>
                        <input type="hidden" id="idriesgo">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                        <button type="submit" class="btn btn-primary"
                                >Editar
                        </button>
                    </div>
                </form>
            </div>
        </div>    
    </div>
    `
    document.getElementById('div-info').innerHTML = 
    `
    <div class="modal fade" id="modal-info" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Información Completa del Riesgo</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                    </button>
                </div>
                <div class="modal-body">
                    <div>
                        <h6>Descripción</h6>
                        <p id="descripcion-3"></p>
                    </div>
                    <div id="divproyecto"> 
                        <h6>Proyecto</h6> 
                    </div> 
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                            style="background: #0A2269; border-color: #E6E6E6">Cerrar</button>
                </div>
            </div>
        </div>
    </div>
    `      
    buscandoriesgo()
} 

const buscandoriesgo = async () => {
    const option = 51
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const resultDiv = document.getElementById('result')
        let stringHtml = '<div style="color: #0A2269; background: #E6E6E6; border-color: #E6E6E6" class="alert alert-primary" role="alert">SIN RIESGOS</div>'
        if (data.length > 0 && (data!=="[Riesgo:SIN RIESGOS]")) 
        {  
        const items = data.map(item =>  
        `
        <tr>
            <td style="text-align: left" data-placement="top" title="Nombre Del Riesgo">${item.Riesgo}</td>
            <td>
                <div class="btn-group">
                    <button class="btn btn-info btn-sm rounded-0" type="button" 
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="modal" data-placement="right" title="Pulsa aquí para conocer toda la información de este riesgo" 
                            data-original-title="Info" data-bs-target="#modal-info" onclick="mostrarinforiesgo('${item.idRiesgo}','${item.descripcionR}','${item.proyectos}')"><i class="bi bi-info-circle" style="background: #0A2269; color: #FFFFFF"></i>
                    </button>
                    <button class="btn btn-success btn-sm rounded-0" type="button" 
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="modal" data-placement="right" title="Editar" 
                            data-original-title="Edit" data-bs-target="#modal-2" onclick="mostrareditarriesgo('${item.idRiesgo}','${item.Riesgo}','${item.descripcionR}','${item.proyectos}')"><i class="bi bi-pencil-square" style="background: #0A2269; color: #FFFFFF"></i>
                    </button> 
                    <button class="btn btn-danger btn-sm rounded-0" type="button"
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="tooltip" data-placement="right" title="Eliminar" 
                            data-original-title="Delete" onclick="eliminarriesgo('${item.idRiesgo}')"><i class="bi bi-trash" style="background: #0A2269; color: #FFFFFF"></i>
                    </button>
                </div>
            </td>
        </tr>
        `
        ).join('')
        stringHtml =
        `
        <table class="table-editable table table-bordered table-responsive-md table-striped text-center">
            <thead>
                <tr>
                    <th class= "col-8" scope="col">Nombre Del Riesgo</th>
                    <th class= "col-1" scope="col">Acción</th> 
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>
        `
        }
        metodoauxiliarcrearriesgo()
        resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}

const agregarriesgo = async () => {
    $('#modal-1').modal('hide')
    const option = 52
    const descripcionries = document.getElementById('descripcionriesgo').value
    const nombreries = document.getElementById('nombreriesgo').value
    var otrosvalores = ""
    const id = ""
    var inputElements = document.getElementsByClassName('proyectosCheckbox');
    for(var i=0; inputElements[i]; ++i){
        if(inputElements[i].checked){
            otrosvalores += inputElements[i].value+":"+"S"
        }
        else
        {
            otrosvalores += inputElements[i].value+":"+"N"
        }
        if(i<(inputElements.length-1)){
            otrosvalores += "," 
        } 
    }
    const queryParams = `?option=${option}&atributo1=${nombreries}&atributo2=${descripcionries}&superatributo=${otrosvalores}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        buscandoriesgo()
    }
    catch (err) 
    { 
        console.log(err)
    }
    document.getElementById('nombreriesgo').value = ""
    document.getElementById('descripcionriesgo').value = ""
    var targetDiv = document.getElementById("div-alert");
    targetDiv.innerHTML = 
    `
    <div class="alert alert-success" id="success-alert" style="display: none; width: 250px">
        <strong>Riesgo creado con éxito</strong>
    </div>
    `
    $("#success-alert").fadeIn(4000);
    $("#success-alert").fadeOut(2000);
}

const editarriesgo = async (aux) => {
    $('#modal-2').modal('hide')
    const option = 53
    const nombre = document.getElementById('nombreriesgo-2').value
    const descripcion = document.getElementById('descripcionriesgo-2').value
    var otrosvalores = ""
    const id = document.getElementById('idriesgo').value
    var inputElements = document.getElementsByClassName('proyectosCheckbox2');
    for(var i=0; inputElements[i]; ++i){
        if(inputElements[i].checked){
            otrosvalores += inputElements[i].value+":"+"S"
        }
        else
        {
            otrosvalores += inputElements[i].value+":"+"N"
        }
        if(i<(inputElements.length-1)){
            otrosvalores += ","
        }
    }
    const queryParams = `?option=${option}&atributo1=${nombre}&atributo2=${descripcion}&atributo7=${id}&superatributo=${otrosvalores}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        buscandoriesgo()
    }
    catch (err) 
    { 
        console.log(err)
    }
    var targetDiv = document.getElementById("div-alert");
    targetDiv.innerHTML = 
    `
    <div class="alert alert-success" id="success-alert" style="display: none; width: 250px">
        <strong>Riesgo editado con éxito</strong>
    </div>
    `
    $("#success-alert").fadeIn(4000);
    $("#success-alert").fadeOut(2000);
}

const mostrareditarriesgo = async(aux1,aux2,aux3,proyectos) => {
    document.getElementById('idriesgo').value = aux1
    document.getElementById('nombreriesgo-2').value = aux2
    document.getElementById('descripcionriesgo-2').value = aux3
    metodoauxiliareditarriesgo(aux1,proyectos)
}

const eliminarriesgo = async(aux) => {
    if(confirm('¿Está seguro de borrar este riesgo?'))
    {
        const option = 54
        const id = aux
        const queryParams = `?option=${option}&atributo7=${id}`
        try 
        {
            const ipserver = location.host
            const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
            buscandoriesgo()
        }
        catch (err) 
        { 
            console.log(err)
        }
        var targetDiv = document.getElementById("div-alert");
        targetDiv.innerHTML = 
        `
        <div class="alert alert-success" id="success-alert" style="display: none; width: 250px">
            <strong>Riesgo eliminado con éxito</strong>
        </div>
        `
        $("#success-alert").fadeIn(4000);
        $("#success-alert").fadeOut(2000);
    }
}

const mostrarinforiesgo = async(id,descripcion,proyectos) => {
    document.getElementById('descripcion-3').innerHTML = descripcion
    let divproyecto = document.getElementById('divproyecto')
    let h6= document.createElement("h6")
    let textoH6 = document.createTextNode("Proyecto")
    divproyecto.innerHTML=""
    divproyecto.setAttribute("class","row")
    h6.appendChild(textoH6)
    divproyecto.append(h6)
    var proyecto = proyectos.split(',,');
    for(let i=0;i<proyecto.length;i++)
    {
        var proy= (proyecto[i].split(":"))[1]
        let parragrafo1 = document.createElement("div")
        let nombreproy= document.createTextNode((i+1)+") "+proy)
        parragrafo1.setAttribute("class","col-md-12")
        parragrafo1.append(nombreproy)
        divproyecto.append(parragrafo1)
    }
    divproyecto.append(document.createElement("p"))
}

const metodoauxiliarcrearriesgo = async() => {
    const option = 55
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const proyectos = data.split(',,')
        let divauxcrearriesgo = document.getElementById('divauxcrearriesgo')
        divauxcrearriesgo.innerHTML=""
        divauxcrearriesgo.setAttribute("class","row")
        let divproyecto = document.createElement("div")
        let h6= document.createElement("h6")
        let textoH6 = document.createTextNode("Proyectos")
        h6.appendChild(textoH6)
        divproyecto.setAttribute("class","row")
        divproyecto.append(h6)
        if (proyectos[0]!=="Proyecto:SIN PROYECTOS") {
            for (let i = 0; i < proyectos.length; i++)
            {
                let div = document.createElement("div")
                let input = document.createElement("input")
                let label = document.createElement("label")
                let aux = proyectos[i].split("&&")
                let nombreproyecto = (aux[0].split(":"))[1]
                let idproyecto = (aux[1].split(":"))[1]
                div.setAttribute("class", "col-md-6")
                input.setAttribute("type", "checkbox")
                input.setAttribute("value", idproyecto)
                input.setAttribute("class", "proyectosCheckbox")
                label.append(input)
                label.append(" " + nombreproyecto)
                div.append(label)
                divproyecto.append(div)
            }
        } 
        else{
            let div = document.createElement("div")
            div.setAttribute("class", "col-md-12")
            div.append("NO HAY PROYECTOS")
            divproyecto.append(div)
        }
        divauxcrearriesgo.append(divproyecto)
        divauxcrearriesgo.append(document.createElement("p"))
    }
    catch (err) 
    { 
        console.log(err)
    }
}

const metodoauxiliareditarriesgo = async(aux1,proye) => {
    const option = 55
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const proyectos = data.split(',,')
        const proyecaux = proye.split(',,')
        var  proyecaux2 = ""
        let divauxeditarriesgo = document.getElementById('divauxeditarriesgo')
        divauxeditarriesgo.innerHTML=""
        divauxeditarriesgo.setAttribute("class","row")
        let divproyecto = document.createElement("div")
        let h6= document.createElement("h6")
        let textoH6 = document.createTextNode("Proyectos")
        h6.appendChild(textoH6)
        divproyecto.setAttribute("class","row")
        divproyecto.append(h6)
        if (proyectos[0]!=="Proyecto:SIN PROYECTOS") {
            for (let i = 0; i < proyectos.length; i++)
            {   
                let div = document.createElement("div")
                let input = document.createElement("input")
                let label = document.createElement("label")
                let aux = proyectos[i].split("&&")
                let nombreProyecto = (aux[0].split(":"))[1]
                let idProyecto = (aux[1].split(":"))[1]
                div.setAttribute("class", "col-md-6")
                input.setAttribute("type", "checkbox")
                input.setAttribute("value", idProyecto)
                input.setAttribute("class", "proyectosCheckbox2")
                for (let j = 0; j < proyecaux.length; j++) {
                    proyecaux2 = proyecaux[j].split('&&')
                    proyecaux2 = proyecaux2[0].split(':')
                    proyecaux2 = proyecaux2[1]
                    if (proyecaux2 === nombreProyecto) {
                        input.setAttribute("checked", true)
                    }
                }
                label.append(input)
                label.append(" " + nombreProyecto)
                div.append(label)
                divproyecto.append(div)
            }
        }
        else{
            let div = document.createElement("div")
            div.setAttribute("class", "col-md-12")
            div.append("NO HAY PROYECTOS")
            divproyecto.append(div)
        }
        divauxeditarriesgo.append(divproyecto)
        divauxeditarriesgo.append(document.createElement("p"))
    }
    catch (err) 
    { 
        console.log(err)
    } 
}



