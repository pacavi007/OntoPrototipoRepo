
const consultarsupuesto = async () => {  
    document.getElementById('linkproyecto').setAttribute("class","navbar-brand")
    document.getElementById('linkproyecto').setAttribute("style","color: #0A2269")
    document.getElementById('linkinteresado').setAttribute("class","navbar-brand")
    document.getElementById('linkinteresado').setAttribute("style","color: #0A2269")
    document.getElementById('linkentregable').setAttribute("class","navbar-brand")
    document.getElementById('linkentregable').setAttribute("style","color: #0A2269")
    document.getElementById('linksupuesto').setAttribute("class","nav-link navbar-brand active")
    document.getElementById('linksupuesto').setAttribute("style","background: #0A2269; color: #E6E6E6")
    document.getElementById('linkincidente').setAttribute("class","navbar-brand")
    document.getElementById('linkincidente').setAttribute("style","color: #0A2269")
    document.getElementById('linkriesgo').setAttribute("class","navbar-brand")
    document.getElementById('linkriesgo').setAttribute("style","color: #0A2269")
    document.getElementById('mensaje').innerHTML= ""
    document.getElementById('nuevoboton').innerHTML= 
    ` 
    <button type="button" 
            data-bs-toggle="modal" 
            data-bs-target="#modal-1" 
            style="background: #0A2269; border-color: #E6E6E6; width:150px" 
            id="nuevoboton" 
            class="col-1 bt btn-primary btn-block"
        >Nuevo Supuesto
    </button> 
    <p></p> 
    `
    document.getElementById('div-modal').innerHTML =
    ` 
    <div class="modal fade" id="modal-1" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"> 
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Formulario Crear Supuesto</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                    </button>
                </div>
                <form onsubmit="agregarsupuesto(); return false">
                    <div class="modal-body">
                        <div> 
                            <h6>Nombre Supuesto</h6> 
                            <input id="nombresupuesto" type="text" class="form-control mb-3" placeholder="Nombre Supuesto" rows="2" autocomplete="off" minlength="3" required>
                        </div> 
                        <div> 
                            <h6>Descripción Supuesto</h6> 
                            <textarea id="descripcionsupuesto" class="form-control mb-3" placeholder="Descripción Supuesto" rows="4" style="height:100%"></textarea>
                        </div> 
                        <div id="divauxcrearsupuesto">

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
                  <h5 class="modal-title" id="exampleModalLabel">Formulario Editar Supuesto</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                  </button>
                </div>
                <form onsubmit="editarsupuesto(); return false">
                    <div class="modal-body">
                        <div> 
                            <h6>Nombre Supuesto</h6> 
                            <input id="nombresupuesto-2" type="text" class="form-control mb-3" autocomplete="off" minlength="3" required>
                        </div> 
                        <div> 
                            <h6>Descripción Supuesto</h6> 
                            <textarea id="descripcionsupuesto-2" class="form-control mb-3" rows="4" style="height:100%"></textarea>
                        </div> 
                        <div id="divauxeditarsupuesto">

                        </div>
                        <input type="hidden" id="idsupuesto">
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
                    <h5 class="modal-title" id="exampleModalLabel">Información Completa del Supuesto</h5>
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
    buscandosupuesto()
} 

const buscandosupuesto = async () => {
    const option = 31
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const resultDiv = document.getElementById('result')
        let stringHtml = '<div style="color: #0A2269; background: #E6E6E6; border-color: #E6E6E6" class="alert alert-primary" role="alert">SIN SUPUESTOS</div>'
        if ((data.length > 0) && (data!=="[Supuesto:SIN SUPUESTOS]")) 
        {   
        const items = data.map(item =>  
        `
        <tr>
            <td style="text-align: left" data-placement="top" title="Nombre Del Supuesto">${item.Supuesto}</td>
            <td>
                <div class="btn-group">
                    <button class="btn btn-info btn-sm rounded-0" type="button" 
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="modal" data-placement="right" title="Pulsa aquí para conocer toda la información de este supuesto" 
                            data-original-title="Info" data-bs-target="#modal-info" onclick="mostrarinfosupuesto('${item.idSupuesto}','${item.descripcionS}','${item.proyectos}')"><i class="bi bi-info-circle" style="background: #0A2269; color: #FFFFFF"></i>
                    </button>
                    <button class="btn btn-success btn-sm rounded-0" type="button" 
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="modal" data-placement="right" title="Editar" 
                            data-original-title="Edit" data-bs-target="#modal-2" onclick="mostrareditarsupuesto('${item.idSupuesto}','${item.Supuesto}','${item.descripcionS}','${item.proyectos}')"><i class="bi bi-pencil-square" style="background: #0A2269; color: #FFFFFF"></i>
                    </button> 
                    <button class="btn btn-danger btn-sm rounded-0" type="button"
                            style="background: #0A2269; border-color: #FFFFFF"
                            data-bs-toggle="tooltip" data-placement="right" title="Eliminar" 
                            data-original-title="Delete" onclick="eliminarsupuesto('${item.idSupuesto}')"><i class="bi bi-trash" style="background: #0A2269; color: #FFFFFF"></i>
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
                    <th class= "col-8" scope="col">Nombre Del Supuesto</th>
                    <th class= "col-1" scope="col">Acción</th> 
                </tr>
            </thead>
            <tbody>${items}</tbody>
        </table>
        `
        }
        metodoauxiliarcrearsupuesto()
        resultDiv.innerHTML = stringHtml
    }
    catch (err) 
    { 
        console.log(err)
    }
}

const agregarsupuesto = async () => {
    $('#modal-1').modal('hide')
    const option = 32
    const descripcionsupu = document.getElementById('descripcionsupuesto').value
    const nombresupu = document.getElementById('nombresupuesto').value
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
    const queryParams = `?option=${option}&atributo1=${nombresupu}&atributo2=${descripcionsupu}&superatributo=${otrosvalores}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        buscandosupuesto()
    }
    catch (err) 
    { 
        console.log(err)
    }
    document.getElementById('nombresupuesto').value = ""
    document.getElementById('descripcionsupuesto').value = ""
    var targetDiv = document.getElementById("div-alert");
    targetDiv.innerHTML = 
    `
    <div class="alert alert-success" id="success-alert" style="display: none; width: 250px">
        <strong>Supuesto creado con éxito</strong>
    </div>
    `
    $("#success-alert").fadeIn(4000);
    $("#success-alert").fadeOut(2000);
}

const editarsupuesto = async (aux) => {
    $('#modal-2').modal('hide')
    const option = 33
    const nombre = document.getElementById('nombresupuesto-2').value
    const descripcion = document.getElementById('descripcionsupuesto-2').value
    var otrosvalores = ""
    const id = document.getElementById('idsupuesto').value
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
        buscandosupuesto()
    }
    catch (err) 
    { 
        console.log(err)
    }
    var targetDiv = document.getElementById("div-alert");
    targetDiv.innerHTML = 
    `
    <div class="alert alert-success" id="success-alert" style="display: none; width: 250px">
        <strong>Supuesto editado con éxito</strong>
    </div>
    `
    $("#success-alert").fadeIn(4000);
    $("#success-alert").fadeOut(2000);
}

const mostrareditarsupuesto = async(aux1,aux2,aux3,proyectos) => {
    document.getElementById('idsupuesto').value = aux1
    document.getElementById('nombresupuesto-2').value = aux2
    document.getElementById('descripcionsupuesto-2').value = aux3
    metodoauxiliareditarsupuesto(aux1,proyectos)
}

const eliminarsupuesto = async(aux) => {
    if(confirm('¿Está seguro de borrar este supuesto?'))
    {
        const option = 34
        const id = aux
        const queryParams = `?option=${option}&atributo7=${id}`
        try 
        {
            const ipserver = location.host
            const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
            buscandosupuesto()
        }
        catch (err) 
        { 
            console.log(err)
        }
        var targetDiv = document.getElementById("div-alert");
        targetDiv.innerHTML = 
        `
        <div class="alert alert-success" id="success-alert" style="display: none; width: 270px">
            <strong>Supuesto eliminado con éxito</strong>
        </div>
        `
        $("#success-alert").fadeIn(4000);
        $("#success-alert").fadeOut(2000);
    }
}

const mostrarinfosupuesto = async(id,descripcion,proyectos) => {
    document.getElementById('descripcion-3').innerHTML = descripcion
    let divproyecto = document.getElementById('divproyecto')
    let h6= document.createElement("h6")
    let textoH6 = document.createTextNode("Proyecto")
    divproyecto.innerHTML=""
    divproyecto.setAttribute("class","row")
    h6.appendChild(textoH6)
    divproyecto.append(h6); console.log("En supu ",proyecto)
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

const metodoauxiliarcrearsupuesto = async() => {
    const option = 35
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const proyectos = data.split(',,')
        let divauxcrearsupuesto = document.getElementById('divauxcrearsupuesto')
        divauxcrearsupuesto.innerHTML=""
        divauxcrearsupuesto.setAttribute("class","row")
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
        divauxcrearsupuesto.append(divproyecto)
        divauxcrearsupuesto.append(document.createElement("p"))
    }
    catch (err) 
    { 
        console.log(err)
    }
}

const metodoauxiliareditarsupuesto = async(aux1,proye) => {
    const option = 35
    const queryParams = `?option=${option}`
    try 
    {
        const ipserver = location.host
        const { data } = await axios.get(`http://`+ipserver+`/OntoPrototipoRepo/webresources/generic${queryParams}`)
        const proyectos = data.split(',,')
        const proyecaux = proye.split(',,')
        var  proyecaux2 = ""
        let divauxeditarsupuesto = document.getElementById('divauxeditarsupuesto')
        divauxeditarsupuesto.innerHTML=""
        divauxeditarsupuesto.setAttribute("class","row")
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
        divauxeditarsupuesto.append(divproyecto)
        divauxeditarsupuesto.append(document.createElement("p"))
    }
    catch (err) 
    { 
        console.log(err)
    } 
}



